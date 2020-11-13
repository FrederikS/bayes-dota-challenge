package gg.bayes.challenge.parser;

import gg.bayes.challenge.parser.MatchLogParser.MatchEventParsed;
import gg.bayes.challenge.persistence.event.HeroEvent.HeroDamaged;
import gg.bayes.challenge.persistence.event.HeroEvent.HeroKilled;
import gg.bayes.challenge.persistence.event.HeroEvent.ItemPurchased;
import gg.bayes.challenge.persistence.event.HeroEvent.SpellCasted;
import gg.bayes.challenge.persistence.event.MatchEvent;
import gg.bayes.challenge.persistence.event.MatchEventRepository.HeroEventRepository.HeroDamagedEventRepository;
import gg.bayes.challenge.persistence.event.MatchEventRepository.HeroEventRepository.HeroKilledEventRepository;
import gg.bayes.challenge.persistence.event.MatchEventRepository.HeroEventRepository.ItemPurchasedEventRepository;
import gg.bayes.challenge.persistence.event.MatchEventRepository.HeroEventRepository.SpellCastedEventRepository;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

@Slf4j
@Component
@RequiredArgsConstructor
public class MatchEventListeners {

    private final List<MatchEventListener> listeners;

    @EventListener
    public void heroKilledListener(MatchEventParsed event) {
        listeners.forEach(listener -> listener.accept(event.getEvent()));
    }

    @Configuration
    static class MatchEventListenerConfig {

        @Bean
        MatchEventListener itemPurchasedEventListener(ItemPurchasedEventRepository itemPurchasedEventRepository) {
            return MatchEventListener.builder()
                                     .selector(e -> e instanceof ItemPurchased)
                                     .consumer(e -> itemPurchasedEventRepository.save((ItemPurchased) e))
                                     .build();
        }

        @Bean
        MatchEventListener heroKilledEventListener(HeroKilledEventRepository heroKilledEventRepository) {
            return MatchEventListener.builder()
                                     .selector(e -> e instanceof HeroKilled)
                                     .consumer(e -> heroKilledEventRepository.save((HeroKilled) e))
                                     .build();
        }

        @Bean
        MatchEventListener spellCastedEventListener(SpellCastedEventRepository spellCastedEventRepository) {
            return MatchEventListener.builder()
                                     .selector(e -> e instanceof SpellCasted)
                                     .consumer(e -> spellCastedEventRepository.save((SpellCasted) e))
                                     .build();
        }

        @Bean
        MatchEventListener heroDamagedEventListener(HeroDamagedEventRepository heroDamagedEventRepository) {
            return MatchEventListener.builder()
                                     .selector(e -> e instanceof HeroDamaged)
                                     .consumer(e -> heroDamagedEventRepository.save((HeroDamaged) e))
                                     .build();
        }

    }

    @Builder
    static class MatchEventListener implements Consumer<MatchEvent> {
        private final Predicate<MatchEvent> selector;
        private final Consumer<MatchEvent> consumer;

        @Override
        public void accept(MatchEvent event) {
            if (selector.test(event)) {
                consumer.accept(event);
            }
        }
    }

}
