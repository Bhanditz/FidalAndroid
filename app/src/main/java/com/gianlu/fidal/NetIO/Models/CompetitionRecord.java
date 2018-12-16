package com.gianlu.fidal.NetIO.Models;

import org.jsoup.nodes.Element;

public class CompetitionRecord {
    public final String competition; // TODO
    public final String type; // TODO
    public final String performance; // TODO
    public final String wind; // TODO
    public final String year; // TODO
    public final String place; // TODO
    public final boolean windy;

    public CompetitionRecord(Element element, boolean windy) {
        this.competition = element.child(0).text();
        this.type = element.child(1).text();
        this.performance = element.child(2).text();
        this.wind = element.child(3).text();
        this.year = element.child(4).text();
        this.place = element.child(5).text();
        this.windy = windy;
    }
}
