plugins {
    java
}

allprojects {
    apply(plugin = "java")

    repositories {
        mavenCentral()
    }
}

subprojects {
    apply(plugin = "java")

    dependencies {
        testImplementation("org.junit.jupiter:junit-jupiter:5.9.2")
    }

    tasks.test {
        useJUnitPlatform()
    }

    java {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}
