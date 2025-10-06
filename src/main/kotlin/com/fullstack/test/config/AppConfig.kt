package com.fullstack.test.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@ConfigurationProperties(prefix = "app")
data class AppConfig(
    var products: ProductsConfig = ProductsConfig()
) {
    data class ProductsConfig(
        var api: ApiConfig = ApiConfig(),
        var clearOnRestart: Boolean = false
    ) {
        data class ApiConfig(
            var url: String = "https://famme.no/products.json"
        )
    }
}
