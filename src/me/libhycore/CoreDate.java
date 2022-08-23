package me.libhycore;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class CoreDate {

    public static String timestamp(String format) {
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern(format);
        LocalDateTime currentTime = LocalDateTime.now();
        return timeFormatter.format(currentTime);
    }

    public static String timestamp() {
        return timestamp("yyyy-MM-dd HH:mm:ss");
    }

    public static String year2Digits() {
        return timestamp("yyyy").substring(2);
    }

    public static String year4Digits() {
        return timestamp("yyyy");
    }

    public static String month() {
        return timestamp("MM");
    }

    public static String date() {
        return timestamp("dd");
    }

    public static String hour24() {
        return timestamp("HH");
    }

    public static String hour12() {
        int integerForm = Integer.parseInt(hour24());
        if (integerForm > 12) return (integerForm - 12) + "";
        return integerForm + "";
    }

    public static String ampm() {
        if (Integer.parseInt(hour24()) >= 12) return "PM";
        return "AM";
    }

    public static String minute() {
        return timestamp("mm");
    }

    public static String second() {
        return timestamp("ss");
    }

    public static long secondsSince1970() {
        return mSecSince1970() / 1000;
    }

    public static long secondsSince1970(long offsetSec) {
        return secondsSince1970() + offsetSec;
    }

    public static long mSecSince1970() {
        return System.currentTimeMillis();
    }

    public static long mSecSince1970(long offsetMsec) {
        return System.currentTimeMillis() + offsetMsec;
    }
}