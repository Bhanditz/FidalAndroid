package com.gianlu.fidal.NetIO.Models.Competitions;

import android.content.Context;

import com.gianlu.fidal.Animations.HorizontalParabolicAnimationView;
import com.gianlu.fidal.R;

import androidx.annotation.NonNull;

public abstract class AbsCompetitionForHorizontalDistance extends AbsCompetition<HorizontalParabolicAnimationView> {
    @NonNull
    @Override
    public HorizontalParabolicAnimationView getCompetitionAnimationView(@NonNull Context context) {
        return new HorizontalParabolicAnimationView(context);
    }

    @Override
    public boolean hasWind() {
        return true;
    }

    @Override
    public final int getIcon() {
        return R.drawable.measuring_tape;
    }
}
