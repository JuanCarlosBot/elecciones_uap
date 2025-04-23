package uap.elecciones.controllers;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

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
import uap.elecciones.model.entity.Delegado;
import uap.elecciones.model.entity.Docente;
import uap.elecciones.model.entity.Facultad;
import uap.elecciones.model.entity.Mesa;
import uap.elecciones.model.entity.TipoDelegado;
import uap.elecciones.model.entity.VotanteHabilitado;
import uap.elecciones.model.service.DelegadoService;
import uap.elecciones.model.service.IAsignacionHabilitadoService;
import uap.elecciones.model.service.ICarreraService;
import uap.elecciones.model.service.IDocenteService;
import uap.elecciones.model.service.IFacultadService;
import uap.elecciones.model.service.IMesaService;
import uap.elecciones.model.service.IVotanteHabilitadoService;
import uap.elecciones.model.service.TipoDelegadoService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequestMapping(value = "/admin")
public class sorteoController {

    @Autowired
    private IAsignacionHabilitadoService asignacionHabilitadoService;
    @Autowired
    private IVotanteHabilitadoService votanteHabilitadoService;
    @Autowired
    private IMesaService mesaService;
    @Autowired
    private ICarreraService carreraService;
    @Autowired
    private IFacultadService facultadService;
    @Autowired
    private IDocenteService docenteService;
    @Autowired
    private TipoDelegadoService tipoDelegadoService;
    @Autowired
    private DelegadoService delegadoService;

    @GetMapping(value = "/sorteo")
    public String sorteo(Model model) {
        model.addAttribute("mesas", mesaService.findAll());
        
        model.addAttribute("delegados", delegadoService.findAllOrderByMesaAndTipo());
        return "Sorteo/sorteo_general";
    }

    @PostMapping(value = "/sorteando")
    public String sorteandodelegados(@RequestParam(name = "id_mesa") Long idMesa, Model model) {
        Mesa mesa = mesaService.findOne(idMesa);
        Carrera carrera = mesa.getCarrera();
        List<Mesa> mesasCarrera = mesaService.findByCarrera(carrera.getId_carrera());
        // Obtener habilitados de todas las mesas de la carrera
        List<AsignacionHabilitado> habilitadosCarrera = asignacionHabilitadoService.findByMesas(mesasCarrera);
        // Habilitados de la misma mesa (solo para estudiantes si la mesa es estudiantil)
        List<AsignacionHabilitado> habilitadosMismaMesa = habilitadosCarrera.stream()
                .filter(a -> a.getMesa().getId_mesa().equals(idMesa))
                .collect(Collectors.toList());
        // Determinar si la mesa es de estudiantes o de docentes (según nombre o algún otro criterio)
        boolean esMesaEstudiantil = mesa.getNombre_mesa().toUpperCase().startsWith("E");
        // Separar estudiantes y docentes
        List<VotanteHabilitado> estudiantes = new ArrayList<>();
        List<VotanteHabilitado> docentes = new ArrayList<>();

        for (AsignacionHabilitado asignado : habilitadosCarrera) {
            VotanteHabilitado vh = asignado.getVotante_habilitado();
            if (vh.getDocente() != null) {
                docentes.add(vh); // Docentes de toda la carrera
            }
        }
        List<AsignacionHabilitado> fuenteEstudiantes = esMesaEstudiantil ? habilitadosMismaMesa : habilitadosCarrera;
        for (AsignacionHabilitado asignado : fuenteEstudiantes) {
            VotanteHabilitado vh = asignado.getVotante_habilitado();
            if (vh.getEstudiante() != null) {
                estudiantes.add(vh); // Estudiantes según lógica de la mesa
            }
        }
        // Excluir los que ya fueron sorteados
        List<Long> idsVotantesDelegados = delegadoService.findAll().stream().map(d -> d.getVotanteHabilitado().getId_votante_habilitado()).collect(Collectors.toList());

        docentes = docentes.stream().filter(vh -> !idsVotantesDelegados.contains(vh.getId_votante_habilitado())).collect(Collectors.toList());
        estudiantes = estudiantes.stream().filter(vh -> !idsVotantesDelegados.contains(vh.getId_votante_habilitado())).collect(Collectors.toList());

        // Tipos de delegado
        TipoDelegado presidenteTipo = tipoDelegadoService.findOne(1L);
        TipoDelegado delegadoDocenteTipo = tipoDelegadoService.findOne(2L);
        TipoDelegado delegadoEstudianteTipo = tipoDelegadoService.findOne(3L);

        Random random = new Random();
        List<Delegado> nuevosDelegados = new ArrayList<>();

        if (!docentes.isEmpty()) {
            VotanteHabilitado presidente = docentes.remove(random.nextInt(docentes.size()));
            nuevosDelegados.add(crearDelegado(mesa, presidente, presidenteTipo));
        }
        for (int i = 0; i < 2 && !docentes.isEmpty(); i++) {
            VotanteHabilitado vh = docentes.remove(random.nextInt(docentes.size()));
            nuevosDelegados.add(crearDelegado(mesa, vh, delegadoDocenteTipo));
        }
        for (int i = 0; i < 2 && !estudiantes.isEmpty(); i++) {
            VotanteHabilitado vh = estudiantes.remove(random.nextInt(estudiantes.size()));
            nuevosDelegados.add(crearDelegado(mesa, vh, delegadoEstudianteTipo));
        }
        delegadoService.saveAll(nuevosDelegados);
        model.addAttribute("mensaje", "Sorteo realizado correctamente.");
        return "redirect:/admin/sorteo";
    }

