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
}

