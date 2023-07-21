plugins {
    kotlin("multiplatform")
    id("com.android.library")
    id("org.jetbrains.compose")
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
            baseName = "core"
        }
    }

    sourceSets {
        val lifecycleVersion = "2.6.1"

        val commonMain by getting {
            dependencies {
                implementation(compose.runtime)
                implementation(compose.foundation)
                implementation(compose.material)
                @OptIn(org.jetbrains.compose.ExperimentalComposeLibrary::class)
                implementation(compose.components.resources)

                api("io.github.qdsfdhvh:image-loader:1.6.0")
            }
        }

        getByName("androidMain") {
            dependencies {
                api("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.2")
                api("androidx.lifecycle:lifecycle-viewmodel-ktx:$lifecycleVersion")
                api("androidx.lifecycle:lifecycle-viewmodel-compose:$lifecycleVersion")

                api("androidx.activity:activity-compose:1.7.2")
                api("androidx.appcompat:appcompat:1.6.1")
                api("androidx.core:core-ktx:1.10.1")
            }
        }
    }
}

android {
    namespace = "com.utsman.tokobola.core"
    compileSdk = 33
    defaultConfig {
        minSdk = 24
    }
}