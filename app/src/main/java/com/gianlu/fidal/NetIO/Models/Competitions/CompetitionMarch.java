package com.gianlu.fidal.NetIO.Models.Competitions;

import android.content.Context;

import com.gianlu.fidal.R;

import androidx.annotation.NonNull;

public final class CompetitionMarch extends AbsCompetitionForTime {
    public final boolean road;

    CompetitionMarch(@NonNull Distance distance, boolean road) {
        super(distance);
        this.road = road;
    }

    @NonNull
    @Override
    public String getText(@NonNull Context context) {
        return context.getString(R.string.competition_march, distance.getText(context));
    }
}
