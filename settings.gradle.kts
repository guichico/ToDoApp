pluginManagement {
    repositories {
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

rootProject.name = "ToDoApp"
include(":app")
include(":designsystem")
include(":core-model")
include(":core-extensions")
include(":core-data")
include(":core-data:database")
include(":core-data:datastore")
include(":core-data:repository")
