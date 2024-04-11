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
import uap.elecciones.model.entity.Frente;
import uap.elecciones.model.service.IFrenteService;

@Controller
@RequestMapping(value = "/admin")
public class frenteController {

    @Autowired
    private IFrenteService frenteService;
    
    @RequestMapping(value = "/frente",method = RequestMethod.GET)
    public String Vista_Frente(Model model,RedirectAttributes flash, HttpServletRequest request,@RequestParam(name = "succes",required = false)String succes){
        if (request.getSession().getAttribute("usuario") != null) {
            

            if (succes != null) {
                model.addAttribute("succes", succes);
            }
            model.addAttribute("frente", new Frente());
            model.addAttribute("frentes", frenteService.findAll());
            return "Frente/frente_vista";
        } else {
            return "redirect:/login";
        }
    }

    @RequestMapping(value = "/frente_form",method = RequestMethod.POST)
    private String Form_Frente(Model model,@Validated @ModelAttribute("frente")Frente frente,RedirectAttributes flash, HttpServletRequest request){

        if (request.getSession().getAttribute("usuario") != null) {
            
            frente.setFecha_registro(new Date());
            frente.setEstado("A");
            frenteService.save(frente);

            if (frente.getId_frente() == null) {
                flash.addAttribute("succes", "Registro Agregado Con Exito");
            }else{
                flash.addAttribute("succes", "Registro Editado Con Exito");
            }
            
            return "redirect:/admin/frente";
        } else {
            return "redirect:/login";
        }
    }

    @RequestMapping(value = "/editar_frente/{id}")
    public String Editar_Frente(Model model,@PathVariable(name = "id")Long id,RedirectAttributes flash, HttpServletRequest request){
        if (request.getSession().getAttribute("usuario") != null) {
            
            Frente frente = frenteService.findOne(id);
            model.addAttribute("frente", frente);
            return "Frente/frente_vista";
        } else {
            return "redirect:/login";
        }
    }

    @RequestMapping(value = "/eliminar_frente/{id}")
    public String Eliminar_Frente(Model model,@PathVariable(name = "id")Long id,RedirectAttributes flash, HttpServletRequest request){
        if (request.getSession().getAttribute("usuario") != null) {
            
            Frente frente = frenteService.findOne(id);
            frente.setEstado("X");
            frenteService.save(frente);
            flash.addAttribute("succes", "Registro Eliminado Con Exito");
            return "redirect:/admin/frente";
        } else {
            return "redirect:/login";
        }
    }
}
