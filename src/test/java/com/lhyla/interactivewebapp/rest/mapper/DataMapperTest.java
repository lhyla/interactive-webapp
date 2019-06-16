package com.lhyla.interactivewebapp.rest.mapper;

import com.lhyla.interactivewebapp.data.entity.Data;
import com.lhyla.interactivewebapp.rest.dto.DataDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

class DataMapperTest {

    private static Stream<Arguments> simpleData() {
        return Stream.of(
                Arguments.of(Data.builder()
                        .engineeringUnit(Data.EngineeringUnit.BARREL)
                        .id(1L)
                        .quality(Data.Quality.GOOD)
                        .measurementDate(new Date())
                        .build()
                ),
                Arguments.of(Data.builder()
                        .engineeringUnit(Data.EngineeringUnit.BARREL)
                        .id(2L)
                        .quality(Data.Quality.BAD)
                        .measurementDate(new Date())
                        .build()
                )
        );
    }

    @ParameterizedTest
    @MethodSource("simpleData")
    void mapToDto_simpleData_mapped(Data data) {
        //given
        //when
        DataDto result = new DataMapper().map(data);

        //then
        assertEquals(result.getValue(), data.getValue());
        assertEquals(result.getMeasurementDate(), data.getMeasurementDate());
        assertEquals(result.getQuality().name(), data.getQuality().name());
        assertEquals(result.getId(), data.getId());
        assertNull(result.getType());
    }

    @Test
    void mapToDto_nullData_mapped() {
        //given
        Data data = Data.builder()
                .engineeringUnit(null)
                .id(null)
                .quality(null)
                .measurementDate(null)
                .build();

        //when
        DataDto result = new DataMapper().map(data);

        //then
        assertNull(result.getId());
        assertNull(result.getType());
        assertNull(result.getMeasurementDate());
        assertNull(result.getQuality());
        assertNull(result.getValue());
    }

    @Test
    void mapToListDto() {
        //given
        Data source1 = Data.builder()
                .engineeringUnit(Data.EngineeringUnit.BARREL)
                .id(1L)
                .quality(Data.Quality.GOOD)
                .measurementDate(new Date())
                .build();

        Data source2 = Data.builder()
                .engineeringUnit(null)
                .id(1L)
                .quality(null)
                .measurementDate(null)
                .build();

        //when
        List<DataDto> resultList = new DataMapper().map(Arrays.asList(source1, source2));

        //then
        assertEquals(resultList.size(), 2);

        DataDto result1 = getSingleResult(resultList, DataDto.Quality.GOOD);
        assertEquals(result1.getMeasurementDate(), source1.getMeasurementDate());
        assertEquals(result1.getValue(), source1.getValue());
        assertEquals(result1.getId(), source1.getId());

        DataDto result2 = getSingleResult(resultList, null);
        assertNull(result2.getMeasurementDate());
        assertNull(result2.getValue());
        assertEquals(result2.getId(), source2.getId());
    }

    private DataDto getSingleResult(List<DataDto> resultList, DataDto.Quality quality) {
        return resultList
                .stream()
                .filter(e -> Objects.equals(quality, e.getQuality()))
                .findFirst()
                .orElse(null);
    }
}