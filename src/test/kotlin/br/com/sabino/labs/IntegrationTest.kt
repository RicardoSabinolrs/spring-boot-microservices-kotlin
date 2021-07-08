package br.com.sabino.labs

import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.boot.test.context.SpringBootTest

@kotlin.annotation.Target(AnnotationTarget.CLASS)
@kotlin.annotation.Retention(AnnotationRetention.RUNTIME)
@SpringBootTest(classes = [SabinoLabsApp::class])
@ExtendWith(RedisTestContainerExtension::class)
annotation class IntegrationTest
