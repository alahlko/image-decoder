plugins {
    base
}

group = (findProperty("group") as String?) ?: "com.github.alahlko"
version = (findProperty("version") as String?) ?: "v1.0.1"

subprojects {
    group = rootProject.group
    version = rootProject.version
}

// JitPack runs: ./gradlew build publishToMavenLocal
// Root project normally doesn't have publishToMavenLocal, so we forward it.
tasks.register("publishToMavenLocal") {
    dependsOn(":image-decoder:publishToMavenLocal")
}