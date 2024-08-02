package com.enigma.wms_api.model.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SearchTransactionRequest {
    private String receiptNumber;
    private String startDate;
    private String endDate;
    private String transType;
    private String productName;

    @NotNull
    private Integer page;

    @NotNull
    private Integer size;
}
