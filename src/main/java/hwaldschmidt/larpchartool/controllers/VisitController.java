package hwaldschmidt.larpchartool.controllers;

import hwaldschmidt.larpchartool.domain.Visit;
import hwaldschmidt.larpchartool.services.CharaService;
import hwaldschmidt.larpchartool.services.ConventionService;
import hwaldschmidt.larpchartool.services.VisitService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

/**
 * Controller for all Views with a name beginning with "visit".
 * @author Heiko Waldschmidt
 */
@Controller
public class VisitController {

    private static Logger logger = LoggerFactory.getLogger(VisitController.class.getName());
    private VisitService visitService;
    private CharaService charaService;
    private ConventionService conventionService;

    @Autowired
    public void setConventionService(ConventionService conventionService) {
        this.conventionService = conventionService;
    }

    @Autowired
    public void setVisitService(VisitService visitService) {
        this.visitService = visitService;
    }

    @Autowired
    public void setCharaController(CharaService charaService){
        this.charaService = charaService;
    }

    @GetMapping(value = "visits")
    public String listVisits(Model model){
        model.addAttribute("visits", visitService.listAllVisits());
        return "visits";
    }

    @GetMapping("visit/{id}")
    public String showVisit(@PathVariable Integer id, Model model){
        Optional<Visit> optionalVisit = visitService.getVisitById(id);
        optionalVisit.ifPresent(visit -> model.addAttribute("visit", visit));
        return "visitshow";
    }

    @GetMapping("visit/edit/{id}")
    public String editVisit(@PathVariable Integer id, Model model){
        Optional<Visit> optionalVisit = visitService.getVisitById(id);
        optionalVisit.ifPresent(visit -> model.addAttribute("visit", visit));
        model.addAttribute("charas", charaService.listAllCharas());
        model.addAttribute("conventions", conventionService.listAllConventions());
        return "visitform";
    }

    @GetMapping(value = "visit/new")
    public String newVisit(Model model){
        model.addAttribute("visit", new Visit());
        model.addAttribute("charas", charaService.listAllCharas());
        model.addAttribute("conventions", conventionService.listAllConventions());
        return "visitform";
    }

    @PostMapping(value = "visit/update", params = {"save"})
    public String updateVisit(Visit visit, Model model, BindingResult bindingResult){

        if (bindingResult.hasErrors()){
            logger.error("visit/update binding result has errors");
            model.addAttribute("visit", visit);
            model.addAttribute("charas", charaService.listAllCharas());
            model.addAttribute("conventions", conventionService.listAllConventions());
            return "visitform";
        }
        if (isUniqueConstraintViolated(visit)){
            logger.error("visit/update: constraint is violated");
            bindingResult.rejectValue("chara", "chara", "A character cannot visit a convention more than once.");
            bindingResult.rejectValue("convention", "convention", "A character cannot visit a convention more than once.");
            model.addAttribute("visit", visit);
            model.addAttribute("charas", charaService.listAllCharas());
            model.addAttribute("conventions", conventionService.listAllConventions());
            return "visitform";
        }
        visitService.saveVisit(visit);
        return "redirect:/visits";
    }

    // TODO check if this is working, browser may call get instead of delete
    @GetMapping("visit/delete/{id}")
    public String deleteVisit(@PathVariable Integer id){
        logger.debug("visit id to delete " + id);
        visitService.deleteVisit(id);
        return "redirect:/visits";
    }

    private boolean isUniqueConstraintViolated(Visit visit){
        Iterable<Visit> visits = visitService.listAllVisits();
        for(Visit anyVisit: visits){
            if ((visit.getId() == null || (visit.getId().intValue() != anyVisit.getId().intValue())) &&
                    visit.getChara().getId() == anyVisit.getChara().getId() &&
                    visit.getConvention().getId() == anyVisit.getConvention().getId()){
                        return true;
                    }
        }
        return false;
    }
}
