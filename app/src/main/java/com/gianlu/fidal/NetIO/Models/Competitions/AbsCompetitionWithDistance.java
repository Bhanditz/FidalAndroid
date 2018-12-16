package com.gianlu.fidal.NetIO.Models.Competitions;

public abstract class AbsCompetitionWithDistance extends AbsCompetition {
    public final Distance distance;

    AbsCompetitionWithDistance(Distance distance) {
        this.distance = distance;
    }

    public enum Distance {
        M50, M60, M80, M100, M110, M150, M200, M300, M600, M400,
        M800, M1000, M1500, M2000, M3000, KM2, KM3, KM4, KM5, KM10
    }
}