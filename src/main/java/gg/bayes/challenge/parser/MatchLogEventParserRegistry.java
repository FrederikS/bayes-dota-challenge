package gg.bayes.challenge.parser;

import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Component
public class MatchLogEventParserRegistry {

    private final Set<MatchLogEventParser> parser = new HashSet<>();

    public void register(MatchLogEventParser matchLogEventParser) {
        parser.add(matchLogEventParser);
    }

    public Optional<MatchLogEventParser> findParser(String logEntry) {
        return parser.stream()
                     .filter(parser -> parser.isParsable(logEntry))
                     .findFirst();
    }

}
