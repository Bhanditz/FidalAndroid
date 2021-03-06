package com.gianlu.fidal.NetIO.Models;

import android.content.Context;

import com.gianlu.commonutils.GetText;
import com.gianlu.fidal.NetIO.FidalApi;
import com.gianlu.fidal.R;

import org.jsoup.nodes.Element;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;

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
        this.level = Level.parse(row.child(2).child(0).attr("title"));
        this.name = row.child(3).child(0).text();
        this.desc = row.child(3).child(2).text();
        this.place = row.child(5).text();
        this.type = FidalApi.Type.parseType(row.child(4).text());
        this.url = parseUrl(row.child(3).child(0).attr("href"), level);
    }

    @NonNull
    private static String parseUrl(String original, @NonNull Level level) throws FidalApi.ParseException {
        int index = original.lastIndexOf('/');
        if (index == -1) throw new FidalApi.ParseException("Couldn't find '/': " + original);

        String last = original.substring(index + 1);
        if (last.contains("COD") || last.contains("REG"))
            return original;

        if (level == Level.NAZIONALE || level == Level.BRONZE || level == Level.GOLD || level == Level.SILVER || level == Level.INTERNAZIONALE) {
            last = "COD" + last;
        } else if (level == Level.REGIONALE_OPEN || level == Level.REGIONALE || level == Level.PROVINCIALE) {
            last = "REG" + last;
        }

        return original.substring(0, index + 1) + last;
    }

    public enum Level implements GetText {
        INTERNAZIONALE, GOLD, SILVER,
        BRONZE, NAZIONALE, PROVINCIALE,
        REGIONALE_OPEN, REGIONALE;

        @NonNull
        private static Level parse(@NonNull String text) throws FidalApi.ParseException {
            switch (text) {
                case "INTERNAZ.LE":
                    return INTERNAZIONALE;
                case "GOLD":
                    return GOLD;
                case "SILVER":
                    return SILVER;
                case "BRONZE":
                    return BRONZE;
                case "NAZ.LE":
                    return NAZIONALE;
                case "PROVINCIALE":
                    return PROVINCIALE;
                case "REGIONALE OPEN":
                    return REGIONALE_OPEN;
                case "REGIONALE":
                    return REGIONALE;
                default:
                    throw new FidalApi.ParseException("Unknown level: " + text);
            }
        }

        @NonNull
        public static Level parseExtended(@NonNull String text) throws FidalApi.ParseException {
            switch (text) {
                case "REGIONALE":
                    return REGIONALE;
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

        @DrawableRes
        public int getIcon() {
            switch (this) {
                case INTERNAZIONALE:
                    return R.drawable.world;
                case GOLD:
                    return R.drawable.gold_medal;
                case SILVER:
                    return R.drawable.silver_medal;
                case BRONZE:
                    return R.drawable.bronze_medal;
                case NAZIONALE:
                    return R.drawable.italy_flag;
                case PROVINCIALE:
                    return R.drawable.pin_1;
                case REGIONALE:
                case REGIONALE_OPEN:
                    return R.drawable.pin_2;
                default:
                    throw new IllegalArgumentException("Missing icon for " + this);
            }
        }

        @NonNull
        @Override
        public String getText(@NonNull Context context) {
            switch (this) {
                case INTERNAZIONALE:
                    return context.getString(R.string.level_international);
                case GOLD:
                    return context.getString(R.string.level_gold);
                case SILVER:
                    return context.getString(R.string.level_silver);
                case BRONZE:
                    return context.getString(R.string.level_bronze);
                case NAZIONALE:
                    return context.getString(R.string.level_national);
                case PROVINCIALE:
                    return context.getString(R.string.level_provincial);
                case REGIONALE_OPEN:
                    return context.getString(R.string.level_regionalOpen);
                case REGIONALE:
                    return context.getString(R.string.level_regional);
                default:
                    throw new IllegalArgumentException("Unknown level: " + this);
            }
        }
    }
}
