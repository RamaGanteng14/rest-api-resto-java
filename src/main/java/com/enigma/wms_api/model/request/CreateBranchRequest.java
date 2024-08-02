package com.enigma.wms_api.model.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateBranchRequest {
    @NotBlank
    private String branchCode;

    @NotBlank
    private String branchName;

    @NotBlank
    private String address;

    private String phoneNumber;
}
