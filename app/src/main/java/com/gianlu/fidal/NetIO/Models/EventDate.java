package com.gianlu.fidal.NetIO.Models;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;

import com.gianlu.fidal.NetIO.FidalApi;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class EventDate {
    private final long start;
    private final long end;

    public EventDate(long start, long end) {
        this.start = start;
        this.end = end;
    }

    @NonNull
    public static EventDate fromEvent(int year, @NonNull String text) throws FidalApi.ParseException {
        long start, end;
        if (text.contains("-")) {
            String[] split = text.split("-");
            String month = split[1].substring(split[1].indexOf('/') + 1);
            start = parseDate("dd/MM/yyyy", split[0] + "/" + month + "/" + year);
            end = parseDate("dd/MM/yyyy", split[1] + "/" + year);
        } else {
            start = end = parseDate("dd/MM/yyyy", text + "/" + year);
        }

        return new EventDate(start, end);
    }

    @SuppressLint("SimpleDateFormat")
    private static long parseDate(@NonNull String format, @NonNull String date) throws FidalApi.ParseException {
        try {
            return new SimpleDateFormat(format).parse(date).getTime();
        } catch (ParseException ex) {
            throw new FidalApi.ParseException(ex);
        }
    }

    @NonNull
    public static EventDate fromEventDetails(String text) throws FidalApi.ParseException {
        long start, end;
        if (text.contains("-")) {
            String[] split = text.split("-");
            start = parseDate("dd/MM/yy", split[0].trim());
            end = parseDate("dd/MM/yy", split[1].trim());
        } else {
            start = end = parseDate("dd/MM/yy", text);
        }

        return new EventDate(start, end);
    }

    @NonNull
    public String toReadableShort() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd", Locale.getDefault());
        if (isSingleDay()) {
            return sdf.format(start);
        } else {
            return sdf.format(start) + " - " + sdf.format(end);
        }
    }

    public boolean isSingleDay() {
        return start == end;
    }
}
