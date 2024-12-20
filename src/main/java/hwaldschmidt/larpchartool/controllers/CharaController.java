package hwaldschmidt.larpchartool.controllers;

import hwaldschmidt.larpchartool.charactersheetwriter.CharacterSheetWriter;
import hwaldschmidt.larpchartool.domain.Chara;
import hwaldschmidt.larpchartool.services.CharaService;
import hwaldschmidt.larpchartool.services.VisitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

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

    /* TODO
    @GetMapping(value = "chara/{id}/charactersheet")
    @ResponseBody
    public FileSystemResource getCharacterSheet(@PathVariable Integer id, HttpServletResponse response){
        Chara chara = charaService.getCharaById(id);
        String filename = "";
        try {
            filename = characterSheetWriter.createCharacterSheet(
                    chara,
                    visitService.findByCharaOrderByConventionStartAsc(chara),
                    visitService.sumCondaysByChara(chara)
            );
        } catch (Exception e){
            // TODO exception handling
            // Exception is already logged, but we need to show the error to the use in the webinterface
            e.printStackTrace();
        }
        response.setContentType("application/pdf");
        // let the browser download and not show the file
        response.setHeader("Content-Disposition", "attachment; filename=" + chara.getName() + ".pdf");
        return new FileSystemResource(filename);
    }
    */

    @GetMapping("chara/edit/{id}")
    public String editChara(@PathVariable Integer id, Model model){
        model.addAttribute("chara", charaService.getCharaById(id));
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

    @DeleteMapping("chara/delete/{id}")
    public String deleteChara(@PathVariable Integer id){
        charaService.deleteChara(id);
        return "redirect:/charas";
    }
}