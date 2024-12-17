package com.nooro.weather.repository

import android.util.Log
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.nooro.weather.api.WeatherNetworkService
import com.nooro.weather.api.WrappedResponse
import com.nooro.weather.model.Location
import com.nooro.weather.model.Weather
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

private const val TAG = "WthrRepo"

@Singleton
class WeatherRepository @Inject constructor(private val weatherService: WeatherNetworkService) {

    private var _weatherForLocation by mutableStateOf(emptyMap<Location, WeatherInfo>())
    val weatherForLocation by derivedStateOf { _weatherForLocation.toMap() }

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    suspend fun getLocations(searchTerm: String) = weatherService.getLocations(searchTerm).also { response ->
        response.requireResponseBody(
            onSuccess = { handleLocationsUpdated(it) },
            onFailureLogMessage = "Error getting locations for search term $searchTerm"
        )
    }

    suspend fun getLocation(locationId: Int) = weatherService.getLocation(locationId).also { response ->
        response.requireResponseBody(
            onSuccess = { handleLocationsUpdated(it) },
            onFailureLogMessage = "Error getting location with ID $locationId"
        )
    }

    suspend fun getCurrentWeather(location: Location) = weatherService.getCurrentWeather(location.id).also { response ->
        when(response) {
            is WrappedResponse.SuccessWithBody -> {
                _weatherForLocation = _weatherForLocation.plus(
                    location to WeatherInfo.Loaded(response.body.current)
                )
             }
            else -> {
                Log.e(TAG, "Error getting weather for location with ID ${location.id}")
                when(_weatherForLocation[location]) {
                    is WeatherInfo.Loaded -> {
                        //TODO Might want a state to indicate that the location's weather was unable to be updated
                        // Just using existing value for now
                    }
                    else -> {
                        _weatherForLocation = _weatherForLocation.plus(location to WeatherInfo.Error)
                    }
                }
            }
        }
    }

    private fun handleLocationsUpdated(locations: List<Location>) {
        locations.forEach { location ->
            when(_weatherForLocation[location]) {
                is WeatherInfo.Loaded -> {
                    //TODO Request updated info from API if current data is old, otherwise skip
                }
                else -> {
                    _weatherForLocation = _weatherForLocation.plus(location to WeatherInfo.Loading)

                    scope.launch { getCurrentWeather(location) }
                }
            }
        }
    }

    //TODO Maybe move this to shared location
    sealed class WeatherInfo {
        object Loading : WeatherInfo()
        data class Loaded(val weather: Weather) : WeatherInfo()
        object Error : WeatherInfo()
    }

    private fun <T> WrappedResponse<T>.requireResponseBody(onSuccess: (T) -> Unit, onFailureLogMessage: String) {
        when(this) {
            is WrappedResponse.SuccessWithBody -> onSuccess(this.body)
            is WrappedResponse.SuccessNoBody -> Log.e(TAG, "$onFailureLogMessage: No Response Body")
            is WrappedResponse.NetworkError -> {
                Log.e(TAG, "$onFailureLogMessage: Network Error ${this.responseCode}", this.throwable)
            }
            is WrappedResponse.LocalError -> Log.e(TAG, "$onFailureLogMessage: Local Error", this.throwable)
        }
    }

}