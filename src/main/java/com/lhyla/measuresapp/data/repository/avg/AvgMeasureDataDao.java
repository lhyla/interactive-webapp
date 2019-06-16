package com.lhyla.measuresapp.data.repository.avg;

import com.lhyla.measuresapp.data.entity.AvgMeasureDataStats;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Date;
import java.util.Optional;
import java.util.Set;

@Service
@AllArgsConstructor
public class AvgMeasureDataDao {

    private AvgMeasureDataRepository avgMeasureDataRepository;

    public Set<AvgMeasureDataStats> getAvgCountMaxMinBetweenGroupByQuality(Date from, Date to) {
        return Collections.unmodifiableSet(
                avgMeasureDataRepository.getAvgCountMaxMinBetweenGroupByQuality(from, to)
        );
    }

    public Optional<BigDecimal> getAvgDataBetween(final Date from,
                                                  final Date to) {

        return avgMeasureDataRepository.getAvgValueBetween(from, to);
    }
}
