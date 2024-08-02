package com.enigma.wms_api.model.request;

import com.enigma.wms_api.entity.TransactionType;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateTransactionRequest {

    private TransactionType transactionType;

    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate transDate;

    private String receiptNumber;

    @NotNull(message = "Transaction details cannot be null")
    @Size(min = 1, message = "Transaction must have at least one detail")
    private List<CreateTransactionDetailRequest> transactionDetails;

}
