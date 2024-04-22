plugins {
    id("com.android.application") version "8.2.1" apply false
}

buildscript {
    repositories {
        google()
        mavenCentral()
        maven { url = uri("https://jitpack.io") } // Add the required repository
    }
    dependencies {
        classpath("com.android.tools.build:gradle:7.2.1")
        classpath("com.google.gms:google-services:4.4.1")
    }
}

allprojects {
    repositories {

//        google()
//        mavenCentral()
//        maven { url = uri("https://jitpack.io") } // Add the required repository
    }
}
