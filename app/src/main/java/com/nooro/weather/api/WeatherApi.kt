package com.nooro.weather.api

import com.nooro.weather.api.response.WeatherResponse
import com.nooro.weather.model.Location

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi {

    @GET("search.json")
    suspend fun getLocations(
        @Query("key") apiKey: String,
        @Query("q") searchTerm: String
    ) : Response<List<Location>>

    @GET("search.json")
    suspend fun getLocation(
        @Query("key") apiKey: String,
        @Query("q") locationId: String
    ) : Response<List<Location>>

    @GET("current.json")
    suspend fun getCurrentWeather(
        @Query("key") apiKey: String,
        @Query("q") locationId: String
    ): Response<WeatherResponse>

}