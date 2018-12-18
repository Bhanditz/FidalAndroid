package com.gianlu.fidal.NetIO.Models.Competitions;

import androidx.annotation.NonNull;

public final class CompetitionVerticalJump extends AbsCompetitionForVertialHeight {
    public final Type type;

    CompetitionVerticalJump(@NonNull Type type) {
        this.type = type;
    }

    public enum Type {
        POLE_VAULT,
        HIGH_JUMP
    }
}
