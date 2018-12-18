package com.gianlu.fidal.NetIO.Models.Competitions;

import androidx.annotation.NonNull;

public final class CompetitionDiscusThrow extends AbsCompetitionForHorizontalDistance {
    public final Weight weight;

    CompetitionDiscusThrow(@NonNull Weight weight) {
        this.weight = weight;
    }

    public enum Weight {
        GR1000, GR1500, GR1750, GR2000,
    }
}
