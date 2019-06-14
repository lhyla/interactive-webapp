package com.lhyla.interactivewebapp.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DataUtils {

    public static final String DATE_FORMAT = "yyyy-MM-dd.HH:mm:ss";

    /**
     * @return LocalDateTime in format yyyyMMddhhmmss
     * @throws IllegalArgumentException in case of exception during date parsing to expected format
     */
    public static Date parseToDate(String date) {
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATE_FORMAT);
            return simpleDateFormat.parse(date);
        } catch (ParseException e) {
            throw new IllegalArgumentException("Cannot parse input date=" + date
                    + " to expected format=" + DATE_FORMAT,
                    e
            );
        }
    }
}