import com.codingfeline.buildkonfig.compiler.FieldSpec.Type.STRING

plugins {
    kotlin("multiplatform")
    id("com.android.library")
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
            baseName = "api"
        }
    }

    sourceSets {

        val commonMain by getting {
            dependencies {
                implementation(project(":libraries:core"))
                implementation(project(":libraries:network"))
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

buildkonfig {
    packageName = "com.utsman.tokobola.api"

    // default config is required
    defaultConfigs {
        buildConfigField(STRING, "BASE_URL", project.properties.get("base.url").toString())
        buildConfigField(STRING, "MAPBOX_BASE_URL", project.properties.get("mapbox.base.url").toString())
    }
}