package com.developer.productservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.developer.productservice.model.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
