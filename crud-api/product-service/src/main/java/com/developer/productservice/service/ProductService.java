package com.developer.productservice.service;

import org.springframework.stereotype.Service;

import com.developer.productservice.dto.request.ProductRequest;
import com.developer.productservice.mapper.ProductMapper;
import com.developer.productservice.model.Product;
import com.developer.productservice.repository.ProductRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    public Product create(ProductRequest productRequest) {
        Product product = productMapper.toProductEntity(productRequest);
        return productRepository.save(product);
    }
}
