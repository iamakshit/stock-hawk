package com.sam_chordas.android.stockhawk.rest;


import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by akshitgupta on 03/07/16.
 */
public class DateUtils {

    public static Date dateToString(String dateString) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try
        {
            date = df.parse(dateString);
            String newDateString = df.format(date);
            System.out.println(newDateString);
        }
        catch (ParseException e)
        {
            e.printStackTrace();
        }
        return date;
    }

    public static String getCurrentDate() {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = df.format(cal.getTime());
        return formattedDate;
    }

    public static String computeDateByString(String month) {
        switch (month) {
            case "1W":
                return getPreviousDaysDate(7);
            case "1M":
                return getPreviousMonthDate(1);
            case "3M":
                return getPreviousMonthDate(3);

            case "6M":
                return getPreviousMonthDate(6);

            case "1Y":
                return getPreviousMonthDate(12);
        }
        return getPreviousDaysDate(7);

    }

    public static String getPreviousMonthDate(int month) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MONTH, -1 * (month));
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = df.format(cal.getTime());
        return formattedDate;
    }

    public static String getPreviousDaysDate(int days) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -1 * (days));
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = df.format(cal.getTime());
        return formattedDate;
    }
}
