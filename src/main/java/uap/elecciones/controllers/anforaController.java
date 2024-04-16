package uap.elecciones.controllers;

import java.util.Date;
import java.util.List;

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
import uap.elecciones.model.entity.Anfora;
import uap.elecciones.model.entity.Mesa;
import uap.elecciones.model.entity.Nivel;
import uap.elecciones.model.entity.Persona;
import uap.elecciones.model.service.IAnforaService;
import uap.elecciones.model.service.IMesaService;
import uap.elecciones.model.service.INivelService;

@Controller
@RequestMapping(value = "/admin")
public class anforaController {
    
    @Autowired
    private IAnforaService anforaService;

    @Autowired
    private IMesaService mesaService;

    @Autowired
    private INivelService nivelService;

       @RequestMapping(value = "/anfora",method = RequestMethod.GET)
    public String Vista_Anfora(Model model,RedirectAttributes flash, HttpServletRequest request,@RequestParam(name = "succes",required = false)String succes){
        if (request.getSession().getAttribute("usuario") != null) {
            

            if (succes != null) {
                model.addAttribute("succes", succes);
            }
            List<Mesa> mesas = mesaService.findAll();
            List<Nivel> niveles = nivelService.findAll();

            model.addAttribute("anfora", new Anfora());
            model.addAttribute("mesas", mesas);
            model.addAttribute("niveles", niveles);
            
            return "Anfora/anfora_vista";
        } else {
            return "redirect:/login";
        }
    }

    
    @RequestMapping(value = "/anfora_form",method = RequestMethod.POST)
    private String Form_Anfora(Model model,
    @RequestParam("mesa") Long id_mesa,
    @RequestParam("nivel") Long id_nivel
    ,@Validated @ModelAttribute("anfora")Anfora anfora,RedirectAttributes flash, HttpServletRequest request){

        if (request.getSession().getAttribute("usuario") != null) {
            
            List<Object[]> frentes = anforaService.getDatosDeMesaYFrenteYNivelPorIdMesaYNivel(id_mesa, id_nivel);
            anfora.setMesa(mesaService.findOne(id_mesa));
            anforaService.save(anfora);
            
            model.addAttribute("frentes", frentes);
            model.addAttribute("anforaId", anfora.getId_anfora());
    
         

            return "redirect:/admin/anfora_frente";
        } else {
            return "redirect:/login";
        }
    }

    @RequestMapping(value = "/anfora_frente",method = RequestMethod.GET)
    public String Vista_Anfora_frente(Model model,RedirectAttributes flash, HttpServletRequest request,@RequestParam(name = "succes",required = false)String succes){
        if (request.getSession().getAttribute("usuario") != null) {
            

            if (succes != null) {
                model.addAttribute("succes", succes);
            }
           
            return "Anfora/anfora_vista_frentes";
        } else {
            return "redirect:/login";
        }
    }
}
