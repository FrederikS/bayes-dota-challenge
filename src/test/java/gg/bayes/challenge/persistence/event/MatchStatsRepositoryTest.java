package gg.bayes.challenge.persistence.event;


import gg.bayes.challenge.persistence.MatchStatsRepository;
import gg.bayes.challenge.persistence.MatchStatsRepository.HeroKillStats;
import gg.bayes.challenge.persistence.event.MatchEventRepository.HeroEventRepository.HeroKilledEventRepository;
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
class MatchStatsRepositoryTest {

    @Autowired
    private MatchStatsRepository matchStatsRepository;
    @Autowired
    private HeroKilledEventRepository heroKilledEventRepository;

    @Test
    void findHeroKillsByIdShouldReturnAggregatedKills() {
        final long matchId = 1234L;
        heroKilledEventRepository.save(sampleHeroKilledEvent(matchId));

        final List<HeroKillStats> heroKills = matchStatsRepository.findHeroKillStatsById(matchId);

        assertThat(heroKills).hasSize(1)
                             .first()
                             .returns("ember_spirit", from(HeroKillStats::getHero))
                             .returns(1, from(HeroKillStats::getKills));
    }

    private static HeroKilled sampleHeroKilledEvent(long matchId) {
        return HeroKilled.builder()
                         .matchId(matchId)
                         .timestamp(System.currentTimeMillis())
                         .hero("techies")
                         .killedBy("ember_spirit")
                         .build();
    }

}