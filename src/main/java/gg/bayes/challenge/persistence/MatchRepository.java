package gg.bayes.challenge.persistence;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

import java.util.List;

public interface MatchRepository extends Repository<Match, Long> {

    Match save(Match match);

    @Query("select killedBy as hero, count(id) as kills from HeroKilled where matchId=?1 group by killedBy")
    List<KillStats> findKillStatsByMatchId(Long matchId);

    @Query("select hero as hero, spell as spell, count(id) as casts from SpellCasted where matchId=?1 and hero=?2 group by spell")
    List<SpellStats> findSpellStatsByMatchIdAndHero(Long matchId, String hero);

    @Query("select target as target, sum(damage) as damage, count(id) as instances from HeroDamaged where matchId=?1 and hero=?2 group by target")
    List<DamageStats> findDamageStatsByMatchIdAndHero(Long matchId, String hero);

    interface KillStats {
        String getHero();
        Integer getKills();
    }

    interface SpellStats {
        String getSpell();
        Integer getCasts();
    }

    interface DamageStats {
        String getTarget();
        Integer getDamage();
        Integer getInstances();
    }

}
