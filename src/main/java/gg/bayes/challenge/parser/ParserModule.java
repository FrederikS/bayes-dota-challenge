package gg.bayes.challenge.parser;

import gg.bayes.challenge.persistence.event.HeroEvent.HeroDamaged;
import gg.bayes.challenge.persistence.event.HeroEvent.HeroKilled;
import gg.bayes.challenge.persistence.event.HeroEvent.ItemPurchased;
import gg.bayes.challenge.persistence.event.HeroEvent.SpellCasted;
import gg.bayes.challenge.persistence.event.MatchEvent;
import gg.bayes.challenge.parser.MatchLogEventPatternParser.PatternMapping;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import java.util.Map;
import java.util.regex.Pattern;

import static gg.bayes.challenge.parser.ParserModule.ParserConfig;

@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties(ParserConfig.class)
@PropertySource(value = "classpath:parser.properties")
public class ParserModule {

    @ConstructorBinding
    @RequiredArgsConstructor
    @ConfigurationProperties("parser.config")
    static class ParserConfig {
        @Getter private final Map<Class<? extends MatchEvent>, Pattern> eventPatterns;
    }

    @Bean
    public PatternMapping<HeroKilled> heroKilledPatternMapping() {
        return matcher -> HeroKilled.builder()
                                    .matchId(matcher.getMatchId())
                                    .timestamp(matcher.getTimestamp())
                                    .hero(matcher.group("hero"))
                                    .killedBy(matcher.group("killedBy"))
                                    .build();
    }

    @Bean
    public PatternMapping<ItemPurchased> itemPurchasedPatternMapping() {
        return matcher -> ItemPurchased.builder()
                                       .matchId(matcher.getMatchId())
                                       .timestamp(matcher.getTimestamp())
                                       .hero(matcher.group("hero"))
                                       .item(matcher.group("item"))
                                       .build();
    }

    @Bean
    public PatternMapping<SpellCasted> spellCastedPatternMapping() {
        return matcher -> SpellCasted.builder()
                                     .matchId(matcher.getMatchId())
                                     .timestamp(matcher.getTimestamp())
                                     .hero(matcher.group("hero"))
                                     .spell(matcher.group("spell"))
                                     .build();
    }

    @Bean
    public PatternMapping<HeroDamaged> heroDamagedPatternMapping() {
        return matcher -> HeroDamaged.builder()
                                     .matchId(matcher.getMatchId())
                                     .timestamp(matcher.getTimestamp())
                                     .hero(matcher.group("hero"))
                                     .target(matcher.group("target"))
                                     .damage(Integer.parseInt(matcher.group("damage")))
                                     .build();
    }

}
