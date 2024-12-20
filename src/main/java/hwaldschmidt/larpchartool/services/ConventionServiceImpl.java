package hwaldschmidt.larpchartool.services;

import hwaldschmidt.larpchartool.domain.Convention;
import hwaldschmidt.larpchartool.repositories.ConventionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * @author Heiko Waldschmidt
 */
@Service
public class ConventionServiceImpl implements ConventionService {

    private ConventionRepository conventionRepository;

    @Autowired
    public void setConventionRepository(ConventionRepository conventionRepository) {
        this.conventionRepository = conventionRepository;
    }

    @Override
    public Iterable<Convention> listAllConventions() {
        return conventionRepository.findAll();
    }

    @Override
    public Optional<Convention> getConventionById(Integer id) {
        return conventionRepository.findById(id);
    }

    @Override
    public Convention saveConvention(Convention convention) {
        return conventionRepository.save(convention);
    }

    @Override
    public void deleteConvention(Integer id){
        conventionRepository.deleteById(id);
    }
}
