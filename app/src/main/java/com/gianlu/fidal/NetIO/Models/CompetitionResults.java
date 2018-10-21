package com.gianlu.fidal.NetIO.Models;

import com.gianlu.fidal.NetIO.FidalApi;
import com.gianlu.fidal.NetIO.Models.Competitions.RawCompetitionResult;

import org.jsoup.nodes.Element;

import java.util.ArrayList;
import java.util.List;

public class CompetitionResults extends ArrayList<RawCompetitionResult> {

    public CompetitionResults(List<Element> elements) throws FidalApi.ParseException {
        super(elements.size());
        for (Element elm : elements)
            add(new RawCompetitionResult(elm));
    }
}
