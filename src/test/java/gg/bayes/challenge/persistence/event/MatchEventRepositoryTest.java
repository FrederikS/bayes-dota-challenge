package gg.bayes.challenge.persistence.event;


import gg.bayes.challenge.persistence.event.HeroEvent.ItemPurchased;
import gg.bayes.challenge.persistence.event.MatchEventRepository.HeroEventRepository.HeroKilledEventRepository;
import gg.bayes.challenge.persistence.event.MatchEventRepository.HeroEventRepository.ItemPurchasedEventRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import static gg.bayes.challenge.persistence.event.HeroEvent.HeroKilled;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.annotation.DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD;

@SpringBootTest
@DirtiesContext(classMode = AFTER_EACH_TEST_METHOD)
class MatchEventRepositoryTest {

    @Autowired
    private ItemPurchasedEventRepository itemPurchasedEventRepository;
    @Autowired
    private HeroKilledEventRepository heroKilledEventRepository;

    @Test
    void itemPurchasedEventRepositorySaveShouldGenerateAnId() {
        final ItemPurchased savedEvent = itemPurchasedEventRepository.save(sampleItemPurchasedEvent(123));
        assertThat(savedEvent).extracting(MatchEvent::getId).isNotNull();
    }

    @Test
    void itemPurchasedEventRepositoryShouldOnlyReturnItemPurchasedEvents() {
        final long matchId = 1234L;
        itemPurchasedEventRepository.save(sampleItemPurchasedEvent(matchId));
        heroKilledEventRepository.save(sampleHeroKilledEvent(matchId));

        assertThat(itemPurchasedEventRepository.findByMatchId(matchId)).hasSize(1);
        assertThat(heroKilledEventRepository.findByMatchId(matchId)).hasSize(1);
    }

    private static ItemPurchased sampleItemPurchasedEvent(long matchId) {
        return ItemPurchased.builder()
                            .matchId(matchId)
                            .timestamp(System.currentTimeMillis())
                            .hero("earth_spirit")
                            .item("orb_of_venom")
                            .build();
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