import dev.architectury.pack200.java.Pack200Adapter
import net.fabricmc.loom.task.RemapSourcesJarTask

plugins {
    kotlin("jvm") version "1.9.20"
    kotlin("plugin.serialization") version "1.9.20"
    id("com.github.johnrengelman.shadow") version "8.1.1"
    id("gg.essential.loom") version "0.10.0.+"
    id("dev.architectury.architectury-pack200") version "0.1.3"
    java
    `maven-publish`
}

version = "0.0.2-h"
group = "me.cephetir"

base {
    archivesName.set("BladeCore")
}

loom {
    launchConfigs {
        getByName("client") {
            arg("--tweakClass", "gg.essential.loader.stage0.EssentialSetupTweaker")
            arg("--mixin", "mixins.bladecore.json")
        }
    }
    runConfigs {
        getByName("client") {
            isIdeConfigGenerated = true
        }
        remove(getByName("server"))
    }
    forge {
        pack200Provider.set(Pack200Adapter())
        mixinConfig("mixins.bladecore.json")
    }
    mixin {
        defaultRefmapName.set("mixins.bladecore.refmap.json")
    }
}

val shade: Configuration by configurations.creating {
    configurations.implementation.get().extendsFrom(this)
}

repositories {
    mavenCentral()
    maven("https://jitpack.io")
    maven("https://maven.ilarea.ru/snapshots")
}

dependencies {
    // Minecraft Forge
    minecraft("com.mojang:minecraft:1.8.9")
    mappings("de.oceanlabs.mcp:mcp_stable:22-1.8.9")
    forge("net.minecraftforge:forge:1.8.9-11.15.1.2318-1.8.9")

    // Essential
    val essential = "gg.essential:essential-1.8.9-forge:14586+g3a23fbfae6"
    api(essential)
    compileOnly(essential)
    runtimeOnly("gg.essential:loader-launchwrapper:1.1.3") {
        isTransitive = false
    }

    // Kotlin
    include(platform(kotlin("bom")))
    include(kotlin("stdlib-jdk8"))
    include(kotlin("reflect"))
    include("org.jetbrains.kotlinx:kotlinx-serialization-json-jvm:1.6.1")
    include("org.jetbrains.kotlinx:kotlinx-coroutines-core-jvm:1.7.3")

    // Discord RPC
    include("com.github.jagrosh:DiscordIPC:a8d6631")

    // Mixins
    annotationProcessor("org.spongepowered:mixin:0.8.5:processor")
    api("org.spongepowered:mixin:0.8.5")

    // WebSocket
    include("org.java-websocket:Java-WebSocket:1.5.3")

    // Optifine
    compileOnly(files("libs/preview_OptiFine_1.8.9_HD_U_M6_pre2.jar"))
}

fun DependencyHandlerScope.include(dependency: Any) {
    api(dependency)
    shade(dependency)
}

sourceSets {
    main {
        output.setResourcesDir(file("${layout.buildDirectory.asFile.get()}/classes/kotlin/main"))
    }
}

java {
    withSourcesJar()
}

tasks {
    processResources {
        inputs.property("version", project.version)
        inputs.property("mcversion", "1.8.9")

        filesMatching("mcmod.info") {
            expand(mapOf("version" to project.version, "mcversion" to "1.8.9"))
        }
        dependsOn(compileJava)
    }

    jar {
        duplicatesStrategy = DuplicatesStrategy.EXCLUDE
        manifest {
            attributes(
                mapOf(
                    "ForceLoadAsMod" to true,
                    "ModSide" to "CLIENT",
                    "TweakOrder" to "0",
                    "MixinConfigs" to "mixins.bladecore.json",
                )
            )
        }
        dependsOn(shadowJar)
        archiveClassifier.set("")
        //enabled = false
    }
    remapJar {
        archiveClassifier.set("full")
        input.set(shadowJar.get().archiveFile)
    }
    shadowJar {
        archiveClassifier.set("full-dev")
        duplicatesStrategy = DuplicatesStrategy.EXCLUDE
        configurations = listOf(shade)

        //relocate("com.jagrosh.discordipc", "me.cephetir.bladecore.discord.discordipc")

        exclude(
            "**/LICENSE.md",
            "**/LICENSE.txt",
            "**/LICENSE",
            "**/NOTICE",
            "**/NOTICE.txt",
            "pack.mcmeta",
            "dummyThing",
            "**/module-info.class",
            "META-INF/proguard/**",
            "META-INF/maven/**",
            "META-INF/versions/**",
            "META-INF/com.android.tools/**",
            "fabric.mod.json"
        )
    }
    named<Jar>("sourcesJar") {
        archiveClassifier.set("sources")
        doFirst {
            archiveClassifier.set("sources")
        }
        exclude("me/cephetir/bladecore/BladeCore**")
        exclude("me/cephetir/bladecore/forge/**")
        exclude("me/cephetir/bladecore/mixins**")
        exclude("mixins.bladecore.json")
        exclude("fabric.mod.json")
    }
    withType<JavaCompile> {
        options.encoding = "UTF-8"
    }
    withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
        kotlinOptions {
            jvmTarget = "1.8"
            languageVersion = "1.7"

            freeCompilerArgs =
                listOf(
                    "-Xjvm-default=all",
                    "-Xbackend-threads=0",
                    //"-Xuse-k2"
                )
        }
        kotlinDaemonJvmArguments.set(
            listOf(
                "-Xmx4G",
                "-Dkotlin.enableCacheBuilding=true",
                "-Dkotlin.useParallelTasks=true",
                "-Dkotlin.enableFastIncremental=true"
            )
        )
    }
    withType<RemapSourcesJarTask> {
        enabled = false
    }
}

kotlin {
    jvmToolchain {
        languageVersion.set(JavaLanguageVersion.of(8))
    }
}

configure<PublishingExtension> {
    publications.apply {
        create("maven", MavenPublication::class.java).apply {
            artifactId = "bladecore-1.8.9-forge"
            version = project.version.toString()

            from(components.getByName("java"))
        }
    }

    if (project.hasProperty("bladeCoreMavenUser")) {
        repositories {
            maven {
                name = "releases"
                url = uri("https://maven.ilarea.ru/releases")
                credentials {
                    username = project.property("bladeCoreMavenUser") as String
                    password = project.property("bladeCoreMavenPass") as String
                }
                authentication {
                    create<BasicAuthentication>("basic")
                }
            }
            maven {
                name = "snapshots"
                url = uri("https://maven.ilarea.ru/snapshots")
                credentials {
                    username = project.property("bladeCoreMavenUser") as String
                    password = project.property("bladeCoreMavenPass") as String
                }
                authentication {
                    create<BasicAuthentication>("basic")
                }
            }
        }
    }
}