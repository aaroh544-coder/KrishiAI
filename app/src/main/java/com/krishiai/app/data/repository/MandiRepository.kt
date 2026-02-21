package com.krishiai.app.data.repository

import com.krishiai.app.data.model.Review
import kotlinx.coroutines.flow.Flow

interface MandiRepository {
    fun getMandiPrices(): Flow<List<Mandi>>
    suspend fun addMandiPrice(mandi: Mandi): Result<Unit>
    fun getReviews(mandiId: String): Flow<List<Review>>
    suspend fun addReview(review: Review): Result<Unit>
}
