package com.gianlu.fidal.NetIO.Models.Competitions;

import android.content.Context;

import com.gianlu.fidal.Animations.HorizontalParabolicAnimationView;
import com.gianlu.fidal.NetIO.Models.BaseCompetitionResult;
import com.gianlu.fidal.R;

import java.util.concurrent.ThreadLocalRandom;

import androidx.annotation.NonNull;

public abstract class AbsCompetitionForHorizontalDistance extends AbsCompetition<HorizontalParabolicAnimationView> {
    @NonNull
    @Override
    public HorizontalParabolicAnimationView getCompetitionAnimationView(@NonNull Context context, @NonNull BaseCompetitionResult result) {
        HorizontalParabolicAnimationView view = new HorizontalParabolicAnimationView(context);
        view.prepareAnimation(result.performance(), ThreadLocalRandom.current().nextInt(35, 55));
        return view;
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
