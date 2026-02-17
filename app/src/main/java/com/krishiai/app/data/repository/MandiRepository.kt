package com.krishiai.app.data.repository

import com.krishiai.app.data.model.Mandi
import kotlinx.coroutines.flow.Flow

interface MandiRepository {
    fun getMandiPrices(): Flow<List<Mandi>>
    suspend fun addMandiPrice(mandi: Mandi): Result<Unit>
}
