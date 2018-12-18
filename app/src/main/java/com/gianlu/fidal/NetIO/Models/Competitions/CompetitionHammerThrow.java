package com.gianlu.fidal.NetIO.Models.Competitions;

import androidx.annotation.NonNull;

public final class CompetitionHammerThrow extends AbsCompetitionForHorizontalDistance {
    public final Weight weight;

    CompetitionHammerThrow(@NonNull Weight weight) {
        this.weight = weight;
    }

    public enum Weight {
        GR3000, GR4000, GR5000, GR7260
    }
}
