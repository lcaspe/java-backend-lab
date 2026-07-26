package com.developer.productservice.service;

import org.springframework.stereotype.Service;

import com.developer.productservice.dto.request.ProductRequest;
import com.developer.productservice.dto.response.ProductResponse;
import com.developer.productservice.mapper.ProductMapper;
import com.developer.productservice.model.Product;
import com.developer.productservice.repository.ProductRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    public ProductResponse create(ProductRequest productRequest) {
        Product product = productMapper.toProductEntity(productRequest);
        return productMapper.toProductResponse(productRepository.save(product));
    }
}
