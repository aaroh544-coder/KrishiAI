// Top-level build file where you add configuration options common to all sub-projects/modules.

plugins {
    // Android Application Plugin
    id("com.android.application") version "8.3.2" apply false
    
    // Android Library Plugin (highly recommended to define here if you plan to add modules later)
    id("com.android.library") version "8.3.2" apply false
    
    // Kotlin Android Plugin
    id("org.jetbrains.kotlin.android") version "1.9.22" apply false
    
    // Google Services Plugin (For Firebase/Analytics)
    id("com.google.gms.google-services") version "4.4.1" apply false
    
    // Dagger Hilt for Dependency Injection
    id("com.google.dagger.hilt.android") version "2.51.1" apply false
    
    // KSP (Kotlin Symbol Processing) - Explicitly matched to Kotlin 1.9.22
    id("com.google.devtools.ksp") version "1.9.22-1.0.17" apply false
}

