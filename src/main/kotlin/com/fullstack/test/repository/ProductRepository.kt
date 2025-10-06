package com.fullstack.test.repository

import com.fasterxml.jackson.databind.ObjectMapper
import com.fullstack.test.entity.Product
import com.fullstack.test.entity.ProductVariant
import org.springframework.jdbc.core.simple.JdbcClient
import org.springframework.stereotype.Repository
import java.math.BigDecimal
import java.sql.ResultSet
import java.time.LocalDateTime

@Repository
class ProductRepository(
    private val jdbcClient: JdbcClient,
    private val objectMapper: ObjectMapper
) {

    fun findAll(): List<Product> {
        return jdbcClient.sql("""
            SELECT id, title, handle, vendor, product_type, price, compare_at_price, 
                   sku, available, description, image_url, variants, created_at, updated_at
            FROM products 
            ORDER BY created_at DESC
        """.trimIndent())
            .query { rs, _ -> mapRowToProduct(rs) }
            .list()
    }

    fun findById(id: Long): Product? {
        return jdbcClient.sql("""
            SELECT id, title, handle, vendor, product_type, price, compare_at_price, 
                   sku, available, description, image_url, variants, created_at, updated_at
            FROM products 
            WHERE id = ?
        """.trimIndent())
            .param(id)
            .query { rs, _ -> mapRowToProduct(rs) }
            .optional()
            .orElse(null)
    }

    fun save(product: Product): Product {
        val variantsJson = product.variants?.let { objectMapper.writeValueAsString(it) }
        
        val id = jdbcClient.sql("""
            INSERT INTO products (title, handle, vendor, product_type, price, compare_at_price, 
                                sku, available, description, image_url, variants, created_at, updated_at)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?::jsonb, ?, ?)
        """.trimIndent())
            .params(
                product.title,
                product.handle,
                product.vendor,
                product.productType,
                product.price,
                product.compareAtPrice,
                product.sku,
                product.available,
                product.description,
                product.imageUrl,
                variantsJson,
                LocalDateTime.now(),
                LocalDateTime.now()
            )
            .update()

        return product.copy(id = id.toLong())
    }

    fun count(): Long {
        return jdbcClient.sql("SELECT COUNT(*) FROM products")
            .query(Long::class.java)
            .single()
    }

    fun deleteAll() {
        jdbcClient.sql("DELETE FROM products").update()
    }

    fun deleteById(id: Long): Boolean {
        val rowsAffected = jdbcClient.sql("DELETE FROM products WHERE id = ?")
            .param(id)
            .update()
        return rowsAffected > 0
    }

    fun findByTitleContaining(searchTerm: String): List<Product> {
        return jdbcClient.sql("""
            SELECT id, title, handle, vendor, product_type, price, compare_at_price, 
                   sku, available, description, image_url, variants, created_at, updated_at
            FROM products 
            WHERE LOWER(title) LIKE LOWER(?)
            ORDER BY created_at DESC
        """.trimIndent())
            .param("%$searchTerm%")
            .query { rs, _ -> mapRowToProduct(rs) }
            .list()
    }

    fun update(product: Product): Product? {
        val variantsJson = product.variants?.let { objectMapper.writeValueAsString(it) }
        
        val rowsAffected = jdbcClient.sql("""
            UPDATE products 
            SET title = ?, handle = ?, vendor = ?, product_type = ?, price = ?, compare_at_price = ?, 
                sku = ?, available = ?, description = ?, image_url = ?, variants = ?::jsonb, updated_at = ?
            WHERE id = ?
        """.trimIndent())
            .params(
                product.title,
                product.handle,
                product.vendor,
                product.productType,
                product.price,
                product.compareAtPrice,
                product.sku,
                product.available,
                product.description,
                product.imageUrl,
                variantsJson,
                LocalDateTime.now(),
                product.id
            )
            .update()

        return if (rowsAffected > 0) product else null
    }

    fun findByFilters(
        searchTerm: String?,
        productType: String?,
        minPrice: BigDecimal?,
        maxPrice: BigDecimal?,
        available: Boolean?
    ): List<Product> {
        var sql = """
            SELECT id, title, handle, vendor, product_type, price, compare_at_price, 
                   sku, available, description, image_url, variants, created_at, updated_at
            FROM products 
            WHERE 1=1
        """.trimIndent()
        
        val params = mutableListOf<Any>()
        var paramIndex = 1
        
        if (!searchTerm.isNullOrBlank()) {
            sql += " AND LOWER(title) LIKE LOWER(?)"
            params.add("%$searchTerm%")
            paramIndex++
        }
        
        if (!productType.isNullOrBlank()) {
            sql += " AND LOWER(product_type) = LOWER(?)"
            params.add(productType)
            paramIndex++
        }
        
        if (minPrice != null) {
            sql += " AND price >= ?"
            params.add(minPrice)
            paramIndex++
        }
        
        if (maxPrice != null) {
            sql += " AND price <= ?"
            params.add(maxPrice)
            paramIndex++
        }
        
        if (available != null) {
            sql += " AND available = ?"
            params.add(available)
            paramIndex++
        }
        
        sql += " ORDER BY created_at DESC"
        
        return jdbcClient.sql(sql)
            .params(*params.toTypedArray())
            .query { rs, _ -> mapRowToProduct(rs) }
            .list()
    }

    fun getDistinctProductTypes(): List<String> {
        return jdbcClient.sql("SELECT DISTINCT product_type FROM products WHERE product_type IS NOT NULL ORDER BY product_type")
            .query(String::class.java)
            .list()
    }

    private fun mapRowToProduct(rs: ResultSet): Product {
        val variantsJson = rs.getString("variants")
        val variants = if (variantsJson != null) {
            try {
                objectMapper.readValue(variantsJson, Array<ProductVariant>::class.java).toList()
            } catch (e: Exception) {
                emptyList()
            }
        } else {
            emptyList()
        }

        return Product(
            id = rs.getLong("id"),
            title = rs.getString("title"),
            handle = rs.getString("handle"),
            vendor = rs.getString("vendor"),
            productType = rs.getString("product_type"),
            price = rs.getBigDecimal("price"),
            compareAtPrice = rs.getBigDecimal("compare_at_price"),
            sku = rs.getString("sku"),
            available = rs.getBoolean("available"),
            description = rs.getString("description"),
            imageUrl = rs.getString("image_url"),
            variants = variants,
            createdAt = rs.getTimestamp("created_at")?.toLocalDateTime(),
            updatedAt = rs.getTimestamp("updated_at")?.toLocalDateTime()
        )
    }
}
