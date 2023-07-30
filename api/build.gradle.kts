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
            baseName = "api"
        }
    }

    sourceSets {

        val commonMain by getting {
            dependencies {
                implementation(project(":core"))
                implementation(project(":network"))
            }
        }
    }
}

android {
    namespace = "com.utsman.tokobola.api"
    compileSdk = 33
    defaultConfig {
        minSdk = 24
    }
}