pluginManagement {
    plugins {
        id("org.jetbrains.kotlin.android") version "2.2.10"
        id("org.jetbrains.kotlin.plugin.compose") version "2.2.10"
        id("com.android.application") version "8.12.0"
        id("com.google.devtools.ksp") version "2.2.10-2.0.2"
        id("com.google.dagger.hilt.android") version "2.57" // Di chuyển Hilt vào đây
        id("com.google.gms.google-services") version "4.4.3" // Thêm google-services
    }
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "Zendo"
include(":app")