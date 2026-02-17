package com.krishiai.app.data.repository

import com.krishiai.app.data.model.Weather
import kotlinx.coroutines.flow.Flow

interface WeatherRepository {
    fun getCurrentLocationWeather(): Flow<Result<Weather>>
}
