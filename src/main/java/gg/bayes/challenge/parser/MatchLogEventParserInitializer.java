package gg.bayes.challenge.parser;

import gg.bayes.challenge.persistence.event.MatchEvent;
import gg.bayes.challenge.parser.MatchLogEventPatternParser.PatternMapping;
import gg.bayes.challenge.parser.ParserModule.ParserConfig;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.ResolvableType;
import org.springframework.stereotype.Component;

import java.util.Optional;

import static org.springframework.core.ResolvableType.forClassWithGenerics;

@Component
@RequiredArgsConstructor
public class MatchLogEventParserInitializer implements ApplicationContextAware, InitializingBean {

    private final ParserConfig config;
    private final MatchLogEventParserRegistry matchLogEventParserRegistry;

    @Setter
    private ApplicationContext applicationContext;

    @Override
    public void afterPropertiesSet() {
        config.getEventPatterns()
              .keySet()
              .stream()
              .map(this::buildEventPatternParserFor)
              .forEach(matchLogEventParserRegistry::register);
    }

    private <T extends MatchEvent> MatchLogEventPatternParser<T> buildEventPatternParserFor(Class<T> type) {
        return MatchLogEventPatternParser.<T>builder()
                                         .pattern(config.getEventPatterns().get(type))
                                         .mapping(resolveMapping(applicationContext, type))
                                         .build();
    }

    private static <T extends MatchEvent> PatternMapping<T> resolveMapping(ApplicationContext ctx, Class<T> type) {
        final ResolvableType mappingType = forClassWithGenerics(PatternMapping.class, type);

        return Optional.ofNullable(ctx.<PatternMapping<T>>getBeanProvider(mappingType).getIfAvailable())
                       .orElseThrow(() -> new IllegalStateException("Please provide a PatternMapping bean for type " + type));
    }

}
