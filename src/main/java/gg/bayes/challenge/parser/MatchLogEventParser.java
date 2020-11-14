package gg.bayes.challenge.parser;

import gg.bayes.challenge.persistence.event.MatchEvent;

interface MatchLogEventParser<T extends MatchEvent> {

    boolean isParsable(String logEntry);
    T parse(long matchId, String logEntry);

}
