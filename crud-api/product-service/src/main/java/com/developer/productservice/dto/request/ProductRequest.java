package com.developer.productservice.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record ProductRequest(
        @NotNull String name,
        @NotNull Double price) {
}
