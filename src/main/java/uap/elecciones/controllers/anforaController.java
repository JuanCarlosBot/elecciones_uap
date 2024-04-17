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
import uap.elecciones.model.entity.ConteoTotal;
import uap.elecciones.model.entity.DetalleAnfora;
import uap.elecciones.model.entity.Facultad;
import uap.elecciones.model.entity.Frente;
import uap.elecciones.model.entity.Mesa;
import uap.elecciones.model.entity.Nivel;
import uap.elecciones.model.entity.Persona;
import uap.elecciones.model.entity.VotoTotalFrente;
import uap.elecciones.model.service.IAnforaService;
import uap.elecciones.model.service.IConteoTotalService;
import uap.elecciones.model.service.IDetalleAnforaService;
import uap.elecciones.model.service.IFrenteService;
import uap.elecciones.model.service.IMesaService;
import uap.elecciones.model.service.INivelService;
import uap.elecciones.model.service.IVotoTotalFrenteService;
import org.springframework.web.bind.annotation.GetMapping;


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

    @Autowired
    private IConteoTotalService conteoTotalService;

    @Autowired
    private IVotoTotalFrenteService votoTotalFrenteService;

    @RequestMapping(value = "/anfora", method = RequestMethod.GET)
    public String Vista_Anfora(Model model, RedirectAttributes flash, HttpServletRequest request,
            @RequestParam(name = "succes", required = false) String succes) {
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

    @GetMapping("/formDetalleAnfora")
    public String formDetalleAnfora(@RequestParam("mesaId") Long id_mesa, @RequestParam("nivelId") Long id_nivel, Model model) {
        
        List<Object[]> frentes = frenteService.frentesPorMesaYNivel(id_mesa, id_nivel);
        for (Object[] frente : frentes) {
            System.out.println("frentes_____  "+frente[2]);
        }
        model.addAttribute("frentes", frentes);
        return "content :: content1";
    }
    
    @RequestMapping(value = "/anfora_form_prueba", method = RequestMethod.POST)
    private String anfora_form_prueba(
        @RequestParam("mesa") Long id_mesa,
        @RequestParam("nivel") Long id_nivel,
        @RequestParam("id_f") Long[] id_frente,
        @RequestParam("cant_voto_nulo") int cant_voto_nulo,
        @RequestParam("cant_voto_blanco") int cant_voto_blanco,
        @RequestParam("cant_voto_valido") int cant_voto_valido,
        @RequestParam("votosDetAnfora") int[] votoFrentes,
        RedirectAttributes flash, HttpServletRequest request){
        if (request.getSession().getAttribute("usuario") != null) {


            System.out.println(id_mesa+ " "+id_nivel+" "+id_frente.length+" "+cant_voto_nulo+" "+cant_voto_blanco+" "+cant_voto_valido+" "+votoFrentes.length);
            
        
            return "redirect:/admin/anfora";
        } else {
            return "redirect:/login";
        }
    }

    @RequestMapping(value = "/anfora_form", method = RequestMethod.POST)
    private String Form_Anfora(Model model,
            @RequestParam("mesa") Long id_mesa,
            @RequestParam("nivel") Long id_nivel,
            @RequestParam("cant_voto_nulo") int cant_voto_nulo,
            @RequestParam("cant_voto_blanco") int cant_voto_blanco,
            @RequestParam("cant_voto_valido") int cant_voto_valido,
            RedirectAttributes flash, HttpServletRequest request) {

        if (request.getSession().getAttribute("usuario") != null) {

            List<Object[]> frentes = anforaService.getDatosDeMesaYFrenteYNivelPorIdMesaYNivel(id_mesa, id_nivel);

            model.addAttribute("id_nivel", id_nivel);
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

    @RequestMapping(value = "/anfora_frente", method = RequestMethod.POST)
public String Vista_Anfora_frente(Model model, RedirectAttributes flash, HttpServletRequest request,
        @RequestParam(name = "succes", required = false) String succes,
        @RequestParam(name = "mesaId") Long mesaId,
        @RequestParam(name = "id_nivel") Long id_nivel,
        @RequestParam("cant_voto_nulo") int cant_voto_nulo,
        @RequestParam("cant_voto_blanco") int cant_voto_blanco,
        @RequestParam("cant_voto_valido") int cant_voto_valido) {
    if (request.getSession().getAttribute("usuario") != null) {

        if (succes != null) {
            model.addAttribute("succes", succes);
        }

        Mesa mesa = mesaService.findOne(mesaId);

        Object facultadObject = anforaService.mesaPorFacultad(mesaId);
        String nombreFacultad = (String) facultadObject;
        String nombreFull = "FULL";
        List<ConteoTotal> listConteo = conteoTotalService.findAll();
        ConteoTotal conteoTotal = new ConteoTotal();
       
        Nivel n = nivelService.findOne(id_nivel);
        if (listConteo.isEmpty()) {
            conteoTotal.setBlanco_total(cant_voto_blanco);
            conteoTotal.setNulo_total(cant_voto_nulo);
            conteoTotal.setVoto_valido_total(cant_voto_valido);
            conteoTotal.setFacultad(nombreFacultad);
            conteoTotal.setNivel(n);
            conteoTotalService.save(conteoTotal);
            System.out.println("entro en size 0");
        } else {
            for (ConteoTotal conteoTotal2 : listConteo) {
                System.out.println(n.getId_nivel()+"aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
                if (n.getId_nivel() == 3) {
                    System.out.println(n.getId_nivel()+"aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
                    if (nombreFull.equals(conteoTotal2.getFacultad())) {
                        conteoTotal = conteoTotal2;
                        conteoTotal.setBlanco_total(conteoTotal.getBlanco_total() + cant_voto_blanco);
                        conteoTotal.setNulo_total(conteoTotal.getNulo_total() + cant_voto_nulo);
                        conteoTotal.setVoto_valido_total(conteoTotal.getVoto_valido_total() + cant_voto_valido);
                        conteoTotal.setFacultad("FULL");
                        conteoTotal.setNivel(n);
                        conteoTotalService.save(conteoTotal);
                        break;
                    }
                    if (nombreFull.equals(conteoTotal2.getFacultad()) ) {
                        conteoTotal = conteoTotal2;
                        conteoTotal.setBlanco_total(conteoTotal.getBlanco_total() + cant_voto_blanco);
                        conteoTotal.setNulo_total(conteoTotal.getNulo_total() + cant_voto_nulo);
                        conteoTotal.setVoto_valido_total(conteoTotal.getVoto_valido_total() + cant_voto_valido);
                        conteoTotal.setFacultad("FULL");
                        conteoTotal.setNivel(n);
                        conteoTotalService.save(conteoTotal);
                        break;
                    }
                }else{
                    if (nombreFacultad.equals(conteoTotal2.getFacultad())) {
                        conteoTotal = conteoTotal2;
                        conteoTotal.setBlanco_total(conteoTotal.getBlanco_total() + cant_voto_blanco);
                        conteoTotal.setNulo_total(conteoTotal.getNulo_total() + cant_voto_nulo);
                        conteoTotal.setVoto_valido_total(conteoTotal.getVoto_valido_total() + cant_voto_valido);
                        conteoTotal.setFacultad(nombreFacultad);
                        conteoTotal.setNivel(n);
                        conteoTotalService.save(conteoTotal);
                        break;
                    }
                    if (nombreFacultad.equals(conteoTotal2.getFacultad()) ) {
                        conteoTotal = conteoTotal2;
                        conteoTotal.setBlanco_total(conteoTotal.getBlanco_total() + cant_voto_blanco);
                        conteoTotal.setNulo_total(conteoTotal.getNulo_total() + cant_voto_nulo);
                        conteoTotal.setVoto_valido_total(conteoTotal.getVoto_valido_total() + cant_voto_valido);
                        conteoTotal.setFacultad(nombreFacultad);
                        conteoTotal.setNivel(n);
                        conteoTotalService.save(conteoTotal);
                        break;
                    }
                }
               
            }
            if (n.getId_nivel() == 3) {
                if (!nombreFull.equals(conteoTotal.getFacultad())) {
                    conteoTotal.setBlanco_total(conteoTotal.getBlanco_total() + cant_voto_blanco);
                    conteoTotal.setNulo_total(conteoTotal.getNulo_total() + cant_voto_nulo);
                    conteoTotal.setVoto_valido_total(conteoTotal.getVoto_valido_total() + cant_voto_valido);
                    conteoTotal.setFacultad("FULL");
                    conteoTotal.setNivel(n);
                    conteoTotalService.save(conteoTotal);
                    System.out.println("No entro en el igual");
                }
            }else{
                if (!nombreFacultad.equals(conteoTotal.getFacultad())) {
                    conteoTotal.setBlanco_total(conteoTotal.getBlanco_total() + cant_voto_blanco);
                    conteoTotal.setNulo_total(conteoTotal.getNulo_total() + cant_voto_nulo);
                    conteoTotal.setVoto_valido_total(conteoTotal.getVoto_valido_total() + cant_voto_valido);
                    conteoTotal.setFacultad(nombreFacultad);
                    conteoTotal.setNivel(n);
                    conteoTotalService.save(conteoTotal);
                    System.out.println("No entro en el igual");
                }
            }
           
        }

        Anfora anfora = new Anfora();
        anfora.setCant_voto_blanco(cant_voto_blanco);
        anfora.setCant_voto_nulo(cant_voto_nulo);
        anfora.setCant_voto_valido(cant_voto_valido);
        anfora.setMesa(mesa);
        anfora.setConteo_total(conteoTotal);
        anforaService.save(anfora);

        List<VotoTotalFrente> listVotosTotalFrente = votoTotalFrenteService.findAll();
        // Iterar sobre los parámetros de la solicitud
        Enumeration<String> parameterNames = request.getParameterNames();
        while (parameterNames.hasMoreElements()) {
            String paramName = parameterNames.nextElement();
            if (paramName.startsWith("cantidadVotantes_")) {
                // Obtener el valor del parámetro
                String paramValue = request.getParameter(paramName);

                // Obtener el frenteId a partir del paramName
                Long frenteId = Long.parseLong(paramName.substring("cantidadVotantes_".length()));

                // Obtener el frente usando el frenteId proporcionado
                Frente frente = frenteService.findOne(frenteId);

                // Crear un nuevo DetalleAnfora y establecer sus valores
                DetalleAnfora detalleAnfora = new DetalleAnfora();
                detalleAnfora.setAnfora(anfora);
                detalleAnfora.setFrente(frente);
                detalleAnfora.setCant_votantes(Integer.parseInt(paramValue));

                // Iterar sobre la lista de VotoTotalFrente
                boolean existeVotoTotalFrente = false;
                for (VotoTotalFrente votoTotalFrente2 : listVotosTotalFrente) {
                    // Verificar si ya existe un registro para el frente actual
                    if (votoTotalFrente2.getFrente().getId_frente() == frenteId) {
                        // Actualizar el registro existente
                        votoTotalFrente2.setVoto_total_frente(
                                votoTotalFrente2.getVoto_total_frente() + detalleAnfora.getCant_votantes());
                        votoTotalFrente2.setConteo_total(conteoTotal);
                        votoTotalFrente2.setFrente(frente);
                        votoTotalFrenteService.save(votoTotalFrente2);
                        detalleAnfora.setVoto_total_frente(votoTotalFrente2);
                        existeVotoTotalFrente = true;
                        break; // Salir del bucle, ya se actualizó el registro
                    }
                }

                // Si no se encontró un registro existente, crear uno nuevo
                if (!existeVotoTotalFrente) {
                    VotoTotalFrente nuevoVotoTotalFrente = new VotoTotalFrente();
                    nuevoVotoTotalFrente.setFrente(frente);
                    nuevoVotoTotalFrente.setVoto_total_frente(detalleAnfora.getCant_votantes());
                    nuevoVotoTotalFrente.setConteo_total(conteoTotal);
                 
                    votoTotalFrenteService.save(nuevoVotoTotalFrente);
                    detalleAnfora.setVoto_total_frente(nuevoVotoTotalFrente);
                   
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
