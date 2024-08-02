package com.enigma.wms_api.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TotalSalesResponse {
    private Integer eatIn;
    private Integer online;
    private Integer takeAway;
}
