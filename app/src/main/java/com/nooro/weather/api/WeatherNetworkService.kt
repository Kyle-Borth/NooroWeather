package com.nooro.weather.api

import android.util.Log
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.nooro.weather.BuildConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okio.IOException
import retrofit2.HttpException
import retrofit2.Response
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

private const val BASE_URL = "https://api.weatherapi.com/v1/"

private const val TAG = "WthrNtwSrv"

@Singleton
class WeatherNetworkService @Inject constructor() {

    //TODO Move out of this class
    private val converterFactory by lazy {
        jsonSerializer.asConverterFactory("application/json".toMediaType())
    }

    private val weatherApi: WeatherApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(buildOkHttpClient())
            .addConverterFactory(converterFactory)
            .build()
            .create(WeatherApi::class.java)
    }

    private val apiKey = BuildConfig.WEATHER_API_KEY

    suspend fun getLocations(searchTerm: String) = wrapRequest {
        weatherApi.getLocations(apiKey = apiKey, searchTerm = searchTerm)
    }

    suspend fun getLocation(locationId: Int) = wrapRequest {
        weatherApi.getLocation(apiKey = apiKey, locationId = "id:$locationId")
    }

    suspend fun getCurrentWeather(locationId: Int) = wrapRequest {
        weatherApi.getCurrentWeather(apiKey = apiKey, locationId = "id:$locationId")
    }

    private fun buildOkHttpClient(timeoutSeconds: Long = 10L) = OkHttpClient.Builder().apply {
        connectTimeout(timeoutSeconds, TimeUnit.SECONDS)
        readTimeout(timeoutSeconds, TimeUnit.SECONDS)
        writeTimeout(timeoutSeconds, TimeUnit.SECONDS)
    }.build()

    //TODO Move this to base class?
    private suspend fun <T> wrapRequest(
        request: suspend () -> Response<T>
    ): WrappedResponse<T> = withContext(Dispatchers.IO) {
        try {
            val response = request()

            if(response.isSuccessful) {
                response.body()?.let { body ->
                    WrappedResponse.SuccessWithBody(body, response.code())
                } ?: WrappedResponse.SuccessNoBody(response.code())
            }
            else {
                WrappedResponse.NetworkError(response.errorBody(), null, response.code())
            }
        }
        catch (ioE: IOException) {
            // May indicate the user does not have an internet connection
            Log.e(TAG, "API Request Exception", ioE)
            WrappedResponse.LocalError(ioE)
        }
        catch (httpE: HttpException) {
            // Error response from the API
            Log.e(TAG, "API Request Exception: ${httpE.message()}", httpE)
            WrappedResponse.NetworkError(null, httpE, httpE.code())
        }
    }

    //TODO Move out of this class
    companion object {
        val jsonSerializer by lazy {
            Json {
                ignoreUnknownKeys = true
                explicitNulls = false
                coerceInputValues = true
            }
        }
    }

}