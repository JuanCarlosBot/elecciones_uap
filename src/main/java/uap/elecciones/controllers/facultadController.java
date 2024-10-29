package uap.elecciones.controllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletRequest;
import uap.elecciones.model.entity.AsignacionHabilitado;
import uap.elecciones.model.entity.Carrera;
import uap.elecciones.model.entity.Estudiante;
import uap.elecciones.model.entity.Facultad;
import uap.elecciones.model.entity.Mesa;
import uap.elecciones.model.entity.VotanteHabilitado;
import uap.elecciones.model.service.IAsignacionHabilitadoService;
import uap.elecciones.model.service.ICarreraService;
import uap.elecciones.model.service.IFacultadService;
import uap.elecciones.model.service.IMesaService;
import uap.elecciones.model.service.IVotanteHabilitadoService;

@Controller
@RequestMapping(value = "/admin")
public class facultadController {

    @Autowired
    private ICarreraService carreraService;

    @Autowired
    private IFacultadService facultadService;

    @Autowired
    private IMesaService mesaService;

    @Autowired
    private IVotanteHabilitadoService votanteHabilitadoService;

    @Autowired
    private IAsignacionHabilitadoService asignacionHabilitadoService;

<<<<<<< HEAD
=======
    @Autowired
    private IAsignacionHabilitadoDao asignacionHabilitadoDao;
    @Autowired
    private IConteoTotalCarreraService conteoTotalCarreraService;

    @RequestMapping(value = "/mesa_votante_fac",method = RequestMethod.GET)
    private String mesaVotanteFac(Model model,RedirectAttributes flash, HttpServletRequest request,@RequestParam(name = "succes",required = false)String succes){
        if (request.getSession().getAttribute("usuario") != null) {
            if (succes != null) {
                model.addAttribute("succes", succes);
            }
            Carrera carrera = carreraService.findOne(1l);
            model.addAttribute("list_asignados", asignacionHabilitadoService.lista_asignados_habilitados(carrera.getId_carrera()));
            List<VotanteHabilitado> vh = new ArrayList<>();
            for (Estudiante e : carrera.getEstudiantes()) {
                if (e.getVotante_habilitado().getEstado_mesa() ==null) {
                    vh.add(e.getVotante_habilitado());
                }
            }
            System.out.println(vh.size());
            model.addAttribute("facultades", facultadService.findAll());
            //model.addAttribute("carrera", vh);
            //model.addAttribute("mesas_asig", mesaService.lista_mesas_por_carrera(carrera.getId_carrera()));
            model.addAttribute("mesas", mesaService.findAll());
            //model.addAttribute("id_fac", carrera.getId_carrera());
            return "Facultad/mesa_votante_fac";
        } else {
            return "redirect:/login";
        }
    }

    @RequestMapping(value = "/listarPorFacultadDocentes/{idFacultad}",method = RequestMethod.GET)
    public String listarPorFacultad(@PathVariable Long idFacultad, Model model) {
        System.out.println("ID Facultad recibido: " + idFacultad);
        //System.out.println("cantidad "+asignacionHabilitadoService.lista_asignados_habilitadosF(idFacultad).size());
        try {
            model.addAttribute("list_asignados", asignacionHabilitadoService.lista_asignados_habilitadosF(idFacultad));
        } catch (Exception e) {
            e.printStackTrace(); // Imprime el error en la consola
            // Puedes devolver un mensaje de error o una vista de error
        }
        return "fragment/listado :: tablaListado"; // Devuelve solo el fragmento de la tabla
    }

>>>>>>> ba09989 (cambio 29)
    @RequestMapping(value = "/lista_facultades",method = RequestMethod.GET)
    private String Lista_Facultades(Model model,RedirectAttributes flash, HttpServletRequest request,@RequestParam(name = "succes",required = false)String succes){
        if (request.getSession().getAttribute("usuario") != null) {
            /*for (Carrera carrera2 : carreraService.findAll()) {
                    
                ConteoTotalCarrera conteoTotalCarrera2 = new ConteoTotalCarrera();  
                    conteoTotalCarrera2.setCarrera("FULL" + "-" + carrera2.getNombre_carrera());
                    conteoTotalCarreraService.save(conteoTotalCarrera2);      
                }*/
            if (succes != null) {
                model.addAttribute("succes", succes);
            }
            int cant_est_fac = 0;
            Integer total_est = 0;

            int cant_doc_fac = 0;
            Integer total_doc = 0;
            List <Facultad> facultades = new ArrayList<>();
            
            for (Facultad f  : facultadService.findAll()) {
                
                for (Carrera c : f.getCarreras()) {
                    cant_est_fac += c.getEstudiantes().size();
                    total_est += c.getEstudiantes().size();
                    cant_doc_fac += c.getDocentes().size();
                    total_doc += c.getDocentes().size();
                }
                f.setCantidad_est(cant_est_fac);
                f.setCantidad_doc(cant_doc_fac);
                facultades.add(f);
                cant_est_fac=0;
                cant_doc_fac=0;

            }

            model.addAttribute("facultades", facultades);
            model.addAttribute("total_est", total_est);
            model.addAttribute("total_doc", total_doc);

            return "Facultad/lista_facultades";
        } else {
            return "redirect:/login";
        }
    }

