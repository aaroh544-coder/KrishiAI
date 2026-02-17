package com.krishiai.app.data.model

data class Weather(
    val temperature: Double,
    val description: String,
    val humidity: Int,
    val windSpeed: Double,
    val iconUrl: String,
    val city: String,
    val date: String
)

// Open-Meteo Response Models
data class WeatherResponse(
    val latitude: Double,
    val longitude: Double,
    val current_weather: CurrentWeather
)

data class CurrentWeather(
    val temperature: Double,
    val windspeed: Double,
    val winddirection: Double,
    val weathercode: Int,
    val time: String
)
