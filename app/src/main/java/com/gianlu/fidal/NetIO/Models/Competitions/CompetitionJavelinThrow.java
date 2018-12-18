package com.gianlu.fidal.NetIO.Models.Competitions;

import androidx.annotation.NonNull;

public final class CompetitionJavelinThrow extends AbsCompetitionForHorizontalDistance {
    public final Weight weight;

    CompetitionJavelinThrow(@NonNull Weight weight) {
        this.weight = weight;
    }

    public enum Weight {
        GR500, GR400, GR600
    }
}
