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
            baseName = "wishlist"
        }
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(project(":libraries:core"))
                implementation(project(":libraries:network"))
                implementation(project(":libraries:database"))

                implementation(project(":libraries:common"))
            }
        }
    }
}

android {
    namespace = "com.utsman.tokobola.wishlist"
    compileSdk = 33
    defaultConfig {
        minSdk = 24
    }
}