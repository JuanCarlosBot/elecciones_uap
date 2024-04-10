package uap.elecciones.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpServletRequest;
import uap.elecciones.model.entity.Persona;
import uap.elecciones.model.entity.Usuario;

@Controller
@RequestMapping(value = "/admin")
public class inicioController {
    
    @RequestMapping(value = "/inicio",method = RequestMethod.GET)
    public String Pagina_Inicio(Model model,RedirectAttributes flash, HttpServletRequest request){
        if (request.getSession().getAttribute("usuario") != null) {
            Persona persona = (Persona) request.getSession().getAttribute("persona");
            // Usuario usuario = (Usuario) request.getSession().getAttribute("usuario");
            System.out.println(persona.getNombres());
            return "menu_principal";
        } else {
            return "redirect:/login";
        }
        
    }
}
