package com.krishiai.app.data.model

import com.google.firebase.Timestamp

data class User(
    val uid: String = "",
    val phoneNumber: String = "",
    val name: String = "",
    val village: String = "",
    val district: String = "",
    val state: String = "",
    val preferredLanguage: String = "en", // en, hi, mr
    val mainCrop: String = "",
    val subscriptionStatus: String = "free", // free, premium
    val createdAt: Timestamp? = null
)
