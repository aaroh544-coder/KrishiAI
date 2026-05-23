# KrishiAI - Agricultural Intelligence Platform

An Android application powered by **Jetpack Compose** and **Google AI (Gemini)** that provides intelligent agricultural insights, crop analysis, and farming solutions.

## 📋 Tech Stack

- **Language:** Kotlin
- **UI Framework:** Jetpack Compose + Material3
- **Architecture:** MVVM + Hilt Dependency Injection
- **Database:** Firebase Firestore
- **Authentication:** Firebase Auth
- **AI/ML:** Google Generative AI (Gemini API)
- **Networking:** Retrofit + OkHttp
- **Camera:** AndroidX Camera Library
- **Image Loading:** Coil
- **Build System:** Gradle with Kotlin DSL

## 🚀 Features

- 🌾 Crop disease detection via camera
- 🤖 AI-powered farming recommendations
- 📊 Real-time agricultural analytics
- 🔐 Secure Firebase authentication
- 📱 Responsive Material Design 3 UI
- 🎥 Live camera capture and analysis

## 📦 Dependencies

**Core Framework:**
- androidx-compose-bom: 2023.10.01
- androidx-lifecycle: 2.7.0
- androidx-navigation-compose: 2.7.6

**Google Services:**
- Firebase Suite (Auth, Firestore, Analytics)
- Google Generative AI: 0.9.0
- Google Play Services: 18.1.0

**Networking:**
- Retrofit: 2.10.0
- OkHttp: 4.11.0
- Gson: 2.10.1

**Dependency Injection:**
- Hilt: 2.48

**Camera & Media:**
- androidx-camera: 1.3.1
- Coil Compose: 2.5.0

**Testing:**
- JUnit: 4.13.2
- Espresso: 3.5.1
- Compose Test: 2023.10.01

## 🛠️ Setup & Build

### Prerequisites
- Android Studio Jellyfish (2023.3.1) or later
- Android SDK 34
- Java 17 or later
- Google Play Services configured

### Installation

1. Clone the repository
2. Configure Firebase (download google-services.json)
3. Add API keys to local.properties
4. Run `./gradlew clean build`

## ✅ Build Status

All build errors have been resolved! The project is ready for development.

**Last Updated:** May 23, 2026
