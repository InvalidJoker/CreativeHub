import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    id("java")
    id("de.eldoria.plugin-yml.paper") version "0.7.1"
    id("io.papermc.paperweight.userdev") version "2.0.0-beta.16"
    id("com.gradleup.shadow") version "8.3.6"
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
}

val shadowDependencies = listOf(
    "dev.jorel:commandapi-bukkit-shade-mojang-mapped:$commandAPIVersion",
)

dependencies {
    paperweight.paperDevBundle("$mcVersion-R0.1-SNAPSHOT")

    shadowDependencies.forEach { dependency ->
        implementation(dependency)
        shadow(dependency)
    }
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
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(21))
}

paper {
    main = "world.novium.creative.CreativePlugin"
    apiVersion = "1.19"
    authors = listOf("InvalidJoker")
}