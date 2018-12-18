package com.gianlu.fidal.NetIO.Models.Competitions;

import android.content.Context;

import com.gianlu.fidal.R;

import androidx.annotation.NonNull;

public final class CompetitionHorizontalJump extends AbsCompetitionForHorizontalDistance {
    public final Type type;

    CompetitionHorizontalJump(@NonNull Type type) {
        this.type = type;
    }

    @NonNull
    @Override
    public String getText(@NonNull Context context) {
        switch (type) {
            case LONG_JUMP:
                return context.getString(R.string.competition_longJump);
            case TRIPLE_JUMP:
                return context.getString(R.string.competition_tripleJump);
            default:
                return context.getString(R.string.unknown);
        }
    }

    public enum Type {
        LONG_JUMP,
        TRIPLE_JUMP
    }
}
