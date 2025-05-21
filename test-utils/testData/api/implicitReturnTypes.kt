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

// WITH_RUNTIME
// TEST PROCESSOR: ImplicitReturnTypesProcessor
// EXPECTED:
// fun1: MyClass0: MyClass0
// fun2: MyClass1: MyClass1
// fun3: MyClass2: MyClass2
// fun4: MyClass2: MyClass2
// fun5: MyClass2: MyClass2
// fun6: MyClass1: MyClass1
// END

class MyClass0
class MyClass1
class MyClass2

class HasInvoke1 {
    operator fun invoke(): MyClass1 = MyClass1()
}

class HasInvoke2 {
    operator fun invoke() = MyClass2()
}

class HasInvoke3 {
    operator fun invoke(hasInvoke2: HasInvoke2) = hasInvoke2()
}

fun interface Sam {
    operator fun invoke(): MyClass1
}

fun fun1() = MyClass0()
fun fun2(hasInvoke1: HasInvoke1) = hasInvoke1()
fun fun3(hasInvoke2: HasInvoke2) = hasInvoke2()
fun fun4(hasInvoke3: HasInvoke3) = hasInvoke3()
fun fun5() = HasInvoke3()()
fun fun6() = Sam { return MyClass1() } ()
