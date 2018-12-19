package com.gianlu.fidal.Animations;

import android.content.Context;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.UiThread;

@UiThread
public abstract class AbsCompetitionAnimationView extends View {

    public AbsCompetitionAnimationView(@NonNull Context context) {
        super(context, null, 0);
    }

    public abstract void startAnimation();

    public abstract void resetAnimation();
}
