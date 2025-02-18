package hwaldschmidt.larpchartool.controllers;

import hwaldschmidt.larpchartool.charactersheetwriter.CharacterSheetWriter;
import hwaldschmidt.larpchartool.domain.Chara;
import hwaldschmidt.larpchartool.services.CharaService;
import hwaldschmidt.larpchartool.services.VisitService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

/**
 * Controller for all Views with a name beginning with "chara".
 *
 * @author Heiko Waldschmidt
 */
@Controller
public class CharaController {

    private CharaService charaService;
    private VisitService visitService;

    private CharacterSheetWriter characterSheetWriter;

    @Autowired
    public void setCharacterSheetWriter(CharacterSheetWriter characterSheetWriter){
        this.characterSheetWriter = characterSheetWriter;
    }

    @Autowired
    public void setVisitService(VisitService visitService){
        this.visitService = visitService;
    }

    @Autowired
    public void setCharaService(CharaService charaService) {
        this.charaService = charaService;
    }

    @GetMapping(value = "/charas")
    public String listCharas(Model model){
        model.addAttribute("charas", charaService.listAllCharas());
        return "charas";
    }

    @GetMapping("chara/{id}")
    public String showChara(@PathVariable Integer id, Model model){
        Optional<Chara> optionalChara = charaService.getCharaById(id);
        if (optionalChara.isPresent()) {
            Chara chara = optionalChara.get();
            model.addAttribute("chara", chara);
            model.addAttribute("visits", visitService.findByCharaOrderByConventionStartAsc(chara));
            model.addAttribute("condays", visitService.sumCondaysByChara(chara));
            return "charashow";
        }
        return "error"; // TODO: create error handling ...
    }

    @GetMapping(value = "chara/{id}/charactersheet")
    @ResponseBody
    public FileSystemResource getCharacterSheet(@PathVariable Integer id, HttpServletResponse response){
        Optional<Chara> optionalChara = charaService.getCharaById(id);
        String filename = "";
        try {
            Chara chara = optionalChara.orElseThrow();
            filename = characterSheetWriter.createCharacterSheet(
                    chara,
                    visitService.findByCharaOrderByConventionStartAsc(chara),
                    visitService.sumCondaysByChara(chara)
            );
            response.setContentType("application/pdf");
            // let the browser download and not show the file
            response.setHeader("Content-Disposition", "attachment; filename=" + chara.getName() + ".pdf");
        } catch (Exception e){
            // TODO exception handling
            // Exception is already logged, but we need to show the error to the user in the webinterface
            e.printStackTrace();
        }
        return new FileSystemResource(filename);
    }

    @GetMapping("chara/edit/{id}")
    public String editChara(@PathVariable Integer id, Model model){
        Optional<Chara> optionalChara = charaService.getCharaById(id);
        optionalChara.ifPresent(chara -> model.addAttribute("chara", chara));
        return "charaform";
    }

    @GetMapping("chara/new")
    public String newChara(Model model){
        model.addAttribute("chara", new Chara());
        return "charaform";
    }

    @PostMapping(value = "chara")
    public String saveChara(Chara chara){
        charaService.saveChara(chara);
        return "redirect:/charas";
    }

    // TODO: do i want to do this extra work and ugly thymeleaf to get it working with a DeleteMapping?
    @GetMapping("chara/delete/{id}")
    public String deleteChara(@PathVariable Integer id){
        charaService.deleteChara(id);
        return "redirect:/charas";
    }
}
