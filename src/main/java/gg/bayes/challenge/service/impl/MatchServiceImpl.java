package gg.bayes.challenge.service.impl;

import gg.bayes.challenge.parser.MatchLogParser;
import gg.bayes.challenge.service.MatchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class MatchServiceImpl implements MatchService {

    private final MatchLogParser matchLogParser;

    @Override
    public Long ingestMatch(String payload) {
        matchLogParser.parse(1L, payload);
        return 1L;
    }
}
