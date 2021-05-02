package com.wfour.onlinestoreapp.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Suusoft on 06/22/2016.
 */
public class DateTimeUtil {
    public static final String FORMAT_DATE = "MM/dd/yy";

    /**
     * @param timeStamp    As second
     * @param outputFormat Expected output format
     * @return String of date
     */
    public static String convertTimeStampToDate(String timeStamp, String outputFormat) {
        SimpleDateFormat formater = new SimpleDateFormat(outputFormat, Locale.getDefault());
        try {
            Date date = new Date(Long.parseLong(timeStamp) * 1000);
            return formater.format(date);
        } catch (NumberFormatException ex) {
            return formater.format(new Date());
        }
    }

    /**
     * @param timeStamp    As second
     * @param outputFormat Expected output format
     * @return String of date
     */
    public static String convertTimeStampToDate(long timeStamp, String outputFormat) {
        SimpleDateFormat formater = new SimpleDateFormat(outputFormat, Locale.getDefault());
        try {
            Date date = new Date(timeStamp * 1000);
            return formater.format(date);
        } catch (NumberFormatException ex) {
            return formater.format(new Date());
        }
    }

    /**
     * @param timeStamp    As second
     * @return String of date
     */
    public static Date convertTimeStampTooDate(long timeStamp) {
        try {
            Date date = new Date(timeStamp * 1000);
            return date;
        } catch (NumberFormatException ex) {
            return new Date();
        }
    }

    /**
     * @param date
     * @return TimeStamp of the given date as millisecond
     */
    public static long convertDateToTimeStamp(Date date) {
        long result;
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        try {
            result = cal.getTimeInMillis();
        } catch (Exception ex) {
            result = 0;
        }
        return result;
    }

    /**
     * @param curTimeStamp  As second
     * @param specTimeStamp As second
     * @return Date difference
     */
    public static int getDateDiff(long curTimeStamp, long specTimeStamp) {
        try {
            long diff = specTimeStamp - curTimeStamp;
            long day = diff / 24 / 60 / 60;

            String strDay = day + "";
            if (strDay.contains(".")) {
                strDay = strDay.substring(0, strDay.indexOf("."));
            }

            return Integer.parseInt(strDay);
        } catch (Exception ex) {
            ex.printStackTrace();
            return 0;
        }
    }

    /**
     * @param strDate
     * @param inputFormat  Original format
     * @param outputFormat Expected output format
     * @return New date string with outputFormat
     */
    public static String changeDateFormat(String strDate, String inputFormat, String outputFormat) {
        SimpleDateFormat dateFormaterInput, dateFormaterOutput;
        dateFormaterInput = new SimpleDateFormat(inputFormat, Locale.getDefault());
        dateFormaterOutput = new SimpleDateFormat(outputFormat, Locale.getDefault());
        Date dob;
        try {
            dob = dateFormaterInput.parse(strDate);
            return dateFormaterOutput.format(dob);
        } catch (ParseException e) {
            return strDate;
        }
    }

    /**
     * @param date
     * @param outputFormat Expected output format
     * @return String from date
     */
    public static String convertDateToString(Date date, String outputFormat) {
        SimpleDateFormat formater = new SimpleDateFormat(outputFormat, Locale.getDefault());
        return formater.format(date);
    }

    /**
     * @param strDate
     * @param format  Format of strDate
     * @return Date from string
     */
    public static Date convertStringToDate(String strDate, String format) {
        DateFormat formatter = new SimpleDateFormat(format, Locale.getDefault());
        try {
            return formatter.parse(strDate);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * @param timeStamp As second
     * @return 00:00:00 of the date from timeStamp
     */
    public static String convertTimeStampToStartOfDate(String timeStamp) {
        String result = "";

        String strDate = convertTimeStampToDate(timeStamp.substring(0, timeStamp.length() - 3), "dd/MM/yyyy") + " 00:00:00";
        Date date = null;
        try {
            date = SimpleDateFormat.getDateInstance().parse(strDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (date != null) {
            result = (date.getTime() + "").substring(0, (date.getTime() + "").length() - 3);
        }

        return result;
    }


    /**
     *
     * @return Current time in timestamp as second
     */
    public static long getCurrentTime() {
        return Calendar.getInstance().getTimeInMillis() / 1000;
    }

    /**
     * This method is just for temporary, need to be improved
     *
     * @param curTimeStamp  As second
     * @param specTimeStamp As second
     * @return Array contains  rest of day, hour, minute, and second
     */
    public static ArrayList<String> countDown(long curTimeStamp, long specTimeStamp) {
        ArrayList<String> arr = new ArrayList<>();

        long diff = specTimeStamp - curTimeStamp;

        long sec = diff % 60;
        long min = (diff / 60) % 60;
        long hour = (diff / 60 / 60) % 24;
        long day = diff / 24 / 60 / 60;

        String strSec = sec + "";
        if (sec < 10) {
            strSec = "0" + sec;
        }
        if (strSec.contains(".")) {
            strSec = strSec.substring(0, strSec.indexOf("."));
        }

        String strMin = min + "";
        if (min < 10) {
            strMin = "0" + min;
        }
        if (strMin.contains(".")) {
            strMin = strMin.substring(0, strMin.indexOf("."));
        }

        String strHour = hour + "";
        if (hour < 10) {
            strHour = "0" + hour;
        }
        if (strHour.contains(".")) {
            strHour = strHour.substring(0, strHour.indexOf("."));
        }

        String strDay = day + "";
        if (day < 10) {
            strDay = "0" + day;
        }
        if (strDay.contains(".")) {
            strDay = strDay.substring(0, strDay.indexOf("."));
        }

        arr.add(strDay);
        arr.add(strHour);
        arr.add(strMin);
        arr.add(strSec);

        return arr;
    }

    public static String getEndAtFromNow(int hours){
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.HOUR, hours);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy, MM-dd HH:mm:ss");
        String currentDateandTime = sdf.format(calendar.getTime());

        return currentDateandTime;
    }


}
