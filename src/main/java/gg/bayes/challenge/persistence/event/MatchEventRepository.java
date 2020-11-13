package gg.bayes.challenge.persistence.event;

import gg.bayes.challenge.persistence.event.HeroEvent.HeroDamaged;
import gg.bayes.challenge.persistence.event.HeroEvent.HeroKilled;
import gg.bayes.challenge.persistence.event.HeroEvent.ItemPurchased;
import gg.bayes.challenge.persistence.event.HeroEvent.SpellCasted;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.Repository;

import java.util.List;

//TODO indices
@NoRepositoryBean
public interface MatchEventRepository<T extends MatchEvent> extends Repository<T, Long> {

    T save(T event);
    List<T> findByMatchId(Long matchId);

    @NoRepositoryBean
    interface HeroEventRepository<T extends HeroEvent> extends MatchEventRepository<T> {

        List<T> findByMatchIdAndHero(Long matchId, String hero);

        interface HeroKilledEventRepository extends HeroEventRepository<HeroKilled> {}
        interface ItemPurchasedEventRepository extends HeroEventRepository<ItemPurchased> {}
        interface HeroDamagedEventRepository extends HeroEventRepository<HeroDamaged> {}
        interface SpellCastedEventRepository extends HeroEventRepository<SpellCasted> {}

    }

}
