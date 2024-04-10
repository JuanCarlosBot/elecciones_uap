package uap.elecciones.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpServletRequest;
import uap.elecciones.model.service.IUsuarioService;

@Controller
@RequestMapping(value = "/admin")
public class UsuarioController {

    @Autowired
    private IUsuarioService usuarioService;
    
    @RequestMapping(value = "/usuario",method = RequestMethod.GET)
    public String Vista_Usuario(Model model,RedirectAttributes flash, HttpServletRequest request){
        if (request.getSession().getAttribute("usuario") != null) {
            
            return "Usuario/usuario_vista";
        } else {
            return "redirect:/login";
        }
    }
}
