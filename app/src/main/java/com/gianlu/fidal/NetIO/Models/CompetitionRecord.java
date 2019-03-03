package com.gianlu.fidal.NetIO.Models;

import com.gianlu.fidal.NetIO.FidalApi;
import com.gianlu.fidal.NetIO.Models.Competitions.AbsCompetition;
import com.gianlu.fidal.NetIO.Models.Competitions.RawCompetitionResult;
import com.gianlu.fidal.Utils;

import org.jsoup.nodes.Element;

import java.io.Serializable;

public class CompetitionRecord implements Serializable, BaseCompetitionResult {
    public final AbsCompetition competition;
    public final RawCompetitionResult.Type type;
    public final float performance;
    public final float wind;
    public final int year;
    public final String place;
    public final boolean windy;

    public CompetitionRecord(Element element, boolean windy) throws FidalApi.ParseException {
        this.competition = AbsCompetition.parse(element.child(0).text());
        this.type = RawCompetitionResult.Type.parseTypeLong(element.child(1).text());
        this.performance = Utils.parsePerformance(element.child(2).text());
        String windStr = element.child(3).text();
        this.wind = windStr.isEmpty() ? 0f : Float.parseFloat(windStr);
        this.year = Integer.parseInt(element.child(4).text());
        this.place = element.child(5).text();
        this.windy = windy;
    }

    @Override
    public float performance() {
        return performance;
    }

    @Override
    public float wind() {
        return wind;
    }
}
