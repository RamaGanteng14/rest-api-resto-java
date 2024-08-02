package com.enigma.wms_api.controller;

import com.enigma.wms_api.model.request.CreateProductRequest;
import com.enigma.wms_api.model.request.SearchProductRequest;
import com.enigma.wms_api.model.request.UpdateProductRequest;
import com.enigma.wms_api.model.response.CommonResponse;
import com.enigma.wms_api.model.response.PagingResponse;
import com.enigma.wms_api.model.response.ProductResponse;
import com.enigma.wms_api.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {
    @Autowired
    private ProductService productService;

    @PostMapping(
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    public CommonResponse<ProductResponse> create(@RequestBody CreateProductRequest request) {
        ProductResponse productResponse = productService.create(request);
        return CommonResponse.<ProductResponse>builder()
                .data(productResponse)
                .build();
    }

    @PutMapping(
            path = "/{productId}",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    public CommonResponse<ProductResponse> update(@RequestBody UpdateProductRequest request, @PathVariable("productId") String productId) {
        request.setProductId(productId);
        ProductResponse productResponse = productService.update(request);
        return CommonResponse.<ProductResponse>builder()
                .data(productResponse)
                .build();
    }

    @DeleteMapping(
            path = "/{productId}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public CommonResponse<String> delete(@PathVariable("productId") String productId) {
        productService.delete(productId);
        return CommonResponse.<String>builder()
                .data("OK")
                .build();
    }

    @GetMapping(
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public CommonResponse<List<ProductResponse>> search(
            @RequestParam(name = "productCode", required = false) String productCode,
            @RequestParam(name = "productName", required = false) String productName,
            @RequestParam(name = "minPrice", required = false) Integer minPrice,
            @RequestParam(name = "maxPrice", required = false) Integer maxPrice,
            @RequestParam(name = "page", defaultValue = "0") Integer page,
            @RequestParam(name = "size", defaultValue = "10") Integer size) {

        SearchProductRequest request = SearchProductRequest.builder()
                .productCode(productCode)
                .productName(productName)
                .minPrice(minPrice)
                .maxPrice(maxPrice)
                .page(page)
                .size(size)
                .build();

        Page<ProductResponse> productResponses = productService.searchProduct(request);

        return CommonResponse.<List<ProductResponse>>builder()
                .data(productResponses.getContent())
                .paging(PagingResponse.builder()
                        .currentPage(productResponses.getNumber())
                        .size(productResponses.getSize())
                        .totalPage(productResponses.getTotalPages())
                        .build())
                .build();
    }

    @GetMapping(
            path = "/{branchId}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public CommonResponse<List<ProductResponse>> getProductByBranch(@PathVariable("branchId") String branchId){
        List<ProductResponse> productResponses = productService.getAllByBranchId(branchId);
        return CommonResponse.<List<ProductResponse>>builder()
                .data(productResponses)
                .build();
    }

}
