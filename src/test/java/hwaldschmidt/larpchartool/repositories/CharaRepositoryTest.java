package hwaldschmidt.larpchartool.repositories;

import hwaldschmidt.larpchartool.domain.Chara;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class CharaRepositoryTest {

    @Autowired
    private CharaRepository charaRepository;

    @BeforeEach
    public void setUp(){
        charaRepository.deleteAll();
    }

    @Test
    public void saveCharacterTest(){
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

        //get all charas, list should only have one
        Iterable<Chara> charas = charaRepository.findAll();

        int count = 0;

        for(Chara c : charas){
            count++;
        }

        assertEquals(1, count);
    }
}
