package gg.bayes.challenge.parser;

import gg.bayes.challenge.persistence.event.MatchEvent;

interface MatchLogEventParser {

    boolean isParsable(String logEntry);
    MatchEvent parse(long matchId, String logEntry);

}
