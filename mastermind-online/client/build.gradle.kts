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
}

application {
    //mainClass.set("")
}