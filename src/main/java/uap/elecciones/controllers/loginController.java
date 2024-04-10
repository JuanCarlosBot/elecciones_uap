package uap.elecciones.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import uap.elecciones.model.entity.Usuario;
import uap.elecciones.model.service.IUsuarioService;

@Controller
public class loginController {

    @Autowired
    private IUsuarioService usuarioService;
    
    @RequestMapping(value = "/login",method = RequestMethod.GET)
    public String Vista_Login(Model model){

        return "login";
    }

    @RequestMapping(value = "/login_form",method = RequestMethod.POST)
    public String Form_Login(Model model, @RequestParam(name = "user",required = false)String user,@RequestParam(name = "pass")String pass,HttpServletRequest request,RedirectAttributes flash){

        Usuario u = usuarioService.getUsuario(user, pass);

        if (u != null && u.getEstado() != "X") {
            HttpSession session = request.getSession(false);
    
            session = request.getSession(true);
            session.setAttribute("usuario", u);
            session.setAttribute("persona", u.getPersona());
            return "redirect:/admin/inicio";
        } else {
            return "login";
        }
    }
    
    @RequestMapping("/cerrar_sesion")
    public String Cerrar_Sesion(HttpServletRequest request, RedirectAttributes flash){
        HttpSession session = request.getSession(false);
		if (session != null) {
			session.invalidate();
			flash.addAttribute("validado", "Se cerro sesion con exito!");
		}
		return "redirect:/login";
    }
}
