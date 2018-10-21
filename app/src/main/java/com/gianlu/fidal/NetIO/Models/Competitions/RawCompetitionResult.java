package com.gianlu.fidal.NetIO.Models.Competitions;

import android.annotation.SuppressLint;

import com.gianlu.fidal.NetIO.FidalApi;

import org.jsoup.nodes.Element;

import java.text.ParseException;
import java.text.SimpleDateFormat;

public class RawCompetitionResult {
    public final long date;

    public RawCompetitionResult(Element row) throws FidalApi.ParseException {
        date = parseDate(row.child(0).text(), row.child(1).text());
        row.child(2); // Type
        row.child(3); // Chrono
        row.child(4); // Category
        row.child(5); // Ranking
        row.child(6); // Performance
        row.child(7); // Wind
        row.child(8); // Place
    }

    @SuppressLint("SimpleDateFormat")
    private static long parseDate(String year, String date) throws FidalApi.ParseException {
        try {
            return new SimpleDateFormat("dd/MM/yyyy").parse(date + "/" + year).getTime();
        } catch (ParseException ex) {
            throw new FidalApi.ParseException(ex);
        }
    }
}
