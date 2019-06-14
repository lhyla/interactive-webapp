package com.lhyla.interactivewebapp.data.repository;

import com.lhyla.interactivewebapp.data.entity.Data;
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
public class DataDao {

    private DataRepository dataRepository;

    public DataDao(@Autowired DataRepository dataRepository) {
        this.dataRepository = dataRepository;
    }

    public Optional<Data> getLatestDataByMeasurementDate() {
        return dataRepository.findTopByOrderByMeasurementDateDescIdDesc();
    }

    public Optional<BigDecimal> getAvgDataBetween(final Date from,
                                                  final Date to,
                                                  final Set<Data.Quality> requestedQualities) {

        return dataRepository.getAvgValueBetween(from, to, requestedQualities);
    }

    public List<Data> getGoodDataBetween(Date from, Date to, Integer limit) {
        return Collections.unmodifiableList(
                dataRepository.findByQualityAndMeasurementDateBetweenOrderByMeasurementDateDescIdDesc(
                        Data.Quality.GOOD,
                        from,
                        to,
                        PageRequest.of(0, limit)
                )
        );
    }
}