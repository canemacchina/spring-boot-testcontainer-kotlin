package it.lorenzobugiani.springboottestcontainerkotlin.sharing

import org.springframework.boot.test.util.TestPropertyValues
import org.springframework.context.ApplicationContextInitializer
import org.springframework.context.ConfigurableApplicationContext
import org.springframework.test.context.ContextConfiguration
import org.testcontainers.containers.MongoDBContainer
import org.testcontainers.junit.jupiter.Testcontainers

class MongoInitializer : ApplicationContextInitializer<ConfigurableApplicationContext> {

    override fun initialize(context: ConfigurableApplicationContext) {
        TestPropertyValues
            .of(listOf("spring.data.mongodb.uri=${MongoContainerSingleton.instance.replicaSetUrl}"))
            .applyTo(context.environment)
    }
}

object MongoContainerSingleton {
    val instance: MongoDBContainer by lazy { startMongoContainer() }
    private fun startMongoContainer() = MongoDBContainer("mongo:4")
        .apply { start() }
}

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.CLASS)
@ContextConfiguration(initializers = [MongoInitializer::class])
@Testcontainers
annotation class MongoTest