package com.enigma.wms_api.model.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor

public class SearchProductRequest {
    private String productCode;
    private String productName;
    private Integer minPrice;
    private Integer maxPrice;

    @NotNull
    private Integer size;

    @NotNull
    private Integer page;
}
