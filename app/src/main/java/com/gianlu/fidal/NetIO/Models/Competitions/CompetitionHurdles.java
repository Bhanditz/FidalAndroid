package com.gianlu.fidal.NetIO.Models.Competitions;

import androidx.annotation.NonNull;

public final class CompetitionHurdles extends AbsCompetitionForTime {
    public final Height height;

    CompetitionHurdles(@NonNull Distance distance, @NonNull Height height) {
        super(distance);
        this.height = height;
    }

    public enum Height {
        H60, H76, H84, H91, H100, H106
    }
}
