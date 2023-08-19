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
            baseName = "cart"
        }
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(project(":libraries:core"))
                implementation(project(":libraries:network"))
                implementation(project(":libraries:database"))
                implementation(project(":libraries:location"))

                implementation(project(":libraries:common"))
            }
        }
    }
}

android {
    namespace = "com.utsman.tokobola.cart"
    compileSdk = 33
    defaultConfig {
        minSdk = 24
    }
}

buildkonfig {
    packageName = "com.utsman.tokobola.cart"

    // default config is required
    defaultConfigs {
        buildConfigField(STRING, "MAPBOX_TOKEN", project.properties.get("mapbox.token").toString())
    }
}