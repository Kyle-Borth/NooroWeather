package com.nooro.weather.ui.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.nooro.weather.R
import com.nooro.weather.model.Location
import com.nooro.weather.model.Weather
import com.nooro.weather.repository.WeatherRepository
import com.nooro.weather.ui.component.InfoBubble
import com.nooro.weather.ui.component.NooroTextField
import com.nooro.weather.ui.theme.IconDimens
import com.nooro.weather.ui.theme.NooroTheme
import com.nooro.weather.ui.theme.PaddingLarge
import com.nooro.weather.ui.theme.PaddingNormal
import com.nooro.weather.ui.theme.PaddingSmall
import com.nooro.weather.viewmodel.MainViewModel

@Composable
fun MainScreen(modifier: Modifier = Modifier, viewModel: MainViewModel = hiltViewModel()) {
    val searchText by viewModel.searchText.collectAsState(initial = TextFieldValue())

    Surface(modifier = modifier) {
        MainScreen(
            selectionLoading = viewModel.selectionLoading,
            locationsLoading = viewModel.locationsLoading,
            searchText = searchText,
            searchResults = viewModel.searchResults,
            selectedResult = viewModel.selectedResult,
            onSearchTextChanged = { viewModel.onSearchTextChanged(it) },
            onLocationSelected = { viewModel.onLocationSelected(it) }
        )
    }
}

@Composable
private fun MainScreen(
    selectionLoading: Boolean,
    locationsLoading: Boolean,
    searchText: TextFieldValue,
    searchResults: List<MainViewModel.SearchResult>,
    selectedResult: MainViewModel.SearchResult?,
    onSearchTextChanged: (TextFieldValue) -> Unit,
    onLocationSelected: (Location) -> Unit
) {
    Column(modifier = Modifier.fillMaxSize()) {
        NooroTextField(
            value = searchText,
            onValueChange = onSearchTextChanged,
            modifier = Modifier.fillMaxWidth().padding(horizontal = PaddingNormal, vertical = PaddingLarge),
            placeholder = { Text(text = stringResource(R.string.search_location)) },
            trailingIcon = {
                if(locationsLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(IconDimens.TrailingIcon),
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
                else {
                    Icon(painter = painterResource(R.drawable.ic_search), contentDescription = null)
                }
            }
        )

        when {
            searchResults.isNotEmpty() -> {
                SearchResults(
                    results = searchResults,
                    modifier = Modifier.fillMaxWidth().weight(1f),
                    onClick = onLocationSelected
                )
            }
            selectedResult != null -> {
                Weather(result = selectedResult, modifier = Modifier.fillMaxWidth().weight(1f))
            }
            selectionLoading -> {
                ScreenLoading(modifier = Modifier.fillMaxWidth().weight(1f))
            }
            else -> {
                NoCity(modifier = Modifier.fillMaxWidth().weight(1f))
            }
        }
    }
}

//region Screen Loading

@Composable
private fun ScreenLoading(modifier: Modifier = Modifier) {
    Box(modifier = modifier, contentAlignment = Alignment.Center) {
        CircularProgressIndicator()
    }
}

//endregion

//region Locations

@Composable
private fun SearchResults(
    results: List<MainViewModel.SearchResult>,
    modifier: Modifier = Modifier,
    onClick: (Location) -> Unit
) {
    LazyColumn(modifier = modifier, verticalArrangement = Arrangement.spacedBy(PaddingSmall)) {
        items(items = results, key = { it.location.id }) { result ->
            LocationRow(
                result = result,
                modifier = Modifier.fillMaxWidth().padding(horizontal = PaddingNormal),
                onClick = { onClick(result.location) }
            )
        }
    }
}

//endregion

//region Location Row

@Composable
private fun LocationRow(result: MainViewModel.SearchResult, modifier: Modifier = Modifier, onClick: () -> Unit) {
    val temperature by remember(result.weatherInfo) {
        derivedStateOf { (result.weatherInfo as? WeatherRepository.WeatherInfo.Loaded)?.weather?.temperature ?: "" }
    }

    InfoBubble(modifier = modifier.clickable(enabled = true, onClick = onClick)) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(PaddingNormal),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = result.location.name,
                    color = MaterialTheme.colorScheme.onBackground,
                    style = MaterialTheme.typography.titleLarge
                )

                Text(
                    text = temperature,
                    color = MaterialTheme.colorScheme.onBackground,
                    style = MaterialTheme.typography.displayLarge
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            ConditionImage(
                weatherInfo = result.weatherInfo,
                modifier = Modifier.size(width = 85.dp, height = 70.dp)
            )
        }
    }
}

