package com.mnowak.operation;

import java.math.BigDecimal;
import java.util.Date;

import lombok.Builder;
import lombok.Data;

@Data
@Builder(builderMethodName = "anOperation")
public class Operation {
    private Date date;
    private String payee;
    private String memo;

    /**
     * outflow if <0, inflow otherwise
     */
    private BigDecimal amount;
}
