package com.gianlu.fidal.NetIO.Models;

import com.gianlu.fidal.NetIO.FidalApi;
import com.gianlu.fidal.NetIO.Models.Competitions.AbsCompetition;
import com.gianlu.fidal.NetIO.Models.Competitions.RawCompetitionResult;

import org.jsoup.nodes.Element;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;

public class CompetitionResults extends ArrayList<RawCompetitionResult> {

    public CompetitionResults(List<Element> elements, @NonNull AbsCompetition competition) throws FidalApi.ParseException {
        super(elements.size());
        for (Element elm : elements)
            add(new RawCompetitionResult(elm, competition));
    }
}
