plugins {
    id("java")
    id("maven-publish")
}

group = "com.github.sniconmc.oblivion"
version = "0.1-dev"
description = "NPC dependency for the SniconMC Network"

repositories {
    mavenCentral()
    maven("https://jitpack.io")
}

dependencies {
    implementation("net.minestom:minestom-snapshots:65f75bb059") // Minestom
    implementation("net.kyori:adventure-text-minimessage:4.17.0") // MiniMessage
    implementation("com.github.SniconMC:Utils:0.1.6.4")

    implementation("org.slf4j:slf4j-api:2.0.15") // SLF4J API
    implementation("ch.qos.logback:logback-classic:1.5.7") // Logback classic
    implementation("ch.qos.logback:logback-core:1.5.7") // Logback core
}

tasks.withType<JavaCompile> {
    sourceCompatibility = "21"
    targetCompatibility = "21"
}

java {
    withSourcesJar()
    withJavadocJar()
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            from(components["java"])

            pom{
                name = "Utils"
                description = project.description
                url = "https://github.com/SniconMC/Oblivion"
                licenses {
                    license {
                        name = "The GNU Affero General Public License Version 3"
                        url = "https://www.gnu.org/licenses/agpl-3.0.txt"
                    }
                }
            }
        }
    }
}