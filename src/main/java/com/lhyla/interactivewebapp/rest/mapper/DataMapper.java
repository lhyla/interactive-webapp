package com.lhyla.interactivewebapp.rest.mapper;

import com.lhyla.interactivewebapp.data.entity.Data;
import com.lhyla.interactivewebapp.rest.dto.AvgDataDto;
import com.lhyla.interactivewebapp.rest.dto.DataDto;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class DataMapper {

    public DataDto map(final Data data) {
        return new ModelMapper().map(data, DataDto.class);
    }

    public AvgDataDto map(final BigDecimal val) {
        return new AvgDataDto(val);
    }
}