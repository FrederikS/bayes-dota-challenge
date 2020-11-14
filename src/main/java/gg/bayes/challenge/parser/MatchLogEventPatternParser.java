package gg.bayes.challenge.parser;

import gg.bayes.challenge.persistence.event.MatchEvent;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalTime;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.time.temporal.ChronoField.MILLI_OF_DAY;

public class MatchLogEventPatternParser<T extends MatchEvent> implements MatchLogEventParser<T> {

    private final Pattern pattern;
    private final PatternMapping<T> mapping;

    private Matcher matcher;

    @Builder
    public MatchLogEventPatternParser(Pattern pattern, PatternMapping<T> mapping) {
        this.pattern = pattern;
        this.mapping = mapping;
    }

    @Override
    public boolean isParsable(String logEntry) {
        matcher = pattern.matcher(logEntry);
        return matcher.matches();
    }

    @Override
    public T parse(long matchId, String logEntry) {
        final long timestamp = LocalTime.parse(matcher.group("time")).getLong(MILLI_OF_DAY);

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
