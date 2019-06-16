package com.lhyla.measuresapp.mapper;

import com.lhyla.measuresapp.data.entity.MeasureData;
import com.lhyla.measuresapp.dto.MeasureDataDto;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MeasureDataMapper {

    public MeasureDataDto map(final MeasureData measureData) {
        return new ModelMapper().map(measureData, MeasureDataDto.class);
    }

    public List<MeasureDataDto> map(final List<MeasureData> data) {
        return new ModelMapper().map(data, new TypeToken<List<MeasureDataDto>>() {
        }.getType());
    }
}