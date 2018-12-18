package com.gianlu.fidal.NetIO.Models.Competitions;

import android.content.Context;

import com.gianlu.commonutils.GetText;
import com.gianlu.fidal.Animations.TrackTimeAnimationView;

import androidx.annotation.NonNull;

public abstract class AbsCompetitionForTime extends AbsCompetition<TrackTimeAnimationView> {
    public final Distance distance;

    AbsCompetitionForTime(@NonNull Distance distance) {
        this.distance = distance;
    }

    @NonNull
    @Override
    public TrackTimeAnimationView getCompetitionAnimationView(@NonNull Context context) {
        return new TrackTimeAnimationView(context);
    }

    public enum Distance implements GetText {
        M50, M60, M80, M100, M110, M150, M200, M300, M600, M400,
        M800, M1000, M1500, M2000, M3000, KM2, KM3, KM4, KM5, KM10;

        @NonNull
        @Override
        public String getText(@NonNull Context context) {
            return name(); // FIXME
        }
    }
}
