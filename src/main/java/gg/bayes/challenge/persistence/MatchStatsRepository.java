package gg.bayes.challenge.persistence;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

import java.util.List;

public interface MatchStatsRepository extends Repository<Match, Long> {

    @Query("select killedBy as hero, count(id) as kills from HeroKilled where matchId=?1 group by killedBy")
    List<HeroKillStats> findHeroKillStatsById(Long matchId);

    interface HeroKillStats {
        String getHero();
        Integer getKills();
    }

}
