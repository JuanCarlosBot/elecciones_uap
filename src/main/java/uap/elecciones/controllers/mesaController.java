package uap.elecciones.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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

    @RequestMapping(value = "/mesa_form",method = RequestMethod.GET)
    public String Form_Mesa(Model model,RedirectAttributes flash, HttpServletRequest request,@RequestParam(name = "succes",required = false)String succes){
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
}
