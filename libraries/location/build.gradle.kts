plugins {
    kotlin("multiplatform")
    id("com.android.library")
    id("org.jetbrains.compose")
    kotlin("plugin.serialization") version "1.8.21"
}

@OptIn(org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi::class)
kotlin {
    targetHierarchy.default()

    android {
        compilations.all {
            kotlinOptions {
                jvmTarget = "1.8"
            }
        }
    }

    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach {
        it.binaries.framework {
            baseName = "location"
        }
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(project(":libraries:core"))
                implementation(project(":libraries:network"))
                implementation(project(":libraries:database"))

                api("dev.icerock.moko:permissions-compose:0.16.0")
                api("dev.icerock.moko:geo-compose:0.6.0")
            }
        }

        getByName("androidMain") {
            dependencies {
                api("com.google.android.gms:play-services-location:21.0.1")
            }
        }
    }
}

android {
    namespace = "com.utsman.tokobola.location"
    compileSdk = 33
    defaultConfig {
        minSdk = 24
    }
}