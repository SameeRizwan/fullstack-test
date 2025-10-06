package com.fullstack.test.entity

import com.fasterxml.jackson.annotation.JsonProperty
import java.math.BigDecimal
import java.time.LocalDateTime

data class Product(
    val id: Long? = null,
    val title: String,
    val handle: String? = null,
    val vendor: String? = null,
    val productType: String? = null,
    val price: BigDecimal? = null,
    val compareAtPrice: BigDecimal? = null,
    val sku: String? = null,
    val available: Boolean? = null,
    val description: String? = null,
    val imageUrl: String? = null,
    val variants: List<ProductVariant>? = null,
    val createdAt: LocalDateTime? = null,
    val updatedAt: LocalDateTime? = null
)

data class ProductVariant(
    val id: Long? = null,
    val title: String? = null,
    val price: BigDecimal? = null,
    val sku: String? = null,
    val available: Boolean? = null,
    val option1: String? = null,
    val option2: String? = null,
    val option3: String? = null
)
