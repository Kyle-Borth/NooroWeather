package com.nooro.weather.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Location(@SerialName("id") val id: Int, @SerialName("name") val name: String)