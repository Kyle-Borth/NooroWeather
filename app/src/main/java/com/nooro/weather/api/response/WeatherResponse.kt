package com.nooro.weather.api.response

import com.nooro.weather.model.Weather
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class WeatherResponse(
    @SerialName("location") val location: Location,
    @SerialName("current") val current: Weather
) {

    @Serializable
    data class Location(@SerialName("name") val name: String)

}