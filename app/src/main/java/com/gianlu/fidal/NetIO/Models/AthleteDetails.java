package com.gianlu.fidal.NetIO.Models;

import android.annotation.SuppressLint;

import com.gianlu.fidal.NetIO.FidalApi;
import com.gianlu.fidal.NetIO.Models.Competitions.AbsCompetition;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;

public class AthleteDetails {
    public final String name;
    public final PageLink club;
    public final long dateOfBirth;
    public final String membershipDetails; // TODO
    public final Map<AbsCompetition, CompetitionResults> results;
    public final List<CompetitionRecord> records;

    public AthleteDetails(Element element) throws FidalApi.ParseException {
        Element common = element.child(1).child(0);

        name = common.child(0).text();
        club = PageLink.extract(common.child(1).child(0));
        dateOfBirth = parseBirth(common.textNodes().get(3).text());
        membershipDetails = common.child(6).text();

        Element tab2 = element.selectFirst("#tab2 .tab-holder"); // Results
        results = parseResults(tab2);

        Element tab3 = element.selectFirst("#tab3 .tab-holder"); // Records
        records = parseRecords(tab3);

        Element tab6 = element.selectFirst("#tab6 .tab-holder"); // History
        // TODO: History
    }

    private static List<CompetitionRecord> parseRecords(@NonNull Element tab) {
        List<CompetitionRecord> list = new ArrayList<>();
        Element notWindy = tab.getElementsContainingOwnText("Non ventosi").first().nextElementSibling();
        parseRecordsTable(notWindy, false, list);

        Element windy = tab.getElementsContainingOwnText("Ventosi").first().nextElementSibling();
        parseRecordsTable(windy, true, list);

        return list;
    }

    private static void parseRecordsTable(Element table, boolean windy, List<CompetitionRecord> list) {
        for (Element row : table.select("table tbody tr"))
            list.add(new CompetitionRecord(row, windy));
    }

    @NonNull
    private static Map<AbsCompetition, CompetitionResults> parseResults(@NonNull Element tab) throws FidalApi.ParseException {
        Elements children = tab.children();
        Map<AbsCompetition, CompetitionResults> map = new HashMap<>(children.size() / 2);
        for (int i = 0; i < children.size() - 1; i += 2) {
            String title = children.get(i).text();
            Element table = children.get(i + 1);
            parseCompetitionResults(title, table, map);
        }

        return map;
    }

    private static void parseCompetitionResults(String title, Element table, Map<AbsCompetition, CompetitionResults> map) throws FidalApi.ParseException {
        AbsCompetition type = AbsCompetition.parse(title);
        List<Element> rows = table.select(".table-responsive tbody tr");
        map.put(type, new CompetitionResults(rows));
    }

    @SuppressLint("SimpleDateFormat")
    private static long parseBirth(String text) throws FidalApi.ParseException {
        try {
            return new SimpleDateFormat("dd-MM-yyyy").parse(text).getTime();
        } catch (ParseException ex) {
            throw new FidalApi.ParseException(ex);
        }
    }
}