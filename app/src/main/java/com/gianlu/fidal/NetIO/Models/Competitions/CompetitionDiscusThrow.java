package com.gianlu.fidal.NetIO.Models.Competitions;

import android.content.Context;

import com.gianlu.commonutils.GetText;
import com.gianlu.fidal.R;

import androidx.annotation.NonNull;

public final class CompetitionDiscusThrow extends AbsCompetitionForHorizontalDistance {
    public final Weight weight;

    CompetitionDiscusThrow(@NonNull Weight weight) {
        this.weight = weight;
    }

    @NonNull
    @Override
    public String getText(@NonNull Context context) {
        return context.getString(R.string.competition_discusThrow, weight.getText(context));
    }

    public enum Weight implements GetText {
        GR1000, GR1500, GR1750, GR2000;

        @NonNull
        @Override
        public String getText(@NonNull Context context) {
            switch (this) {
                case GR1000:
                    return "1,000 kg";
                case GR1500:
                    return "1,500 kg";
                case GR1750:
                    return "1,750 kg";
                case GR2000:
                    return "2,000 kg";
                default:
                    return "? kg";
            }
        }
    }
}
