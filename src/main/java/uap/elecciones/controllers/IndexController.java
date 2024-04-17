package uap.elecciones.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import uap.elecciones.model.service.IAsignacionHabilitadoService;

@Controller
public class IndexController {
    
    @Autowired
    private IAsignacionHabilitadoService asignacionHabilitadoService;

    @RequestMapping(value = "/index",method = RequestMethod.GET)
    public String Pagina_Index(){
        return "index";
    }

   @RequestMapping(value = "/delegados")
    public String vista_delegados(Model model){

        model.addAttribute("delegados", asignacionHabilitadoService.lista_asignados_delegados());

        return "Publico/lista_delegados";
    }
}
