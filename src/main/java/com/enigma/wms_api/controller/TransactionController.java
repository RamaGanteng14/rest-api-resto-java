package com.enigma.wms_api.controller;

import com.enigma.wms_api.model.request.CreateTransactionRequest;
import com.enigma.wms_api.model.request.SearchTransactionRequest;
import com.enigma.wms_api.model.request.UpdateTransactionRequest;
import com.enigma.wms_api.model.response.CommonResponse;
import com.enigma.wms_api.model.response.PagingResponse;
import com.enigma.wms_api.model.response.TotalSalesResponse;
import com.enigma.wms_api.model.response.TransactionResponse;
import com.enigma.wms_api.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/transaksi")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @PostMapping(
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    public CommonResponse<TransactionResponse> create(@RequestBody CreateTransactionRequest request){
        TransactionResponse transactionResponse = transactionService.createTransaction(request);
        return CommonResponse.<TransactionResponse>builder().data(transactionResponse).build();
    }

    @PutMapping(
            path = "/{billId}",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    public CommonResponse<TransactionResponse> update(@PathVariable String billId, @RequestBody UpdateTransactionRequest request) {
        request.setBillId(billId);
        TransactionResponse transactionResponse = transactionService.updateTransaction(request);
        return CommonResponse.<TransactionResponse>builder().data(transactionResponse).build();
    }

    @GetMapping(
            path = "/{billId}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public CommonResponse<TransactionResponse> getById(@PathVariable("billId") String billId) {
      TransactionResponse transactionResponse = transactionService.getTransactionById(billId);
      return CommonResponse.<TransactionResponse>builder().data(transactionResponse).build();
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse<List<TransactionResponse>> search(
            @RequestParam(name = "receiptNumber", required = false) String receiptNumber,
            @RequestParam(name = "transType", required = false) String transType,
            @RequestParam(name = "startDate", required = false) String startDate,
            @RequestParam(name = "endDate", required = false) String endDate,
            @RequestParam(name = "productName", required = false) String productName,
            @RequestParam(name = "page", required = false, defaultValue = "0") Integer page,
            @RequestParam(name = "size", required = false, defaultValue = "10") Integer size) {

        SearchTransactionRequest request = SearchTransactionRequest.builder()
                .page(page)
                .size(size)
                .receiptNumber(receiptNumber)
                .productName(productName)
                .transType(transType)
                .startDate(startDate)
                .endDate(endDate)
                .build();

        Page<TransactionResponse> transactionResponses = transactionService.search(request);

        return CommonResponse.<List<TransactionResponse>>builder()
                .data(transactionResponses.getContent())
                .paging(PagingResponse.builder()
                        .currentPage(transactionResponses.getNumber())
                        .size(transactionResponses.getSize())
                        .totalPage(transactionResponses.getTotalPages())
                        .build())
                .build();
    }

    @GetMapping(
            path = "/total-sales",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public CommonResponse<TotalSalesResponse> getTotalSales(
            @RequestParam(value = "startDate") @DateTimeFormat(pattern = "dd-MM-yyyy") LocalDate startDate,
            @RequestParam(value = "endDate") @DateTimeFormat(pattern = "dd-MM-yyyy") LocalDate endDate
    ) {
        TotalSalesResponse totalSalesResponse = transactionService.getTotalSales(startDate, endDate);
        return CommonResponse.<TotalSalesResponse>builder()
                .data(totalSalesResponse)
                .build();
    }

}
