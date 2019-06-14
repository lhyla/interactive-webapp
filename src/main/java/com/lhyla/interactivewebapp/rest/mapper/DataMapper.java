package com.lhyla.interactivewebapp.rest.mapper;

import com.lhyla.interactivewebapp.data.entity.Data;
import com.lhyla.interactivewebapp.rest.dto.DataDto;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DataMapper {

    public DataDto map(final Data data) {
        return new ModelMapper().map(data, DataDto.class);
    }

    public List<DataDto> map(final List<Data> data) {
        return new ModelMapper().map(data, new TypeToken<List<DataDto>>() {}.getType());
    }
}