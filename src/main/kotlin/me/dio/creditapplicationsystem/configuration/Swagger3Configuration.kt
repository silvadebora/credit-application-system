package me.dio.creditapplicationsystem.configuration

import org.springdoc.core.models.GroupedOpenApi
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class Swagger3Configuration {

    // incrementar mais coisas
    @Bean
    fun publicApi(): GroupedOpenApi?{
        return GroupedOpenApi.builder()
            .group("springcreditapplicationsystem-public")
            .pathsToMatch("/api/customers/**", "/api/credits/**")
            .build()
    }


}