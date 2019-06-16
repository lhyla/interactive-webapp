package com.lhyla.measuresapp.data.repository.avg;

import com.lhyla.measuresapp.data.entity.AvgMeasureDataStats;
import com.lhyla.measuresapp.data.entity.MeasureData;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Optional;
import java.util.Set;

public interface AvgMeasureDataRepository extends CrudRepository<MeasureData, BigDecimal> {

    @Query(value = "SELECT AVG(d.value) from MeasureData as d" +
            " where d.measurementDate >= ?1" +
            " and d.measurementDate <= ?2")
    Optional<BigDecimal> getAvgValueBetween(Date from, Date to);

    @Query(value = "SELECT AVG(d.value) as avg," +
            " min(d.value) as min," +
            " max (d.value) as max, " +
            " count (d.id) as count," +
            " d.quality as quality from MeasureData as d" +
            " where d.measurementDate >= ?1 and d.measurementDate <= ?2" +
            " group by quality")
    Set<AvgMeasureDataStats> getAvgCountMaxMinBetweenGroupByQuality(Date from, Date to);
}