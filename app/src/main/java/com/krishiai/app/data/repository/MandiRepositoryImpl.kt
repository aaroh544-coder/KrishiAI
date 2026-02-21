package com.krishiai.app.data.repository

import com.krishiai.app.data.model.Review
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MandiRepositoryImpl @Inject constructor() : MandiRepository {
    
    private val _mandis = mutableListOf(
        Mandi("1", "Nashik Mandi", "Nashik", "Maharashtra", "Onion", 1200.0, "17 Feb", "https://example.com/onion.jpg"),
        Mandi("2", "Pune Market Yard", "Pune", "Maharashtra", "Potato", 900.0, "17 Feb", "https://example.com/potato.jpg"),
        Mandi("3", "Azadpur Mandi", "Delhi", "Delhi", "Tomato", 1500.0, "16 Feb", "https://example.com/tomato.jpg"),
        Mandi("4", "Vashi APMC", "Navi Mumbai", "Maharashtra", "Wheat", 2200.0, "17 Feb", "https://example.com/wheat.jpg"),
        Mandi("5", "Indore Mandi", "Indore", "Madhya Pradesh", "Soybean", 4000.0, "15 Feb", "https://example.com/soybean.jpg")
    )

    private val _reviews = mutableListOf<Review>(
        Review("r1", "1", "u1", "Ramesh", 5, "Very accurate prices!", System.currentTimeMillis()),
        Review("r2", "1", "u2", "Suresh", 4, "Prices are mostly correct.", System.currentTimeMillis())
    )

    override fun getMandiPrices(): Flow<List<Mandi>> = flow {
        kotlinx.coroutines.delay(500)
        emit(_mandis.toList())
    }

    override suspend fun addMandiPrice(mandi: Mandi): Result<Unit> {
        _mandis.add(0, mandi)
        return Result.success(Unit)
    }

    override fun getReviews(mandiId: String): Flow<List<Review>> = flow {
        emit(_reviews.filter { it.mandiId == mandiId }.sortedByDescending { it.timestamp })
    }

    override suspend fun addReview(review: Review): Result<Unit> {
        _reviews.add(0, review)
        return Result.success(Unit)
    }
}
