package com.gianlu.fidal.NetIO.Models.Competitions;

public final class CompetitionShotPut extends AbsCompetition {
    public final Weight weight;

    CompetitionShotPut(Weight weight) {
        this.weight = weight;
    }

    public enum Weight {
        GR2000, GR3000, GR4000, GR5000, GR7260
    }
}
