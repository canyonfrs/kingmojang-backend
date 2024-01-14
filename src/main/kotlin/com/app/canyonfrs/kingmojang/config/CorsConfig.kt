package com.app.canyonfrs.kingmojang.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.CorsRegistration
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class CorsConfig {
    @Value("\${cors.allowed-origins}")
    var corsAllowedOrigins: List<String> = listOf()

    @Bean
    fun corsConfigurer(): WebMvcConfigurer {
        return object : WebMvcConfigurer {
            override fun addCorsMappings(registry: CorsRegistry) {
                val corsRegistration: CorsRegistration = registry.addMapping("/**")
                corsAllowedOrigins.forEach { origin -> corsRegistration.allowedOrigins(origin) }
            }
        }
    }
}
