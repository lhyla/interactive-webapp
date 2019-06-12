package com.lhyla.interactivewebapp.rest.mapper;

import com.lhyla.interactivewebapp.data.entity.Data;
import com.lhyla.interactivewebapp.rest.dto.DataDto;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class DataMapper {

    public DataDto map(final Data data) {
        return new ModelMapper().map(data, DataDto.class);
    }
}