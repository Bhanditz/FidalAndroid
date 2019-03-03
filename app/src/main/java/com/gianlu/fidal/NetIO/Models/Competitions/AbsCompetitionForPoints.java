package com.gianlu.fidal.NetIO.Models.Competitions;

import android.content.Context;

import com.gianlu.fidal.Animations.PointsAnimationView;
import com.gianlu.fidal.NetIO.Models.BaseCompetitionResult;
import com.gianlu.fidal.R;

import androidx.annotation.NonNull;

public abstract class AbsCompetitionForPoints extends AbsCompetition<PointsAnimationView> {
    @NonNull
    @Override
    public PointsAnimationView getCompetitionAnimationView(@NonNull Context context, @NonNull BaseCompetitionResult result) {
        PointsAnimationView view = new PointsAnimationView(context);
        view.prepareAnimation((int) result.performance());
        return view;
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
