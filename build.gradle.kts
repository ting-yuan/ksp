import com.google.devtools.ksp.configureKtlint
import com.google.devtools.ksp.configureKtlintApplyToIdea
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

if (!extra.has("kspVersion")) {
    val kotlinBaseVersion: String by project
    val today = LocalDateTime.now().format(DateTimeFormatter.BASIC_ISO_DATE)
    extra.set("kspVersion", "$kotlinBaseVersion-dev-experimental-$today")
}
repositories {
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/kotlin/p/kotlin/bootstrap/")
}
project.configureKtlintApplyToIdea()
subprojects {
    group = "com.google.devtools.ksp"
    version = rootProject.extra.get("kspVersion") as String
    this.configureKtlint()
    repositories {
        mavenCentral()
        google()
        maven("https://dl.bintray.com/kotlin/kotlin-eap")
        maven("https://maven.pkg.jetbrains.space/kotlin/p/kotlin/bootstrap/")
        jcenter()
    }
    tasks.withType<Jar>().configureEach {
        manifest.attributes.apply {
            put("Implementation-Vendor", "Google")
            put("Implementation-Version", project.version)
        }
    }
    pluginManager.withPlugin("maven-publish") {
        val publishExtension = extensions.getByType<PublishingExtension>()
        publishExtension.repositories {
            if (extra.has("outRepo")) {
                val outRepo = extra.get("outRepo") as String
                maven {
                    url = File(outRepo).toURI()
                }
            } else {
                mavenLocal()
            }
            maven {
                name = "test"
                url = uri("${rootProject.buildDir}/repos/test")
            }
        }
        publishExtension.publications.whenObjectAdded {
            check(this is MavenPublication) {
                "unexpected publication $this"
            }
            val publication = this
            publication.pom {
                url.set("https://goo.gle/ksp")
                licenses {
                    license {
                        name.set("The Apache License, Version 2.0")
                        url.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
                    }
                }
                developers {
                    developer {
                        name.set("KSP Team")
                    }
                }
                scm {
                    connection.set("scm:git:https://github.com/google/ksp.git")
                    developerConnection.set("scm:git:https://github.com/google/ksp.git")
                    url.set("https://github.com/google/ksp")
                }
            }
        }
    }
}
