package com.gianlu.fidal.NetIO.Models.Competitions;

import android.content.Context;

import com.gianlu.commonutils.GetText;
import com.gianlu.fidal.R;

import androidx.annotation.NonNull;

public final class CompetitionShotPut extends AbsCompetitionForHorizontalDistance {
    public final Weight weight;

    CompetitionShotPut(@NonNull Weight weight) {
        this.weight = weight;
    }

    @NonNull
    @Override
    public String getText(@NonNull Context context) {
        return context.getString(R.string.competition_shotPut, weight.getText(context));
    }

    public enum Weight implements GetText {
        GR2000, GR3000, GR4000, GR5000, GR7260;

        @NonNull
        @Override
        public String getText(@NonNull Context context) {
            switch (this) {
                case GR2000:
                    return "2,000 kg";
                case GR3000:
                    return "3,000 kg";
                case GR4000:
                    return "4,000 kg";
                case GR5000:
                    return "5,000 kg";
                case GR7260:
                    return "7,260 kg";
                default:
                    return "? kg";
            }
        }
    }
}
