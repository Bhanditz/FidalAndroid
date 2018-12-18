package com.gianlu.fidal.NetIO.Models.Competitions;

import android.content.Context;

import com.gianlu.fidal.R;

import androidx.annotation.NonNull;

public final class CompetitionEptathlon extends AbsCompetitionForPoints {
    @NonNull
    @Override
    public String getText(@NonNull Context context) {
        return context.getString(R.string.competition_eptathlon);
    }
}
