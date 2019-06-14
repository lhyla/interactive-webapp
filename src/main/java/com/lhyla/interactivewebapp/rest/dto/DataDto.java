package com.lhyla.interactivewebapp.rest.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.lhyla.interactivewebapp.data.entity.Data;
import com.lhyla.interactivewebapp.util.DataUtils;
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

    private Long id;

    @JsonFormat(pattern= DataUtils.DATE_FORMAT, timezone = "Europe/Berlin")
    private Date measurementDate;
    private BigDecimal value;
    private Data.Quality quality;
}