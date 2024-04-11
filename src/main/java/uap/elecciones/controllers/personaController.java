package uap.elecciones.controllers;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


import jakarta.servlet.http.HttpServletRequest;
import uap.elecciones.model.entity.Persona;
import uap.elecciones.model.service.IPersonaService;

@Controller
@RequestMapping(value = "/admin")
public class personaController {
    
    @Autowired
    private IPersonaService personaService;

    @RequestMapping(value = "/persona",method = RequestMethod.GET)
    public String Vista_Persona(Model model,RedirectAttributes flash, HttpServletRequest request){
        if (request.getSession().getAttribute("usuario") != null) {
            
            model.addAttribute("persona", new Persona());
            
            return "Persona/persona_vista";
        } else {
            return "redirect:/login";
        }
    }

    @RequestMapping(value = "/persona_form",method = RequestMethod.POST)
    private String Form_Persona(Model model,@Validated @ModelAttribute("persona")Persona persona,RedirectAttributes flash, HttpServletRequest request){

        if (request.getSession().getAttribute("usuario") != null) {
            
            persona.setEstado("A");
            persona.setFecha_registro(new Date());
            personaService.save(persona);

            return "redirect:/admin/persona";
        } else {
            return "redirect:/login";
        }
    }
}
