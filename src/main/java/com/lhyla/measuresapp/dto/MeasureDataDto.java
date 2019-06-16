package com.lhyla.measuresapp.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.lhyla.measuresapp.util.MeasuresAppUtils;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

@lombok.Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MeasureDataDto {

    private Long id;

    @JsonFormat(pattern = MeasuresAppUtils.DATE_FORMAT, timezone = MeasuresAppUtils.TIMEZONE)
    private Date measurementDate;
    private BigDecimal value;
    private MeasureDataDto.Quality quality;
    private MeasureDataDto.Type type;

    public enum Quality {
        GOOD,
        BAD
    }

    public enum Type {
        MEASURED,
        AVERAGE,
        INTERPOLATED
    }
}