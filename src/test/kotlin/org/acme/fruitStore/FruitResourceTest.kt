package org.acme.fruitStore

import io.quarkus.test.junit.QuarkusTest
import io.restassured.RestAssured.given
import org.hamcrest.CoreMatchers.`is`
import org.junit.jupiter.api.Test

@QuarkusTest
open class FruitResourceTest {

    @Test
    fun testHelloEndpoint() {
        given()
          .`when`().get("/fruits")
          .then()
             .statusCode(200)
             .body(`is`("hello"))
    }

}