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
            baseName = "core"
        }
    }

    sourceSets {
        val lifecycleVersion = "2.6.1"
        val voyagerVersion = "1.0.0-rc05"

        val commonMain by getting {
            dependencies {
                api(compose.runtime)
                api(compose.foundation)
                api(compose.material)
                @OptIn(org.jetbrains.compose.ExperimentalComposeLibrary::class)
                api(compose.components.resources)
                api(compose.animation)

                implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.5.1")

                api(project(":resources"))

                api("io.github.qdsfdhvh:image-loader:1.6.0")
                api("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")

                api("cafe.adriel.voyager:voyager-navigator:$voyagerVersion")
                api("cafe.adriel.voyager:voyager-transitions:$voyagerVersion")
                api("cafe.adriel.voyager:voyager-tab-navigator:$voyagerVersion")

                api("org.jetbrains.kotlinx:kotlinx-datetime:0.4.0")

            }
        }

        getByName("androidMain") {
            dependencies {
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

    sourceSets["main"].resources.srcDirs("src/commonMain/resources")

    defaultConfig {
        minSdk = 24
    }
}