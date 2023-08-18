import com.codingfeline.buildkonfig.compiler.FieldSpec.Type.STRING

plugins {
    kotlin("multiplatform")
    id("com.android.library")
    id("org.jetbrains.compose")
    kotlin("plugin.serialization") version "1.8.21"
    id("com.codingfeline.buildkonfig")
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
            baseName = "common"
        }
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(project(":libraries:core"))
                implementation(project(":libraries:network"))
                implementation(project(":libraries:database"))
                implementation(project(":libraries:location"))
                api(project(":libraries:api"))
            }
        }

        getByName("androidMain") {
            dependencies {
                implementation("com.mapbox.maps:android:10.15.0")
            }
        }
    }
}

android {
    namespace = "com.utsman.tokobola.feature.common"
    compileSdk = 33
    defaultConfig {
        minSdk = 24
    }
}

buildkonfig {
    packageName = "com.utsman.tokobola.common"

    // default config is required
    defaultConfigs {
        buildConfigField(STRING, "MAPBOX_TOKEN", project.properties.get("mapbox.token").toString())
    }
}