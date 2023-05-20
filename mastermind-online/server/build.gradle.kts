plugins {
    java
    `java-library`
    application
}

repositories {
    mavenCentral()
}

dependencies {
    api("io.javalin:javalin:5.1.3")

    api(project(":common"))

    implementation("org.apache.commons:commons-collections4:4.4")
    implementation("org.slf4j:slf4j-simple:2.0.3")

    annotationProcessor("io.javalin.community.openapi:openapi-annotation-processor:5.1.3")

    implementation("io.javalin.community.openapi:javalin-openapi-plugin:5.1.3") // for /openapi route with JSON scheme
    implementation("io.javalin.community.openapi:javalin-swagger-plugin:5.1.3") // for Swagger UI
}

tasks.getByName<JavaExec>("run") {
    standardInput = System.`in`
    if (project.hasProperty("port")) {
        args(project.property("port"))
    }
}

application {
    mainClass.set("it.negri.negri.mastermind.server.MastermindService")
}