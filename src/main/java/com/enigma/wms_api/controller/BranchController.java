package com.enigma.wms_api.controller;

import com.enigma.wms_api.entity.Branch;
import com.enigma.wms_api.model.request.CreateBranchRequest;
import com.enigma.wms_api.model.request.UpdateBranchRequest;
import com.enigma.wms_api.model.response.BranchResponse;
import com.enigma.wms_api.model.response.CommonResponse;
import com.enigma.wms_api.service.BranchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController

public class BranchController {
    @Autowired
    private BranchService branchService;

    @PostMapping(
            path = "/api/branch",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public CommonResponse<BranchResponse> create(@RequestBody CreateBranchRequest request){
        BranchResponse branchResponse = branchService.create(request);
        return CommonResponse.<BranchResponse>builder().data(branchResponse).build();
    }

    @GetMapping(
            path = "/api/branch/{branchId}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public CommonResponse<BranchResponse> get(@PathVariable("branchId") String branchId){
        BranchResponse branchResponse = branchService.get(branchId);
        return CommonResponse.<BranchResponse>builder().data(branchResponse).build();
    }

    @PutMapping(
            path = "/api/branch/{branchId}",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    public CommonResponse<BranchResponse> update(@RequestBody UpdateBranchRequest request, @PathVariable("branchId") String branchId){
        request.setBranchId(branchId);
        BranchResponse branchResponse = branchService.update(request);
        return CommonResponse.<BranchResponse>builder().data(branchResponse).build();
    }

    @DeleteMapping(
            path = "/api/branch/{branchId}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public CommonResponse<String> delete(@PathVariable("branchId") String branchId){
        branchService.delete(branchId);
        return CommonResponse.<String>builder().data("OK").build();
    }
}
