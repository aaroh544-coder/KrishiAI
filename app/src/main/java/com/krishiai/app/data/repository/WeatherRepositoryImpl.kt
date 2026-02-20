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
        val weatherCode = response.current_weather.weathercode
        val description = getWeatherDescription(weatherCode)
        val icon = getWeatherIcon(weatherCode)
        
        val humidity = response.hourly?.relative_humidity_2m?.firstOrNull()?.toInt() ?: 0
        
        return Weather(
            temperature = response.current_weather.temperature,
            description = description,
            humidity = humidity,
            windSpeed = response.current_weather.windspeed,
            iconUrl = "https://openweathermap.org/img/wn/$icon@2x.png", // reusing OWM icons for simplicity if possible, or local resources. actually for now let's use a generic url or skip icon logic to avoid crash. 
            // Better: use local drawables based on code. For now, empty or placeholder.
            // Let's use a simple mapping to OWM icon codes for the URL to work.
            city = "Lat: ${response.latitude}, Lon: ${response.longitude}", // Open-Meteo doesn't return city name directly without geocoding
            date = SimpleDateFormat("EEE, dd MMM", Locale.getDefault()).format(Date())
        )
    }

    private fun getWeatherDescription(code: Int): String {
        return when(code) {
             0 -> "Clear sky"
             1, 2, 3 -> "Mainly clear, partly cloudy, and overcast"
             45, 48 -> "Fog and depositing rime fog"
             51, 53, 55 -> "Drizzle: Light, moderate, and dense intensity"
             56, 57 -> "Freezing Drizzle: Light and dense intensity"
             61, 63, 65 -> "Rain: Slight, moderate and heavy intensity"
             66, 67 -> "Freezing Rain: Light and heavy intensity"
             71, 73, 75 -> "Snow fall: Slight, moderate, and heavy intensity"
             77 -> "Snow grains"
             80, 81, 82 -> "Rain showers: Slight, moderate, and violent"
             85, 86 -> "Snow showers slight and heavy"
             95 -> "Thunderstorm: Slight or moderate"
             96, 99 -> "Thunderstorm with slight and heavy hail"
             else -> "Unknown"
        }
    }

    private fun getWeatherIcon(code: Int): String {
        // Map WMO codes to OpenWeatherMap icon codes roughly
        return when(code) {
            0 -> "01d"
            1, 2, 3 -> "02d" // clouds
            45, 48 -> "50d" // fog
            51, 53, 55, 61, 63, 65 -> "09d" // rain
            71, 73, 75, 77, 85, 86 -> "13d" // snow
            95, 96, 99 -> "11d" // thunderstorm
            else -> "01d"
        }
    }

    @SuppressLint("MissingPermission") // Permissions should be checked in UI before calling this
    override fun getCurrentLocationWeather(): Flow<Result<Weather>> = flow {
        try {
            val location: Location? = fusedLocationClient.lastLocation.await()
            if (location != null) {
                // No API Key needed for Open-Meteo
                val response = weatherApi.getCurrentWeather(location.latitude, location.longitude)
                emit(Result.success(mapToWeather(response)))
            } else {
                emit(Result.failure(Exception("Location not found")))
            }
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }
}
