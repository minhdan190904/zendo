pluginManagement {
    plugins {
        id("org.jetbrains.kotlin.plugin.compose") version "2.1.0"
        id("org.jetbrains.kotlin.android") version "2.1.0"
        id("com.android.application") version "8.3.0"
    }
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
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
 