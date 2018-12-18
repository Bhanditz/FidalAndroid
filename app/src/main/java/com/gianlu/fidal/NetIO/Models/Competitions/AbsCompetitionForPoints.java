package com.gianlu.fidal.NetIO.Models.Competitions;

import android.content.Context;

import com.gianlu.fidal.Animations.PointsAnimationView;

import androidx.annotation.NonNull;

public abstract class AbsCompetitionForPoints extends AbsCompetition<PointsAnimationView> {
    @NonNull
    @Override
    public PointsAnimationView getCompetitionAnimationView(@NonNull Context context) {
        return new PointsAnimationView(context);
    }
}
