import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.springframework.boot") version "2.2.6.RELEASE"
    id("io.spring.dependency-management") version "1.0.9.RELEASE"
    kotlin("jvm") version "1.3.71"
    kotlin("plugin.spring") version "1.3.71"
}

group = "br.com.sabino.lab"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_11

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("io.dropwizard.metrics:metrics-core")
    implementation("io.micrometer:micrometer-registry-prometheus")
    implementation("net.logstash.logback:logstash-logback-encoder") version ""
    implementation("com.zaxxer:HikariCP")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.springframework.boot:spring-boot-starter-logging")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("io.prometheus:simpleclient_spring_boot:${prometheus_simpleclient_spring_boot_version}")
    implementation("com.google.code.gson:gson:${gson_version}")
    implementation("org.mapstruct:mapstruct") version "${mapstruct_version}"
    implementation("com.h2database:h2")
    implementation("org.projectlombok:lombok") version "${lombok_version}"
    implementation("io.springfox:springfox-swagger2:${swagger_version}")
    implementation("io.springfox:springfox-swagger-ui:${swagger_version}")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    compileOnly("org.projectlombok:lombok")
    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor:${spring_boot_version}") {
        exclude(group = "com.vaadin.external.google", module = "android-json")
    }
    annotationProcessor("org.mapstruct:mapstruct-processor:${mapstruct_version}")


    testImplementation("org.springframework.boot:spring-boot-starter-test") {
        exclude(group = "org.junit.vintage", module = "junit-vintage-engine")
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "14"
    }
}
