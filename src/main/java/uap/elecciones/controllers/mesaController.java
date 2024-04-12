package uap.elecciones.controllers;

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
import uap.elecciones.model.entity.Mesa;
import uap.elecciones.model.service.IMesaService;

@Controller
@RequestMapping(value = "/admin")
public class mesaController {
    
    @Autowired
    private IMesaService mesaService;

    @RequestMapping(value = "/mesa",method = RequestMethod.GET)
    public String Vista_Mesa(Model model,RedirectAttributes flash, HttpServletRequest request,@RequestParam(name = "succes",required = false)String succes){
        if (request.getSession().getAttribute("usuario") != null) {
            
            if (succes != null) {
                model.addAttribute("succes", succes);
            }
            model.addAttribute("mesa", new Mesa());
            model.addAttribute("mesas", mesaService.findAll());
            return "Mesa/mesa_vista";
        } else {
            return "redirect:/login";
        }
    }

    @RequestMapping(value = "/mesa_form",method = RequestMethod.POST)
    public String Form_Mesa(Model model,RedirectAttributes flash, HttpServletRequest request,
    @Validated @ModelAttribute("mesa") Mesa mesa){
        if (request.getSession().getAttribute("usuario") != null) {
            
            mesa.setEstado("A");
           
            if (mesa.getId_mesa() == null) {
                mesa.setFecha_registro(new Date());
                flash.addAttribute("succes", "Registro Agregado Con Exito!");
            } else {
                flash.addAttribute("succes", "Registro Editado Con Exito!");
            }
            mesaService.save(mesa);
            return "redirect:/admin/mesa";
        } else {
            return "redirect:/login";
        }
    }

    @RequestMapping(value = "/editar_mesa/{id}")
    public String Editar_Asignacion_Eleccion(Model model,@PathVariable(name = "id")Long id,RedirectAttributes flash, HttpServletRequest request){
        if (request.getSession().getAttribute("usuario") != null) {
            
            Mesa mesa = mesaService.findOne(id);
            model.addAttribute("mesa", mesa);
            return "Mesa/mesa_vista";
        } else {
            return "redirect:/login";
        }
    }
}
