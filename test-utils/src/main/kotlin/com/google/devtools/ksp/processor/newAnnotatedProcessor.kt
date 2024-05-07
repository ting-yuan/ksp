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

package com.google.devtools.ksp.processor

import com.google.devtools.ksp.processing.Dependencies
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.symbol.KSAnnotated

class NewAnnotatedProcessor : AbstractTestProcessor() {
    private val result = mutableListOf<String>()
    lateinit private var environment: SymbolProcessorEnvironment

    override fun toResult(): List<String> {
        return result
    }

    private var generated = false
    override fun process(resolver: Resolver): List<KSAnnotated> {
        if (!generated) {
            generated = true
            environment.codeGenerator.createNewFile(Dependencies(false), "com.example", "GeneratedK").use {
                it.write("package com.example;".toByteArray())
                it.write("@com.example.A interface GeneratedK {}".toByteArray())
            }
            environment.codeGenerator.createNewFile(Dependencies(false), "com.example", "GeneratedJ", "java").use {
                it.write("package com.example;".toByteArray())
                it.write("@com.example.A interface GeneratedJ {}".toByteArray())
            }
        }
        resolver.getSymbolsWithAnnotation("com.example.A").forEach {
            result.add(it.toString())
        }
        return emptyList()
    }

    override fun create(environment: SymbolProcessorEnvironment): SymbolProcessor {
        this.environment = environment
        return this
    }
}
