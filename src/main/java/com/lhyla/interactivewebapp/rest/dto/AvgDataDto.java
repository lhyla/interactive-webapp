package com.lhyla.interactivewebapp.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@lombok.Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AvgDataDto {
    private BigDecimal value;
}