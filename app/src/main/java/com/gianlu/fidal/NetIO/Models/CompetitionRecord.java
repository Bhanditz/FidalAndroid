package com.gianlu.fidal.NetIO.Models;

import com.gianlu.fidal.NetIO.FidalApi;
import com.gianlu.fidal.NetIO.Models.Competitions.AbsCompetition;

import org.jsoup.nodes.Element;

import androidx.annotation.NonNull;

public class CompetitionRecord {
    public final AbsCompetition competition;
    public final Type type;
    public final float performance;
    public final float wind;
    public final int year;
    public final String place;
    public final boolean windy;

    public CompetitionRecord(Element element, boolean windy) throws FidalApi.ParseException {
        this.competition = AbsCompetition.parse(element.child(0).text());
        this.type = Type.parseType(element.child(1).text());
        this.performance = parsePerformance(element.child(2).text());
        String windStr = element.child(3).text();
        this.wind = windStr.isEmpty() ? 0f : Float.parseFloat(windStr);
        this.year = Integer.parseInt(element.child(4).text());
        this.place = element.child(5).text();
        this.windy = windy;
    }

    private static float parsePerformance(@NonNull String val) {
        if (val.contains(":")) {
            String[] split = val.split(":");
            float result = 0f;
            for (int i = 0; i < split.length; i++) {
                if (i == split.length - 1) result += Float.parseFloat(split[i]);
                else result += Integer.parseInt(split[i]) * 60 * (i + 1);
            }

            return result;
        } else {
            return Float.parseFloat(val);
        }
    }

    public enum Type {
        TRACK, INDOOR, ROAD;

        @NonNull
        public static Type parseType(@NonNull String val) throws FidalApi.ParseException {
            switch (val) {
                case "Pista":
                    return TRACK;
                case "Indoor":
                    return INDOOR;
                case "M":
                    return ROAD; // TODO: ??
                default:
                    throw new FidalApi.ParseException("Unknown type: " + val);
            }
        }
    }
}
