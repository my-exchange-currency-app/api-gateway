package com.demo.skyros.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CurrencyVO {

    private String from;
    private String to;
    private BigDecimal quantity;
    private String tag;
}
