package br.com.sabino.labs.infra.config

import org.springframework.cloud.openfeign.EnableFeignClients
import org.springframework.cloud.openfeign.FeignClientsConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import

@Configuration
@EnableFeignClients(basePackages = ["br.com.sabino.labs"])
@Import(FeignClientsConfiguration::class)
class FeignConfiguration {

    @Bean
    internal fun feignLoggerLevel() = feign.Logger.Level.BASIC
}
