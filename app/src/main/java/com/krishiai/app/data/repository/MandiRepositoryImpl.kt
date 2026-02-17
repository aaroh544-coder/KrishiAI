package com.krishiai.app.data.repository

import com.krishiai.app.data.model.Mandi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MandiRepositoryImpl @Inject constructor() : MandiRepository {
    
    // Mock Data for now
    private val _mandis = mutableListOf(
        Mandi("1", "Nashik Mandi", "Nashik", "Maharashtra", "Onion", 1200.0, "17 Feb", "https://example.com/onion.jpg"),
        Mandi("2", "Pune Market Yard", "Pune", "Maharashtra", "Potato", 900.0, "17 Feb", "https://example.com/potato.jpg"),
        Mandi("3", "Azadpur Mandi", "Delhi", "Delhi", "Tomato", 1500.0, "16 Feb", "https://example.com/tomato.jpg"),
        Mandi("4", "Vashi APMC", "Navi Mumbai", "Maharashtra", "Wheat", 2200.0, "17 Feb", "https://example.com/wheat.jpg"),
        Mandi("5", "Indore Mandi", "Indore", "Madhya Pradesh", "Soybean", 4000.0, "15 Feb", "https://example.com/soybean.jpg")
    )

    override fun getMandiPrices(): Flow<List<Mandi>> = flow {
        // Simulate network delay
        kotlinx.coroutines.delay(500)
        emit(_mandis.toList()) // Emit copy
    }

    override suspend fun addMandiPrice(mandi: Mandi): Result<Unit> {
        _mandis.add(0, mandi) // Add to top
        return Result.success(Unit)
    }
}