    private Delegado crearDelegado(Mesa mesa, VotanteHabilitado votante, TipoDelegado tipo) {
        Delegado delegado = new Delegado();
        delegado.setMesa(mesa);
        delegado.setVotanteHabilitado(votante);
        delegado.setTipoDelegado(tipo);
        return delegado;
    }

    /*@PostMapping(value = "/sorteando")
public String sorteandodelegados(@RequestParam(name = "id_mesa") Long idMesa, Model model) {
    Mesa mesa = mesaService.findOne(idMesa);
    Carrera carrera = mesa.getCarrera();
    List<Mesa> mesasCarrera = mesaService.findByCarrera(carrera.getId_carrera());
    List<AsignacionHabilitado> habilitadosCarrera = asignacionHabilitadoService.findByMesas(mesasCarrera);

    List<VotanteHabilitado> estudiantes = new ArrayList<>();
    List<VotanteHabilitado> docentes = new ArrayList<>();

    for (AsignacionHabilitado asignado : habilitadosCarrera) {
        VotanteHabilitado vh = asignado.getVotante_habilitado();
        if (vh.getEstudiante() != null) {
            estudiantes.add(vh);
        } else if (vh.getDocente() != null) {
            docentes.add(vh);
        }
    }
    TipoDelegado presidenteTipo = tipoDelegadoService.findOne(1L); // Presidente
    TipoDelegado delegadoDocenteTipo = tipoDelegadoService.findOne(2L);
    TipoDelegado delegadoEstudianteTipo = tipoDelegadoService.findOne(3L);

    Random random = new Random();
    List<Delegado> nuevosDelegados = new ArrayList<>();
    if (!docentes.isEmpty()) {
        VotanteHabilitado presidente = docentes.remove(random.nextInt(docentes.size()));
        nuevosDelegados.add(crearDelegado(mesa, presidente, presidenteTipo));
    }
    for (int i = 0; i < 2 && !docentes.isEmpty(); i++) {
        VotanteHabilitado vh = docentes.remove(random.nextInt(docentes.size()));
        nuevosDelegados.add(crearDelegado(mesa, vh, delegadoDocenteTipo));
    }
    for (int i = 0; i < 2 && !estudiantes.isEmpty(); i++) {
        VotanteHabilitado vh = estudiantes.remove(random.nextInt(estudiantes.size()));
        nuevosDelegados.add(crearDelegado(mesa, vh, delegadoEstudianteTipo));
    }
    delegadoService.saveAll(nuevosDelegados);
    model.addAttribute("mensaje", "Sorteo realizado correctamente.");
    return "redirect:/admin/sorteo";
}

    private Delegado crearDelegado(Mesa mesa, VotanteHabilitado votante, TipoDelegado tipo) {
        Delegado delegado = new Delegado();
        delegado.setMesa(mesa);
        delegado.setVotanteHabilitado(votante);
        delegado.setTipoDelegado(tipo);
        return delegado;
    }*/

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
                    List<AsignacionHabilitado> copiaLista = new ArrayList<>(list_ahb_mesa); // Copia para evitar
                                                                                            // modificaciones
                    System.out.println(tamañoLista);
                    if (tamañoLista >= 2) {
                        for (int i = 0; i < 2; i++) {
                            int indiceAleatorio = random.nextInt(tamañoLista);
                            AsignacionHabilitado seleccionado = copiaLista.remove(indiceAleatorio); // Eliminar para
                                                                                                    // evitar repetición
                            idsAleatorios.add(seleccionado);

                        }
                    } else if (tamañoLista < 2) {
                        for (int i = 0; i < tamañoLista; i++) {
                            int indiceAleatorio = random.nextInt(tamañoLista);
                            AsignacionHabilitado seleccionado = copiaLista.remove(indiceAleatorio); // Eliminar para
                                                                                                    // evitar repetición
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
                        System.out.println(asignacionHabilitado2.getId_asignacion_habilitado() + "...");
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
                int c = 1;
                for (Object[] ob : list_ahb_mesa) {
                    Long idAH = (long) ob[0];
                    AsignacionHabilitado ah = asignacionHabilitadoService.findOne(idAH);
                    listaAsgHabilitados.add(ah);
                }
                List<AsignacionHabilitado> idsAleatorios = new ArrayList<>();
                int tamañoLista = list_ahb_mesa.size();
                System.out.println(tamañoLista);
                List<AsignacionHabilitado> copiaLista = new ArrayList<>(listaAsgHabilitados); // Copia para evitar
                                                                                              // modificaciones

                if (tamañoLista >= 3) {
                    for (int i = 0; i < 3; i++) {
                        int indiceAleatorio = random.nextInt(copiaLista.size());
                        AsignacionHabilitado seleccionado = copiaLista.remove(indiceAleatorio); // Eliminar para evitar
                                                                                                // repetición
                        idsAleatorios.add(seleccionado);
                    }
                } else if (tamañoLista < 3) {
                    for (int i = 0; i < tamañoLista; i++) {
                        int indiceAleatorio = random.nextInt(copiaLista.size());
                        AsignacionHabilitado seleccionado = copiaLista.remove(indiceAleatorio); // Eliminar para evitar
                                                                                                // repetición
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
                    System.out.println(asignacionHabilitado2.getId_asignacion_habilitado() + "...");
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
