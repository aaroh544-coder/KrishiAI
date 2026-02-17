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
            .baseUrl("https://api.openweathermap.org/")
            .addConverterFactory(retrofit2.converter.gson.GsonConverterFactory.create())
            .build()
            .create(com.krishiai.app.data.remote.WeatherApi::class.java)
    }

    @Provides
    @Singleton
    fun provideWeatherRepository(impl: com.krishiai.app.data.repository.WeatherRepositoryImpl): com.krishiai.app.data.repository.WeatherRepository = impl
}
