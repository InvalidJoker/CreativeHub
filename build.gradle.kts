import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    id("java")
    id("de.eldoria.plugin-yml.paper") version "0.7.1"
    id("io.papermc.paperweight.userdev") version "2.0.0-beta.16"
    id("com.gradleup.shadow") version "8.3.6"
    id("xyz.jpenilla.run-paper") version "2.3.1"
}

group = "world.novium"
version = "1.0-SNAPSHOT"

val mcVersion = "1.21.4"
val commandAPIVersion = "10.0.1"

paperweight {
    reobfArtifactConfiguration = io.papermc.paperweight.userdev
        .ReobfArtifactConfiguration.MOJANG_PRODUCTION
}


repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/")
}

val shadowDependencies = listOf(
    "dev.jorel:commandapi-bukkit-shade-mojang-mapped:$commandAPIVersion",
    "dev.triumphteam:triumph-gui:3.1.11"
)

dependencies {
    paperweight.paperDevBundle("$mcVersion-R0.1-SNAPSHOT")

    shadowDependencies.forEach { dependency ->
        implementation(dependency)
        shadow(dependency)
    }

    implementation(platform("com.intellectualsites.bom:bom-newest:1.52"))
    compileOnly("com.intellectualsites.plotsquared:plotsquared-core")
    compileOnly("com.intellectualsites.plotsquared:plotsquared-bukkit") { isTransitive = false }
}


tasks {
    build {
        dependsOn("shadowJar")
        dependsOn(reobfJar)
    }

    withType<ShadowJar> {
        mergeServiceFiles()
        configurations = listOf(project.configurations.shadow.get())
        archiveFileName.set("${project.name}.jar")
    }

    runServer {
        minecraftVersion(mcVersion)
    }
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(21))
}

paper {
    main = "world.novium.creative.CreativePlugin"
    apiVersion = "1.19"
    authors = listOf("InvalidJoker")
}