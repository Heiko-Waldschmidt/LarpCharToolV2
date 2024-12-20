package hwaldschmidt.larpchartool.repositories;

import hwaldschmidt.larpchartool.domain.Chara;
import hwaldschmidt.larpchartool.domain.Convention;
import hwaldschmidt.larpchartool.domain.Visit;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class VisitRepositoryTest {

    @Autowired
    private VisitRepository visitRepository;

    @Autowired
    private ConventionRepository conventionRepository;

    @Autowired
    private CharaRepository charaRepository;

    @BeforeEach
    public void setUp(){
        charaRepository.deleteAll();
        conventionRepository.deleteAll();
        visitRepository.deleteAll();
    }

    @Test
    public void saveVisitTest() {
        Chara chara = new Chara();
        chara.setName("TestChara");
        assertNull(chara.getId());
        charaRepository.save(chara);
        assertNotNull(chara.getId());

        Chara fetchedChara = charaRepository.findById(chara.getId()).orElseThrow();
        assertEquals(chara.getId(), fetchedChara.getId());
        assertEquals(chara.getName(), fetchedChara.getName());

        fetchedChara.setName("New TestChara");
        charaRepository.save(fetchedChara);

        //get from DB, should be updated
        Chara fetchedUpdatedChara = charaRepository.findById(fetchedChara.getId()).orElseThrow();
        assertEquals(fetchedChara.getName(), fetchedUpdatedChara.getName());

        //verify count of charas in DB
        long charaCount = charaRepository.count();
        assertEquals(1, charaCount);

        Convention convention = new Convention();
        convention.setTitle("Title");
        convention.setDf(false);
        convention.setStart(LocalDate.of(2000, 1, 1));
        convention.setFinish(LocalDate.of(2000, 12, 31));
        assertNull(convention.getId()); //null before save
        conventionRepository.save(convention);
        assertNotNull(convention.getId()); // not null after save
        //fetch from DB
        Convention fetchedConvention = conventionRepository.findById(convention.getId()).orElseThrow();

        //should equal
        assertEquals(convention.getId(), fetchedConvention.getId());
        assertEquals(convention.getTitle(), fetchedConvention.getTitle());

        //update description and save
        fetchedConvention.setTitle("New Title");
        conventionRepository.save(fetchedConvention);

        //get from DB, should be updated
        Convention fetchedUpdatedConvention = conventionRepository.findById(fetchedConvention.getId()).orElseThrow();
        assertEquals(fetchedConvention.getTitle(), fetchedUpdatedConvention.getTitle());

        //verify count of conventions in DB
        long conventionCount = conventionRepository.count();
        assertEquals(1, conventionCount);

        Visit visit = new Visit();
        visit.setChara(fetchedUpdatedChara);
        visit.setConvention(fetchedUpdatedConvention);
        visit.setCondays(Short.parseShort("3"));
        assertNull(visit.getId());
        visitRepository.save(visit);
        assertNotNull(visit.getId());

        Visit fetchedVisit = visitRepository.findById(visit.getId()).orElseThrow();
        assertEquals(visit.getId(), fetchedVisit.getId());
        assertEquals(visit.getConvention(), fetchedVisit.getConvention());
        assertEquals(visit.getChara(), fetchedVisit.getChara());
        assertEquals(visit.getCondays(), fetchedVisit.getCondays());

        assertEquals(1, visitRepository.count());
    }
}
