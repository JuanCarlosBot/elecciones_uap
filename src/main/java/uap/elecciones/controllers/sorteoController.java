package uap.elecciones.controllers;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletRequest;
import uap.elecciones.model.entity.AsignacionHabilitado;
import uap.elecciones.model.entity.Carrera;
import uap.elecciones.model.entity.Facultad;
import uap.elecciones.model.entity.Mesa;
import uap.elecciones.model.service.IAsignacionHabilitadoService;
import uap.elecciones.model.service.ICarreraService;
import uap.elecciones.model.service.IFacultadService;
import uap.elecciones.model.service.IMesaService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;


@Controller
@RequestMapping(value = "/admin")
public class sorteoController {

    @Autowired
    private IAsignacionHabilitadoService asignacionHabilitadoService;

    @Autowired
    private IMesaService mesaService;

    @Autowired
    private ICarreraService carreraService;

    @Autowired
    private IFacultadService facultadService;

    @GetMapping(value = "sorteo")
    public String sorteo(Model model) {
        model.addAttribute("mesas", mesaService.findAll());
        return "Sorteo/sorteo_general";
    }
    @PostMapping(value = "sorteando")
    public String sorteando(@RequestParam("id_mesa")Long id_mesa, Model model) {
        Object carre = mesaService.mesaPorCarrera(id_mesa);
            Object[] carreraArray = (Object[]) carre;
            Long idMesa = (Long) carreraArray[0];
            Long idCarrera = (Long) carreraArray[4];
            Carrera carrera = carreraService.findOne(idCarrera);
            Facultad facultad = facultadService.findOne(carrera.getFacultad().getId_facultad());
            List<AsignacionHabilitado> asHabilitados = asignacionHabilitadoService.listaHabilitadosMesas(id_mesa);

            for (AsignacionHabilitado asignacionHabilitado : asHabilitados) {
                System.out.println("cantidad votantes estudiantes "+asHabilitados.size());
            }
            

        return "Sorteo/sorteo_general";
    }
    

