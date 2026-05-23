sourceSets {
    named("main") {
        java {
            srcDir("src/main/java")
            exclude("com/krishiai/app/data/**")
            exclude("com/krishiai/app/di/**")
            exclude("com/krishiai/app/ui/**")
        }
    }
}
