package com.lhyla.measuresapp.data.repository.measure;

import com.lhyla.measuresapp.data.entity.MeasureData;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Handles more complicated business logic
 * which are not possible or not efficient to be executed on DB level
 * <p>
 * Also could contain logic, which query method from repository should be executed, based on parameters values
 */
@Service
@AllArgsConstructor
public class MeasureDataDao {

    private MeasureDataRepository measureDataRepository;

    public Optional<MeasureData> getLatestDataByMeasurementDate() {
        return measureDataRepository.findTopByOrderByMeasurementDateDescIdDesc();
    }

    public List<MeasureData> getSortedGoodDataBetween(final Date from,
                                                      final Date to,
                                                      final Integer limit) {
        return Collections.unmodifiableList(
                measureDataRepository.findByQualityAndMeasurementDateBetweenOrderByMeasurementDateDescIdDesc(
                        MeasureData.Quality.GOOD,
                        from,
                        to,
                        PageRequest.of(0, limit)
                )
        );
    }

    public Set<MeasureData> getGoodDataBetween(final Date from,
                                               final Date to) {
        return Collections.unmodifiableSet(
                measureDataRepository.findByQualityAndMeasurementDateBetween(
                        MeasureData.Quality.GOOD,
                        from,
                        to
                )
        );
    }
}