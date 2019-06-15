package com.lhyla.interactivewebapp.service.interpolation;

import com.lhyla.interactivewebapp.data.entity.Data;
import com.lhyla.interactivewebapp.exception.InterpolationException;

import java.util.Date;
import java.util.List;

public interface InterpolationService {
    List<Data> interpolate(Date from, Date to, int intervalsInMinutes) throws InterpolationException;
}