    @RequestMapping(value = "/seleccion_estudiantes/{id_facultad}",method = RequestMethod.GET)
    public String lista_carreras_por_facultad(Model model, RedirectAttributes flash,
            @PathVariable("id_facultad") Long id_facultad, HttpServletRequest request,
            @RequestParam(name = "succes", required = false) String succes) {
        if (request.getSession().getAttribute("usuario") != null) {

            if (succes != null) {
                model.addAttribute("succes", succes);
            }

            Facultad f = facultadService.findOne(id_facultad);

            for (Carrera c : f.getCarreras()) {
                System.out.println("catidad e estudiantes "+c.getEstudiantes().size());
            }
            model.addAttribute("carreras", f.getCarreras());

            for (Carrera carrera : f.getCarreras()) {
                System.out.println(carrera.getNombre_carrera());

            }
            model.addAttribute("mesas_asig_doc", mesaService.lista_mesas_por_facultad_docente(id_facultad));
            model.addAttribute("id_fac", id_facultad);
            return "Carrera/lista_carreras";
        } else {
            return "redirect:/login";
        }
    }
    
    @RequestMapping(value = "/seleccion_est_carrera/{id_carrera}",method = RequestMethod.GET)
    private String Lista_Seleccion_Estudiantes(Model model,RedirectAttributes flash,@PathVariable("id_carrera")Long id_carrera, HttpServletRequest request,@RequestParam(name = "succes",required = false)String succes){
        if (request.getSession().getAttribute("usuario") != null) {
            
            if (succes != null) {
                model.addAttribute("succes", succes);
            }
            
            Carrera carrera = carreraService.findOne(id_carrera);
            System.out.println("cantidad "+asignacionHabilitadoService.lista_asignados_habilitados(id_carrera).size());
            model.addAttribute("list_asignados", asignacionHabilitadoService.lista_asignados_habilitados(id_carrera));
            List<VotanteHabilitado> vh = new ArrayList<>();
            for (Estudiante e : carrera.getEstudiantes()) {
                if (e.getVotante_habilitado().getEstado_mesa() ==null) {
                    vh.add(e.getVotante_habilitado());
                }
                
            }
            System.out.println(vh.size());
            
            model.addAttribute("carrera", vh);
            model.addAttribute("mesas_asig", mesaService.lista_mesas_por_carrera(id_carrera));
            model.addAttribute("mesas", mesaService.findAll());
            model.addAttribute("id_fac", id_carrera);
            return "Facultad/lista_selec_estudiantes";
        } else {
            return "redirect:/login";
        }
    }

    @PostMapping(value = "/asignacion_habilitado_post")
    public String asignacion_habilitado_post(Model model,RedirectAttributes flash, HttpServletRequest request,
    @RequestParam(name = "id_fac" ,required = false)Long id_car,
    @RequestParam(name = "num_1" ,required = false)Integer num1,
    @RequestParam(name = "id_asignacion_habilitado", required = false) String id_asignacion_habilitadoStr,
    @RequestParam(name = "id_mesa",required = false)Long id_mesa){
        if (request.getSession().getAttribute("usuario") != null) {
            
            if (id_asignacion_habilitadoStr != null && !id_asignacion_habilitadoStr.isEmpty()) {
                try {
                    ObjectMapper mapper = new ObjectMapper();
                    Long[] id_asignacion_habilitado = mapper.readValue(id_asignacion_habilitadoStr, Long[].class);
    
                    for (Long id : id_asignacion_habilitado) {
                        AsignacionHabilitado asignacionHabilitado = new AsignacionHabilitado();
                        Mesa mesa = mesaService.findOne(id_mesa);
                        mesa.setEstado("O"); // O mesa Ocupada
                        mesaService.save(mesa);
                        asignacionHabilitado.setMesa(mesa);
                        VotanteHabilitado votanteHabilitado = votanteHabilitadoService.findOne(id);
                        votanteHabilitado.setEstado_mesa("M");
                        votanteHabilitadoService.save(votanteHabilitado);
                        asignacionHabilitado.setVotante_habilitado(votanteHabilitado);
                        asignacionHabilitadoService.save(asignacionHabilitado);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            
            return "redirect:/admin/seleccion_est_carrera/"+id_car;
        } else {
            return "redirect:/login";
        }
    }
}
