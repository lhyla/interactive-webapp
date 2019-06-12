package com.lhyla.interactivewebapp.rest.dto;

import com.lhyla.interactivewebapp.data.entity.Data;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

@lombok.Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DataDto {
    private Date measurementDate;
    private BigDecimal value;
    private Data.Quality quality;
}