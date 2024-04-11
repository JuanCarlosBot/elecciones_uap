package uap.elecciones.controllers;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpServletRequest;
import uap.elecciones.model.entity.Usuario;
import uap.elecciones.model.service.IPersonaService;
import uap.elecciones.model.service.IUsuarioService;

@Controller
@RequestMapping(value = "/admin")
public class UsuarioController {

    @Autowired
    private IUsuarioService usuarioService;

    @Autowired
    private IPersonaService personaService;
    
    @RequestMapping(value = "/usuario",method = RequestMethod.GET)
    public String Vista_Usuario(Model model,RedirectAttributes flash, HttpServletRequest request){
        if (request.getSession().getAttribute("usuario") != null) {
            
            model.addAttribute("usuario", new Usuario());
            model.addAttribute("personas", personaService.findAll());
            return "Usuario/usuario_vista";
        } else {
            return "redirect:/login";
        }
    }

    @RequestMapping(value = "/usuario_form",method = RequestMethod.POST)
    private String Form_Usuario(Model model,@Validated @ModelAttribute("usuario")Usuario usuario,@RequestParam(name = "id_persona")Long id_persona,RedirectAttributes flash, HttpServletRequest request){

        if (request.getSession().getAttribute("usuario") != null) {
            
            usuario.setEstado("A");
            usuario.setFecha_registro(new Date());
            usuario.setPersona(personaService.findOne(id_persona));
            usuarioService.save(usuario);

            return "redirect:/admin/usuario";
        } else {
            return "redirect:/login";
        }
    }
}
