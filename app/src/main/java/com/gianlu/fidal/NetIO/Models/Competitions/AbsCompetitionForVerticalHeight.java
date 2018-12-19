package com.gianlu.fidal.NetIO.Models.Competitions;

import android.content.Context;

import com.gianlu.fidal.Animations.VerticalParabolicAnimationView;
import com.gianlu.fidal.R;

import androidx.annotation.NonNull;

public abstract class AbsCompetitionForVerticalHeight extends AbsCompetition<VerticalParabolicAnimationView> {
    @NonNull
    @Override
    public VerticalParabolicAnimationView getCompetitionAnimationView(@NonNull Context context) {
        return new VerticalParabolicAnimationView(context);
    }

    @Override
    public boolean hasWind() {
        return false;
    }

    @Override
    public int getIcon() {
        return R.drawable.ruler;
    }
}
