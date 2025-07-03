package com.evolutionnext.domain.aggregates.product;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class ProductTest {

    @Test
    void shouldCreateValidProduct() {
        UUID uuid = UUID.randomUUID();
        ProductId productId = new ProductId(uuid);
        String name = "Test Product";
        BigDecimal price = new BigDecimal("10.00");

        Product product = new Product(productId, name, price);

        assertEquals(productId, product.id());
        assertEquals(name, product.name());
        assertEquals(price, product.price());
    }

    @Test
    void shouldThrowExceptionWhenPriceIsNegative() {
        UUID uuid = UUID.randomUUID();
        ProductId productId = new ProductId(uuid);
        String name = "Test Product";
        BigDecimal price = new BigDecimal("-1.00");

        assertThrows(IllegalArgumentException.class,
            () -> new Product(productId, name, price));
    }

    @Test
    void shouldThrowExceptionWhenNameIsBlank() {
        UUID uuid = UUID.randomUUID();
        ProductId productId = new ProductId(uuid);
        String name = "";
        BigDecimal price = new BigDecimal("10.00");

        assertThrows(IllegalArgumentException.class,
            () -> new Product(productId, name, price));
    }

    @Test
    void twoProductsNotEqualToEachOther() {
        Product product1 = new Product(new ProductId(UUID.randomUUID()), "Product 1", new BigDecimal("10.00"));
        Product product2 = new Product(new ProductId(UUID.randomUUID()), "Product 2", new BigDecimal("20.00"));
        Assertions.assertThat(product1).isNotEqualTo(product2);
    }
}
