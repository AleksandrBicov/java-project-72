plugins {
    id("java")
    id("application")
    id("io.freefair.lombok") version "8.6"

}
group = "hexlet.code"
version = "1.0-SNAPSHOT"


repositories {
    mavenCentral()
}

dependencies {
    implementation("io.javalin:javalin:6.1.3")
    implementation("io.javalin:javalin-rendering:6.1.3")
    implementation("gg.jte:jte:3.1.12")
    implementation("io.javalin:javalin-bundle:6.1.3")


    implementation("org.slf4j:slf4j-simple:2.0.7")

    implementation("com.h2database:h2:2.2.220")
    implementation("com.zaxxer:HikariCP:5.0.1")

    implementation ("com.konghq:unirest-java:3.11.09")
    implementation ("org.jsoup:jsoup:1.14.3")

    implementation("net.datafaker:datafaker:2.0.1")
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testImplementation("org.assertj:assertj-core:3.23.1")
}

tasks.test {
    useJUnitPlatform()
}

application {
    mainClass.set("hexlet.code.App")
}

tasks.register<JavaExec>("runMain") {
    group = "application"
    description = "Runs the main class"
    classpath = sourceSets["main"].runtimeClasspath
    mainClass.set("hexlet.code.App")
}