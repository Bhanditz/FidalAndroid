package com.gianlu.fidal;

import android.os.Bundle;
import android.os.Handler;

import com.gianlu.commonutils.Dialogs.ActivityWithDialog;
import com.gianlu.fidal.Animations.ParabolicAnimationView;

public class AthleteActivity extends ActivityWithDialog {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_athlete);

        ParabolicAnimationView anim = findViewById(R.id.athlete_anim);
        anim.setPadding(32, 32, 32, 32);
        new Handler().postDelayed(() -> anim.startAnimation(53, 45), 2000);
    }
}
