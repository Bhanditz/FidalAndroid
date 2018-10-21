package com.gianlu.fidal.NetIO.Models.Competitions;

public final class CompetitionHurdles extends AbsCompetitionWithDistance {
    public final Height height;

    CompetitionHurdles(Distance distance, Height height) {
        super(distance);
        this.height = height;
    }

    public enum Height {
        H60, H76, H84, H91, H100, H106
    }
}
