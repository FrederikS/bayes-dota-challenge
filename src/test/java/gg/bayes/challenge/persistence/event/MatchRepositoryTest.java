package gg.bayes.challenge.persistence.event;


import gg.bayes.challenge.persistence.MatchRepository;
import gg.bayes.challenge.persistence.MatchRepository.DamageStats;
import gg.bayes.challenge.persistence.MatchRepository.KillStats;
import gg.bayes.challenge.persistence.MatchRepository.SpellStats;
import gg.bayes.challenge.persistence.event.HeroEvent.HeroDamaged;
import gg.bayes.challenge.persistence.event.HeroEvent.SpellCasted;
import gg.bayes.challenge.persistence.event.MatchEventRepository.HeroEventRepository.HeroDamagedEventRepository;
import gg.bayes.challenge.persistence.event.MatchEventRepository.HeroEventRepository.HeroKilledEventRepository;
import gg.bayes.challenge.persistence.event.MatchEventRepository.HeroEventRepository.SpellCastedEventRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import java.util.List;

import static gg.bayes.challenge.persistence.event.HeroEvent.HeroKilled;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.from;
import static org.springframework.test.annotation.DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD;

@SpringBootTest
@DirtiesContext(classMode = AFTER_EACH_TEST_METHOD)
class MatchRepositoryTest {

    @Autowired
    private MatchRepository matchRepository;
    @Autowired
    private HeroKilledEventRepository heroKilledEventRepository;
    @Autowired
    private SpellCastedEventRepository spellCastedEventRepository;
    @Autowired
    private HeroDamagedEventRepository heroDamagedEventRepository;

    @Test
    void findKillsStatsByMatchIdShouldReturnAggregatedKills() {
        final long matchId = 1234L;
        heroKilledEventRepository.save(sampleHeroKilledEvent(matchId));
        heroKilledEventRepository.save(sampleHeroKilledEvent(matchId));

        final List<KillStats> heroKills = matchRepository.findKillStatsByMatchId(matchId);

        assertThat(heroKills).hasSize(1)
                             .first()
                             .returns("ember_spirit", from(KillStats::getHero))
                             .returns(2, from(KillStats::getKills));
    }

    @Test
    void findSpellStatsByMatchIdShouldReturnAggregatedCasts() {
        final long matchId = 1234L;
        spellCastedEventRepository.save(sampleSpellCastedEvent(matchId));
        spellCastedEventRepository.save(sampleSpellCastedEvent(matchId));

        final List<SpellStats> spellStats = matchRepository.findSpellStatsByMatchIdAndHero(matchId, "enigma");

        assertThat(spellStats).hasSize(1)
                              .first()
                              .returns("enigma", from(SpellStats::getHero))
                              .returns(2, from(SpellStats::getCasts));
    }

    @Test
    void findDamageStatsByMatchIdShouldReturnAggregatedDamagePerTarget() {
        final long matchId = 1234L;
        heroDamagedEventRepository.save(sampleHeroDamagedEvent(matchId));
        heroDamagedEventRepository.save(sampleHeroDamagedEvent(matchId));

        final List<DamageStats> dmgStats = matchRepository.findDamageStatsByMatchIdAndHero(matchId, "earth_spirit");

        assertThat(dmgStats).hasSize(1)
                            .first()
                            .returns("pudge", from(DamageStats::getTarget))
                            .returns(200, from(DamageStats::getDamage))
                            .returns(2, from(DamageStats::getInstances));
    }

    private static HeroKilled sampleHeroKilledEvent(long matchId) {
        return HeroKilled.builder()
                         .matchId(matchId)
                         .timestamp(System.currentTimeMillis())
                         .hero("techies")
                         .killedBy("ember_spirit")
                         .build();
    }

    private static SpellCasted sampleSpellCastedEvent(long matchId) {
        return SpellCasted.builder()
                          .matchId(matchId)
                          .timestamp(System.currentTimeMillis())
                          .hero("enigma")
                          .spell("black_hole")
                          .build();
    }

    private static HeroDamaged sampleHeroDamagedEvent(long matchId) {
        return HeroDamaged.builder()
                          .matchId(matchId)
                          .timestamp(System.currentTimeMillis())
                          .hero("earth_spirit")
                          .target("pudge")
                          .damage(100)
                          .build();
    }

}