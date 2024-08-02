package com.enigma.wms_api.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BranchResponse {

    private String branchId;
    private String branchCode;
    private String branchName;
    private String address;
    private String phoneNumber;
}
