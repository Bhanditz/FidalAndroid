package com.gianlu.fidal.NetIO.Models.Competitions;

import androidx.annotation.NonNull;

public final class CompetitionMarch extends AbsCompetitionForTime {
    public final boolean road;

    CompetitionMarch(@NonNull Distance distance, boolean road) {
        super(distance);
        this.road = road;
    }
}
