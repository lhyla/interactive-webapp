package com.lhyla.interactivewebapp.service;

import com.lhyla.interactivewebapp.data.entity.Data;
import com.lhyla.interactivewebapp.data.repository.DataDao;
import com.lhyla.interactivewebapp.service.interpolation.LinearInterpolationService;
import com.lhyla.interactivewebapp.util.DataUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

@Service
public class DataService {

    private DataDao dataDao;
    private LinearInterpolationService interpolationService;

    public DataService(@Autowired DataDao dataDao,
                       @Autowired LinearInterpolationService interpolationService) {
        this.dataDao = dataDao;
        this.interpolationService = interpolationService;
    }

    public Optional<Data> getLatestData() {
        return dataDao.getLatestDataByMeasurementDate();
    }

    public Optional<BigDecimal> getAvgDataBetween(final String from,
                                                  final String to,
                                                  final boolean isIncludeBad) {
        return dataDao.getAvgDataBetween(
                DataUtils.parseToDate(from),
                DataUtils.parseToDate(to),
                getRequestedQualities(isIncludeBad)
        );
    }

    public List<Data> getGoodDataBetween(final String from,
                                         final String to,
                                         final Integer limit) {
        return dataDao.getSortedGoodDataBetween(
                DataUtils.parseToDate(from),
                DataUtils.parseToDate(to),
                limit
        );
    }

    private Set<Data.Quality> getRequestedQualities(final boolean isIncludeBad) {
        Set<Data.Quality> requestedQuality = new HashSet<>();
        requestedQuality.add(Data.Quality.GOOD);

        if (isIncludeBad) {
            requestedQuality.add(Data.Quality.BAD);
        }

        return Collections.unmodifiableSet(requestedQuality);
    }

    public List<Data> getInterpolation(final String from,
                                       final String to,
                                       final int intervalsInMinutes) {
        return interpolationService.interpolate(
                DataUtils.parseToDate(from),
                DataUtils.parseToDate(to),
                intervalsInMinutes
        );
    }
}