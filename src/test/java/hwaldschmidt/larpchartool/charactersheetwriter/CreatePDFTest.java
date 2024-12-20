package hwaldschmidt.larpchartool.charactersheetwriter;

import hwaldschmidt.larpchartool.domain.Chara;
import hwaldschmidt.larpchartool.domain.Convention;
import hwaldschmidt.larpchartool.domain.Visit;
import hwaldschmidt.larpchartool.repositories.CharaRepository;
import hwaldschmidt.larpchartool.repositories.ConventionRepository;
import hwaldschmidt.larpchartool.repositories.VisitRepository;
import hwaldschmidt.larpchartool.services.CharaService;
import hwaldschmidt.larpchartool.services.VisitService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Heiko Waldschmidt
 */
@SpringBootTest
public class CreatePDFTest {

    private static Logger logger = LoggerFactory.getLogger(CreatePDFTest.class.getName());

    @Autowired
    private CharaRepository charaRepository;

    @Autowired
    private VisitRepository visitRepository;

    @Autowired
    private ConventionRepository conventionRepository;

    @Autowired
    private CharaService charaService;

    @Autowired
    private VisitService visitService;

    private Chara chara;

    @BeforeEach
    public void init(){
        charaRepository.deleteAll();
        conventionRepository.deleteAll();
        visitRepository.deleteAll();

        chara = new Chara();
        chara.setName("Merlin");
        assertNull(chara.getId());
        charaRepository.save(chara);
        assertNotNull(chara.getId());

        Optional<Chara> optionalFetchedChara = charaRepository.findById(chara.getId());
        assertThat(optionalFetchedChara).isNotEmpty();
        Chara fetchedChara = optionalFetchedChara.get();

        assertEquals(chara.getId(), fetchedChara.getId());
        assertEquals(chara.getName(), fetchedChara.getName());

        //verify count of charas in DB
        long charaCount = charaRepository.count();
        assertEquals(1, charaCount);

        Convention convention = new Convention();
        convention.setTitle("Convention 1");
        convention.setDf(false);
        convention.setStart(LocalDate.of(2000, 1, 1));
        convention.setEnding(LocalDate.of(2000, 12, 31));
        assertNull(convention.getId()); //null before save
        conventionRepository.save(convention);
        assertNotNull(convention.getId()); // not null after save
        //fetch from DB
        Optional<Convention> optionalFetchedConvention = conventionRepository.findById(convention.getId());
        assertThat(optionalFetchedConvention).isNotEmpty();
        Convention fetchedConvention = optionalFetchedConvention.get();

        //should equal
        assertEquals(convention.getId(), fetchedConvention.getId());
        assertEquals(convention.getTitle(), fetchedConvention.getTitle());

        //verify count of conventions in DB
        long conventionCount = conventionRepository.count();
        assertEquals(1, conventionCount);

        Visit visit = new Visit();
        visit.setChara(chara);
        visit.setConvention(convention);
        visit.setCondays(Short.parseShort("3"));
        assertNull(visit.getId());
        visitRepository.save(visit);
        assertNotNull(visit.getId());

        Optional<Visit> optionalFetchedVisit = visitRepository.findById(visit.getId());
        assertThat(optionalFetchedVisit).isNotEmpty();
        Visit fetchedVisit = optionalFetchedVisit.get();

        assertEquals(visit.getId(), fetchedVisit.getId());
        assertEquals(visit.getConvention(), fetchedVisit.getConvention());
        assertEquals(visit.getChara(), fetchedVisit.getChara());
        assertEquals(visit.getCondays(), fetchedVisit.getCondays());

        assertEquals(1, visitRepository.count());

        for (int i = 2; i <= 100; ++i){
            addConventionWithVisit(
                    chara,
                    "Convention " + i,
                    Short.parseShort("2"),
                    LocalDate.of(2000 + i, 2, 2),
                    LocalDate.of(2000 + i, 11, 30),
                    false
            );
        }

        assertEquals(100, visitRepository.count());
    }

    private void addConventionWithVisit(Chara chara, String conName, Short condays, LocalDate start, LocalDate end,
                                        boolean df){
        Convention convention = new Convention();
        convention.setTitle(conName);
        convention.setDf(df);
        convention.setStart(start);
        convention.setEnding(end);
        assertNull(convention.getId()); //null before save
        conventionRepository.save(convention);

        Visit visit = new Visit();
        visit.setChara(chara);
        visit.setConvention(convention);
        visit.setCondays(condays);
        visitRepository.save(visit);
    }

    /**
     * This is not really an automated test, it's it is a driver for a manual test. This is too
     * much work for a hobby project. This test is more a helper for a manual tests of the CharacterSheetWriter.
     * @throws IOException
     */
    @Test
    public void test() throws IOException {
        Iterable<Visit> iterable = visitRepository.findAll();
        for (Visit visit: iterable){
            logger.trace(visit.toString());
        }
        createPDF(chara);
    }

    private void createPDF(Chara chara) throws IOException {
        List<Visit> visitList = visitService.findByCharaOrderByConventionStartAsc(chara);
        int condays = visitService.sumCondaysByChara(chara);
        CharacterSheetWriter sheetWriter = new PdfCharacterSheetWriter();
        String filename = sheetWriter.createCharacterSheet(chara, visitList, condays);
        logger.info("created file with name: {}", filename);
    }
}
