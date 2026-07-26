package com.developer.productservice.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.developer.productservice.dto.request.ProductRequest;
import com.developer.productservice.mapper.ProductMapper;
import com.developer.productservice.model.Product;
import com.developer.productservice.repository.ProductRepository;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ProductMapper productMapper;

    @InjectMocks
    private ProductService productService;

    @Test
    void create_ShouldCreateProduct_WhenProductIsValid() {
        ProductRequest request = new ProductRequest(
                "Laptop",
                BigDecimal.valueOf(50000)
        );

        Product product = Product.builder()
                .name("Laptop")
                .price(BigDecimal.valueOf(50000))
                .build();

        when(productMapper.toProductEntity(request)).thenReturn(product);
        when(productRepository.save(product)).thenReturn(product);

        Product savedProduct = productService.create(request);

        assertThat(savedProduct).isEqualTo(product);
    }
}
