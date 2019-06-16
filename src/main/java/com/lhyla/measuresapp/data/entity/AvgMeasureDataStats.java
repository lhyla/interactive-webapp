package com.lhyla.measuresapp.data.entity;

import java.math.BigDecimal;
import java.math.BigInteger;

public interface AvgMeasureDataStats {
    MeasureData.Quality getQuality();

    BigInteger getCount();

    BigDecimal getAvg();

    BigDecimal getMin();

    BigDecimal getMax();
}