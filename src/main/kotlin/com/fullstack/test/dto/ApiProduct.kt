package com.fullstack.test.dto

import com.fasterxml.jackson.annotation.JsonProperty
import java.math.BigDecimal

data class ApiProduct(
    val id: Long,
    val title: String,
    val handle: String? = null,
    val vendor: String? = null,
    @JsonProperty("product_type")
    val productType: String? = null,
    @JsonProperty("body_html")
    val bodyHtml: String? = null,
    @JsonProperty("published_at")
    val publishedAt: String? = null,
    @JsonProperty("created_at")
    val createdAt: String? = null,
    @JsonProperty("updated_at")
    val updatedAt: String? = null,
    val tags: List<String>? = null,
    val variants: List<ApiVariant>? = null
)

data class ApiVariant(
    val id: Long,
    val title: String? = null,
    val price: String? = null,
    val sku: String? = null,
    val available: Boolean? = null,
    val option1: String? = null,
    val option2: String? = null,
    val option3: String? = null,
    @JsonProperty("compare_at_price")
    val compareAtPrice: String? = null,
    @JsonProperty("featured_image")
    val featuredImage: ApiImage? = null,
    val grams: Int? = null,
    val position: Int? = null,
    @JsonProperty("product_id")
    val productId: Long? = null,
    @JsonProperty("requires_shipping")
    val requiresShipping: Boolean? = null,
    val taxable: Boolean? = null
)

data class ApiImage(
    val id: Long,
    val src: String,
    val alt: String? = null,
    val width: Int? = null,
    val height: Int? = null,
    val position: Int? = null,
    @JsonProperty("product_id")
    val productId: Long? = null,
    @JsonProperty("created_at")
    val createdAt: String? = null,
    @JsonProperty("updated_at")
    val updatedAt: String? = null,
    @JsonProperty("variant_ids")
    val variantIds: List<Long>? = null
)

data class ApiProductResponse(
    val products: List<ApiProduct>
)

