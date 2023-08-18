plugins {
    kotlin("multiplatform")
    id("com.android.library")
    id("org.jetbrains.compose")
    kotlin("plugin.serialization") version "1.8.21"
    id("io.realm.kotlin") version "1.10.0"
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
            baseName = "database"
        }
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(project(":libraries:core"))
                api("io.realm.kotlin:library-base:1.10.0")
            }
        }
    }
}

android {
    namespace = "com.utsman.tokobola.database"
    compileSdk = 33
    defaultConfig {
        minSdk = 24
    }
}
