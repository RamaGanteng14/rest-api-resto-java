package com.enigma.wms_api.model.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateProductRequest {
    @JsonIgnore
    @NotBlank
    private String productId;

    @NotBlank
    private String productCode;
    private String productName;
    private Integer price;
    private String branchId;
}
