package com.enigma.wms_api.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommonResponse <T>{
    private T data;
    private String errors;
    private PagingResponse paging;
}
