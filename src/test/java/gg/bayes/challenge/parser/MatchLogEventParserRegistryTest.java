package gg.bayes.challenge.parser;

import gg.bayes.challenge.persistence.event.HeroEvent.HeroDamaged;
import gg.bayes.challenge.persistence.event.HeroEvent.HeroKilled;
import gg.bayes.challenge.persistence.event.HeroEvent.SpellCasted;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.from;
import static org.assertj.core.api.InstanceOfAssertFactories.type;

@SpringBootTest
class MatchLogEventParserRegistryTest {

    @Autowired
    private MatchLogEventParserRegistry matchLogEventParserRegistry;

    @Nested
    @DisplayName("Given a hero-killed log entry")
    class HeroKillLogEntry {

        private final String logEntry = "[00:11:20.322] npc_dota_hero_rubick is killed by npc_dota_hero_pangolier";

        @Test
        @DisplayName("then event-parser-registry should have a parser")
        void heroKilledEventPatternParserShouldExist() {
            assertThat(matchLogEventParserRegistry.findParser(logEntry)).isPresent();
        }

        @Test
        @DisplayName("then parsed event should contain correct fields")
        void heroKilledEntryShouldGetParsedCorrectly() {
            final MatchLogEventParser heroKilledParser = matchLogEventParserRegistry.findParser(logEntry)
                                                                                    .orElseThrow(AssertionError::new);

            assertThat(heroKilledParser.parse(1L, logEntry)).isInstanceOf(HeroKilled.class)
                                                            .asInstanceOf(type(HeroKilled.class))
                                                            .returns("rubick", from(HeroKilled::getHero))
                                                            .returns("pangolier", from(HeroKilled::getKilledBy));
        }
    }

    @Nested
    @DisplayName("Given a spell-casted log entry")
    class SpellCastedLogEntry {

        private final String logEntry = "[00:10:52.129] npc_dota_hero_rubick casts ability rubick_fade_bolt (lvl 1) on npc_dota_creep_goodguys_ranged";

        @Test
        @DisplayName("then event-parser-registry should have a parser")
        void spellCastedEventPatternParserShouldExist() {
            assertThat(matchLogEventParserRegistry.findParser(logEntry)).isPresent();
        }

        @Test
        @DisplayName("then parsed event should contain correct fields")
        void spellCastedEntryShouldGetParsedCorrectly() {
            final MatchLogEventParser spellCastedParser = matchLogEventParserRegistry.findParser(logEntry)
                                                                                     .orElseThrow(AssertionError::new);

            assertThat(spellCastedParser.parse(1L, logEntry)).isInstanceOf(SpellCasted.class)
                                                             .asInstanceOf(type(SpellCasted.class))
                                                             .returns("rubick", from(SpellCasted::getHero))
                                                             .returns("fade_bolt", from(SpellCasted::getSpell));
        }
    }

    @Nested
    @DisplayName("Given a hero-damaged log entry")
    class HeroDamagedLogEntry {

        private final String logEntry = "[00:10:53.595] npc_dota_hero_bane hits npc_dota_hero_puck with dota_unknown for 64 damage (464->400)";

        @Test
        @DisplayName("then event-parser-registry should have a parser")
        void heroDamagedEventPatternParserShouldExist() {
            assertThat(matchLogEventParserRegistry.findParser(logEntry)).isPresent();
        }

        @Test
        @DisplayName("then parsed event should contain correct fields")
        void heroDamagedEntryShouldGetParsedCorrectly() {
            final MatchLogEventParser spellCastedParser = matchLogEventParserRegistry.findParser(logEntry)
                                                                                     .orElseThrow(AssertionError::new);

            assertThat(spellCastedParser.parse(1L, logEntry)).isInstanceOf(HeroDamaged.class)
                                                             .asInstanceOf(type(HeroDamaged.class))
                                                             .returns("bane", from(HeroDamaged::getHero))
                                                             .returns("puck", from(HeroDamaged::getTarget))
                                                             .returns(64, from(HeroDamaged::getDamage));
        }
    }

}