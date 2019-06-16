package com.lhyla.measuresapp.service.interpolation;

import com.lhyla.measuresapp.data.entity.MeasureData;
import com.lhyla.measuresapp.exception.InterpolationException;

import java.util.Date;
import java.util.List;

public interface InterpolationService {
    List<MeasureData> interpolate(Date from, Date to, int intervalsInMinutes) throws InterpolationException;
}