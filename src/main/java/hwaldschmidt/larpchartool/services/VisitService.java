package hwaldschmidt.larpchartool.services;

import hwaldschmidt.larpchartool.domain.Chara;
import hwaldschmidt.larpchartool.domain.Visit;

import java.util.List;
import java.util.Optional;

/**
 * Interface for the service used to get visit data.
 *
 * @author Heiko Waldschmidt
 */
public interface VisitService {
    Iterable<Visit> listAllVisits();

    Optional<Visit> getVisitById(Integer id);

    Visit saveVisit(Visit visit);

    void deleteVisit(Integer id);

    List<Visit> findByCharaOrderByConventionStartAsc(Chara chara);

    int sumCondaysByChara(Chara chara);
}
