package com.lhyla.measuresapp.data.repository;

import com.lhyla.measuresapp.data.entity.MeasureData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.*;

/**
 * Handles more complicated business logic
 * which are not possible or not efficient to be executed on DB level
 * <p>
 * Also could contain logic, which query method from repository should be executed, based on parameters values
 */
@Component
public class MeasureDataDao {

    private MeasureDataRepository measureDataRepository;

    public MeasureDataDao(@Autowired MeasureDataRepository measureDataRepository) {
        this.measureDataRepository = measureDataRepository;
    }

    public Optional<MeasureData> getLatestDataByMeasurementDate() {
        return measureDataRepository.findTopByOrderByMeasurementDateDescIdDesc();
    }

    public Optional<BigDecimal> getAvgDataBetween(final Date from,
                                                  final Date to,
                                                  final Set<MeasureData.Quality> requestedQualities) {

        return measureDataRepository.getAvgValueBetween(from, to, requestedQualities);
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