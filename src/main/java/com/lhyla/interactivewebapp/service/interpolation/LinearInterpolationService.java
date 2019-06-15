package com.lhyla.interactivewebapp.service.interpolation;

import com.lhyla.interactivewebapp.data.entity.Data;
import com.lhyla.interactivewebapp.data.repository.DataDao;
import com.lhyla.interactivewebapp.exception.InterpolationException;
import com.lhyla.interactivewebapp.util.DataUtils;
import org.apache.commons.math3.analysis.interpolation.LinearInterpolator;
import org.apache.commons.math3.analysis.polynomials.PolynomialSplineFunction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

@Service
public class LinearInterpolationService implements InterpolationService {

    private static final long DIVIDER = 1_000_000_000;

    private DataDao dataDao;

    public LinearInterpolationService(@Autowired DataDao dataDao) {
        this.dataDao = dataDao;
    }

    public List<Data> interpolate(final Date from,
                                  final Date to,
                                  final int intervalsInMinutes) throws InterpolationException {
        try {
            Set<Data> data = dataDao.getGoodDataBetween(from, to);

            double[] intervals = getIntervals(
                    from.getTime(),
                    to.getTime(),
                    intervalsInMinutes
            );

            return getInterpolations(getSplineFunction(data), intervals);
        } catch (Exception e) {
            throw new InterpolationException(
                    "Cannot interpolate data base on given input" +
                            " from=" + from +
                            " to=" + to +
                            " intervalsInMinutes=" + intervalsInMinutes, e);
        }
    }

    private PolynomialSplineFunction getSplineFunction(Set<Data> data) {
        return new LinearInterpolator()
                .interpolate(
                        getMeasurementDateTimesInMillis(data),
                        getValues(data)
                );
    }

    private double[] getValues(final Set<Data> data) {
        return data.stream()
                .map(Data::getValue)
                .filter(Objects::nonNull)
                .mapToDouble(BigDecimal::doubleValue)
                .toArray();
    }

    private double[] getMeasurementDateTimesInMillis(final Set<Data> data) {
        return data.stream()
                .map(Data::getMeasurementDate)
                .filter(Objects::nonNull)
                .mapToDouble(e -> e.getTime() / DataUtils.MILLIS_IN_MINUTE)
                .toArray();
    }

    private double[] getIntervals(long from,
                                  long to,
                                  final int intervalsInMinutes) {
        from = from / DataUtils.MILLIS_IN_MINUTE;
        to = to / DataUtils.MILLIS_IN_MINUTE;

        long intervalsSize = (to - from) / intervalsInMinutes;

        List<Long> intervalsList = new ArrayList<>();
        intervalsList.add(Long.valueOf(from));

        for (int i = 1; i < intervalsSize; i++) {
            long interval = intervalsList.get(i - 1) + intervalsInMinutes;

            if (interval > to || interval < from) {
                break;
            }

            intervalsList.add(Long.valueOf(interval));
        }

        return intervalsList.stream().mapToDouble(Long::doubleValue).toArray();
    }

    private List<Data> getInterpolations(final PolynomialSplineFunction splineFunction,
                                         final double[] intervals) {
        List<Data> data = new ArrayList<>();

        for (double interval : intervals) {
            double value = splineFunction.value(interval);
            data.add(buildData(interval, value));
        }

        return Collections.unmodifiableList(data);
    }

    private Data buildData(double interval, double value) {
        return Data.builder()
                .value(BigDecimal.valueOf(value))
                .engineeringUnit(Data.EngineeringUnit.BARREL)
                .quality(Data.Quality.GOOD)
                .id(null)
                .measurementDate(new Date((long) interval))
                .build();
    }
}