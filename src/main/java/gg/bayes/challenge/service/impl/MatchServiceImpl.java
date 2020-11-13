package gg.bayes.challenge.service.impl;

import gg.bayes.challenge.parser.MatchLogParser;
import gg.bayes.challenge.persistence.Match;
import gg.bayes.challenge.persistence.MatchRepository;
import gg.bayes.challenge.persistence.event.MatchEventRepository.HeroEventRepository.ItemPurchasedEventRepository;
import gg.bayes.challenge.rest.model.HeroDamage;
import gg.bayes.challenge.rest.model.HeroItems;
import gg.bayes.challenge.rest.model.HeroKills;
import gg.bayes.challenge.rest.model.HeroSpells;
import gg.bayes.challenge.service.MatchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class MatchServiceImpl implements MatchService {

    private final MatchLogParser matchLogParser;
    private final MatchRepository matchRepository;
    private final ItemPurchasedEventRepository itemPurchasedEventRepository;

    @Override
    public Long ingestMatch(String payload) {
        final Long matchId = matchRepository.save(new Match()).getId();
        matchLogParser.parse(matchId, payload);
        return matchId;
    }

    @Override
    public List<HeroKills> stats(Long matchId) {
        return matchRepository.findKillStatsByMatchId(matchId)
                              .stream()
                              .map(s -> {
                                  final HeroKills heroKills = new HeroKills();
                                  heroKills.setHero(s.getHero());
                                  heroKills.setKills(s.getKills());
                                  return heroKills;
                              }).collect(Collectors.toList());
    }

    @Override
    public List<HeroItems> items(Long matchId, String hero) {
        return itemPurchasedEventRepository.findByMatchIdAndHero(matchId, hero)
                                           .stream()
                                           .map(e -> {
                                               final HeroItems heroItems = new HeroItems();
                                               heroItems.setItem(e.getItem());
                                               heroItems.setTimestamp(e.getTimestamp());
                                               return heroItems;
                                           })
                                           .collect(Collectors.toList());
    }

    @Override
    public List<HeroSpells> spells(Long matchId, String hero) {
        return matchRepository.findSpellStatsByMatchIdAndHero(matchId, hero)
                              .stream()
                              .map(s -> {
                                  final HeroSpells heroSpells = new HeroSpells();
                                  heroSpells.setSpell(s.getSpell());
                                  heroSpells.setCasts(s.getCasts());
                                  return heroSpells;
                              }).collect(Collectors.toList());
    }

    @Override
    public List<HeroDamage> damage(Long matchId, String hero) {
        return matchRepository.findDamageStatsByMatchIdAndHero(matchId, hero)
                              .stream()
                              .map(s -> {
                                  final HeroDamage heroDamage = new HeroDamage();
                                  heroDamage.setTarget(s.getTarget());
                                  heroDamage.setTotalDamage(s.getDamage());
                                  heroDamage.setDamageInstances(s.getInstances());
                                  return heroDamage;
                              }).collect(Collectors.toList());
    }

    // TODO mapstruct

}
