plugins {
    java
    id("org.springframework.boot") version "2.7.14"
    id("io.spring.dependency-management") version "1.1.2"
}

group = "com.tap-apac-hackathon"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-web")
    compileOnly("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    runtimeOnly("com.h2database:h2")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310")
    implementation("com.mysql:mysql-connector-j")
    implementation("org.json:json:20230227")
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}