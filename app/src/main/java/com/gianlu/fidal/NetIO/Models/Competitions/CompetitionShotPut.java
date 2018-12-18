package com.gianlu.fidal.NetIO.Models.Competitions;

import androidx.annotation.NonNull;

public final class CompetitionShotPut extends AbsCompetitionForHorizontalDistance {
    public final Weight weight;

    CompetitionShotPut(@NonNull Weight weight) {
        this.weight = weight;
    }

    public enum Weight {
        GR2000, GR3000, GR4000, GR5000, GR7260
    }
}
