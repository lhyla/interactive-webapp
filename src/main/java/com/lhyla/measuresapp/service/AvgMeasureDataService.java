package com.lhyla.measuresapp.service;

import com.lhyla.measuresapp.data.entity.AvgMeasureDataStats;
import com.lhyla.measuresapp.data.repository.avg.AvgMeasureDataDao;
import com.lhyla.measuresapp.dto.avg.AvgMeasureDataDto;
import com.lhyla.measuresapp.mapper.MeasureDataMapper;
import com.lhyla.measuresapp.util.MeasuresAppUtils;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;

@Service
@AllArgsConstructor
public class AvgMeasureDataService {

    private AvgMeasureDataDao avgMeasureDataDao;
    private MeasureDataMapper measureDataMapper;

    public Optional<AvgMeasureDataDto> getAvgDataBetween(final String from,
                                                         final String to) {
        AvgMeasureDataDto data = AvgMeasureDataDto.builder()
                .build();

        avgMeasureDataDao.getAvgDataBetween(
                MeasuresAppUtils.parseToDate(from),
                MeasuresAppUtils.parseToDate(to)
        ).ifPresent(data::setAvg);

        Set<AvgMeasureDataStats> avgStats = avgMeasureDataDao.getAvgCountMaxMinBetweenGroupByQuality(
                MeasuresAppUtils.parseToDate(from),
                MeasuresAppUtils.parseToDate(to)
        );

        data.setAvgDataStats(
                measureDataMapper.map(avgStats)
        );

        return Optional.of(data);
    }
}
