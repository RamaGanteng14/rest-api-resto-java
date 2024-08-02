package com.enigma.wms_api.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductResponse {
    private String productId;
    private String productCode;
    private String productName;
    private Integer price;
    private BranchResponse branch;
}
