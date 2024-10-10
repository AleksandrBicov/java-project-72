plugins {
    id("java")
    
}
group = "hexlet.code"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.test {
    useJUnitPlatform()
}

tasks.register<JavaExec>("runApp") {
    group = "application"
    classpath = sourceSets["main"].runtimeClasspath
    mainClass.set("hexlet.code.App")
}