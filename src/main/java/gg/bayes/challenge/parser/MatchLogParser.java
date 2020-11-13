package gg.bayes.challenge.parser;

import gg.bayes.challenge.persistence.event.MatchEvent;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.stream.Stream;

@Slf4j
@Component
@RequiredArgsConstructor
public class MatchLogParser implements ApplicationEventPublisherAware {

    private final MatchLogEventParserRegistry eventParserRegistry;

    @Setter
    private ApplicationEventPublisher applicationEventPublisher;

    public void parse(Long matchId, String matchLog) {
        try (final BufferedReader bufferedReader = new BufferedReader(new StringReader(matchLog))) {
            bufferedReader.lines()
                          .flatMap(l -> parseLine(matchId, l))
                          .forEach(e -> applicationEventPublisher.publishEvent(new MatchEventParsed(e)));

        } catch (IOException e) {
            log.error("Error while trying to parse match log...", e);
        }
    }

    Stream<MatchEvent> parseLine(Long matchId, String line) {
        return eventParserRegistry.findParser(line)
                                  .map(p -> Stream.of(p.parse(matchId, line)))
                                  .orElse(Stream.empty());
    }

    @Getter
    @RequiredArgsConstructor
    static class MatchEventParsed  {
        private final MatchEvent event;
    }

}
