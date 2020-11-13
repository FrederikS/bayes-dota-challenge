package gg.bayes.challenge.parser;

import gg.bayes.challenge.persistence.event.MatchEvent;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalTime;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MatchLogEventPatternParser implements MatchLogEventParser {

    private final Pattern pattern;
    private final PatternMapping<? extends MatchEvent> mapping;

    private Matcher matcher;

    @Builder
    public MatchLogEventPatternParser(Pattern pattern, PatternMapping<? extends MatchEvent> mapping) {
        this.pattern = pattern;
        this.mapping = mapping;
    }

    @Override
    public boolean isParsable(String logEntry) {
        matcher = pattern.matcher(logEntry);
        return matcher.matches();
    }

    @Override
    public MatchEvent parse(long matchId, String logEntry) {
        final long timestamp = LocalTime.parse(matcher.group("time")).toNanoOfDay();

        return mapping.apply(PatternMatcher.builder()
                                           .matchId(matchId)
                                           .timestamp(timestamp)
                                           .delegate(matcher)
                                           .build());
    }

    public interface PatternMapping<T extends MatchEvent> extends Function<PatternMatcher, T> {}

    @Builder
    public static class PatternMatcher {

        @Getter private final Long matchId;
        @Getter private final Long timestamp;
        private final Matcher delegate;

        public String group(String name) {
            return delegate.group(name);
        }

    }
}
