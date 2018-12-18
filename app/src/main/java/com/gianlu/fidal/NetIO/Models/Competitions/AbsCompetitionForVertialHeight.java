package com.gianlu.fidal.NetIO.Models.Competitions;

import android.content.Context;

import com.gianlu.fidal.Animations.VerticalParabolicAnimationView;

import androidx.annotation.NonNull;

public abstract class AbsCompetitionForVertialHeight extends AbsCompetition<VerticalParabolicAnimationView> {
    @NonNull
    @Override
    public VerticalParabolicAnimationView getCompetitionAnimationView(@NonNull Context context) {
        return new VerticalParabolicAnimationView(context);
    }
}
