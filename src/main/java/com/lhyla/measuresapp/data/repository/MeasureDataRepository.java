package com.lhyla.measuresapp.data.repository;

import com.lhyla.measuresapp.data.entity.MeasureData;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface MeasureDataRepository extends JpaRepository<MeasureData, BigInteger> {

    Optional<MeasureData> findTopByOrderByMeasurementDateDescIdDesc();

    @Query(value = "SELECT AVG(d.value) from MeasureData as d" +
            " where d.measurementDate >= ?1" +
            " and d.measurementDate <= ?2 and d.quality in (?3)")
    Optional<BigDecimal> getAvgValueBetween(Date from, Date to, Set<MeasureData.Quality> requestedQualities);

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