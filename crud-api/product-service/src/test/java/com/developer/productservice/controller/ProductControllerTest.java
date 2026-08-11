package com.developer.productservice.controller;

import static org.hamcrest.Matchers.anyOf;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.developer.productservice.dto.request.ProductRequest;
import com.developer.productservice.dto.response.PagedResponse;
import com.developer.productservice.dto.response.ProductResponse;
import com.developer.productservice.exception.ProductNotFoundException;
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
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("name must not be blank"));
    }

    @ParameterizedTest
    @ValueSource(doubles = {0, -50000D})
    @NullSource
    void shouldNotCreateProduct_WhenProductPriceIsInvalid(Double invalidPrice) throws Exception {
        String payload = String.format("""
                {
                    "name": "Laptop",
                    "price": %f
                }
                """, invalidPrice);

        mockMvc.perform(post("/api/v1/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", anyOf(
                        is("price must be greater than 0"),
                        is("price must not be null"))));
    }

    @Test
    void shouldReturnProductPage() throws Exception {
        List<ProductResponse> productResponses = List.of(
                new ProductResponse(1L, "Laptop", BigDecimal.valueOf(50000)),
                new ProductResponse(2L, "Keyboard", BigDecimal.valueOf(1000)),
                new ProductResponse(3L, "Mouse", BigDecimal.valueOf(500))
        );

        PagedResponse<ProductResponse> pagedResponse = new PagedResponse<>(
                productResponses, 0, 10, 3, 1, true
        );

        when(productService.getAll(any(Pageable.class))).thenReturn(pagedResponse);

        mockMvc.perform(get("/api/v1/products"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.page").value(0))
                .andExpect(jsonPath("$.size").value(10))
                .andExpect(jsonPath("$.totalElements").value(3))
                .andExpect(jsonPath("$.totalPages").value(1))
                .andExpect(jsonPath("$.last").value(true))
                .andExpect(jsonPath("$.content.length()").value(3))
                .andExpect(jsonPath("$.content[0].id").value(1))
                .andExpect(jsonPath("$.content[0].name").value("Laptop"))
                .andExpect(jsonPath("$.content[0].price").value(50000))
                .andExpect(jsonPath("$.content[1].id").value(2))
                .andExpect(jsonPath("$.content[1].name").value("Keyboard"))
                .andExpect(jsonPath("$.content[1].price").value(1000))
                .andExpect(jsonPath("$.content[2].id").value(3))
                .andExpect(jsonPath("$.content[2].name").value("Mouse"))
                .andExpect(jsonPath("$.content[2].price").value(500));
    }

    @Test
    void shouldReturnProduct_WhenProductExists() throws Exception {
        ProductResponse productResponse = new ProductResponse(1L, "Laptop", BigDecimal.valueOf(50000));

        when(productService.getById(any(Long.class))).thenReturn(productResponse);

        mockMvc.perform(get("/api/v1/products/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Laptop"))
                .andExpect(jsonPath("$.price").value(50000));
    }

    @Test
    void shouldUpdateProduct() throws Exception {
        String payload = """
                    {
                        "name": "Updated Laptop",
                        "price": 50000
                    }
                """;

        when(productService.update(any(Long.class), any(ProductRequest.class))).thenReturn(
                new ProductResponse(1L, "Updated Laptop", BigDecimal.valueOf(50000))
        );

        mockMvc.perform(put("/api/v1/products/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Updated Laptop"))
                .andExpect(jsonPath("$.price").value(50000));
    }

    @Test
    void shouldReturn404_WhenUpdatingNonExistingProduct() throws Exception {
        long productIdToUpdate = 100;
        String payload = """
                    {
                        "name": "Updated Laptop",
                        "price": 50000
                    }
                """;

        when(productService.update(any(Long.class), any(ProductRequest.class)))
                .thenThrow(new ProductNotFoundException(productIdToUpdate));

        mockMvc.perform(put("/api/v1/products/{id}", productIdToUpdate)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error")
                        .value("Product not found with id " + productIdToUpdate));
    }

    @Test
    void shouldDeleteProduct() throws Exception {
        doNothing().when(productService).delete(any(Long.class));

        mockMvc.perform(delete("/api/v1/products/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void shouldReturn404_WhenDeletingNonExistingProduct() throws Exception {
        long productIdToDelete = 100;
        doThrow(new ProductNotFoundException(productIdToDelete)).when(productService).delete(any(Long.class));

        mockMvc.perform(delete("/api/v1/products/{id}", productIdToDelete))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error")
                        .value("Product not found with id " + productIdToDelete));
        // comment
    }
}
