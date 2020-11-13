package gg.bayes.challenge.persistence.event;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import static javax.persistence.GenerationType.SEQUENCE;

@Getter
@SuperBuilder
@MappedSuperclass
@NoArgsConstructor
public abstract class MatchEvent {

    @Id
    @GeneratedValue(strategy = SEQUENCE)
    private Long id;

    private Long matchId;
    private Long timestamp;

}

