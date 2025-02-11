pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        maven { url = uri("https://jitpack.io") }
        maven { url = uri("https://repo.maven.apache.org/maven2/") }
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven {
            url = uri("https://api.mapbox.com/downloads/v2/releases/maven")
            authentication {
                create<BasicAuthentication>("basic")
            }
            credentials {
                username = "mapbox"
                password = providers.gradleProperty("sk.eyJ1IjoidmFsZXNjaHYyIiwiYSI6ImNtNm10NWU0azBuZXkyanBqNHQ0eHdkZnMifQ.SqClKk_ePQN8kJa1pvMcsA").getOrElse("")
            }
        }
    }
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        // ... otros repositorios si los tienes ...
    }
}

rootProject.name = "App"
include(":app")
