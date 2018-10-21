package com.gianlu.fidal.NetIO.Models.Competitions;

public final class CompetitionHorizontalJump extends AbsCompetition {
    public final Type type;

    CompetitionHorizontalJump(Type type) {
        this.type = type;
    }

    public enum Type {
        LONG_JUMP,
        TRIPLE_JUMP
    }
}
