package gg.bayes.challenge.service.impl;

import gg.bayes.challenge.parser.MatchLogParser;
import gg.bayes.challenge.persistence.Match;
import gg.bayes.challenge.persistence.MatchRepository;
import gg.bayes.challenge.persistence.MatchRepository.DamageStats;
import gg.bayes.challenge.persistence.MatchRepository.KillStats;
import gg.bayes.challenge.persistence.MatchRepository.SpellStats;
import gg.bayes.challenge.persistence.event.HeroEvent.ItemPurchased;
import gg.bayes.challenge.persistence.event.MatchEventRepository.HeroEventRepository.ItemPurchasedEventRepository;
import gg.bayes.challenge.rest.model.HeroDamage;
import gg.bayes.challenge.rest.model.HeroItems;
import gg.bayes.challenge.rest.model.HeroKills;
import gg.bayes.challenge.rest.model.HeroSpells;
import gg.bayes.challenge.service.MatchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
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
    private final StatsMapper statsMapper;

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
                              .map(statsMapper::toHeroKillsDto)
                              .collect(Collectors.toList());
    }

    @Override
    public List<HeroItems> items(Long matchId, String hero) {
        return itemPurchasedEventRepository.findByMatchIdAndHero(matchId, hero)
                                           .stream()
                                           .map(statsMapper::toHeroItemsDto)
                                           .collect(Collectors.toList());
    }

    @Override
    public List<HeroSpells> spells(Long matchId, String hero) {
        return matchRepository.findSpellStatsByMatchIdAndHero(matchId, hero)
                              .stream()
                              .map(statsMapper::toHeroSpellsDto)
                              .collect(Collectors.toList());
    }

    @Override
    public List<HeroDamage> damage(Long matchId, String hero) {
        return matchRepository.findDamageStatsByMatchIdAndHero(matchId, hero)
                              .stream()
                              .map(statsMapper::toHeroDamageDto)
                              .collect(Collectors.toList());
    }

    @Mapper(componentModel = "spring")
    interface StatsMapper {
        HeroKills toHeroKillsDto(KillStats killStats);
        HeroItems toHeroItemsDto(ItemPurchased itemPurchasedEvent);
        HeroSpells toHeroSpellsDto(SpellStats spellStats);

        @Mappings({
                @Mapping(source = "damage", target = "totalDamage"),
                @Mapping(source = "instances", target = "damageInstances")
        })
        HeroDamage toHeroDamageDto(DamageStats damageStats);
    }

}
