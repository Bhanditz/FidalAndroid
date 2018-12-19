package com.gianlu.fidal.NetIO.Models.Competitions;

import android.content.Context;

import com.gianlu.fidal.Animations.PointsAnimationView;
import com.gianlu.fidal.R;

import androidx.annotation.NonNull;

public abstract class AbsCompetitionForPoints extends AbsCompetition<PointsAnimationView> {
    @NonNull
    @Override
    public PointsAnimationView getCompetitionAnimationView(@NonNull Context context) {
        return new PointsAnimationView(context);
    }

    @Override
    public boolean hasWind() {
        return false;
    }

    @Override
    public final int getIcon() {
        return R.drawable.clipboard;
    }

    @Override
    public final boolean twoDigitsPrecision() {
        return false;
    }
}
