pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal() // <-- AÑADIR ESTA LÍNEA ES LA SOLUCIÓN
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "ketekura"
include(":app")
