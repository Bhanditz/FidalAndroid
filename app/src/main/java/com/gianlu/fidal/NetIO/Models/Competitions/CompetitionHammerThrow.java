package com.gianlu.fidal.NetIO.Models.Competitions;

public final class CompetitionHammerThrow extends AbsCompetition {
    public final Weight weight;

    CompetitionHammerThrow(Weight weight) {
        this.weight = weight;
    }

    public enum Weight {
        GR3000, GR4000, GR5000, GR7260
    }
}
