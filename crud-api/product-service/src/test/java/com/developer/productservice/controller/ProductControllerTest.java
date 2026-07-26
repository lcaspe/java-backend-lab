package com.developer.productservice.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.developer.productservice.dto.request.ProductRequest;
import com.developer.productservice.dto.response.ProductResponse;
import com.developer.productservice.service.ProductService;

@WebMvcTest(ProductController.class)
public class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ProductService productService;

    @Test
    void shouldCreateProduct() throws Exception {
        when(productService.create(any(ProductRequest.class))).thenReturn(
                new ProductResponse(
                        1L,
                        "Laptop",
                        BigDecimal.valueOf(50000)));

        mockMvc.perform(post("/api/v1/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "name": "Laptop",
                                    "price": 50000
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Laptop"));
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "   "})
    @NullSource
        // Combines null, empty, and blank strings
    void shouldNotCreateProduct_WhenProductNameIsInvalid(String invalidName) throws Exception {
        String payload = String.format("""
                {
                    "name": %s,
                    "price": 50000
                }
                """, invalidName == null ? "null" : "\"" + invalidName + "\"");

        mockMvc.perform(post("/api/v1/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isBadRequest());
    }
}
