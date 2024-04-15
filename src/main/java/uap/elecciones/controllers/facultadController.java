package uap.elecciones.controllers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpServletRequest;
import uap.elecciones.model.entity.AsignacionHabilitado;
import uap.elecciones.model.entity.Carrera;
import uap.elecciones.model.entity.Estudiante;
import uap.elecciones.model.entity.Facultad;
import uap.elecciones.model.entity.VotanteHabilitado;
import uap.elecciones.model.service.ICarreraService;
import uap.elecciones.model.service.IFacultadService;
import uap.elecciones.model.service.IMesaService;

@Controller
@RequestMapping(value = "/admin")
public class facultadController {

    @Autowired
    private ICarreraService carreraService;

    @Autowired
    private IFacultadService facultadService;

    @Autowired
    private IMesaService mesaService;
    
    @RequestMapping(value = "/lista_facultades",method = RequestMethod.GET)
    private String Lista_Facultades(Model model,RedirectAttributes flash, HttpServletRequest request,@RequestParam(name = "succes",required = false)String succes){
        if (request.getSession().getAttribute("usuario") != null) {
            
            if (succes != null) {
                model.addAttribute("succes", succes);
            }
            
            int cant_est_fac = 0;

            List <Facultad> facultades = new ArrayList<>();
            for (Facultad f  : facultadService.findAll()) {
                
                for (Carrera c : f.getCarreras()) {
                    cant_est_fac += c.getEstudiantes().size();
                }
                f.setCantidad_est(cant_est_fac);
                facultades.add(f);
                cant_est_fac=0;
            }

            model.addAttribute("facultades", facultades);

            return "Facultad/lista_facultades";
        } else {
            return "redirect:/login";
        }
    }

    @RequestMapping(value = "/seleccion_estudiantes/{id_facultad}",method = RequestMethod.GET)
    private String Lista_Seleccion_Estudiantes(Model model,RedirectAttributes flash,@PathVariable("id_facultad")Long id_facultad, HttpServletRequest request,@RequestParam(name = "succes",required = false)String succes){
        if (request.getSession().getAttribute("usuario") != null) {
            
            if (succes != null) {
                model.addAttribute("succes", succes);
            }
            
            Facultad f = facultadService.findOne(id_facultad);

            List <VotanteHabilitado> vhs = new ArrayList<>();
            for (Carrera c : f.getCarreras()) {
                
                for (Estudiante e : c.getEstudiantes()) {
                   vhs.add(e.getVotante_habilitado());

                }
            }
            // Collections.sort(vhs, Comparator.comparing(VotanteHabilitado::getEstudiante.getPersona.getApellidos));
            Collections.sort(vhs, Comparator.comparing(VotanteHabilitado -> VotanteHabilitado.getEstudiante().getPersona().getApellidos()));

            // for (VotanteHabilitado vh : vhs) {
                
            //     vh.get
            // }
            model.addAttribute("habilitados_fac", vhs);
            model.addAttribute("mesas", mesaService.findAll());
            model.addAttribute("id_fac", id_facultad);
            return "Facultad/lista_selec_estudiantes";
        } else {
            return "redirect:/login";
        }
    }

    @PostMapping(value = "/asignacion_habilitado_post")
    public String asignacion_habilitado_post(Model model,RedirectAttributes flash, HttpServletRequest request,
    @RequestParam(name = "id_fac" ,required = false)Long id_fac,
    @RequestParam(name = "num_1" ,required = false)Integer num1,
    @RequestParam(name = "num_2", required = false)Integer num2,
    @RequestParam(name = "id_mesa",required = false)Long id_mesa){
        if (request.getSession().getAttribute("usuario") != null) {
            
            // Facultad f = facultadService.findOne(id_fac);
            // for (Carrera c : f.getCarreras()) {
            //     cant_est_fac += c.getEstudiantes().size();
            // }
            // //f.setCantidad_est(cant_est_fac);
            // facultades.add(f);
            // cant_est_fac=0;
            

            // Collections.sort(vhs, Comparator.comparing(VotanteHabilitado -> VotanteHabilitado.getEstudiante().getPersona().getApellidos()));
            
            AsignacionHabilitado asignacionHabilitado = new AsignacionHabilitado();
            asignacionHabilitado.setMesa(null);
            return "redirect:/admin/seleccion_estudiantes/"+id_fac;
        } else {
            return "redirect:/login";
        }
    }
}
