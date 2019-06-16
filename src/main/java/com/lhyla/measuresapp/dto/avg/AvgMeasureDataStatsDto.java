package com.lhyla.measuresapp.dto.avg;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.math.BigInteger;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AvgMeasureDataStatsDto {
    private BigDecimal avg;
    private BigInteger count;
    private BigDecimal min;
    private BigDecimal max;
    private Quality quality;

    public enum Quality {
        GOOD,
        BAD
    }
}