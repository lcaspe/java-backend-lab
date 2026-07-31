package com.developer.productservice.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.developer.productservice.dto.request.ProductRequest;
import com.developer.productservice.dto.response.PagedResponse;
import com.developer.productservice.dto.response.ProductResponse;
import com.developer.productservice.exception.ProductNotFoundException;
import com.developer.productservice.mapper.ProductMapper;
import com.developer.productservice.model.Product;
import com.developer.productservice.repository.ProductRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    @Transactional
    public ProductResponse create(ProductRequest productRequest) {
        Product product = productMapper.toProductEntity(productRequest);
        return productMapper.toProductResponse(productRepository.save(product));
    }

    @Transactional(readOnly = true)
    public PagedResponse<ProductResponse> getAll(Pageable pageable) {
        Page<ProductResponse> page = productRepository.findAll(pageable)
                .map(productMapper::toProductResponse);
        return new PagedResponse<>(
                page.getContent(),
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages(),
                page.isLast()
        );
    }

    @Transactional(readOnly = true)
    public ProductResponse getById(Long id) {
        return productRepository.findById(id)
                .map(productMapper::toProductResponse)
                .orElseThrow(() -> new ProductNotFoundException(id));
    }
}
