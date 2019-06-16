package com.lhyla.measuresapp.dto.avg;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Set;

@lombok.Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AvgMeasureDataDto {
    private BigDecimal avg;
    private Set<AvgMeasureDataStatsDto> avgDataStats;
}