package com.gianlu.fidal.NetIO.Models.Competitions;

import android.support.annotation.NonNull;

import com.gianlu.fidal.NetIO.FidalApi;

public abstract class AbsCompetition {

    @NonNull
    public static AbsCompetition parse(@NonNull String title) throws FidalApi.ParseException { // TODO: Stuff is missing
        title = title.toUpperCase();
        switch (title) {
            case "50 METRI":
                return new CompetitionSprint(AbsCompetitionWithDistance.Distance.M50);
            case "60 PIANI":
                return new CompetitionSprint(AbsCompetitionWithDistance.Distance.M60);
            case "80 PIANI":
                return new CompetitionSprint(AbsCompetitionWithDistance.Distance.M80);
            case "100 METRI":
                return new CompetitionSprint(AbsCompetitionWithDistance.Distance.M100);
            case "150 METRI":
                return new CompetitionSprint(AbsCompetitionWithDistance.Distance.M150);
            case "200 METRI":
                return new CompetitionSprint(AbsCompetitionWithDistance.Distance.M200);
            case "300 PIANI":
                return new CompetitionSprint(AbsCompetitionWithDistance.Distance.M300);
            case "400 METRI":
                return new CompetitionSprint(AbsCompetitionWithDistance.Distance.M400);
            case "600 METRI":
                return new CompetitionSprint(AbsCompetitionWithDistance.Distance.M600);
            case "50 HS H76 AF":
                return new CompetitionHurdles(AbsCompetitionWithDistance.Distance.M50, CompetitionHurdles.Height.H76);
            case "60 HS- 5 HS H 60":
            case "60 HS H 60":
                return new CompetitionHurdles(AbsCompetitionWithDistance.Distance.M50, CompetitionHurdles.Height.H60);
            case "60 HS H 76-8.50":
            case "60 HS H 76-8.00 CF":
                return new CompetitionHurdles(AbsCompetitionWithDistance.Distance.M60, CompetitionHurdles.Height.H76);
            case "60 HS H 84-8.50":
            case "OSTACOLI M 60":
                return new CompetitionHurdles(AbsCompetitionWithDistance.Distance.M60, CompetitionHurdles.Height.H84);
            case "60 HS H 91-9.14":
                return new CompetitionHurdles(AbsCompetitionWithDistance.Distance.M60, CompetitionHurdles.Height.H91);
            case "60 HS H 100":
                return new CompetitionHurdles(AbsCompetitionWithDistance.Distance.M60, CompetitionHurdles.Height.H100);
            case "60 HS H 106":
                return new CompetitionHurdles(AbsCompetitionWithDistance.Distance.M60, CompetitionHurdles.Height.H106);
            case "80 HS H 76 CF":
                return new CompetitionHurdles(AbsCompetitionWithDistance.Distance.M80, CompetitionHurdles.Height.H76);
            case "100 HS H 76-8.50":
                return new CompetitionHurdles(AbsCompetitionWithDistance.Distance.M100, CompetitionHurdles.Height.H76);
            case "100 HS H 84-8.50":
                return new CompetitionHurdles(AbsCompetitionWithDistance.Distance.M100, CompetitionHurdles.Height.H84);
            case "110 HS H 106":
                return new CompetitionHurdles(AbsCompetitionWithDistance.Distance.M110, CompetitionHurdles.Height.H106);
            case "200 HS H76-18.29 M/F":
                return new CompetitionHurdles(AbsCompetitionWithDistance.Distance.M200, CompetitionHurdles.Height.H76);
            case "300 HS H 76":
                return new CompetitionHurdles(AbsCompetitionWithDistance.Distance.M300, CompetitionHurdles.Height.H76);
            case "400 HS H 84":
                return new CompetitionHurdles(AbsCompetitionWithDistance.Distance.M400, CompetitionHurdles.Height.H84);
            case "400 HS H 91":
                return new CompetitionHurdles(AbsCompetitionWithDistance.Distance.M400, CompetitionHurdles.Height.H91);
            case "SALTO IN LUNGO/LJ":
                return new CompetitionHorizontalJump(CompetitionHorizontalJump.Type.LONG_JUMP);
            case "SALTO TRIPLO/TJ":
                return new CompetitionHorizontalJump(CompetitionHorizontalJump.Type.TRIPLE_JUMP);
            case "SALTO CON L'ASTA/PV":
                return new CompetitionVerticalJump(CompetitionVerticalJump.Type.POLE_VAULT);
            case "SALTO IN ALTO/HJ":
                return new CompetitionVerticalJump(CompetitionVerticalJump.Type.HIGH_JUMP);
            case "PESO/SP KG 2.000":
                return new CompetitionShotPut(CompetitionShotPut.Weight.GR2000);
            case "PESO/SP KG 4.000":
                return new CompetitionShotPut(CompetitionShotPut.Weight.GR4000);
            case "PESO/SP KG 5.000":
                return new CompetitionShotPut(CompetitionShotPut.Weight.GR5000);
            case "PESO/SP KG 7.260":
                return new CompetitionShotPut(CompetitionShotPut.Weight.GR7260);
            case "DISCO/DT KG 1,000":
                return new CompetitionDiscusThrow(CompetitionDiscusThrow.Weight.GR1000);
            case "DISCO/DT KG 1,500":
                return new CompetitionDiscusThrow(CompetitionDiscusThrow.Weight.GR1500);
            case "DISCO/DT KG 1,750":
                return new CompetitionDiscusThrow(CompetitionDiscusThrow.Weight.GR1750);
            case "DISCO/DT KG 2,000":
                return new CompetitionDiscusThrow(CompetitionDiscusThrow.Weight.GR2000);
            case "GIAVELLOTTO/JT GR400":
                return new CompetitionJavelinThrow(CompetitionJavelinThrow.Weight.GR400);
            case "GIAVELLOTTO/JT GR500":
                return new CompetitionJavelinThrow(CompetitionJavelinThrow.Weight.GR500);
            case "GIAVELLOTTO/JT GR600":
                return new CompetitionJavelinThrow(CompetitionJavelinThrow.Weight.GR600);
            case "MARTELLO/HT KG 3.000":
                return new CompetitionHammerThrow(CompetitionHammerThrow.Weight.GR3000);
            case "VORTEX":
                return new CompetitionVortex();
            default:
                throw new FidalApi.ParseException("Unknown competition type: " + title);
        }
    }
}
