package com.gianlu.fidal.NetIO.Models;

import android.support.annotation.NonNull;

import com.gianlu.fidal.NetIO.FidalApi;

import org.jsoup.nodes.Element;

public class Event {
    public final EventDate date;
    public final Level level;
    public final String name;
    public final String desc;
    public final FidalApi.Type type;
    public final String place;
    public final String url;

    public Event(int year, @NonNull Element row) throws FidalApi.ParseException {
        this.date = EventDate.fromEvent(year, row.child(1).child(0).text());
        this.level = Level.parse(row.child(2).child(0).text());
        this.name = row.child(3).child(0).text();
        this.url = row.child(3).child(0).attr("href");
        this.desc = row.child(3).child(2).text();
        this.place = row.child(5).text();
        this.type = FidalApi.Type.parseType(row.child(4).text());
    }

    public enum Level {
        INTERNAZIONALE, GOLD, SILVER,
        BRONZE, NAZIONALE, PROVINCIALE,
        REGIONALE_OPEN;

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
                case "P":
                    return PROVINCIALE;
                case "R":
                    return REGIONALE_OPEN;
                default:
                    throw new FidalApi.ParseException("Unknown level: " + text);
            }
        }

        @NonNull
        public static Level parseExtended(@NonNull String text) throws FidalApi.ParseException {
            switch (text) {
                case "REGIONALE OPEN":
                    return REGIONALE_OPEN;
                case "Nazionale":
                    return NAZIONALE;
                case "Internazionale":
                    return INTERNAZIONALE;
                case "BRONZE":
                    return BRONZE;
                case "SILVER":
                    return SILVER;
                case "GOLD":
                    return GOLD;
                case "PROVINCIALE":
                    return PROVINCIALE;
                default:
                    throw new FidalApi.ParseException("Unknown level: " + text);
            }
        }
    }
}