package gg.bayes.challenge.parser;

import gg.bayes.challenge.persistence.event.MatchEvent;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Component
public class MatchLogEventParserRegistry {

    private final Set<MatchLogEventParser<? extends MatchEvent>> parser = new HashSet<>();

    public void register(MatchLogEventParser<? extends MatchEvent> matchLogEventParser) {
        parser.add(matchLogEventParser);
    }

    public Optional<MatchLogEventParser<? extends MatchEvent>> findParser(String logEntry) {
        return parser.stream()
                     .filter(parser -> parser.isParsable(logEntry))
                     .findFirst();
    }

}
