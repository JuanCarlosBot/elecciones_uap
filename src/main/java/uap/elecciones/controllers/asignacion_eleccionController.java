package uap.elecciones.controllers;

import java.time.LocalDate;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpServletRequest;
import uap.elecciones.model.entity.AsignacionEleccion;
import uap.elecciones.model.entity.DetalleAsignacionMesa;
import uap.elecciones.model.service.IAsignacionEleccionService;
import uap.elecciones.model.service.IDetalleAsignacionMesaService;
import uap.elecciones.model.service.IFrenteService;
import uap.elecciones.model.service.IMesaService;
import uap.elecciones.model.service.INivelService;
import uap.elecciones.model.service.ITipoEleccionService;

@Controller
@RequestMapping(value = "/admin")
public class asignacion_eleccionController {
    
    @Autowired
    private IFrenteService frenteService;

    @Autowired
    private INivelService nivelService;

    @Autowired
    private ITipoEleccionService tipoEleccionService;

    @Autowired
    private IAsignacionEleccionService asignacionEleccionService;

    @Autowired
    private IDetalleAsignacionMesaService detalleAsignacionMesaService;

    @Autowired
    private IMesaService mesaService;

    @RequestMapping(value = "/asignacion_eleccion", method = RequestMethod.GET)
    public String Vista_Asignacion_Eleccion(Model model, RedirectAttributes flash, HttpServletRequest request,
            @RequestParam(name = "succes", required = false) String succes) {
        if (request.getSession().getAttribute("usuario") != null) {

            if (succes != null) {
                model.addAttribute("succes", succes);
            }
            model.addAttribute("asignacion", new AsignacionEleccion());
            model.addAttribute("det_asignacion", new DetalleAsignacionMesa());
            model.addAttribute("frentes", frenteService.findAll());
            model.addAttribute("niveles", nivelService.findAll());
            model.addAttribute("tipo_elecciones", tipoEleccionService.findAll());
            model.addAttribute("asignacion_elecciones", asignacionEleccionService.findAll());
            model.addAttribute("mesas", mesaService.findAll());
            return "Asignacion_Eleccion/asignacion_eleccion_vista";
        } else {
            return "redirect:/login";
        }
    }

    @RequestMapping(value = "/asignacion_eleccion_form", method = RequestMethod.POST)
    private String Form_Asignacion_Eleccion(Model model,
            @RequestParam(name = "id_frente", required = false) Long id_frente,
            @RequestParam(name = "id_nivel", required = false) Long id_nivel,
            @RequestParam(name = "id_tipoeleccion",required = false)Long id_tipoeleccion,
            @Validated @ModelAttribute("asignacion") AsignacionEleccion asignacionEleccion, RedirectAttributes flash,
            HttpServletRequest request) {

        if (request.getSession().getAttribute("usuario") != null) {

            Date fecha = new Date();
            LocalDate localDate = fecha.toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDate();

            int year = localDate.getYear();
            String yearAsString = String.valueOf(year);

            asignacionEleccion.setEstado("A");
            asignacionEleccion.setFrente(frenteService.findOne(id_frente));
            asignacionEleccion.setNivel(nivelService.findOne(id_nivel));
            asignacionEleccion.setTipo_eleccion(tipoEleccionService.findOne(id_tipoeleccion));

            if (asignacionEleccion.getId_asignacion_eleccion() == null) {
                asignacionEleccion.setGestion(yearAsString);
                asignacionEleccion.setFecha_registro(new Date());
                flash.addAttribute("succes", "Registro Agregado Con Exito!");
            } else {
                flash.addAttribute("succes", "Registro Editado Con Exito!");
            }
            asignacionEleccionService.save(asignacionEleccion);
            return "redirect:/admin/asignacion_eleccion";
        } else {
            return "redirect:/login";
        }
    }

    @RequestMapping(value = "/det_asignacion_mesa_form", method = RequestMethod.POST)
    private String Form_Det_Asignacion_Mesa(Model model,
            @RequestParam(name = "id_asignacion_eleccion", required = false) Long id_asignacion_eleccion,
            @RequestParam(name = "id_mesa", required = false) Long id_mesa,
            @Validated @ModelAttribute("det_asignacion") DetalleAsignacionMesa detalleAsignacionMesa, RedirectAttributes flash,
            HttpServletRequest request) {

        if (request.getSession().getAttribute("usuario") != null) {

            detalleAsignacionMesa.setEstado("A");
            detalleAsignacionMesa.setAsignacion_eleccion(asignacionEleccionService.findOne(id_asignacion_eleccion));
            detalleAsignacionMesa.setMesa(mesaService.findOne(id_mesa));
            if (detalleAsignacionMesa.getId_detalle_asignacion_mesa() == null) {
                detalleAsignacionMesa.setFecha_registro(new Date());
                flash.addAttribute("succes", "Registro Agregado Con Exito!");
            } else {
                flash.addAttribute("succes", "Registro Editado Con Exito!");
            }
            detalleAsignacionMesaService.save(detalleAsignacionMesa);
            return "redirect:/admin/asignacion_eleccion";
        } else {
            return "redirect:/login";
        }
    }

    @RequestMapping(value = "/editar_asignacion_eleccion/{id}")
    public String Editar_Asignacion_Eleccion(Model model,@PathVariable(name = "id")Long id,RedirectAttributes flash, HttpServletRequest request){
        if (request.getSession().getAttribute("usuario") != null) {
            
            AsignacionEleccion asignacionEleccion = asignacionEleccionService.findOne(id);
            model.addAttribute("asignacion", asignacionEleccion);
            model.addAttribute("frentes", frenteService.findAll());
            model.addAttribute("niveles", nivelService.findAll());
            model.addAttribute("tipo_elecciones", tipoEleccionService.findAll());
            return "Asignacion_Eleccion/asignacion_eleccion_vista";
        } else {
            return "redirect:/login";
        }
    }

    @RequestMapping(value = "/eliminar_asignacion_eleccion/{id}")
    public String ELiminar_Asignacion_Eleccion(Model model,@PathVariable(name = "id")Long id,RedirectAttributes flash, HttpServletRequest request){
        if (request.getSession().getAttribute("usuario") != null) {
            
            AsignacionEleccion asignacionEleccion = asignacionEleccionService.findOne(id);
            asignacionEleccion.setEstado("X");
            asignacionEleccionService.save(asignacionEleccion);
            flash.addAttribute("succes", "Registro Eliminado Con Exito!");
            return "redirect:/admin/asignacion_eleccion";
        } else {
            return "redirect:/login";
        }
    }
}
