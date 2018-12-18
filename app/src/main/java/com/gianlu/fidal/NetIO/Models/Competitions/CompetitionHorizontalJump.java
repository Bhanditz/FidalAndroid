package com.gianlu.fidal.NetIO.Models.Competitions;

import androidx.annotation.NonNull;

public final class CompetitionHorizontalJump extends AbsCompetitionForHorizontalDistance {
    public final Type type;

    CompetitionHorizontalJump(@NonNull Type type) {
        this.type = type;
    }

    public enum Type {
        LONG_JUMP,
        TRIPLE_JUMP
    }
}
