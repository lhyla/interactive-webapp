package com.lhyla.interactivewebapp.util;

import org.joda.time.DateTime;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class DataUtilsTest {

    @Test
    void parseToDate_midnightDate_parsed() {
        //given
        String date = "2019-06-14.00:00:00";

        //when
        Date result = DataUtils.parseToDate(date);
        Date expected = new DateTime()
                .withYear(2019)
                .withMonthOfYear(6)
                .withDayOfMonth(14)
                .withTimeAtStartOfDay()
                .toDate();

        //then
        assertEquals(result.getTime(), expected.getTime());
    }

    @Test
    void parseToDate_wrongDate_exception() {
        //given
        String date = "2019-06-14T00:00:00";

        //when
        //then
        assertThrows(IllegalArgumentException.class, () -> DataUtils.parseToDate(date));
    }
}