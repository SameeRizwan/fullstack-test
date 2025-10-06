package com.fullstack.test.controller

import com.fullstack.test.entity.Product
import com.fullstack.test.service.ProductService
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*

@Controller
class ProductController(
    private val productService: ProductService
) {

    @GetMapping("/")
    fun index(model: Model): String {
        model.addAttribute("productCount", productService.getProductCount())
        return "index"
    }

    @GetMapping("/products")
    fun getProducts(model: Model): String {
        val products = productService.getAllProducts()
        model.addAttribute("products", products)
        return "fragments/products-table"
    }

    @PostMapping("/products")
    fun addProduct(
        @RequestParam title: String,
        @RequestParam(required = false) handle: String?,
        @RequestParam(required = false) vendor: String?,
        @RequestParam(required = false) productType: String?,
        @RequestParam(required = false) price: String?,
        @RequestParam(required = false) sku: String?,
        @RequestParam(required = false) description: String?,
        @RequestParam(required = false) imageUrl: String?,
        model: Model
    ): String {
        val product = Product(
            title = title,
            handle = handle,
            vendor = vendor,
            productType = productType,
            price = price?.toBigDecimalOrNull(),
            sku = sku,
            description = description,
            imageUrl = imageUrl,
            available = true
        )
        
        productService.saveProduct(product)
        
        val products = productService.getAllProducts()
        model.addAttribute("products", products)
        return "fragments/products-table"
    }

    @GetMapping("/products/count")
    fun getProductCount(model: Model): String {
        model.addAttribute("productCount", productService.getProductCount())
        return "fragments/product-count"
    }

    @PostMapping("/products/load")
    fun loadProducts(model: Model): String {
        val products = productService.getAllProducts()
        model.addAttribute("products", products)
        return "fragments/products-table"
    }

    @PostMapping("/products/clear")
    fun clearProducts(model: Model): String {
        productService.clearAllProducts()
        
        val products = productService.getAllProducts()
        model.addAttribute("products", products)
        return "fragments/products-table"
    }

    @GetMapping("/search")
    fun searchPage(model: Model): String {
        val productTypes = productService.getProductTypes()
        model.addAttribute("productTypes", productTypes)
        return "search"
    }

    @GetMapping("/products/search")
    fun searchProducts(
        @RequestParam(required = false) q: String?,
        @RequestParam(required = false) productType: String?,
        @RequestParam(required = false) minPrice: String?,
        @RequestParam(required = false) maxPrice: String?,
        @RequestParam(required = false) available: String?,
        model: Model
    ): String {
        val products = productService.searchProductsWithFilters(q, productType, minPrice, maxPrice, available)
        model.addAttribute("products", products)
        return "fragments/products-table"
    }

    @GetMapping("/products/{id}")
    fun getProduct(@PathVariable id: Long, model: Model): String {
        val product = productService.getProductById(id)
        if (product == null) {
            return "redirect:/"
        }
        model.addAttribute("product", product)
        return "product-detail"
    }

    @GetMapping("/products/{id}/edit")
    fun editProductPage(@PathVariable id: Long, model: Model): String {
        val product = productService.getProductById(id)
        if (product == null) {
            return "redirect:/"
        }
        model.addAttribute("product", product)
        return "edit-product"
    }

    @PostMapping("/products/{id}/edit")
    fun updateProduct(
        @PathVariable id: Long,
        @RequestParam title: String,
        @RequestParam(required = false) handle: String?,
        @RequestParam(required = false) vendor: String?,
        @RequestParam(required = false) productType: String?,
        @RequestParam(required = false) price: String?,
        @RequestParam(required = false) sku: String?,
        @RequestParam(required = false) description: String?,
        @RequestParam(required = false) imageUrl: String?,
        @RequestParam(required = false) available: String?,
        model: Model
    ): String {
        val existingProduct = productService.getProductById(id)
        if (existingProduct == null) {
            return "redirect:/"
        }

        val updatedProduct = existingProduct.copy(
            title = title,
            handle = handle,
            vendor = vendor,
            productType = productType,
            price = price?.toBigDecimalOrNull(),
            sku = sku,
            description = description,
            imageUrl = imageUrl,
            available = available?.toBoolean()
        )
        
        productService.updateProduct(updatedProduct)
        
        return "redirect:/products/$id"
    }

    @DeleteMapping("/products/{id}")
    fun deleteProduct(@PathVariable id: Long, model: Model): String {
        productService.deleteProduct(id)
        
        val products = productService.getAllProducts()
        model.addAttribute("products", products)
        return "fragments/products-table"
    }
}

