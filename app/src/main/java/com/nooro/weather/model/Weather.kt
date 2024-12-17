package com.nooro.weather.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Weather(
    @SerialName("last_updated_epoch") val lastUpdatedDateTimeMillis: Int,
    @SerialName("temp_c") val temperatureInCelsius: Double,
    @SerialName("temp_f") val temperatureInFahrenheit: Double,
    @SerialName("condition") val condition: Condition,
    @SerialName("humidity") val humidity: Int,
    @SerialName("feelslike_c") val feelsLikeCelsius: Double,
    @SerialName("feelslike_f") val feelsLikeFahrenheit: Double,
    @SerialName("uv") val uvIndex: Double
) {
    //TODO app should allow user to choose F or C
    val temperature: String get() = "${(temperatureInFahrenheit + 0.5).toInt()}°"

    val feelsLike: String get() = "${(feelsLikeFahrenheit + 0.5).toInt()}°"

    @Serializable
    data class Condition(
        @SerialName("text") val description: String,
        @SerialName("icon") private val _iconUrl: String,
        @SerialName("code") val code: Int
    ) {
        val iconUrl: String get() = "https:$_iconUrl".replace("64x64", "128x128")
    }

}