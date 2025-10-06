package com.fullstack.test.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.client.SimpleClientHttpRequestFactory
import org.springframework.web.client.RestTemplate

@Configuration
class WebConfig {

    @Bean
    fun restTemplate(): RestTemplate {
        val factory = SimpleClientHttpRequestFactory()
        factory.setConnectTimeout(30000) // 30 seconds connection timeout
        factory.setReadTimeout(60000)    // 60 seconds read timeout
        return RestTemplate(factory)
    }
}

