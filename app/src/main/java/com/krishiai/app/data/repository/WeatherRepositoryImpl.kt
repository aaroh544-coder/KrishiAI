package com.krishiai.app.data.repository

import android.annotation.SuppressLint
import android.location.Location
import com.google.android.gms.location.FusedLocationProviderClient
import com.krishiai.app.data.model.Weather
import com.krishiai.app.data.remote.WeatherApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WeatherRepositoryImpl @Inject constructor(
    private val weatherApi: WeatherApi,
    private val fusedLocationClient: FusedLocationProviderClient
) : WeatherRepository {

    // Helper to map response to UI model
    private fun mapToWeather(response: com.krishiai.app.data.model.WeatherResponse): Weather {
        return Weather(
            temperature = response.main.temp,
            description = response.weather.firstOrNull()?.description ?: "",
            humidity = response.main.humidity,
            windSpeed = response.wind.speed,
            iconUrl = "https://openweathermap.org/img/wn/${response.weather.firstOrNull()?.icon}@2x.png",
            city = response.name,
            date = SimpleDateFormat("EEE, dd MMM", Locale.getDefault()).format(Date())
        )
    }

    @SuppressLint("MissingPermission") // Permissions should be checked in UI before calling this
    override fun getCurrentLocationWeather(): Flow<Result<Weather>> = flow {
        try {
            val location: Location? = fusedLocationClient.lastLocation.await()
            if (location != null) {
                // Use a placeholder key or BuildConfig
                val apiKey = "YOUR_API_KEY_HERE" // TODO: Move to BuildConfig
                val response = weatherApi.getCurrentWeather(location.latitude, location.longitude, apiKey)
                emit(Result.success(mapToWeather(response)))
            } else {
                emit(Result.failure(Exception("Location not found")))
            }
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }
}
