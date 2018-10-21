package com.gianlu.fidal.NetIO.Models.Competitions;

public final class CompetitionDiscusThrow extends AbsCompetition {
    public final Weight weight;

    CompetitionDiscusThrow(Weight weight) {
        this.weight = weight;
    }

    public enum Weight {
        GR1000, GR1500, GR1750, GR2000,
    }
}
