package br.com.sabino.lab

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class SpringBootMicroservicesKotlinApplication

fun main(args: Array<String>) {
    runApplication<SpringBootMicroservicesKotlinApplication>(*args)
}
