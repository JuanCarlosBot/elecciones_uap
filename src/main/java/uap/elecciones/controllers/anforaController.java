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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpServletRequest;
import uap.elecciones.model.dao.IAsignacionHabilitadoDao;
import uap.elecciones.model.entity.Anfora;
import uap.elecciones.model.entity.AsignacionHabilitado;
import uap.elecciones.model.entity.Carrera;
import uap.elecciones.model.entity.ConteoTotal;
import uap.elecciones.model.entity.ConteoTotalCarrera;
import uap.elecciones.model.entity.DetalleAnfora;
import uap.elecciones.model.entity.Facultad;
import uap.elecciones.model.entity.Frente;
import uap.elecciones.model.entity.Mesa;
import uap.elecciones.model.entity.Nivel;
import uap.elecciones.model.entity.Persona;
import uap.elecciones.model.entity.VotoTotalCarrera;
import uap.elecciones.model.entity.VotoTotalFrente;
import uap.elecciones.model.service.IAnforaService;
import uap.elecciones.model.service.IAsignacionHabilitadoService;
import uap.elecciones.model.service.ICarreraService;
import uap.elecciones.model.service.IConteoTotalCarreraService;
import uap.elecciones.model.service.IConteoTotalService;
import uap.elecciones.model.service.IDetalleAnforaService;
import uap.elecciones.model.service.IFrenteService;
import uap.elecciones.model.service.IMesaService;
import uap.elecciones.model.service.INivelService;
import uap.elecciones.model.service.IVotoTotalCarreraService;
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
    private IConteoTotalCarreraService conteoTotalCarreraService;

    @Autowired
    private IVotoTotalFrenteService votoTotalFrenteService;

    @Autowired
    private IVotoTotalCarreraService votoTotalCarreraService;

    @Autowired
    private IAsignacionHabilitadoDao asignacionHabilitadoService;

    @Autowired
    private ICarreraService carreraService;

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
    public String formDetalleAnfora(@RequestParam("mesaId") Long id_mesa, @RequestParam("nivelId") Long id_nivel,
            Model model) {

        List<Object[]> frentes = frenteService.frentesPorMesaYNivel(id_mesa, id_nivel);
        for (Object[] frente : frentes) {
            System.out.println("frentes_____  " + frente[2]);
        }
        model.addAttribute("frentes", frentes);
        return "content :: content1";
    }

    @GetMapping("/sacarCantidadVotanteMesa")
    @ResponseBody
    public String sacarCantidadVotanteMesa(@RequestParam("mesaId") Long id_mesa) {
        List<AsignacionHabilitado> listaVotantesPorMesas = asignacionHabilitadoService
                .lista_asignados_habilitados_por_mesa(id_mesa);
        int cant = listaVotantesPorMesas.size();
        return String.valueOf(cant); // Convierte la cantidad a cadena y devuelve como respuesta
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
            RedirectAttributes flash, HttpServletRequest request) {
        if (request.getSession().getAttribute("usuario") != null) {

            Mesa mesa = mesaService.findOne(id_mesa);

            Object facultadObject = anforaService.mesaPorFacultad(id_mesa);
            Object carreraObject = mesaService.mesaPorCarrera(id_mesa);
            Object[] carreraArray = (Object[]) carreraObject;
            String nombreFacultad = (String) facultadObject;
            String nombreCarrera = (String) carreraArray[2];
            Long idCarrera = (Long) carreraArray[4];
            Carrera carrera = carreraService.findOne(idCarrera);

            String nombreFull = "FULL";
            List<ConteoTotal> listConteo = conteoTotalService.findAll();
            List<ConteoTotalCarrera> listConteoCarrera = conteoTotalCarreraService.findAll();
            ConteoTotal conteoTotal = new ConteoTotal();
            ConteoTotalCarrera conteoTotalCarrera = new ConteoTotalCarrera();
            Nivel n = nivelService.findOne(id_nivel);

            if (n.getId_nivel() == 3) {
                mesa.setEstado_full("A");
                mesaService.save(mesa);
            } else {
                mesa.setEstado_centro("A");
                mesaService.save(mesa);
            }

            if (mesa.getEstado_full() != null && mesa.getEstado_centro() != null) {
                mesa.setEstado("COMPLETADO");
                mesaService.save(mesa);
            }

            // CONTEO TOTAL
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

                    if (n.getId_nivel() == 3) {

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

                    } else {
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
                } else {
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
            String nombreFullCarrera = "FULL" + "-" + carrera.getNombre_carrera();
            // CONTEO TOTAL CARRERA
            if (listConteoCarrera.isEmpty()) {
                if (n.getId_nivel() != 3) {
                    conteoTotalCarrera.setBlanco_total(cant_voto_blanco);
                    conteoTotalCarrera.setNulo_total(cant_voto_nulo);
                    conteoTotalCarrera.setVoto_valido_total(cant_voto_valido);
                    conteoTotalCarrera.setCarrera(nombreCarrera);
                    conteoTotalCarreraService.save(conteoTotalCarrera); 
                }
               

                for (Carrera carrera2 : carreraService.findAll()) {
                    
                ConteoTotalCarrera conteoTotalCarrera2 = new ConteoTotalCarrera();  
                    conteoTotalCarrera2.setCarrera("FULL" + "-" + carrera2.getNombre_carrera());
                    conteoTotalCarreraService.save(conteoTotalCarrera2);      
                }

            } else {
                if (n.getId_nivel() == 3) {
                    for (ConteoTotalCarrera conteoTotalCarrera2 : listConteoCarrera) {
                        if (nombreFullCarrera.equals(conteoTotalCarrera2.getCarrera())) {
                            conteoTotalCarrera = conteoTotalCarrera2;
                        }    
                    }
                }else{
                    for (ConteoTotalCarrera conteoTotalCarrera2 : listConteoCarrera) {
                        if (nombreCarrera.equals(conteoTotalCarrera2.getCarrera())) {
                            conteoTotalCarrera = conteoTotalCarrera2;
                        }
    
                    }
                }                
                if (n.getId_nivel() == 3) {
                    if (nombreFullCarrera.equals(conteoTotalCarrera.getCarrera())) {
                        //conteoTotalCarrera = conteoTotalCarrera2;
                        conteoTotalCarrera.setBlanco_total(conteoTotalCarrera.getBlanco_total() + cant_voto_blanco);
                        conteoTotalCarrera.setNulo_total(conteoTotalCarrera.getNulo_total() + cant_voto_nulo);
                        conteoTotalCarrera.setVoto_valido_total(conteoTotalCarrera.getVoto_valido_total() + cant_voto_valido);
                        // conteoTotalCarrera.setCarrera("FULL" +"-"+nombreCarrera);
                        conteoTotalCarreraService.save(conteoTotalCarrera);
                        System.out.println("Entro en el igual FULL");
                        
                    }    
                }else{
                    if (nombreCarrera.equals(conteoTotalCarrera.getCarrera())) {
                        //conteoTotalCarrera = conteoTotalCarrera2;
                        conteoTotalCarrera.setBlanco_total(conteoTotalCarrera.getBlanco_total() + cant_voto_blanco);
                        conteoTotalCarrera.setNulo_total(conteoTotalCarrera.getNulo_total() + cant_voto_nulo);
                        conteoTotalCarrera.setVoto_valido_total(conteoTotalCarrera.getVoto_valido_total() + cant_voto_valido);
                        // conteoTotalCarrera.setCarrera("FULL" +"-"+nombreCarrera);
                        conteoTotalCarreraService.save(conteoTotalCarrera);
                        System.out.println("Entro en el igual CENTRO");
                        
                    } else {
                        
                            //conteoTotalCarrera = conteoTotalCarrera2;
                            conteoTotalCarrera.setBlanco_total(conteoTotalCarrera.getBlanco_total() + cant_voto_blanco);
                            conteoTotalCarrera.setNulo_total(conteoTotalCarrera.getNulo_total() + cant_voto_nulo);
                            conteoTotalCarrera.setVoto_valido_total(conteoTotalCarrera.getVoto_valido_total() + cant_voto_valido);
                            conteoTotalCarrera.setCarrera(nombreCarrera);
                            conteoTotalCarreraService.save(conteoTotalCarrera);
                            System.out.println("CREO un Nuevo CENTRO");
                        
                    }    
                }
                   
                /*for (ConteoTotalCarrera conteoTotalCarrera2 : listConteoCarrera) {

                    if (nombreFullCarrera.equals(conteoTotalCarrera2.getCarrera())) {
                        System.out.println("Encontrado Full Carrera");
                        conteoTotalCarrera = conteoTotalCarrera2;
                    }else{
                        System.out.println("No Encontrado Full Carrera");
                    }
                }*/

            }
            
            System.out.println(">>>>>>>>>>>>   "+listConteoCarrera.size());
            /*if (!nombreFullCarrera.equals(conteoTotalCarrera.getCarrera())) {
                conteoTotalCarrera.setBlanco_total(conteoTotalCarrera.getBlanco_total() + cant_voto_blanco);
                conteoTotalCarrera.setNulo_total(conteoTotalCarrera.getNulo_total() + cant_voto_nulo);
                conteoTotalCarrera.setVoto_valido_total(conteoTotalCarrera.getVoto_valido_total() + cant_voto_valido);
                conteoTotalCarrera.setCarrera("FULL" + "-" + nombreCarrera);
                conteoTotalCarreraService.save(conteoTotalCarrera);
                System.out.println("No entro en el igual FULL");
            } else {
                if (!nombreCarrera.equals(conteoTotalCarrera.getCarrera())) {
                    conteoTotalCarrera.setBlanco_total(conteoTotalCarrera.getBlanco_total() + cant_voto_blanco);
                    conteoTotalCarrera.setNulo_total(conteoTotalCarrera.getNulo_total() + cant_voto_nulo);
                    conteoTotalCarrera.setVoto_valido_total(conteoTotalCarrera.getVoto_valido_total() + cant_voto_valido);
                    conteoTotalCarrera.setCarrera(nombreCarrera);
                    conteoTotalCarreraService.save(conteoTotalCarrera);
                    System.out.println("No entro en el igual CENTRO");
                }
            }*/

            Anfora anfora = new Anfora();
            anfora.setCant_voto_blanco(cant_voto_blanco);
            anfora.setCant_voto_nulo(cant_voto_nulo);
            anfora.setCant_voto_valido(cant_voto_valido);
            anfora.setMesa(mesa);
            anfora.setConteo_total(conteoTotal);
            anfora.setConteo_total_carrera(conteoTotalCarrera);
            anforaService.save(anfora);

            List<VotoTotalFrente> listVotosTotalFrente = votoTotalFrenteService.findAll();
            List<VotoTotalCarrera> listVotosTotalCarrera = votoTotalCarreraService.findAll();

            for (int i = 0; i < id_frente.length; i++) {
                Long id_frentes = id_frente[i];
                int cantidadesVotantes = votoFrentes[i];

                Frente frente = frenteService.findOne(id_frentes);

                DetalleAnfora detalleAnfora = new DetalleAnfora();
                detalleAnfora.setAnfora(anfora);
                detalleAnfora.setFrente(frente);
                detalleAnfora.setCant_votantes(cantidadesVotantes);
                // Iterar sobre la lista de VotoTotalFrente
                boolean existeVotoTotalFrente = false;
                boolean existeVotoTotalCarrera = false;

                for (VotoTotalFrente votoTotalFrente2 : listVotosTotalFrente) {
                    // Verificar si ya existe un registro para el frente actual
                    if (votoTotalFrente2.getFrente().getId_frente() == id_frentes) {
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
                for (VotoTotalCarrera votoTotalCarrera2 : listVotosTotalCarrera) {
                    // Verificar si ya existe un registro para el frente actual
                    if (votoTotalCarrera2.getFrente().getId_frente() == id_frentes) {
                        // Actualizar el registro existente
                        votoTotalCarrera2.setVoto_total_frente(
                                votoTotalCarrera2.getVoto_total_frente() + detalleAnfora.getCant_votantes());
                        votoTotalCarrera2.setConteo_total_carrera(conteoTotalCarrera);
                        votoTotalCarrera2.setFrente(frente);
                        votoTotalCarreraService.save(votoTotalCarrera2);
                        detalleAnfora.setVoto_total_carrera(votoTotalCarrera2);
                        existeVotoTotalCarrera = true;
                        break; // Salir del bucle, ya se actualizó el registro
                    }
                }
                // Si no se encontró un registro existente, crear uno nuevo
                if (!existeVotoTotalCarrera) {
                    VotoTotalCarrera nuevoVotoTotalCarrera = new VotoTotalCarrera();
                    nuevoVotoTotalCarrera.setFrente(frente);
                    nuevoVotoTotalCarrera.setVoto_total_frente(detalleAnfora.getCant_votantes());
                    nuevoVotoTotalCarrera.setConteo_total_carrera(conteoTotalCarrera);

                    votoTotalCarreraService.save(nuevoVotoTotalCarrera);
                    detalleAnfora.setVoto_total_carrera(nuevoVotoTotalCarrera);

                }
                // Guardar el DetalleAnfora en la base de datos

                detalleAnforaService.save(detalleAnfora);
            }

            return "redirect:/admin/anfora";
        } else {
            return "redirect:/login";
        }
    }

}
