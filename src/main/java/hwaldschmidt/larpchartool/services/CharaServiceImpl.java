package hwaldschmidt.larpchartool.services;

import hwaldschmidt.larpchartool.domain.Chara;
import hwaldschmidt.larpchartool.repositories.CharaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * @author Heiko Waldschmidt
 */
@Service
public class CharaServiceImpl implements CharaService {

    private CharaRepository charaRepository;

    @Autowired
    public void setCharaRepository(CharaRepository charaRepository) {
        this.charaRepository = charaRepository;
    }

    @Override
    public Iterable<Chara> listAllCharas() {
        return charaRepository.findAll();
    }

    @Override
    public Optional<Chara> getCharaById(Integer id) {
        return charaRepository.findById(id);
    }

    @Override
    public Chara saveChara(Chara chara) {
        return charaRepository.save(chara);
    }

    @Override
    public void deleteChara(Integer id) {
        charaRepository.deleteById(id);
    }
}
