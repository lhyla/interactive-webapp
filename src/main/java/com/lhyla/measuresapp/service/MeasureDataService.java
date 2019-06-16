package com.lhyla.measuresapp.service;

import com.lhyla.measuresapp.data.entity.MeasureData;
import com.lhyla.measuresapp.data.repository.MeasureDataDao;
import com.lhyla.measuresapp.dto.MeasureDataDto;
import com.lhyla.measuresapp.mapper.MeasureDataMapper;
import com.lhyla.measuresapp.service.interpolation.LinearInterpolationService;
import com.lhyla.measuresapp.util.MeasuresAppUtils;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@AllArgsConstructor
public class MeasureDataService {

    private MeasureDataDao measureDataDao;
    private LinearInterpolationService interpolationService;
    private MeasureDataMapper measureDataMapper;

    public Optional<MeasureDataDto> getLatestData() {
        Optional<MeasureDataDto> dataDto = measureDataDao
                .getLatestDataByMeasurementDate()
                .map(measureDataMapper::map);
        dataDto.ifPresent(e -> e.setType(MeasureDataDto.Type.MEASURED));

        return dataDto;
    }

    public Optional<MeasureDataDto> getAvgDataBetween(final String from,
                                                      final String to,
                                                      final boolean isIncludeBad) {
        MeasureDataDto data = MeasureDataDto.builder()
                .type(MeasureDataDto.Type.AVERAGE)
                .build();

        measureDataDao.getAvgDataBetween(
                MeasuresAppUtils.parseToDate(from),
                MeasuresAppUtils.parseToDate(to),
                getRequestedQualities(isIncludeBad)
        ).ifPresent(data::setValue);

        return Optional.of(data);
    }

    public List<MeasureDataDto> getGoodDataBetween(final String from,
                                                   final String to,
                                                   final Integer limit) {
        List<MeasureDataDto> data = measureDataMapper.map(
                measureDataDao.getSortedGoodDataBetween(
                        MeasuresAppUtils.parseToDate(from),
                        MeasuresAppUtils.parseToDate(to),
                        limit
                )
        );
        data.forEach(e -> e.setType(MeasureDataDto.Type.MEASURED));

        return data;
    }

    public List<MeasureDataDto> getInterpolation(final String from,
                                                 final String to,
                                                 final int intervalsInMinutes) {
        List<MeasureDataDto> data = measureDataMapper.map(
                interpolationService.interpolate(
                        MeasuresAppUtils.parseToDate(from),
                        MeasuresAppUtils.parseToDate(to),
                        intervalsInMinutes
                )
        );
        data.forEach(e -> e.setType(MeasureDataDto.Type.INTERPOLATED));

        return data;
    }

    private Set<MeasureData.Quality> getRequestedQualities(final boolean isIncludeBad) {
        Set<MeasureData.Quality> requestedQuality = new HashSet<>();
        requestedQuality.add(MeasureData.Quality.GOOD);

        if (isIncludeBad) {
            requestedQuality.add(MeasureData.Quality.BAD);
        }

        return Collections.unmodifiableSet(requestedQuality);
    }
}