plugins {
    java
}

group = "company.maxmc"
version = "1.0-SNAPSHOT"

repositories {
    mavenLocal()
    maven("https://hub.spigotmc.org/nexus/content/repositories/public/") {
        name = "spigotmc-public"
    }
    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/") {
        name = "spigotmc-snapshots"
    }
}

dependencies {
    compileOnly("org.spigotmc:spigot-api:1.12.2-R0.1-SNAPSHOT")
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
    sourceCompatibility = "1.8"
    targetCompatibility = "1.8"
}

tasks.withType<ProcessResources> {
    from(sourceSets.main.get().resources.srcDirs) {
        expand("version" to rootProject.version)
    }
}
