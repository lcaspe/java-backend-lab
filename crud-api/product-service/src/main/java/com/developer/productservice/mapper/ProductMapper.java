package com.developer.productservice.mapper;

import org.mapstruct.Mapper;

import com.developer.productservice.dto.request.ProductRequest;
import com.developer.productservice.model.Product;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    public Product toProductEntity(ProductRequest productRequest);
}
