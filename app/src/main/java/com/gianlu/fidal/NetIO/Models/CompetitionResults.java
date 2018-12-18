package com.gianlu.fidal.NetIO.Models;

import com.gianlu.fidal.NetIO.FidalApi;
import com.gianlu.fidal.NetIO.Models.Competitions.AbsCompetition;
import com.gianlu.fidal.NetIO.Models.Competitions.RawCompetitionResult;

import org.jsoup.nodes.Element;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;

public class CompetitionResults extends ArrayList<RawCompetitionResult> implements Serializable {
    public final AbsCompetition competition;

    public CompetitionResults(List<Element> elements, @NonNull AbsCompetition competition) throws FidalApi.ParseException {
        super(elements.size());
        this.competition = competition;
        for (Element elm : elements)
            add(new RawCompetitionResult(elm, competition));
    }
}
