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

// TEST PROCESSOR: ImplicitElementProcessor
// EXPECTED:
// <init>; origin: SYNTHETIC
// synthetic constructor for Cls
// <null>
// <null>
// <init>,<init>,<init>
// readOnly.get(): SYNTHETIC annotations from property: GetAnno
// readOnly.getter.owner: readOnly: KOTLIN
// readWrite.get(): KOTLIN
// readWrite.set(): SYNTHETIC annotations from property: SetAnno
// <init>
// comp1.get(): SYNTHETIC
// comp2.get(): SYNTHETIC
// comp2.set(): SYNTHETIC
// <init>
// synthetic constructor for ImplictConstructorJava
// Test, p: [MyKotlinAnnotation: null, MyJavaAnnotation: null]
// lib.Test, p: [MyKotlinAnnotation: null, MyJavaAnnotation: null]
// END
// MODULE: lib
// FILE: lib/MyJavaAnnotation.java
package lib;
import static java.lang.annotation.ElementType.CONSTRUCTOR;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.LOCAL_VARIABLE;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.MODULE;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.ElementType.TYPE;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({FIELD})
public @interface MyJavaAnnotation {}

// FILE: lib/MyKotlinAnnotation.kt
package lib
@Target(AnnotationTarget.PROPERTY)
annotation class MyKotlinAnnotation

// FILE: lib/Test.kt
package lib
class Test(
    @MyKotlinAnnotation
    @MyJavaAnnotation
    val p: Int
)

// MODULE: main(lib)
// FILE: Test.kt
import lib.*
class Test(
    @MyKotlinAnnotation
    @MyJavaAnnotation
    val p: Int
)
// FILE: a.kt
annotation class GetAnno
annotation class SetAnno

class Cls {
    @get:GetAnno
    val readOnly: Int = 1

    @set:SetAnno
    var readWrite: Int = 2
    get() = 1
}

data class Data(@get:GetAnno val comp1: Int, var comp2: Int)

class ClassWithoutImplicitPrimaryConstructor : ITF {
    constructor(x: Int)
}

interface ITF

// FILE: JavaClass.java
public class JavaClass {
    public JavaClass() { this(1); }
    public JavaClass(int a) { this(a, "ok"); }
    public JavaClass(int a, String s) { }
}

// FILE:ImplictConstructorJava.java

public class ImplictConstructorJava {

}
