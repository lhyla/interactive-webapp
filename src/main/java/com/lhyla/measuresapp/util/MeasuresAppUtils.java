package com.lhyla.measuresapp.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MeasuresAppUtils {

    public static final String DATE_FORMAT = "yyyy-MM-dd.HH:mm:ss";
    public static final int MILLIS_IN_MINUTE = 60000;
    public static final String TIMEZONE = "Europe/Berlin";

    /**
     * @return Create Date object from String in format yyyyMMddhhmmss
     * @throws IllegalArgumentException in case of exception during date parsing to expected format
     */
    public static Date parseToDate(String date) {
        try {
            return new SimpleDateFormat(DATE_FORMAT).parse(date);
        } catch (ParseException e) {
            throw new IllegalArgumentException("Cannot parse input date=" + date
                    + " to expected format=" + DATE_FORMAT,
                    e
            );
        }
    }

    /**
     * @return Create String in format yyyyMMddhhmmss from Date
     */
    public static String parseToDate(Date date) {
        return new SimpleDateFormat(DATE_FORMAT).format(date);
    }
}