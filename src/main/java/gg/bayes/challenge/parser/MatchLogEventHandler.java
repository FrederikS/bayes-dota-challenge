package gg.bayes.challenge.parser;

import gg.bayes.challenge.persistence.event.MatchEvent;
import gg.bayes.challenge.persistence.event.MatchEventRepository.HeroEventRepository.HeroDamagedEventRepository;
import gg.bayes.challenge.persistence.event.MatchEventRepository.HeroEventRepository.HeroKilledEventRepository;
import gg.bayes.challenge.persistence.event.MatchEventRepository.HeroEventRepository.ItemPurchasedEventRepository;
import gg.bayes.challenge.persistence.event.MatchEventRepository.HeroEventRepository.SpellCastedEventRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.function.Consumer;

import static gg.bayes.challenge.persistence.event.HeroEvent.HeroDamaged;
import static gg.bayes.challenge.persistence.event.HeroEvent.HeroKilled;
import static gg.bayes.challenge.persistence.event.HeroEvent.ItemPurchased;
import static gg.bayes.challenge.persistence.event.HeroEvent.SpellCasted;

interface MatchLogEventHandler extends Consumer<MatchEvent> {

    @Slf4j
    @Component
    @RequiredArgsConstructor
    class SaveToRepoMatchLogEventHandler implements MatchLogEventHandler {

        private final HeroKilledEventRepository heroKilledEventRepository;
        private final ItemPurchasedEventRepository itemPurchasedEventRepository;
        private final HeroDamagedEventRepository heroDamagedEventRepository;
        private final SpellCastedEventRepository spellCastedEventRepository;

        @Override
        public void accept(MatchEvent event) {
            if (event instanceof HeroKilled) {
                heroKilledEventRepository.save((HeroKilled) event);
            } else if (event instanceof ItemPurchased) {
                itemPurchasedEventRepository.save((ItemPurchased) event);
            } else if (event instanceof HeroDamaged) {
                heroDamagedEventRepository.save((HeroDamaged) event);
            } else if (event instanceof SpellCasted) {
                spellCastedEventRepository.save((SpellCasted) event);
            } else {
                log.warn("Received unknown MatchEvent of type {}. Ignoring...", event.getClass());
            }
        }
    }

}
