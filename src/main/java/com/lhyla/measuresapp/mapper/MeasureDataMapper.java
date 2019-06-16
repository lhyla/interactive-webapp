package com.lhyla.measuresapp.mapper;

import com.lhyla.measuresapp.data.entity.AvgMeasureDataStats;
import com.lhyla.measuresapp.data.entity.MeasureData;
import com.lhyla.measuresapp.dto.MeasureDataDto;
import com.lhyla.measuresapp.dto.avg.AvgMeasureDataStatsDto;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

@Component
public class MeasureDataMapper {

    public MeasureDataDto map(final MeasureData measureData) {
        return new ModelMapper().map(measureData, MeasureDataDto.class);
    }

    public List<MeasureDataDto> map(final List<MeasureData> data) {
        return new ModelMapper().map(data, new TypeToken<List<MeasureDataDto>>() {
        }.getType());
    }

    public Set<AvgMeasureDataStatsDto> map(final Set<AvgMeasureDataStats> stats) {
        return new ModelMapper().map(stats, new TypeToken<Set<AvgMeasureDataStatsDto>>() {
        }.getType());
    }
}