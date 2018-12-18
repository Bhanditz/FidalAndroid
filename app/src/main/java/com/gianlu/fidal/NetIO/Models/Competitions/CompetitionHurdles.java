package com.gianlu.fidal.NetIO.Models.Competitions;

import android.content.Context;

import com.gianlu.commonutils.GetText;
import com.gianlu.fidal.R;

import androidx.annotation.NonNull;

public final class CompetitionHurdles extends AbsCompetitionForTime {
    public final Height height;

    CompetitionHurdles(@NonNull Distance distance, @NonNull Height height) {
        super(distance);
        this.height = height;
    }

    @NonNull
    @Override
    public String getText(@NonNull Context context) {
        return context.getString(R.string.competition_hurdles, distance.getText(context), height.getText(context));
    }

    public enum Height implements GetText {
        H60, H76, H84, H91, H100, H106;

        @NonNull
        @Override
        public String getText(@NonNull Context context) {
            return name(); // FIXME
        }
    }
}
