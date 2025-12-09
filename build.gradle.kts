plugins {
    kotlin("jvm") version "2.2.21"
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
    implementation("org.mongodb:mongodb-driver-sync:4.11.0")
    implementation("de.bwaldvogel:mongo-java-server:1.45.0")
    implementation("org.json:json:20231013")
    implementation("org.slf4j:slf4j-simple:2.0.9")
}

kotlin {
    jvmToolchain(21)
}

tasks.test {
    useJUnitPlatform()
}