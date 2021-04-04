package it.lorenzobugiani.springboottestcontainerkotlin

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class SpringBootTestcontainerKotlinApplication

fun main(args: Array<String>) {
    runApplication<SpringBootTestcontainerKotlinApplication>(*args)
}
