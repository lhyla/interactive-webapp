package com.lhyla.measuresapp.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
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

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long id;

    @JsonFormat(pattern = MeasuresAppUtils.DATE_FORMAT, timezone = MeasuresAppUtils.TIMEZONE)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Date measurementDate;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private BigDecimal value;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private MeasureDataDto.Quality quality;

    @JsonInclude(JsonInclude.Include.NON_NULL)
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