package hwaldschmidt.larpchartool.repositories;

import hwaldschmidt.larpchartool.domain.Convention;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class ConventionRepositoryTest {

    @Autowired
    private ConventionRepository conventionRepository;

    @BeforeEach
    public void setUp(){
        conventionRepository.deleteAll();
    }

    @Test
    public void testSaveConvention(){
        Convention convention = new Convention();
        convention.setTitle("Title");
        convention.setDf(false);
        convention.setStart(LocalDate.of(2000, 1, 1));
        convention.setEnding(LocalDate.of(2000, 12, 31));
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

        //get all conventions, list should only have one
        Iterable<Convention> conventions = conventionRepository.findAll();

        int count = 0;

        for(Convention c : conventions){
            count++;
        }

        assertEquals(1, count);
    }
}
