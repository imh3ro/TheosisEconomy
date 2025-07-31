plugins {
    id("java")
    id("com.gradleup.shadow") version "8.3.5"
}

group = "me.Short.TheosisEconomy"
version = "1.44"

tasks {
    build {
        dependsOn(shadowJar)
    }

    shadowJar {
        // Make it so that a separate jar with "-all" at the end doesn't generate (https://imperceptiblethoughts.com/shadow/configuration/#configuring-output-name)
        archiveClassifier.set("")

        // Relocations
        relocate("org.bstats", "shadow.org.bstats")
        relocate("net.kyori", "shadow.net.kyori")
        relocate("org.apache.commons", "shadow.org.apache.commons")
        relocate("com.google", "shadow.com.google")
    }
}

repositories {
    mavenCentral()

    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
    maven("https://oss.sonatype.org/content/repositories/snapshots/")
    maven("https://jitpack.io")
    maven("https://repo.extendedclip.com/content/repositories/placeholderapi/")
}

dependencies {
    implementation("org.bstats:bstats-bukkit:3.0.2")

    implementation("net.kyori:adventure-api:4.20.0")
    implementation("net.kyori:adventure-text-minimessage:4.20.0")
    implementation("net.kyori:adventure-platform-bukkit:4.3.4")
    implementation("net.kyori:adventure-text-serializer-plain:4.21.0")

    implementation("org.apache.commons:commons-text:1.13.1")
    implementation("org.apache.commons:commons-collections4:4.5.0")

    implementation("com.google.code.gson:gson:2.13.1")

    compileOnly("org.spigotmc:spigot-api:1.13.2-R0.1-SNAPSHOT")
    compileOnly("com.github.MilkBowl:VaultAPI:1.7.1")
    compileOnly("com.gitlab.ruany:LiteBansAPI:0.5.0")
    compileOnly("me.clip:placeholderapi:2.11.6")
}