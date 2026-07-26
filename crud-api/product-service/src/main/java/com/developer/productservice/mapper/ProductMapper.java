package com.developer.productservice.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import com.developer.productservice.dto.request.ProductRequest;
import com.developer.productservice.dto.response.ProductResponse;
import com.developer.productservice.model.Product;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ProductMapper {

    public Product toProductEntity(ProductRequest productRequest);

    public ProductResponse toProductResponse(Product product);

    public List<ProductResponse> toProductResponseList(List<Product> products);
}
