package com.robert.ParkingLot.utils;

import java.util.Calendar;
import java.util.Date;

public class CurrentDateUtil {
    public static String getCurrentDateCustomFormatAsString() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH) + 1;
        int year = calendar.get(Calendar.YEAR);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        int second = calendar.get(Calendar.SECOND);
        return day + "-" + month + "-" + year + " " + hour + ":" + minute + ":" + second;
    }
}
