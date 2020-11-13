package gg.bayes.challenge.rest.controller;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.io.InputStreamReader;

import static org.springframework.test.annotation.DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.util.FileCopyUtils.copyToByteArray;
import static org.springframework.util.FileCopyUtils.copyToString;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = AFTER_EACH_TEST_METHOD)
class MatchControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Value("classpath:combatlog_1.txt")
    private Resource combatLog;

    private String matchId;

    @BeforeEach
    void setUp() throws Exception {
        matchId = mockMvc.perform(MockMvcRequestBuilders.post("/api/match")
                                                        .contentType(MediaType.TEXT_PLAIN)
                                                        .content(copyToByteArray(combatLog.getInputStream())))
                         .andExpect(status().isOk())
                         .andReturn()
                         .getResponse()
                         .getContentAsString();
    }

    @Test
    void GetMatchStats(@Value("classpath:fixtures/get_match_stats.json") Resource expectedJson) throws Exception {
        mockMvc.perform(get("/api/match/{id}", matchId))
               .andExpect(status().isOk())
               .andExpect(content().contentType(MediaType.APPLICATION_JSON))
               .andExpect(content().json(copyToString(new InputStreamReader(expectedJson.getInputStream()))));
    }

    @Test
    void GetHeroItemsMatchStats(@Value("classpath:fixtures/get_match_hero_items.json") Resource expectedJson) throws Exception {
        mockMvc.perform(get("/api/match/{id}/{hero}/items", matchId, "snapfire"))
               .andExpect(status().isOk())
               .andExpect(content().contentType(MediaType.APPLICATION_JSON))
               .andExpect(content().json(copyToString(new InputStreamReader(expectedJson.getInputStream()))));
    }

    @Test
    void GetHeroSpellsMatchStats(@Value("classpath:fixtures/get_match_hero_spells.json") Resource expectedJson) throws Exception {
        mockMvc.perform(get("/api/match/{id}/{hero}/spells", matchId, "snapfire"))
               .andExpect(status().isOk())
               .andExpect(content().contentType(MediaType.APPLICATION_JSON))
               .andExpect(content().json(copyToString(new InputStreamReader(expectedJson.getInputStream()))));
    }

    @Test
    void GetHeroDamageMatchStats(@Value("classpath:fixtures/get_match_hero_damage.json") Resource expectedJson) throws Exception {
        mockMvc.perform(get("/api/match/{id}/{hero}/damage", matchId, "snapfire"))
               .andExpect(status().isOk())
               .andExpect(content().contentType(MediaType.APPLICATION_JSON))
               .andExpect(content().json(copyToString(new InputStreamReader(expectedJson.getInputStream()))));
    }

}