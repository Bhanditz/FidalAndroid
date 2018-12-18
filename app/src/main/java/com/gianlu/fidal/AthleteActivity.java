package com.gianlu.fidal;

import android.os.Bundle;
import android.widget.LinearLayout;

import com.gianlu.commonutils.Dialogs.ActivityWithDialog;
import com.gianlu.fidal.Animations.HorizontalParabolicAnimationView;

public class AthleteActivity extends ActivityWithDialog {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_athlete);

        LinearLayout athlete = findViewById(R.id.athlete);
        HorizontalParabolicAnimationView anim = new HorizontalParabolicAnimationView(this);
        anim.setPadding(32, 32, 32, 32);
        athlete.addView(anim);
        anim.prepareAnimation(53, 45);
        anim.startAnimation();
    }
}
