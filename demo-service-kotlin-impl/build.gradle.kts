import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

group = "ru.zubmike"
version = "1.0"

plugins {
    kotlin("jvm") version "1.3.41"
    application
    id("com.github.johnrengelman.shadow") version "5.0.0"
}

application {
    mainClassName = "ru.zubmike.service.demo.MainKt"
}

repositories {
    mavenLocal()
    mavenCentral()
    jcenter()
}

val commonServiceVersion = "1.0"
val demoServiceApiVersion = "1.0"
val ktorVersion = "1.2.3"
val koinVersion = "2.0.1"
val jacksonVersion = "2.9.9"
val hibernateVersion = "5.4.4.Final"
val ehcacheVersion = "3.5.2"
val h2Version = "1.4.199"
val junitVersion = "4.12"
val mockitoCoreVersion = "3.0.0"

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    compile("ru.zubmike:common-service:$commonServiceVersion")
    compile("ru.zubmike:demo-service-api:$demoServiceApiVersion")
    compile("org.koin:koin-ktor:$koinVersion")
    compile("io.ktor:ktor-server-netty:$ktorVersion")
    compile("io.ktor:ktor-jackson:$ktorVersion")
    compile("com.fasterxml.jackson.dataformat:jackson-dataformat-yaml:$jacksonVersion")
    compile("org.hibernate:hibernate-jcache:$hibernateVersion")
    compile("org.ehcache:ehcache:$ehcacheVersion")
    compile("com.h2database:h2:$h2Version")

    testCompile("junit:junit:$junitVersion")
    testCompile("org.mockito:mockito-core:$mockitoCoreVersion")
}

tasks {
    withType<KotlinCompile> {
        kotlinOptions.jvmTarget = "11"
    }
    shadowJar {
        archiveBaseName.set("demo-service-kotlin")
        archiveClassifier.set("")
    }
}