package gg.bayes.challenge.persistence.event;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.MappedSuperclass;

@Getter
@SuperBuilder
@MappedSuperclass
@NoArgsConstructor
public abstract class HeroEvent extends MatchEvent {

    private String hero;

    @Getter
    @SuperBuilder
    @NoArgsConstructor
    @Entity(name = "ItemPurchased")
    public static class ItemPurchased extends HeroEvent {
        private String item;
    }

    @Getter
    @SuperBuilder
    @NoArgsConstructor
    @Entity(name = "SpellCasted")
    public static class SpellCasted extends HeroEvent {
        private String spell;
    }


    @Getter
    @SuperBuilder
    @NoArgsConstructor
    @Entity(name = "HeroDamaged")
    public static class HeroDamaged extends HeroEvent {
        private Integer damage;
        private String target;
    }


    @Getter
    @SuperBuilder
    @NoArgsConstructor
    @Entity(name = "HeroKilled")
    public static class HeroKilled extends HeroEvent {
        @Column(name = "killed_by")
        private String killedBy;
    }

}
