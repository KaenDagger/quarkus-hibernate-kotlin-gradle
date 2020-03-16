package org.acme.fruitStore

import io.quarkus.test.junit.NativeImageTest

@NativeImageTest
open class NativeFruitResourceIT : FruitResourceTest()