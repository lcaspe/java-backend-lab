package com.developer.productservice.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

import com.developer.productservice.dto.request.ProductRequest;
import com.developer.productservice.dto.response.ProductResponse;
import com.developer.productservice.model.Product;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.WARN)
public interface ProductMapper {

    Product toProductEntity(ProductRequest productRequest);

    ProductResponse toProductResponse(Product product);

    void updateProductFromRequest(ProductRequest request, @MappingTarget Product product);
}
