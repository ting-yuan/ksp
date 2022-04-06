/*
 * Copyright 2020 Google LLC
 * Copyright 2010-2020 JetBrains s.r.o. and Kotlin Programming Language contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.google.devtools.ksp.gradle

import com.google.devtools.ksp.gradle.testing.KspIntegrationTestRule
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder

class ProcessorClasspathConfigurationsTest {
    @Rule
    @JvmField
    val tmpDir = TemporaryFolder()

    @Rule
    @JvmField
    val testRule = KspIntegrationTestRule(tmpDir)

    private val kspConfigs by lazy {
        """configurations.matching { it.name.startsWith("ksp") && !it.name.endsWith("ProcessorClasspath") }"""
    }

    // config name is <KotlinCompileTaskName>.replace("compile", "ksp") + "ProcessorClasspath"
    // they should extend all non-empty ksp configurations
    @Test
    fun testConfigurationsForSinglePlatformApp() {
        testRule.setupAppAsJvmApp()
        testRule.appModule.addSource("Foo.kt", "class Foo")
        testRule.appModule.buildFileAdditions.add(
            """
                $kspConfigs.all {
                    // Make sure ksp configs are not empty.
                    project.dependencies.add(name, "androidx.room:room-compiler:2.4.2")
                }
                tasks.register("testConfigurations") {
                    // Resolve all tasks to trigger classpath config creation
                    dependsOn(tasks["tasks"])
                    doLast {
                        val main = configurations["kspKotlinProcessorClasspath"]
                        val test = configurations["kspTestKotlinProcessorClasspath"]
                        require(main.extendsFrom.map { it.name } == listOf("ksp"))
                        require(test.extendsFrom.map { it.name } == listOf("kspTest", "ksp"))
                    }
                }
            """.trimIndent()
        )
        testRule.runner()
            .withArguments(":app:testConfigurations", "--info")
            .build()
    }

    @Test
    fun testConfigurationsForSinglePlatformAppDisallowAll() {
        testRule.setupAppAsJvmApp()
        testRule.appModule.addSource("Foo.kt", "class Foo")
        testRule.appModule.buildFileAdditions.add(
            """
                $kspConfigs.all {
                    // Make sure ksp configs are not empty.
                    project.dependencies.add(name, "androidx.room:room-compiler:2.4.2")
                }
                tasks.register("testConfigurations") {
                    // Resolve all tasks to trigger classpath config creation
                    dependsOn(tasks["tasks"])
                    doLast {
                        val main = configurations["kspKotlinProcessorClasspath"]
                        val test = configurations["kspTestKotlinProcessorClasspath"]
                        require(main.extendsFrom.map { it.name } == listOf("ksp"))
                        require(test.extendsFrom.map { it.name } == listOf("kspTest"))
                    }
                }
            """.trimIndent()
        )
        testRule.runner()
            .withArguments(":app:testConfigurations", "-Pksp.allow.all.target.configuration=false")
            .build()
    }

    @Test
    fun testConfigurationsForMultiPlatformApp() {
        testRule.setupAppAsMultiplatformApp(
            """
                kotlin {
                    jvm { }
                    js { browser() }
                }
            """.trimIndent()
        )
        testRule.appModule.addMultiplatformSource("commonMain", "Foo.kt", "class Foo")
        testRule.appModule.buildFileAdditions.add(
            """
                $kspConfigs.matching { it.name != "ksp" }.all {
                    // Make sure ksp configs are not empty.
                    project.dependencies.add(name, "androidx.room:room-compiler:2.4.2")
                }
                tasks.register("testConfigurations") {
                    // Resolve all tasks to trigger classpath config creation
                    dependsOn(tasks["tasks"])
                    doLast {
                        val jvmMain = configurations["kspKotlinJvmProcessorClasspath"]
                        val jvmTest = configurations["kspTestKotlinJvmProcessorClasspath"]
                        val jsMain = configurations["kspKotlinJsProcessorClasspath"]
                        val jsTest = configurations["kspTestKotlinJsProcessorClasspath"]
                        require(jvmMain.extendsFrom.map { it.name } == listOf("kspJvm"))
                        require(jvmTest.extendsFrom.map { it.name } == listOf("kspJvmTest"))
                        require(jsMain.extendsFrom.map { it.name } == listOf("kspJs"))
                        require(jsTest.extendsFrom.map { it.name } == listOf("kspJsTest"))
                    }
                }
            """.trimIndent()
        )
        testRule.runner()
            .withArguments(":app:testConfigurations")
            .build()
    }
}
