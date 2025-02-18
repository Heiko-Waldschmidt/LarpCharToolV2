package hwaldschmidt.larpchartool.controllers;

import hwaldschmidt.larpchartool.domain.Convention;
import hwaldschmidt.larpchartool.services.ConventionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

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
        Optional<Convention> optionalConvention = conventionService.getConventionById(id);
        optionalConvention.ifPresent(convention -> model.addAttribute("convention", convention));
        return "conventionshow";
    }

    // TODO write tests
    // TODO show old start and ending
    @GetMapping("convention/edit/{id}")
    public String editConvention(@PathVariable Integer id, Model model){
        Optional<Convention> optionalConvention = conventionService.getConventionById(id);
        optionalConvention.ifPresent(convention -> model.addAttribute("convention", convention));
        return "conventionform";
    }

    @GetMapping("convention/new")
    public String newConvention(Model model){
        model.addAttribute("convention", new Convention());
        return "conventionform";
    }

    @PostMapping(value = "convention")
    public String saveConvention(Convention convention){
        Convention newConvention = conventionService.saveConvention(convention);
        return "redirect:/convention/" + newConvention.getId();
    }

    // it seems this is the only way to call a delete endpoint!?
//    <form action="#" th:action="@{'/books/delete/{id}'(id=${book.id})}" th:method="delete" >
//    <button type="submit" class="btn">
//        Delete
//            </button>
//    </form>
    // TODO: Check if this works because the browser typically only uses get and post ...
    @GetMapping("convention/delete/{id}")
    public String deleteConvention(@PathVariable Integer id){
        conventionService.deleteConvention(id);
        return "redirect:/conventions";
    }
}
