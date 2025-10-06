package com.fullstack.test.service

import com.fullstack.test.config.AppConfig
import com.fullstack.test.dto.ApiProduct
import com.fullstack.test.dto.ApiProductResponse
import com.fullstack.test.entity.Product
import com.fullstack.test.entity.ProductVariant
import com.fullstack.test.repository.ProductRepository
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.context.event.EventListener
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import java.math.BigDecimal

@Service
class ProductService(
    private val productRepository: ProductRepository,
    private val restTemplate: RestTemplate,
    private val appConfig: AppConfig
) {

    private val logger = LoggerFactory.getLogger(ProductService::class.java)

    @Value("\${app.products.api.url:https://famme.no/products.json}")
    private lateinit var productsApiUrl: String

    private var maxProducts: Int = 50

    @EventListener(ApplicationReadyEvent::class)
    fun onApplicationReady() {
        if (appConfig.products.clearOnRestart) {
            logger.info("Clearing all products on restart as configured")
            clearAllProducts()
        }
    }

    @Scheduled(initialDelay = 0, fixedDelay = 3600000) // Run immediately, then every hour
    fun fetchAndSaveProducts() {
        try {
            logger.info("Starting to fetch products from API: $productsApiUrl")
            
            // Wait for full response with timeout
            val response = restTemplate.getForObject(productsApiUrl, ApiProductResponse::class.java)
            
            if (response == null) {
                logger.error("Received null response from API")
                return
            }
            
            val apiProducts = response.products
            logger.info("Fetched ${apiProducts.size} products from API")
            
            if (apiProducts.isEmpty()) {
                logger.warn("No products received from API")
                return
            }
            
            // Clear existing products
            logger.info("Clearing existing products from database")
            productRepository.deleteAll()
            
            // Take exactly maxProducts (50) products
            val productsToSave = apiProducts.take(maxProducts)
            logger.info("Processing ${productsToSave.size} products for saving")
            
            var savedCount = 0
            var errorCount = 0
            
            for ((index, apiProduct) in productsToSave.withIndex()) {
                try {
                    logger.debug("Processing product ${index + 1}/${productsToSave.size}: ${apiProduct.title}")
                    val product = convertToProduct(apiProduct)
                    productRepository.save(product)
                    savedCount++
                    logger.debug("Successfully saved product: ${product.title}")
                } catch (e: Exception) {
                    errorCount++
                    logger.warn("Failed to save product ${apiProduct.id} (${apiProduct.title}): ${e.message}")
                }
            }
            
            logger.info("Successfully saved $savedCount products to database (${errorCount} errors)")
            
        } catch (e: Exception) {
            logger.error("Error fetching products from API: ${e.message}", e)
        }
    }

    fun getAllProducts(): List<Product> {
        return productRepository.findAll()
    }

    fun saveProduct(product: Product): Product {
        return productRepository.save(product)
    }

    fun getProductCount(): Long {
        return productRepository.count()
    }

    private fun convertToProduct(apiProduct: ApiProduct): Product {
        // Convert variants
        val variants = apiProduct.variants?.map { apiVariant ->
            ProductVariant(
                id = apiVariant.id,
                title = apiVariant.title,
                price = apiVariant.price?.toBigDecimalOrNull(),
                sku = apiVariant.sku,
                available = apiVariant.available,
                option1 = apiVariant.option1,
                option2 = apiVariant.option2,
                option3 = apiVariant.option3
            )
        }

        // Get the first variant's featured image or first variant's image
        val imageUrl = apiProduct.variants?.firstOrNull()?.featuredImage?.src

        // Get price from first variant if available
        val firstVariant = apiProduct.variants?.firstOrNull()
        val price = firstVariant?.price?.toBigDecimalOrNull()
        val compareAtPrice = firstVariant?.compareAtPrice?.toBigDecimalOrNull()
        
        // Get SKU from first variant if available
        val sku = firstVariant?.sku
        
        // Determine if product is available (any variant available)
        val available = apiProduct.variants?.any { it.available == true } ?: false

        return Product(
            title = apiProduct.title,
            handle = apiProduct.handle,
            vendor = apiProduct.vendor,
            productType = apiProduct.productType,
            price = price,
            compareAtPrice = compareAtPrice,
            sku = sku,
            available = available,
            description = apiProduct.bodyHtml?.replace(Regex("<[^>]*>"), "")?.trim(), // Strip HTML tags
            imageUrl = imageUrl,
            variants = variants
        )
    }

    fun clearAllProducts() {
        try {
            logger.info("Clearing all products from database")
            productRepository.deleteAll()
            logger.info("Successfully cleared all products")
        } catch (e: Exception) {
            logger.error("Error clearing products: ${e.message}", e)
        }
    }

    fun getProductById(id: Long): Product? {
        return productRepository.findById(id)
    }

    fun searchProductsByTitle(searchTerm: String): List<Product> {
        return if (searchTerm.isBlank()) {
            getAllProducts()
        } else {
            productRepository.findByTitleContaining(searchTerm)
        }
    }

    fun updateProduct(product: Product): Product? {
        return productRepository.update(product)
    }

    fun deleteProduct(id: Long): Boolean {
        return productRepository.deleteById(id)
    }

    fun searchProductsWithFilters(
        searchTerm: String?,
        productType: String?,
        minPrice: String?,
        maxPrice: String?,
        available: String?
    ): List<Product> {
        val minPriceDecimal = minPrice?.toBigDecimalOrNull()
        val maxPriceDecimal = maxPrice?.toBigDecimalOrNull()
        val availableBoolean = available?.toBooleanStrictOrNull()
        
        return productRepository.findByFilters(
            searchTerm = searchTerm,
            productType = productType,
            minPrice = minPriceDecimal,
            maxPrice = maxPriceDecimal,
            available = availableBoolean
        )
    }

    fun getProductTypes(): List<String> {
        return productRepository.getDistinctProductTypes()
    }
}

