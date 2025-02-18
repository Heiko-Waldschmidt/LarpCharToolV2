package hwaldschmidt.larpchartool.controllers;

import hwaldschmidt.larpchartool.TestDataFactory;
import hwaldschmidt.larpchartool.charactersheetwriter.CharacterSheetWriter;
import hwaldschmidt.larpchartool.configuration.SecurityConfiguration;
import hwaldschmidt.larpchartool.domain.Chara;
import hwaldschmidt.larpchartool.domain.Convention;
import hwaldschmidt.larpchartool.services.CharaService;
import hwaldschmidt.larpchartool.services.VisitService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

// load the security config
@Import(SecurityConfiguration.class)

// WebMvcTest: loads only controllers and not the complete server, class: load only this controller not all controllers
@WebMvcTest(CharaController.class)
public class CharaControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    CharaService charaService;

    @MockitoBean
    CharacterSheetWriter characterSheetWriter;

    @MockitoBean
    VisitService visitService;

    // .andExpect(view().name("redirect:todo/{id}"))
    @Test
    void newChara() throws Exception {
        mockMvc.perform(get("/chara/new")).andDo(print())
                .andExpect(view().name("charaform"))
                .andExpect(model().attribute("chara",
                        allOf(
                                hasProperty("name", nullValue()),
                                hasProperty("id", nullValue()),
                                hasProperty("version", nullValue())
                        )
                ))
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    void showChara() throws Exception {
        Chara max = TestDataFactory.createMax();
        Convention drachenFest = TestDataFactory.createDrachenfest();
        Convention conquest = TestDataFactory.createConquest();
        TestDataFactory.createCharaVisitsConvention(max, drachenFest, (short) 6, 1);
        TestDataFactory.createCharaVisitsConvention(max, conquest, (short) 5, 2);

        when(charaService.getCharaById(TestDataFactory.MAX_ID)).thenReturn(Optional.of(max));
        when(visitService.sumCondaysByChara(max)).thenReturn(11);
        when(visitService.findByCharaOrderByConventionStartAsc(max)).thenReturn(max.getVisits());

        // extreme check to see what can be done with hamcrest
        mockMvc.perform(get("/chara/" + TestDataFactory.MAX_ID)).andDo(print())
                .andExpect(view().name("charashow"))
                .andExpect(model().attribute("chara",
                        allOf(
                                hasProperty("name", is(TestDataFactory.MAX_NAME)),
                                hasProperty("id", is(TestDataFactory.MAX_ID)),
                                hasProperty("version", is(TestDataFactory.MAX_VERSION))
                        )
                ))
                .andExpect(model().attribute("visits", hasSize(2)))
                .andExpect(model().attribute("visits", containsInRelativeOrder(
                        allOf(
                                hasProperty("condays", is((short) 6)),
                                hasProperty("id", is(1)),
                                hasProperty("version", is(TestDataFactory.VISITS_VERSION)),
                                hasProperty("chara",
                                        // it would not be necessary to transport all this details to the client, maybe reduce this later
                                        allOf(
                                                hasProperty("name", is(TestDataFactory.MAX_NAME)),
                                                hasProperty("id", is(TestDataFactory.MAX_ID)),
                                                hasProperty("version", is(TestDataFactory.MAX_VERSION))
                                        )
                                ),
                                hasProperty("convention",
                                        allOf(
                                                hasProperty("title", is(TestDataFactory.DRACHENFEST_TITLE)),
                                                hasProperty("id", is(TestDataFactory.DRACHENFEST_ID)),
                                                hasProperty("version", is(TestDataFactory.DRACHENFEST_VERSION)),
                                                hasProperty("df", is(TestDataFactory.DRACHENFEST_DF)),
                                                hasProperty("start", is(TestDataFactory.DRACHENFEST_START)),
                                                hasProperty("ending", is(TestDataFactory.DRACHENFEST_ENDING)),
                                                hasProperty("visits", anything()) // unused
                                        )
                                )
                        ),
                        allOf(
                                hasProperty("condays", is((short) 5)),
                                hasProperty("id", is(2)),
                                hasProperty("version", is(TestDataFactory.VISITS_VERSION)),
                                hasProperty("chara",
                                        allOf(
                                                hasProperty("name", is(TestDataFactory.MAX_NAME)),
                                                hasProperty("id", is(TestDataFactory.MAX_ID)),
                                                hasProperty("version", is(TestDataFactory.MAX_VERSION))
                                        )
                                ),
                                hasProperty("convention", allOf(
                                        hasProperty("title", is(TestDataFactory.CONQUEST_TITLE)),
                                        hasProperty("id", is(TestDataFactory.CONQUEST_ID)),
                                        hasProperty("version", is(TestDataFactory.CONQUEST_VERSION)),
                                        hasProperty("df", is(TestDataFactory.CONQUEST_DF)),
                                        hasProperty("start", is(TestDataFactory.CONQUEST_START)),
                                        hasProperty("ending", is(TestDataFactory.CONQUEST_ENDING)),
                                        hasProperty("visits", anything()) // unused
                                ))
                        )
                )))
                .andExpect(model().attribute("condays", is(5 + 6)
                ))
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    void saveChara(){}

    @Test
    void editChara(){}

    @Test
    void deleteChara(){}
}
