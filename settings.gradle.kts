rootProject.name = "BladeCore"

pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenCentral()
        maven("https://oss.sonatype.org/content/repositories/snapshots")
        maven("https://maven.architectury.dev/")
        maven("https://maven.fabricmc.net")
        maven("https://maven.minecraftforge.net/") {
            content {
                includeGroup("net.minecraftforge")
                includeGroup("de.oceanlabs.mcp")
            }
        }
        maven("https://maven.ilarea.ru/snapshots")
        maven("https://jitpack.io")
    }
}