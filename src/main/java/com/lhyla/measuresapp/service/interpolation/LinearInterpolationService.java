package com.lhyla.measuresapp.service.interpolation;

import com.lhyla.measuresapp.data.entity.MeasureData;
import com.lhyla.measuresapp.data.repository.measure.MeasureDataDao;
import com.lhyla.measuresapp.exception.InterpolationException;
import com.lhyla.measuresapp.util.MeasuresAppUtils;
import lombok.AllArgsConstructor;
import org.apache.commons.math3.analysis.interpolation.LinearInterpolator;
import org.apache.commons.math3.analysis.polynomials.PolynomialSplineFunction;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

@Service
@AllArgsConstructor
public class LinearInterpolationService implements InterpolationService {

    private MeasureDataDao measureDataDao;

    public List<MeasureData> interpolate(final Date from,
                                         final Date to,
                                         final int intervalsInMinutes) throws InterpolationException {
        try {
            Set<MeasureData> data = measureDataDao.getGoodDataBetween(from, to);

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

    private PolynomialSplineFunction getSplineFunction(final Set<MeasureData> data) {
        return new LinearInterpolator()
                .interpolate(
                        getMeasurementDateTimesInMillis(data),
                        getValues(data)
                );
    }

    private double[] getValues(final Set<MeasureData> data) {
        return data.stream()
                .map(MeasureData::getValue)
                .filter(Objects::nonNull)
                .mapToDouble(BigDecimal::doubleValue)
                .toArray();
    }

    private double[] getMeasurementDateTimesInMillis(final Set<MeasureData> data) {
        return data.stream()
                .map(MeasureData::getMeasurementDate)
                .filter(Objects::nonNull)
                .mapToDouble(e -> e.getTime())
                .toArray();
    }

    private double[] getIntervals(final long from,
                                  final long to,
                                  final int intervalsInMinutes) {
        long intervalsSize = (to - from) / intervalsInMinutes;

        List<Long> intervalsList = new ArrayList<>();
        intervalsList.add(Long.valueOf(from));

        for (int i = 1; i < intervalsSize; i++) {
            long interval = intervalsList.get(i - 1) + (intervalsInMinutes * MeasuresAppUtils.MILLIS_IN_MINUTE);

            if (interval > to || interval < from) {
                break;
            }

            intervalsList.add(Long.valueOf(interval));
        }

        return intervalsList.stream().mapToDouble(Long::doubleValue).toArray();
    }

    private List<MeasureData> getInterpolations(final PolynomialSplineFunction splineFunction,
                                                final double[] intervals) {
        List<MeasureData> data = new ArrayList<>();

        for (double interval : intervals) {
            double value = splineFunction.value(interval);
            data.add(buildData(interval, value));
        }

        return Collections.unmodifiableList(data);
    }

    private MeasureData buildData(final double interval, final double value) {
        return MeasureData.builder()
                .value(BigDecimal.valueOf(value))
                .engineeringUnit(MeasureData.EngineeringUnit.BARREL)
                .measurementDate(new Date((long) interval))
                .build();
    }
}