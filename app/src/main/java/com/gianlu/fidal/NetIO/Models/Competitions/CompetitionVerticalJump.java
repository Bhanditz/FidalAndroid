package com.gianlu.fidal.NetIO.Models.Competitions;

public final class CompetitionVerticalJump extends AbsCompetition {
    public final Type type;

    CompetitionVerticalJump(Type type) {
        this.type = type;
    }

    public enum Type {
        POLE_VAULT,
        HIGH_JUMP
    }
}
