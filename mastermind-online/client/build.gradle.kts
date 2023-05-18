plugins {
    java
    `java-library`
    application
}

dependencies {
    api(project(":common"))
}

tasks.getByName<JavaExec>("run") {
    standardInput = System.`in`
    if (project.hasProperty("host")) {
        args(project.property("host"))
    }
    if (project.hasProperty("port")) {
        args(project.property("port"))
    }
}

application {
    mainClass.set("it.negri.mastermind.client.MastermindClient")
}