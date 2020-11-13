package gg.bayes.challenge.service;

import gg.bayes.challenge.rest.model.HeroDamage;
import gg.bayes.challenge.rest.model.HeroItems;
import gg.bayes.challenge.rest.model.HeroKills;
import gg.bayes.challenge.rest.model.HeroSpells;

import java.util.List;

public interface MatchService {
    Long ingestMatch(String payload);
    List<HeroKills> stats(Long matchId);
    List<HeroItems> items(Long matchId, String hero);
    List<HeroSpells> spells(Long matchId, String hero);
    List<HeroDamage> damage(Long matchId, String hero);
}
