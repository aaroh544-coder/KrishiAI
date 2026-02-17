package com.krishiai.app.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.krishiai.app.data.repository.AuthRepository
import com.krishiai.app.data.repository.AuthRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideBillingManager(@dagger.hilt.android.qualifiers.ApplicationContext context: android.content.Context): com.krishiai.app.util.BillingManager {
        return com.krishiai.app.util.BillingManager(context)
    }

    @Provides
    @Singleton
    fun provideSpeechManager(@dagger.hilt.android.qualifiers.ApplicationContext context: android.content.Context): com.krishiai.app.util.SpeechManager {
        return com.krishiai.app.util.SpeechManager(context)
    }

    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth = FirebaseAuth.getInstance()

    @Provides
    @Singleton
    fun provideFirebaseFirestore(): FirebaseFirestore = FirebaseFirestore.getInstance()

    @Provides
    @Singleton
    @Provides
    @Singleton
    fun provideAuthRepository(impl: AuthRepositoryImpl): AuthRepository = impl

    @Provides
    @Singleton
    fun provideFusedLocationProviderClient(
        @dagger.hilt.android.qualifiers.ApplicationContext context: android.content.Context
    ): com.google.android.gms.location.FusedLocationProviderClient {
        return com.google.android.gms.location.LocationServices.getFusedLocationProviderClient(context)
    }

    @Provides
    @Singleton
    fun provideWeatherApi(): com.krishiai.app.data.remote.WeatherApi {
        return retrofit2.Retrofit.Builder()
            .baseUrl("https://api.open-meteo.com/")
            .addConverterFactory(retrofit2.converter.gson.GsonConverterFactory.create())
            .build()
            .create(com.krishiai.app.data.remote.WeatherApi::class.java)
    }

    @Provides
    @Singleton
    fun provideWeatherRepository(impl: com.krishiai.app.data.repository.WeatherRepositoryImpl): com.krishiai.app.data.repository.WeatherRepository = impl

    @Provides
    @Singleton
    fun provideChatRepository(): com.krishiai.app.data.repository.ChatRepository {
        return com.krishiai.app.data.repository.ChatRepository()
    }

    @Provides
    @Singleton
    fun provideMandiRepository(impl: com.krishiai.app.data.repository.MandiRepositoryImpl): com.krishiai.app.data.repository.MandiRepository = impl
}
