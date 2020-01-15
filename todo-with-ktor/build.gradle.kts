val kotlin_version: String by project
val ktor_version: String by project
val exposed_version: String by project
val h2_version: String by project
val hikaricp_version: String by project
val flyway_version: String by project
val logback_version: String by project

plugins {
    application
    kotlin("jvm") version "1.3.61"
}

group = "dev.akif"
version = "0.0.1-SNAPSHOT"

application {
    mainClassName = "io.ktor.server.netty.EngineMain"
}

repositories {
    mavenLocal()
    jcenter()
}

dependencies {
    // Kotlin
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:$kotlin_version")
    // Ktor
    implementation("io.ktor:ktor-server-netty:$ktor_version")
    implementation("io.ktor:ktor-gson:$ktor_version")
    // Database
    implementation("org.jetbrains.exposed:exposed-core:$exposed_version")
    implementation("org.jetbrains.exposed:exposed-jdbc:$exposed_version")
    implementation("org.jetbrains.exposed:exposed-java-time:$exposed_version")
    implementation("com.h2database:h2:$h2_version")
    implementation("com.zaxxer:HikariCP:$hikaricp_version")
    implementation("org.flywaydb:flyway-core:$flyway_version")
    // Logging
    implementation("ch.qos.logback:logback-classic:$logback_version")
    // Tests
    testImplementation("io.ktor:ktor-server-tests:$ktor_version")
}

kotlin.sourceSets["main"].kotlin.srcDirs("src")
kotlin.sourceSets["test"].kotlin.srcDirs("test")

sourceSets["main"].resources.srcDirs("resources")
sourceSets["test"].resources.srcDirs("testresources")
