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

// API Response Models (Simplified for OpenWeatherMap or similar)
data class WeatherResponse(
    val main: Main,
    val weather: List<WeatherDescription>,
    val wind: Wind,
    val name: String
)

data class Main(
    val temp: Double,
    val humidity: Int
)

data class WeatherDescription(
    val description: String,
    val icon: String
)

data class Wind(
    val speed: Double
)
