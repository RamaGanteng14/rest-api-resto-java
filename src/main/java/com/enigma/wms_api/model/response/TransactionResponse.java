package com.enigma.wms_api.model.response;

import com.enigma.wms_api.entity.TransactionType;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TransactionResponse {
    private String billId;
    private String receiptNumber;
    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate transDate;
    private String transactionType;
    private List<TransactionDetailResponse> billDetails;
}
