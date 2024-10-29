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

    
    @RequestMapping(value = "/memorandum-estudiantes",method = RequestMethod.GET)
    public String memoE(Model model){
        model.addAttribute("delegados", asignacionHabilitadoService.lista_asignados_delegados());
        return "Publico/memo_est";
    }
    @RequestMapping(value = "/memorandum-docentes",method = RequestMethod.GET)
    public String memoD(Model model){
        model.addAttribute("delegadosDocentes", asignacionHabilitadoService.lista_asignados_delegados_docentes());
        return "Publico/memo_doc";
    }



   @RequestMapping(value = "/delegados-estudiantes",method = RequestMethod.GET)
    public String vista_delegadosE(Model model){
        model.addAttribute("delegados", asignacionHabilitadoService.lista_asignados_delegados());
        return "Publico/lista_delegados";
    }
    @RequestMapping(value = "/delegados-docentes",method = RequestMethod.GET)
    public String vista_delegadosD(Model model){
        model.addAttribute("delegadosDocentes", asignacionHabilitadoService.lista_asignados_delegados_docentes());  
        return "Publico/lista_delegados_doc";
    }


    @RequestMapping(value = "/consulta",method = RequestMethod.GET)
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
                    return "Content/content :: contentEstudiante";
                }else{
                    model.addAttribute("succes", "Estudiante No Matriculado");
                    return "Content/content :: alert_modal";
                }
            }
            return "redirect:/consulta";
        }  catch (Exception e) {
            System.out.println("6");
            flash.addFlashAttribute("succes", "ERROR!");
            return "redirect:/consulta";
        }
    }

    @RequestMapping(value = "/mesa_asignada_docente/{rd}",method = RequestMethod.GET)
    public String mesa_asignada_formDocente(Model model,@PathVariable(name = "rd",required = false)String rd, RedirectAttributes flash ){
        
        try {
            if (rd != null) {
                
                if (asignacionHabilitadoService.asignado_habilitadoDocente(rd) != null) {
                    System.out.println(rd);
                    model.addAttribute("docente", asignacionHabilitadoService.asignado_habilitadoDocente(rd));
                    return "Content/content :: contentDocente";
                }else{
                    model.addAttribute("succes", "Docente no Habilitado");
                    return "Content/content :: alert_modal";
                }
            }
            return "redirect:/consulta";
        }  catch (Exception e) {
            System.out.println("6");
            flash.addFlashAttribute("succes", "ERROR! "+e);
            return "redirect:/consulta";
        }
    }

}