//endregion

//region Weather

@Composable
private fun Weather(result: MainViewModel.SearchResult, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ConditionImage(weatherInfo = result.weatherInfo, modifier = Modifier.size(width = 125.dp, height = 115.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                result.location.name,
                modifier = Modifier.padding(end = PaddingSmall),
                style = MaterialTheme.typography.headlineMedium
            )

            Icon(painter = painterResource(R.drawable.ic_location), contentDescription = null)
        }

        when(result.weatherInfo) {
            is WeatherRepository.WeatherInfo.Loading -> {
                //TODO
            }
            is WeatherRepository.WeatherInfo.Loaded -> {
                Temperature(result.weatherInfo.weather.temperature)

                DetailsBox(
                    weather = result.weatherInfo.weather,
                    modifier = Modifier.fillMaxWidth().padding(horizontal = PaddingLarge)
                )
            }
            is WeatherRepository.WeatherInfo.Error -> {
                //TODO
            }
        }
    }
}

//endregion

//region Condition Image

@Composable
private fun ConditionImage(weatherInfo: WeatherRepository.WeatherInfo, modifier: Modifier = Modifier) {
    val condition by remember(weatherInfo) {
        derivedStateOf { (weatherInfo as? WeatherRepository.WeatherInfo.Loaded)?.weather?.condition }
    }

    //TODO Could use a placeholder/loading animation and maybe error image
    AsyncImage(
        model = condition?.iconUrl,
        contentDescription = condition?.description ?: stringResource(R.string.loading),
        modifier = modifier
    )
}

//endregion

//region Temperature

@Composable
private fun Temperature(temperature: String, modifier: Modifier = Modifier) {
    Row(modifier = modifier, verticalAlignment = Alignment.Top) {
        Text(text = temperature, style = MaterialTheme.typography.headlineLarge)

        Icon(
            painter = painterResource(R.drawable.ic_degree),
            modifier = Modifier.padding(top = PaddingNormal),
            contentDescription = null
        )
    }
}

//endregion

//region Details Box

@Composable
private fun DetailsBox(weather: Weather, modifier: Modifier = Modifier) {
    val humidity by remember(weather.humidity) {
        derivedStateOf { "${weather.humidity}%" }
    }
    val uvRounded by remember(weather.uvIndex) {
        derivedStateOf { (weather.uvIndex + 0.5).toInt().toString() }
    }

    InfoBubble(modifier = modifier) {
        Row(
            modifier = Modifier.padding(PaddingNormal),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TitleDetail(
                title = stringResource(R.string.humidity),
                detail = humidity,
                modifier = Modifier.weight(1f)
            )

            TitleDetail(
                title = stringResource(R.string.uv),
                detail = uvRounded,
                modifier = Modifier.weight(1f)
            )

            TitleDetail(
                title = stringResource(R.string.feels_like),
                detail = weather.feelsLike,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

//endregion

//region Title Detail

@Composable
private fun TitleDetail(title: String, detail: String, modifier: Modifier = Modifier) {
    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = title, style = MaterialTheme.typography.labelMedium)

        Text(text = detail, style = MaterialTheme.typography.titleMedium)
    }
}

//endregion

//region No City

@Composable
private fun NoCity(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = stringResource(R.string.no_city_selected), style = MaterialTheme.typography.headlineMedium)
        Text(text = stringResource(R.string.search_for_city), style = MaterialTheme.typography.headlineSmall)
    }
}

//endregion

@Preview(showBackground = true)
@Composable
private fun MainScreenPreview() {
    val location = Location(id = 0, name = "Minneapolis")
    val weather = Weather(
        lastUpdatedDateTimeMillis = 0,
        temperatureInCelsius = 3.9,
        temperatureInFahrenheit = 39.0,
        condition = Weather.Condition(
            description = "Overcast",
            _iconUrl = "",
            code = 0
        ),
        humidity = 70,
        feelsLikeCelsius = 0.1,
        feelsLikeFahrenheit = 32.1,
        uvIndex = 0.6
    )

    val searchResult = MainViewModel.SearchResult(
        location = location,
        weatherInfo = WeatherRepository.WeatherInfo.Loaded(weather = weather)
    )

    NooroTheme {
        MainScreen(
            selectionLoading = false,
            locationsLoading = false,
            searchText = TextFieldValue(),
            searchResults = emptyList(),
            selectedResult = searchResult,
            onSearchTextChanged = {},
            onLocationSelected = {}
        )
    }
}