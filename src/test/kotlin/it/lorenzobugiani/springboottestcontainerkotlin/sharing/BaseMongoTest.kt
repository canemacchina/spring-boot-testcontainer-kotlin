package it.lorenzobugiani.springboottestcontainerkotlin.sharing

import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.testcontainers.containers.MongoDBContainer

open class BaseMongoTest {

    companion object {
        @JvmStatic
        var mongoDBContainer: MongoDBContainer = MongoDBContainer("mongo:4")
            .apply { start() }

        @DynamicPropertySource
        @JvmStatic
        fun setProperties(registry: DynamicPropertyRegistry) {
            registry.add("spring.data.mongodb.uri") { mongoDBContainer.replicaSetUrl }
        }
    }
}