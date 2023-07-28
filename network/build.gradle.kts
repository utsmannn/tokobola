plugins {
    kotlin("multiplatform")
    id("com.android.library")
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
            baseName = "network"
        }
    }

    sourceSets {
        val ktorVersion = "2.3.2"

        val commonMain by getting {
            dependencies {
                implementation("io.ktor:ktor-client-core:$ktorVersion")
                implementation("io.ktor:ktor-client-content-negotiation:$ktorVersion")
                implementation("io.ktor:ktor-client-logging:$ktorVersion")
                api("io.ktor:ktor-serialization-kotlinx-json:$ktorVersion")

                implementation(project(":core"))
            }
        }
        getByName("androidMain").dependencies {
            implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.2")
            implementation("io.ktor:ktor-client-okhttp:$ktorVersion")
        }

        getByName("iosMain").dependencies {
            implementation("io.ktor:ktor-client-darwin:$ktorVersion")
        }
    }
}

android {
    namespace = "com.utsman.tokobola.network"
    compileSdk = 33
    defaultConfig {
        minSdk = 24
    }
}