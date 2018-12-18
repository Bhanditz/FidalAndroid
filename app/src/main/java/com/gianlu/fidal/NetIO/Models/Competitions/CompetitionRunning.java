package com.gianlu.fidal.NetIO.Models.Competitions;

import android.content.Context;

import androidx.annotation.NonNull;

public final class CompetitionRunning extends AbsCompetitionForTime {

    CompetitionRunning(@NonNull Distance distance) {
        super(distance);
    }

    @NonNull
    @Override
    public String getText(@NonNull Context context) {
        return distance.getText(context);
    }
}
