package hwaldschmidt.larpchartool.controllers;

import hwaldschmidt.larpchartool.domain.Convention;
import hwaldschmidt.larpchartool.services.ConventionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * Controller for all views with a name beginning with "convention"
 *
 * @author Heiko Waldschmidt
 */
@Controller
public class ConventionController {

    private ConventionService conventionService;

    @Autowired
    public void setConventionService(ConventionService conventionService) {
        this.conventionService = conventionService;
    }

    @GetMapping(value = "/conventions")
    public String listConventions(Model model){
        model.addAttribute("conventions", conventionService.listAllConventions());
        return "conventions";
    }

    @GetMapping("convention/{id}")
    public String showConvention(@PathVariable Integer id, Model model){
        model.addAttribute("convention", conventionService.getConventionById(id));
        return "conventionshow";
    }

    @GetMapping("convention/edit/{id}")
    public String editConvention(@PathVariable Integer id, Model model){
        model.addAttribute("convention", conventionService.getConventionById(id));
        return "conventionform";
    }

    @GetMapping("convention/new")
    public String newConvention(Model model){
        model.addAttribute("convention", new Convention());
        return "conventionform";
    }

    @PostMapping(value = "convention")
    public String saveConvention(Convention convention){
        conventionService.saveConvention(convention);
        return "redirect:/convention/" + convention.getId();
    }

    // TODO: Check if this works because the browser typically only uses get and post ...
    @DeleteMapping("convention/delete/{id}")
    public String deleteConvention(@PathVariable Integer id){
        conventionService.deleteConvention(id);
        return "redirect:/conventions";
    }
}
