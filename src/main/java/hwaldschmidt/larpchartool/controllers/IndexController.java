package hwaldschmidt.larpchartool.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * These Controller is for the basic pages "/" and "/index"
 *
 * @author Heiko Waldschmidt
 */
@Controller
public class IndexController {

    /**
     * I don't want to make a difference between "/" and "/index" so redirect here.
     * @return
     */
    @GetMapping("/")
    String root(){
        return "redirect:/index";
    }

    @GetMapping("/index")
    String index(){
        return "index";
    }
}