    @RequestMapping(value = "/form_sorteo", method = RequestMethod.POST)
    public String realizar_sorteo(RedirectAttributes flash,
            @RequestParam(name = "ids_lista_asignados", required = false) String ids_lista_asignados,
            @RequestParam(name = "id_car") Long id_car,
            @RequestParam(name = "id_mesa") Long id_mesa, HttpServletRequest request) {
        if (request.getSession().getAttribute("usuario") != null) {

            if (ids_lista_asignados != null && !ids_lista_asignados.isEmpty()) {
                try {
                    ObjectMapper mapper = new ObjectMapper();
                    Long[] id_asignacion_habilitado = mapper.readValue(ids_lista_asignados, Long[].class);

                    Random random = new Random();
                    List<AsignacionHabilitado> ahb = new ArrayList<>();
                    List<AsignacionHabilitado> list_ahb_mesa = new ArrayList<>();
                    List<AsignacionHabilitado> idsAleatorios = new ArrayList<>();
                    for (AsignacionHabilitado ah : asignacionHabilitadoService.findAll()) {
                        for (Long ids : id_asignacion_habilitado) {
                            if (ah.getId_asignacion_habilitado().equals(ids)) {
                                ahb.add(ah);
                                // System.out.println(ah.getId_asignacion_habilitado());
                            }
                        }
                    }

                    for (AsignacionHabilitado asignacionHabilitado : ahb) {
                        if (asignacionHabilitado.getMesa().getId_mesa() == id_mesa) {
                            list_ahb_mesa.add(asignacionHabilitado);

                        }
                    }

                    int tamañoLista = list_ahb_mesa.size();
                    List<AsignacionHabilitado> copiaLista = new ArrayList<>(list_ahb_mesa); // Copia para evitar modificaciones
                    System.out.println(tamañoLista);
                    if(tamañoLista>=5){
                    for (int i = 0; i < 5; i++) {
                        int indiceAleatorio = random.nextInt(tamañoLista);
                        AsignacionHabilitado seleccionado = copiaLista.remove(indiceAleatorio); // Eliminar para evitar repetición
                        idsAleatorios.add(seleccionado);

                    }
                    }else if(tamañoLista<5){
                        for (int i = 0; i < tamañoLista; i++) {
                            int indiceAleatorio = random.nextInt(tamañoLista);
                            AsignacionHabilitado seleccionado = copiaLista.remove(indiceAleatorio); // Eliminar para evitar repetición
                            idsAleatorios.add(seleccionado);
    
                        }
                    }
                    for (AsignacionHabilitado asignacionHabilitado : idsAleatorios) {
                        System.out.println(asignacionHabilitado.getId_asignacion_habilitado());
                        AsignacionHabilitado asignacionHabilitado2 = asignacionHabilitadoService
                                .findOne(asignacionHabilitado.getId_asignacion_habilitado());
                        asignacionHabilitado2.setDelegado("Delegado");
                        Mesa m = mesaService.findOne(asignacionHabilitado2.getMesa().getId_mesa());
                        m.setEstado("OD");
                        System.out.println(asignacionHabilitado2.getId_asignacion_habilitado()+"...");
                        mesaService.save(m);
                        asignacionHabilitadoService.save(asignacionHabilitado2);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            return "redirect:/admin/seleccion_est_carrera/" + id_car;
        } else {
            return "redirect:/login";
        }
    }

    @RequestMapping(value = "/form_sorteo_docente", method = RequestMethod.POST)
    public String realizar_sorteo_docente(RedirectAttributes flash,
            @RequestParam(name = "ids_lista_asignados", required = false) String ids_lista_asignados,
            @RequestParam(name = "id_car") Long id_car,
            @RequestParam(name = "id_mesa") Long id_mesa, HttpServletRequest request) {
        System.out.println(id_car + " - " + id_mesa);
        if (request.getSession().getAttribute("usuario") != null) {

            try {
                Random random = new Random();
                List<AsignacionHabilitado> listaAsgHabilitados = new ArrayList<>();
                List<Object[]> list_ahb_mesa = asignacionHabilitadoService.lista_asignacion_por_mesa(id_car);
                int c=1;
                for (Object[] ob : list_ahb_mesa) {
                    Long idAH = (long) ob[0];
                    AsignacionHabilitado ah = asignacionHabilitadoService.findOne(idAH);
                    listaAsgHabilitados.add(ah);
                }
                List<AsignacionHabilitado> idsAleatorios = new ArrayList<>();
                int tamañoLista = list_ahb_mesa.size();
                    System.out.println(tamañoLista);
                    List<AsignacionHabilitado> copiaLista = new ArrayList<>(listaAsgHabilitados); // Copia para evitar modificaciones

                    if(tamañoLista>=5){
                        for (int i = 0; i < 5; i++) {
                            int indiceAleatorio = random.nextInt(copiaLista.size());
                            AsignacionHabilitado seleccionado = copiaLista.remove(indiceAleatorio); // Eliminar para evitar repetición
                            idsAleatorios.add(seleccionado);
                        }
                    }else if(tamañoLista<5){
                        for (int i = 0; i < tamañoLista; i++) {
                            int indiceAleatorio = random.nextInt(copiaLista.size());
                            AsignacionHabilitado seleccionado = copiaLista.remove(indiceAleatorio); // Eliminar para evitar repetición
                            idsAleatorios.add(seleccionado);
                        }
                    }


                    for (AsignacionHabilitado asignacionHabilitado : idsAleatorios) {
                        System.out.println(asignacionHabilitado.getId_asignacion_habilitado());
                        AsignacionHabilitado asignacionHabilitado2 = asignacionHabilitadoService
                                .findOne(asignacionHabilitado.getId_asignacion_habilitado());
                        asignacionHabilitado2.setDelegado("Delegado");
                        Mesa m = mesaService.findOne(asignacionHabilitado2.getMesa().getId_mesa());
                        m.setEstado("OD");
                        System.out.println(asignacionHabilitado2.getId_asignacion_habilitado()+"...");
                        mesaService.save(m);
                        asignacionHabilitadoService.save(asignacionHabilitado2);
                    }

            } catch (Exception e) {
                e.printStackTrace();
            }

            return "redirect:/admin/seleccion_estudiantes/" + id_car;
        } else {
            return "redirect:/login";
        }
    }
}
