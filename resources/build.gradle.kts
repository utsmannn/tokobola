plugins {
    kotlin("multiplatform")
    kotlin("native.cocoapods")
    id("com.android.library")
    id("org.jetbrains.compose")
    id("dev.icerock.mobile.multiplatform-resources")
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

    cocoapods {
        version = "1.0.0"
        summary = "Some description for the Shared Module"
        homepage = "Link to the Shared Module homepage"
        ios.deploymentTarget = "14.1"
        podfile = project.file("../iosApp/Podfile")
        framework {
            baseName = "shared"
            isStatic = true
        }
        extraSpecAttributes["resources"] = "['src/commonMain/resources/**', 'src/iosMain/resources/**']"
        extraSpecAttributes["exclude_files"] = "['src/commonMain/resources/SharedRes/**']"
    }
    
    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach {
        it.binaries.framework {
            baseName = "resources"
        }
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                api("dev.icerock.moko:resources-compose:0.23.0")
            }
        }
    }
}

multiplatformResources {
    multiplatformResourcesPackage = "com.utsman.tokobola.resources"
    multiplatformResourcesClassName = "SharedRes"
}

android {
    namespace = "com.utsman.tokobola.resources"
    sourceSets["main"].resources.srcDirs("src/commonMain/resources/SharedRes**")

    compileSdk = 33
    defaultConfig {
        minSdk = 24
    }
}