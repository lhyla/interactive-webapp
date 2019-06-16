package com.lhyla.measuresapp.data.repository.measure;

import com.lhyla.measuresapp.data.entity.MeasureData;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface MeasureDataRepository extends JpaRepository<MeasureData, BigInteger> {

    Optional<MeasureData> findTopByOrderByMeasurementDateDescIdDesc();

    List<MeasureData> findByQualityAndMeasurementDateBetweenOrderByMeasurementDateDescIdDesc(
            MeasureData.Quality quality,
            Date from,
            Date to,
            Pageable pagable
    );

    Set<MeasureData> findByQualityAndMeasurementDateBetween(
            MeasureData.Quality quality,
            Date from,
            Date to
    );
}