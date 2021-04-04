package it.lorenzobugiani.springboottestcontainerkotlin

import io.restassured.RestAssured
import io.restassured.http.ContentType
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.http.HttpStatus
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.testcontainers.containers.MongoDBContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers

@Testcontainers
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestInstance(Lifecycle.PER_CLASS)
class ControllerIT {

    companion object {
        @Container
        @JvmStatic
        var mongoDBContainer = MongoDBContainer("mongo:4")

        @DynamicPropertySource
        @JvmStatic
        fun setProperties(registry: DynamicPropertyRegistry) {
            mongoDBContainer.start()
            registry.add("spring.data.mongodb.uri") { mongoDBContainer.replicaSetUrl }
        }
    }

    @Autowired
    private lateinit var mongoTemplate: MongoTemplate

    @LocalServerPort
    private var randomServerPort: Int = 0

    @BeforeAll
    fun setUpAll() {
        RestAssured.port = randomServerPort
    }

    @BeforeEach
    fun setUp() {
        mongoTemplate.db.drop()
    }

    @Test
    fun `save test`() {
        val result = RestAssured
            .given()
            .contentType(ContentType.TEXT)
            .body("text")
            .`when`()
            .post("/api")
            .then()
            .assertThat()
            .statusCode(HttpStatus.OK.value())
            .extract().body().`as`(MongoSampleEntity::class.java)

        assertThat(mongoTemplate.findAll(MongoSampleEntity::class.java)).hasSize(1)

        assertThat(mongoTemplate.findById(result.id, MongoSampleEntity::class.java)).isEqualTo(result)
    }

    @Test
    fun `find test`() {
        val entity = MongoSampleEntity(text = "text")
        mongoTemplate.save(entity)

        val result = RestAssured
            .given()
            .pathParam("id", entity.id)
            .`when`()
            .get("/api/{id}")
            .then()
            .assertThat()
            .statusCode(HttpStatus.OK.value())
            .extract().body().`as`(MongoSampleEntity::class.java)

        assertThat(result).isEqualTo(entity)
    }

    @Test
    fun `save and retrieve`() {
        val entitySaved = RestAssured
            .given()
            .contentType(ContentType.TEXT)
            .body("text")
            .`when`()
            .post("/api")
            .then()
            .assertThat()
            .statusCode(HttpStatus.OK.value())
            .extract().body().`as`(MongoSampleEntity::class.java)

        val entityRetrieved = RestAssured
            .given()
            .pathParam("id", entitySaved.id)
            .`when`()
            .get("/api/{id}")
            .then()
            .assertThat()
            .statusCode(HttpStatus.OK.value())
            .extract().body().`as`(MongoSampleEntity::class.java)

        assertThat(entityRetrieved).isEqualTo(entitySaved)
    }
}