package com.nooro.weather.viewmodel

import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nooro.weather.api.WrappedResponse
import com.nooro.weather.model.Location
import com.nooro.weather.repository.WeatherRepository
import com.nooro.weather.service.NooroPreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(FlowPreview::class)
@HiltViewModel
class MainViewModel @Inject constructor(
    private val weatherRepo: WeatherRepository,
    private val preferences: NooroPreferences
) : ViewModel() {

    private var _searchText = MutableStateFlow(TextFieldValue())
    val searchText: StateFlow<TextFieldValue> get() = _searchText

    private var _searchResults by mutableStateOf(emptyList<Location>())
    val searchResults by derivedStateOf {
        _searchResults.mapNotNull { location ->
            weatherRepo.weatherForLocation[location]?.let { weather -> SearchResult(location, weather) }
        }
    }

    private var selectedLocation by mutableStateOf<Location?>(null)
    val selectedResult by derivedStateOf {
        selectedLocation?.let { location ->
            weatherRepo.weatherForLocation[location]?.let { SearchResult(location, it) }
        }
    }

    var selectionLoading by mutableStateOf(false)
        private set
    var locationsLoading by mutableStateOf(false)
        private set

    init {
        viewModelScope.launch {
            _searchText.map { it.text }.debounce(500).collect { text ->
                if(text.length >= 3) {
                    getLocations(searchTerm = text)
                }
                else {
                    _searchResults = emptyList()
                }
            }
        }

        viewModelScope.launch {
            preferences.preferredLocationFlow.filterNotNull().collect { locationId ->
                // Get the existing Location instance if already downloaded
                val location = weatherRepo.weatherForLocation.firstNotNullOfOrNull {
                    if(it.key.id == locationId) it.key else null
                }

                if(location == null) {
                    getSelectedLocation(locationId)
                }
                else {
                    selectedLocation = location
                }
            }
        }
    }

    fun onSearchTextChanged(text: TextFieldValue) = viewModelScope.launch {
        _searchText.emit(text)
    }

    fun onLocationSelected(location: Location) = viewModelScope.launch {
        preferences.updatePreferredLocation(location.id)

        _searchText.emit(TextFieldValue())
        _searchResults = emptyList()
    }

    private fun getSelectedLocation(locationId: Int) = viewModelScope.launch {
        selectionLoading = true

        when(val response = weatherRepo.getLocation(locationId)) {
            is WrappedResponse.SuccessWithBody -> {
                response.body.firstOrNull { it.id == locationId }?.let { selectedLocation = it }
                //TODO Handle the scenario where the location is not found
            }
            else -> Unit //TODO Handle Errors
        }

        selectionLoading = false
    }

    private fun getLocations(searchTerm: String) = viewModelScope.launch {
        locationsLoading = true

        when(val response = weatherRepo.getLocations(searchTerm)) {
            is WrappedResponse.SuccessWithBody -> {
                _searchResults = response.body
            }
            else -> Unit //TODO Show some kind of error
        }

        locationsLoading = false
    }

    //TODO Maybe move this to shared location
    data class SearchResult(val location: Location, val weatherInfo: WeatherRepository.WeatherInfo)

}