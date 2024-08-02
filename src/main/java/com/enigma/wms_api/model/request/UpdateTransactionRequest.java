package com.enigma.wms_api.model.request;

import com.enigma.wms_api.entity.TransactionType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateTransactionRequest {
    @JsonIgnore
    @NotBlank
    private String billId;
    @NotNull
    private TransactionType transactionType;
    private List<CreateTransactionDetailRequest> transactionDetails;
}
