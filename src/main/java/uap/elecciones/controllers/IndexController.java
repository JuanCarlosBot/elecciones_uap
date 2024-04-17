package uap.elecciones.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

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
    public String vista_estudiantes(Model model){

        return "Publico/vista_publica_estudiantes";
    }

    @RequestMapping(value = "/mesa_asignada/{ru}",method = RequestMethod.GET)
    public String mesa_asignada_form(Model model,@PathVariable(name = "ru",required = false)String ru){

        
        try {
            if (ru != null) {
                model.addAttribute("estudiante", asignacionHabilitadoService.asignado_habilitado(ru));
            }
            return "Content/content :: content";
        } catch (Exception e) {
            System.out.println("Error");
            
        }
        return "redirect:/estudiante";
    }

}
