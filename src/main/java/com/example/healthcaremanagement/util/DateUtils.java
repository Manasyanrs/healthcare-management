package com.example.healthcaremanagement.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class DateUtils {
    private static final SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
    private static final SimpleDateFormat timeFormatter = new SimpleDateFormat("HH:mm");
    private static final SimpleDateFormat CURRENT_DATE_TIME = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    private static final List<String> OFFICE_HOURS = List.of("09:00", "09:30", "10:00", "10:30", "11:00",
            "11:30", "12:00", "12:30", "14:00", "14:30", "15:00", "15:30", "16:00", "17:30");

    private DateUtils() {
    }

    public static List<String> getFreeTime(List<String> registerDate) {
        List<String> registerTimeInDB = registerDate.stream().map((p) -> p.split(" ")[1].substring(0, 5)).collect(Collectors.toList());
        Set<String> officeHours = findDifference(OFFICE_HOURS, registerTimeInDB);
        List<String> collect = officeHours.stream().collect(Collectors.toList());
        Collections.sort(collect);
        return collect;
    }

    public static Date parseToDate(String date) {
        try {
            return dateFormatter.parse(date);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    public static Date parseToTime(String time) {
        try {
            return timeFormatter.parse(time);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }


    public static Date parsToFormatDate(String currentDataTime) {
        try {
            return CURRENT_DATE_TIME.parse(currentDataTime);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    public static String dateToString(Date date) {
        return dateFormatter.format(date);
    }

    public static String timeToString(Date date) {
        return timeFormatter.format(date);
    }

    public static String currentDateTimeToString(Date date) {
        return CURRENT_DATE_TIME.format(date);
    }

    private static <T> Set<T> findDifference(List<T> first, List<T> second) {
        return first.stream()
                .filter(i -> !second.contains(i))
                .collect(Collectors.toSet());
    }

}
