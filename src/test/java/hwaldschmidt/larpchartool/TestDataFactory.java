package hwaldschmidt.larpchartool;

import hwaldschmidt.larpchartool.domain.Chara;
import hwaldschmidt.larpchartool.domain.Convention;
import hwaldschmidt.larpchartool.domain.Visit;
import org.assertj.core.util.Lists;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class TestDataFactory {

    public static final int CONQUEST_ID = 1;
    public static final String CONQUEST_TITLE = "Conquest";
    public static final LocalDate CONQUEST_START = LocalDate.of(2070, 8, 1);
    public static final LocalDate CONQUEST_ENDING = LocalDate.of(2070, 8, 5);
    public static final boolean CONQUEST_DF = false;
    public static final int CONQUEST_VERSION = 1;

    public static final int DRACHENFEST_ID = 2;
    public static final String DRACHENFEST_TITLE = "Drachenfest";
    public static final LocalDate DRACHENFEST_START = LocalDate.of(2070, 7, 20);
    public static final LocalDate DRACHENFEST_ENDING = LocalDate.of(2070, 7, 25);
    public static final boolean DRACHENFEST_DF = true;
    public static final int DRACHENFEST_VERSION = 1;

    public static final int CROSSING_ID = 3;
    public static final String CROSSING_TITLE = "Taverne in Crossing 2070";
    public static final LocalDate CROSSING_START = LocalDate.of(2070, 2, 23);
    public static final LocalDate CROSSING_END = LocalDate.of(2070, 2, 23);
    public static final boolean CROSSING_DF = false;
    public static final int CROSSING_VERSION = 1;

    public static final int MAX_ID = 1;
    public static final String MAX_NAME = "Max";
    public static final int MAX_VERSION = 1;

    public static final int MORITZ_ID = 2;
    public static final String MORITZ_NAME = "Moritz";
    public static final int MORITZ_VERSION = 1;
    public static final int VISITS_VERSION = 1;

    public static Convention createConquest() {
        Convention conquest = new Convention();
        conquest.setId(CONQUEST_ID);
        conquest.setTitle(CONQUEST_TITLE);
        conquest.setStart(CONQUEST_START);
        conquest.setEnding(CONQUEST_ENDING);
        conquest.setVersion(CONQUEST_VERSION);
        conquest.setDf(CONQUEST_DF);
        return conquest;
    };

    public static Convention createDrachenfest() {
        Convention drachenfest = new Convention();
        drachenfest.setId(DRACHENFEST_ID);
        drachenfest.setTitle(DRACHENFEST_TITLE);
        drachenfest.setStart(DRACHENFEST_START);
        drachenfest.setEnding(DRACHENFEST_ENDING);
        drachenfest.setVersion(DRACHENFEST_VERSION);
        drachenfest.setDf(DRACHENFEST_DF);
        return drachenfest;
    }

    public static Convention createCrossing() {
        Convention tavern = new Convention();
        tavern.setId(CROSSING_ID);
        tavern.setTitle(CROSSING_TITLE);
        tavern.setStart(CROSSING_START);
        tavern.setEnding(CROSSING_END);
        tavern.setVersion(CROSSING_VERSION);
        tavern.setDf(CROSSING_DF);
        return tavern;
    }

    public static Chara createMax() {
        Chara max = new Chara();
        max.setId(MAX_ID);
        max.setName(MAX_NAME);
        max.setVersion(MAX_VERSION);
        return max;
    }

    public static Chara createMoritz() {
        Chara moritz = new Chara();
        moritz.setId(MORITZ_ID);
        moritz.setName(MORITZ_NAME);
        moritz.setVersion(MORITZ_VERSION);
        return moritz;
    }

    public static Visit createCharaVisitsConvention(Chara chara, Convention convention, short daysVisiting, int visitId) {
        Visit visit = new Visit();
        visit.setChara(chara);
        visit.setConvention(convention);
        visit.setCondays(daysVisiting);
        visit.setId(visitId);
        visit.setVersion(VISITS_VERSION);
        List<Visit> visits = chara.getVisits();
        visits.add(visit);
        convention.getVisits().add(visit);
        return visit;
    }
}
