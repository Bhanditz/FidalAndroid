package com.gianlu.fidal.NetIO.Models.Competitions;

public final class CompetitionJavelinThrow extends AbsCompetition {
    public final Weight weight;

    CompetitionJavelinThrow(Weight weight) {
        this.weight = weight;
    }

    public enum Weight {
        GR500, GR400, GR600
    }
}
