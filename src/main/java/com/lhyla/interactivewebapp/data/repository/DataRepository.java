package com.lhyla.interactivewebapp.data.repository;

import com.lhyla.interactivewebapp.data.entity.Data;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface DataRepository extends JpaRepository<Data, BigInteger> {

    Optional<Data> findTopByOrderByMeasurementDateDescIdDesc();

    @Query(value = "SELECT AVG(d.value) from Data as d" +
            " where d.measurementDate >= ?1" +
            " and d.measurementDate <= ?2 and d.quality in (?3)")
    Optional<BigDecimal> getAvgValueBetween(Date from, Date to, Set<Data.Quality> requestedQualities);

    List<Data> findByQualityAndMeasurementDateBetweenOrderByMeasurementDateDescIdDesc(
            Data.Quality quality,
            Date from,
            Date to,
            Pageable pagable
    );
}