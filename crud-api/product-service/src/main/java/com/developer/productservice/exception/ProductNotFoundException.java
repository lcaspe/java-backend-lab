package com.developer.productservice.exception;

public class ProductNotFoundException extends RuntimeException {

    public ProductNotFoundException() {
        super("Product not found");
    }

    public ProductNotFoundException(Long id) {
        super("Product not found with id " + id);
    }
}
