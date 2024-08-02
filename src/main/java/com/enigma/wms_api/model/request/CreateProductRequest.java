package com.enigma.wms_api.model.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateProductRequest {

    @NotBlank(message = "Product code is mandatory")
    private String productCode;

    @NotBlank(message = "Product name is mandatory")
    private String productName;

    @NotNull(message = "Price is mandatory")
    @Positive(message = "Price must be a positive value")
    private Integer price;

    @NotNull
    private String branchId;
}
