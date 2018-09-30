package com.gianlu.fidal.NetIO;

import android.support.annotation.NonNull;

import org.jsoup.nodes.Element;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class Event {
    public final Date date;
    public final Level level;
    public final String name;
    public final String desc;
    public final FidalApi.Type type;
    public final String place;
    private final String url;

    public Event(int year, @NonNull Element row) throws FidalApi.ParseException {
        this.date = new Date(year, row.child(1).child(0).text());
        this.level = Level.parse(row.child(2).child(0).text());
        this.name = row.child(3).child(0).text();
        this.url = row.child(3).child(0).attr("href");
        this.desc = row.child(3).child(2).text();
        this.type = parseType(row.child(4).text());
        this.place = row.child(5).text();
    }

    @NonNull
    private static FidalApi.Type parseType(@NonNull String text) throws FidalApi.ParseException {
        switch (text) {
            case "OUTDOOR":
                return FidalApi.Type.OUTDOOR;
            case "STRADA":
                return FidalApi.Type.STRADA;
            case "MONTAGNA":
                return FidalApi.Type.MONTAGNA;
            case "ULTRAMARATONA":
                return FidalApi.Type.ULTRAMARATONA;
            case "TRAIL":
                return FidalApi.Type.TRAIL;
            case "PIAZZA e altri ambiti":
                return FidalApi.Type.PIAZZA_ALTRO;
            case "INDOOR":
                return FidalApi.Type.INDOR;
            case "NORDIC WALKING":
                return FidalApi.Type.NORDIC_WALKING;
            case "CROSS":
                return FidalApi.Type.CROSS;
            default:
                throw new FidalApi.ParseException("Unknown type: " + text);
        }
    }

    public enum Level {
        INTERNAZIONALE,
        GOLD,
        SILVER,
        BRONZE,
        NAZIONALE;

        @NonNull
        private static Level parse(@NonNull String text) throws FidalApi.ParseException {
            switch (text) {
                case "I":
                    return INTERNAZIONALE;
                case "G":
                    return GOLD;
                case "S":
                    return SILVER;
                case "B":
                    return BRONZE;
                case "N":
                    return NAZIONALE;
                default:
                    throw new FidalApi.ParseException("Unknown level: " + text);
            }
        }
    }

    public class Date {
        private final long start;
        private final long end;

        private Date(int year, String text) {
            String[] split = text.split("/");
            int month = Integer.parseInt(split[1]);

            if (text.contains("-")) {
                split = split[0].split("-");
                start = 31556952000L * year + Integer.parseInt(split[0]) * 86400000L + month * 2629746000L;
                end = 31556952000L * year + Integer.parseInt(split[1]) * 86400000L + month * 2629746000L;
            } else {
                int day = Integer.parseInt(split[0]);
                start = end = 31556952000L * year + day * 86400000L + month * 2629746000L;
            }
        }

        @NonNull
        public String toReadable() {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM", Locale.getDefault());
            if (isSingleDay()) return sdf.format(start);
            else return sdf.format(start) + " - " + sdf.format(end);
        }

        public boolean isSingleDay() {
            return start == end;
        }
    }
}
