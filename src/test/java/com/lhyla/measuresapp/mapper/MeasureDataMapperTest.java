package com.lhyla.measuresapp.mapper;

import com.lhyla.measuresapp.data.entity.MeasureData;
import com.lhyla.measuresapp.dto.MeasureDataDto;
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

class MeasureDataMapperTest {

    private static Stream<Arguments> simpleData() {
        return Stream.of(
                Arguments.of(MeasureData.builder()
                        .engineeringUnit(MeasureData.EngineeringUnit.BARREL)
                        .id(1L)
                        .quality(MeasureData.Quality.GOOD)
                        .measurementDate(new Date())
                        .build()
                ),
                Arguments.of(MeasureData.builder()
                        .engineeringUnit(MeasureData.EngineeringUnit.BARREL)
                        .id(2L)
                        .quality(MeasureData.Quality.BAD)
                        .measurementDate(new Date())
                        .build()
                )
        );
    }

    @ParameterizedTest
    @MethodSource("simpleData")
    void mapToDto_simpleData_mapped(MeasureData measureData) {
        //given
        //when
        MeasureDataDto result = new MeasureDataMapper().map(measureData);

        //then
        assertEquals(result.getValue(), measureData.getValue());
        assertEquals(result.getMeasurementDate(), measureData.getMeasurementDate());
        assertEquals(result.getQuality().name(), measureData.getQuality().name());
        assertEquals(result.getId(), measureData.getId());
        assertNull(result.getType());
    }

    @Test
    void mapToDto_nullData_mapped() {
        //given
        MeasureData measureData = MeasureData.builder()
                .engineeringUnit(null)
                .id(null)
                .quality(null)
                .measurementDate(null)
                .build();

        //when
        MeasureDataDto result = new MeasureDataMapper().map(measureData);

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
        MeasureData source1 = MeasureData.builder()
                .engineeringUnit(MeasureData.EngineeringUnit.BARREL)
                .id(1L)
                .quality(MeasureData.Quality.GOOD)
                .measurementDate(new Date())
                .build();

        MeasureData source2 = MeasureData.builder()
                .engineeringUnit(null)
                .id(1L)
                .quality(null)
                .measurementDate(null)
                .build();

        //when
        List<MeasureDataDto> resultList = new MeasureDataMapper().map(Arrays.asList(source1, source2));

        //then
        assertEquals(resultList.size(), 2);

        MeasureDataDto result1 = getSingleResult(resultList, MeasureDataDto.Quality.GOOD);
        assertEquals(result1.getMeasurementDate(), source1.getMeasurementDate());
        assertEquals(result1.getValue(), source1.getValue());
        assertEquals(result1.getId(), source1.getId());

        MeasureDataDto result2 = getSingleResult(resultList, null);
        assertNull(result2.getMeasurementDate());
        assertNull(result2.getValue());
        assertEquals(result2.getId(), source2.getId());
    }

    private MeasureDataDto getSingleResult(List<MeasureDataDto> resultList, MeasureDataDto.Quality quality) {
        return resultList
                .stream()
                .filter(e -> Objects.equals(quality, e.getQuality()))
                .findFirst()
                .orElse(null);
    }
}