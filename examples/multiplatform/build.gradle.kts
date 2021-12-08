plugins {
    kotlin("multiplatform") apply false
}

allprojects {
    repositories {
        mavenCentral()
        maven("https://oss.sonatype.org/content/repositories/comgoogledevtools-1090/")
    }
}
