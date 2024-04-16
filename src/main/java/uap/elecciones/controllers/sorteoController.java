package uap.elecciones.controllers;

<<<<<<< HEAD
public class sorteoController {
    
=======
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpServletRequest;
import uap.elecciones.model.entity.Carrera;
import uap.elecciones.model.entity.Facultad;
import uap.elecciones.model.service.ICarreraService;
import uap.elecciones.model.service.IFacultadService;

@Controller
@RequestMapping(value = "/admin")
public class sorteoController {

    @Autowired
    private IFacultadService facultadService;

    @Autowired
    private ICarreraService carreraService;
    
    @RequestMapping(value = "/lista_facultades_sorteo",method = RequestMethod.GET)
    private String Lista_Facultades_Sorteo(Model model,RedirectAttributes flash, HttpServletRequest request,@RequestParam(name = "succes",required = false)String succes){
        if (request.getSession().getAttribute("usuario") != null) {
            
            if (succes != null) {
                model.addAttribute("succes", succes);
            }
            Integer cant_est_fac = 0;
            Integer total_est = 0;
            List <Facultad> facultades = new ArrayList<>();
            for (Facultad f  : facultadService.findAll()) {
                
                for (Carrera c : f.getCarreras()) {
                    cant_est_fac += c.getEstudiantes().size();
                    total_est += c.getEstudiantes().size();
                }
                f.setCantidad_est(cant_est_fac);
                facultades.add(f);
                cant_est_fac=0;
            }
            
            model.addAttribute("facultades", facultades);

            return "Sorteo/sorteo_vista";
        } else {
            return "redirect:/login";
        }
    }

    @RequestMapping(value = "/seleccion_estudiantes_sorteo/{id_facultad}",method = RequestMethod.GET)
    public String lista_carreras_por_facultad_sorteo(Model model, RedirectAttributes flash,
            @PathVariable("id_facultad") Long id_facultad, HttpServletRequest request,
            @RequestParam(name = "succes", required = false) String succes) {
        if (request.getSession().getAttribute("usuario") != null) {

            if (succes != null) {
                model.addAttribute("succes", succes);
            }

            Facultad f = facultadService.findOne(id_facultad);

            model.addAttribute("carreras", f.getCarreras());
            model.addAttribute("id_fac", id_facultad);
            return "Sorteo/lista_carrera_sorteo";
        } else {
            return "redirect:/login";
        }
    }

    @RequestMapping(value = "/seleccion_est_carrera_sorteo/{id_carrera}",method = RequestMethod.GET)
    private String Lista_Seleccion_Estudiantes_Sorteo(Model model,RedirectAttributes flash,@PathVariable("id_carrera")Long id_carrera, HttpServletRequest request,@RequestParam(name = "succes",required = false)String succes){
        if (request.getSession().getAttribute("usuario") != null) {
            
            if (succes != null) {
                model.addAttribute("succes", succes);
            }
            
            Carrera carrera = carreraService.findOne(id_carrera);
            
            model.addAttribute("carrera", carrera);
            model.addAttribute("id_fac", id_carrera);
            return "Sorteo/lista_est_sorteo";
        } else {
            return "redirect:/login";
        }
    }
>>>>>>> 850a889 (update 16-04-2024)
}
