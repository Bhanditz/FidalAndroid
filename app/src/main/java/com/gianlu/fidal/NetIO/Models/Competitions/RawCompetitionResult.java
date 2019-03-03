package com.gianlu.fidal.NetIO.Models.Competitions;

import android.annotation.SuppressLint;
import android.content.Context;

import com.gianlu.commonutils.GetText;
import com.gianlu.fidal.NetIO.FidalApi;
import com.gianlu.fidal.NetIO.Models.BaseCompetitionResult;
import com.gianlu.fidal.R;
import com.gianlu.fidal.Utils;

import org.jsoup.nodes.Element;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import androidx.annotation.NonNull;

public class RawCompetitionResult implements BaseCompetitionResult {
    public final AbsCompetition competition;
    public final long date;
    public final float performance;
    public final float wind;
    public final Type type;
    public final String place;
    public final FidalApi.Category category;
    public final ChronoType chronoType;
    public final String ranking;

    public RawCompetitionResult(Element row, @NonNull AbsCompetition competition) throws FidalApi.ParseException {
        this.competition = competition;
        date = parseDate(row.child(0).text(), row.child(1).text());
        type = Type.parseType(row.child(2).text());
        chronoType = ChronoType.parse(row.child(3).text());
        category = FidalApi.Category.parseTwoLetters(row.child(4).text());
        ranking = row.child(5).text();
        performance = Utils.parsePerformance(row.child(6).text());
        String windStr = row.child(7).text();
        wind = windStr.isEmpty() ? 0f : Float.parseFloat(windStr);
        place = row.child(8).text();
    }

    @SuppressLint("SimpleDateFormat")
    private static long parseDate(String year, String date) throws FidalApi.ParseException {
        try {
            return new SimpleDateFormat("dd/MM/yyyy").parse(date + "/" + year).getTime();
        } catch (ParseException ex) {
            throw new FidalApi.ParseException(ex);
        }
    }

    @Override
    public float performance() {
        return performance;
    }

    @Override
    public float wind() {
        return wind;
    }

    public enum ChronoType {
        ELECTRIC, MANUAL, NONE;

        @NonNull
        public static ChronoType parse(@NonNull String str) throws FidalApi.ParseException {
            switch (str) {
                case "E":
                    return ELECTRIC;
                case "M":
                    return MANUAL;
                case "":
                    return NONE;
                default:
                    throw new FidalApi.ParseException("Unknown chrono type: " + str);
            }
        }
    }

    public enum Type implements GetText {
        OUTDOOR, INDOOR, ROAD;

        @NonNull
        public static Type parseTypeLong(@NonNull String val) throws FidalApi.ParseException {
            switch (val) {
                case "Pista":
                    return OUTDOOR;
                case "Indoor":
                    return INDOOR;
                case "M":
                    return ROAD;
                default:
                    throw new FidalApi.ParseException("Unknown type: " + val);
            }
        }

        @NonNull
        public static Type parseType(@NonNull String val) throws FidalApi.ParseException {
            switch (val) {
                case "P":
                    return OUTDOOR;
                case "I":
                    return INDOOR;
                case "M":
                    return ROAD;
                default:
                    throw new FidalApi.ParseException("Unknown type: " + val);
            }
        }

        @NonNull
        @Override
        public String getText(@NonNull Context context) {
            switch (this) {
                case OUTDOOR:
                    return context.getString(R.string.type_outdoor);
                case INDOOR:
                    return context.getString(R.string.type_indoor);
                case ROAD:
                    return context.getString(R.string.type_road);
                default:
                    return context.getString(R.string.unknown);
            }
        }
    }
}
