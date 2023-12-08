plugins {
    id("java")
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    // https://mvnrepository.com/artifact/ch.qos.logback/logback-classic
    implementation("ch.qos.logback:logback-classic:1.4.14")
    implementation("joda-time:joda-time:2.3")
    implementation("org.apache.activemq:activemq-broker:6.0.0")
    implementation("org.apache.activemq:activemq-kahadb-store:6.0.0")
   // implementation("org.apache.activemq:activemq-all:6.0.0")
    implementation("org.apache.activemq:activemq-jaas:6.0.0")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.16.0")
    //implementation("javax.jms:javax.jms-api:2.0.1")
    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testImplementation("junit:junit:4.13.1")
}

tasks.test {
    useJUnitPlatform()
}