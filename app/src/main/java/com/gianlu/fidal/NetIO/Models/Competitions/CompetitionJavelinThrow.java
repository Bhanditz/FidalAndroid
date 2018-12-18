package com.gianlu.fidal.NetIO.Models.Competitions;

import android.content.Context;

import com.gianlu.commonutils.GetText;
import com.gianlu.fidal.R;

import androidx.annotation.NonNull;

public final class CompetitionJavelinThrow extends AbsCompetitionForHorizontalDistance {
    public final Weight weight;

    CompetitionJavelinThrow(@NonNull Weight weight) {
        this.weight = weight;
    }

    @NonNull
    @Override
    public String getText(@NonNull Context context) {
        return context.getString(R.string.competition_javelinThrow, weight.getText(context));
    }

    public enum Weight implements GetText {
        GR500, GR400, GR600;

        @NonNull
        @Override
        public String getText(@NonNull Context context) {
            switch (this) {
                case GR400:
                    return "400 g";
                case GR500:
                    return "500 g";
                case GR600:
                    return "600 g";
                default:
                    return "? g";
            }
        }
    }
}
