package br.com.sabino.lab.restful.api

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

@SpringBootApplication
class RestfulApiApplication

fun main(args: Array<String>) {
    SpringApplication.run(RestfulApiApplication::class.java, *args)
}