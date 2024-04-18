package uap.elecciones.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import uap.elecciones.model.service.IAsignacionHabilitadoService;

@Controller
public class IndexController {
    
    @Autowired
    private IAsignacionHabilitadoService asignacionHabilitadoService;

    @RequestMapping(value = "/index",method = RequestMethod.GET)
    public String Pagina_Index(){
        return "index";
    }

   @RequestMapping(value = "/delegados",method = RequestMethod.GET)
    public String vista_delegados(Model model){

        model.addAttribute("delegados", asignacionHabilitadoService.lista_asignados_delegados());
        
        return "Publico/lista_delegados";
    }

    @RequestMapping(value = "/estudiante",method = RequestMethod.GET)
    public String vista_estudiantes(Model model, @RequestParam(name = "succes",required = false)String succes){

        if (succes != null) {
            model.addAttribute("succes", succes);
        }

        return "Publico/vista_publica_estudiantes";
    }

    @RequestMapping(value = "/mesa_asignada/{ru}",method = RequestMethod.GET)
    public String mesa_asignada_form(Model model,@PathVariable(name = "ru",required = false)String ru, RedirectAttributes flash ){

        try {
            if (ru != null) {
                
                if (asignacionHabilitadoService.asignado_habilitado(ru) != null) {
                    model.addAttribute("estudiante", asignacionHabilitadoService.asignado_habilitado(ru));
                    return "Content/content :: content";
                }else{
                    model.addAttribute("succes", "Estudiante No Matriculado");
                    return "Content/content :: alert_modal";
                }
            }
            return "redirect:/estudiante";
        }  catch (Exception e) {
            System.out.println("6");
            flash.addFlashAttribute("succes", "ERROR!");
            return "redirect:/estudiante";
        }
    }

}
