package uap.elecciones.controllers;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpServletRequest;
import uap.elecciones.model.entity.Acta;
import uap.elecciones.model.entity.Usuario;
import uap.elecciones.model.service.ActaService;
import uap.elecciones.model.service.IMesaService;
import uap.elecciones.model.service.UtilidadService;

@Controller
@RequestMapping(value = "/acta")
public class ActaController {

    @Autowired
    private ActaService actaService;

    @Autowired
    private IMesaService mesaService;

    @Autowired
    private UtilidadService utilidadService;

    @RequestMapping(value = "/ventana", method = RequestMethod.GET)
    public String Vista_Usuario(Model model, RedirectAttributes flash, HttpServletRequest request,
            @RequestParam(name = "succes", required = false) String succes) {
        if (request.getSession().getAttribute("usuario") != null) {

            if (succes != null) {
                model.addAttribute("succes", succes);
            }
            model.addAttribute("acta", new Acta());
            model.addAttribute("mesas", mesaService.listarMesasOrdenadas());
            model.addAttribute("actas", actaService.listarActasPorOdenMesa());
            return "Acta/vista";
        } else {
            return "redirect:/login";
        }
    }

    @RequestMapping(value = "/ventanaEdit/{idActa}", method = RequestMethod.GET)
    public String Vista_Usuario(Model model, RedirectAttributes flash, HttpServletRequest request,
            @RequestParam(name = "succes", required = false) String succes,
            @PathVariable("idActa") Long idActa) {
        if (request.getSession().getAttribute("usuario") != null) {

            if (succes != null) {
                model.addAttribute("succes", succes);
            }
            model.addAttribute("acta", actaService.findOne(idActa));
            model.addAttribute("mesas", mesaService.listarMesasOrdenadas());
            model.addAttribute("actas", actaService.listarActasPorOdenMesa());
            model.addAttribute("edit", "true");
            return "Acta/vista";
        } else {
            return "redirect:/login";
        }
    }

    @PostMapping(value = "/registrar")
    public String registrar(Model model, RedirectAttributes flash, HttpServletRequest request,
            @Validated Acta acta, @RequestParam(name = "file", required = false) MultipartFile archivo) {

        if (request.getSession().getAttribute("usuario") != null) {
            if (actaService.actaPorIdMesa(acta.getMesa().getId_mesa()) == null) {
                if (archivo != null && !archivo.isEmpty()) {
                    acta.setRutaArchivo(utilidadService.guardarArchivo(archivo));
                    acta.setTipoArchivo(archivo.getContentType());
                }

                acta.setEstado("A");
                acta.setFechaRegistro(new Date());
                actaService.save(acta);
                flash.addFlashAttribute("tipoMensaje", "success");
                flash.addFlashAttribute("mensaje", "El registro se guardó con éxito");
                return "redirect:/acta/ventana";
            } else {
                flash.addFlashAttribute("tipoMensaje", "danger");
                flash.addFlashAttribute("mensaje", "Ya existe un acta en la mesa seleccionada");
                return "redirect:/acta/ventana";
            }

        } else {
            return "redirect:/login";
        }
    }

    @PostMapping(value = "/modificar")
    public String modificar(Model model, RedirectAttributes flash, HttpServletRequest request,
            @Validated Acta a, @RequestParam(name = "file", required = false) MultipartFile archivo) {
        Acta acta = actaService.findOne(a.getId_acta());

        if (request.getSession().getAttribute("usuario") != null) {
            if (actaService.actaPorIdMesa2(acta.getMesa().getId_mesa(), acta.getId_acta()) == null) {
                if (archivo != null && !archivo.isEmpty() && !archivo.getOriginalFilename().isEmpty()) {
                    // Guardar la ruta y tipo de archivo solo si se envió un archivo válido
                    acta.setRutaArchivo(utilidadService.guardarArchivo(archivo));
                    acta.setTipoArchivo(archivo.getContentType());
                }

                // Actualiza la fecha de modificación, independientemente de si hay archivo o no
                acta.setMesa(a.getMesa());
                acta.setFechaModificacion(new Date());
                actaService.save(acta);

                // Redirige después de guardar
                flash.addFlashAttribute("mensaje", "El registro se modificó con éxito");
                return "redirect:/acta/ventana";
            } else {
                flash.addFlashAttribute("tipoMensaje", "danger");
                flash.addFlashAttribute("mensaje", "Ya existe un acta en la mesa seleccionada");
                return "redirect:/acta/ventana";
            }

        } else {
            // Si no hay usuario en la sesión, redirige al login
            return "redirect:/login";
        }
    }

    @RequestMapping(value = "/eliminar/{idActa}", method = RequestMethod.GET)
    public String eliminar(Model model, RedirectAttributes flash, HttpServletRequest request,
            @RequestParam(name = "succes", required = false) String succes,
            @PathVariable("idActa") Long idActa) {
        if (request.getSession().getAttribute("usuario") != null) {
            actaService.delete(idActa);
            return "redirect:/acta/ventana";
        } else {
            return "redirect:/login";
        }
    }
}
