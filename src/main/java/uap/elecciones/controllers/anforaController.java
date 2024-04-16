package uap.elecciones.controllers;

import java.util.Date;
import java.util.Enumeration;
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
import uap.elecciones.model.entity.DetalleAnfora;
import uap.elecciones.model.entity.Frente;
import uap.elecciones.model.entity.Mesa;
import uap.elecciones.model.entity.Nivel;
import uap.elecciones.model.entity.Persona;
import uap.elecciones.model.service.IAnforaService;
import uap.elecciones.model.service.IDetalleAnforaService;
import uap.elecciones.model.service.IFrenteService;
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

    @Autowired
    private IFrenteService frenteService;

    @Autowired
    private IDetalleAnforaService detalleAnforaService;

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
    @RequestParam("nivel") Long id_nivel,
    @RequestParam("cant_voto_nulo") int cant_voto_nulo,
    @RequestParam("cant_voto_blanco") int cant_voto_blanco,
    @RequestParam("cant_voto_valido") int cant_voto_valido,
    RedirectAttributes flash, HttpServletRequest request){

        if (request.getSession().getAttribute("usuario") != null) {
            
            List<Object[]> frentes = anforaService.getDatosDeMesaYFrenteYNivelPorIdMesaYNivel(id_mesa, id_nivel);
        
            
            model.addAttribute("frentes", frentes);
            model.addAttribute("mesaId", id_mesa);
            model.addAttribute("cant_voto_nulo", cant_voto_nulo);
            model.addAttribute("cant_voto_blanco", cant_voto_blanco);
            model.addAttribute("cant_voto_valido", cant_voto_valido);

         

            return "Anfora/anfora_vista_frentes";
        } else {
            return "redirect:/login";
        }
    }

 

    @RequestMapping(value = "/anfora_frente",method = RequestMethod.POST)
    public String Vista_Anfora_frente(Model model,RedirectAttributes flash, HttpServletRequest request,@RequestParam(name = "succes",required = false)String succes,
    @RequestParam(name = "frenteId")Long frenteId,
    @RequestParam(name = "mesaId")Long mesaId,
    @RequestParam("cant_voto_nulo") int cant_voto_nulo,
    @RequestParam("cant_voto_blanco") int cant_voto_blanco,
    @RequestParam("cant_voto_valido") int cant_voto_valido){
        if (request.getSession().getAttribute("usuario") != null) {
            

            if (succes != null) {
                model.addAttribute("succes", succes);
            }
            Mesa mesa = mesaService.findOne(mesaId);
            Anfora anfora = new Anfora();
            anfora.setCant_voto_blanco(cant_voto_blanco);
            anfora.setCant_voto_nulo(cant_voto_nulo);
            anfora.setCant_voto_valido(cant_voto_valido);
            anfora.setMesa(mesa);
            anforaService.save(anfora);



              // Iterar sobre los parámetros de la solicitud
        Enumeration<String> parameterNames = request.getParameterNames();
        while (parameterNames.hasMoreElements()) {
            String paramName = parameterNames.nextElement();
            if (paramName.startsWith("cantidadVotantes_")) {
                // Obtener el valor del parámetro
                String paramValue = request.getParameter(paramName);

                // Extraer el identificador del frente del nombre del parámetro (ya no es necesario)
                // Long frenteId = Long.parseLong(frente[3].toString()); // Eliminado

                // Obtener el frente usando el frenteId proporcionado
                Frente frente = frenteService.findOne(frenteId);

                // Crear un nuevo DetalleAnfora y establecer sus valores
                DetalleAnfora detalleAnfora = new DetalleAnfora();
                detalleAnfora.setAnfora(anfora);
                detalleAnfora.setFrente(frente);

                if (paramName.startsWith("cantidadVotantes_")) {
                    detalleAnfora.setCant_votantes(Integer.parseInt(paramValue));
                }

                // Guardar el DetalleAnfora en la base de datos
                detalleAnforaService.save(detalleAnfora);
            }
        }

            return "redirect:/admin/inicio";
        } else {
            return "redirect:/login";
        }
    }
}
