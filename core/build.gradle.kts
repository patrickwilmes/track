plugins {
    kotlin("jvm") version "1.5.10"
}

version = "unspecified"

repositories {
    mavenCentral()
}

val exposedVersion: String by project
dependencies {
    implementation(kotlin("stdlib"))
    implementation("org.jetbrains.exposed:exposed-core:$exposedVersion")
    implementation("org.jetbrains.exposed:exposed-dao:$exposedVersion")
    implementation("org.jetbrains.exposed:exposed-jdbc:$exposedVersion")
    implementation("org.jetbrains.exposed:exposed-java-time:$exposedVersion")
    implementation("org.xerial:sqlite-jdbc:3.36.0.2")
    implementation("org.flywaydb:flyway-core:8.2.0")
    runtimeOnly("org.flywaydb:flyway-gradle-plugin:8.2.0")
}
