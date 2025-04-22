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

    // Obtener todas las mesas de la carrera
    List<Mesa> mesasCarrera = mesaService.findByCarrera(carrera.getId_carrera());

    // Obtener todos los habilitados de esas mesas
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

    // Obtener tipos de delegado
    TipoDelegado presidenteTipo = tipoDelegadoService.findOne(1L); // Presidente
    TipoDelegado delegadoDocenteTipo = tipoDelegadoService.findOne(2L);
    TipoDelegado delegadoEstudianteTipo = tipoDelegadoService.findOne(3L);

    Random random = new Random();
    List<Delegado> nuevosDelegados = new ArrayList<>();

    // Elegir presidente (docente)
    if (!docentes.isEmpty()) {
        VotanteHabilitado presidente = docentes.remove(random.nextInt(docentes.size()));
        nuevosDelegados.add(crearDelegado(mesa, presidente, presidenteTipo));
    }

    // 2 delegados docentes
    for (int i = 0; i < 2 && !docentes.isEmpty(); i++) {
        VotanteHabilitado vh = docentes.remove(random.nextInt(docentes.size()));
        nuevosDelegados.add(crearDelegado(mesa, vh, delegadoDocenteTipo));
    }

    // 2 delegados estudiantes
    for (int i = 0; i < 2 && !estudiantes.isEmpty(); i++) {
        VotanteHabilitado vh = estudiantes.remove(random.nextInt(estudiantes.size()));
        nuevosDelegados.add(crearDelegado(mesa, vh, delegadoEstudianteTipo));
    }

    delegadoService.saveAll(nuevosDelegados);

    model.addAttribute("mensaje", "Sorteo realizado correctamente.");
    return "redirect:/admin/sorteo";
}

/*
    @PostMapping(value = "/sorteando")
    public String sorteandoDelegados(@RequestParam(name = "id_mesa") Long idMesa, Model model) {
        System.out.println(idMesa+" iddddddddddd");
        Mesa mesa = mesaService.findOne(idMesa); // Asegúrate de tener este método en tu servicio
        System.out.println(mesa.getNombre_mesa());
        List<AsignacionHabilitado> habilitados = asignacionHabilitadoService.lista_asignados_habilitados_por_mesa(idMesa);
        System.out.println(habilitados.size());

        // Separar habilitados entre estudiantes y docentes
        List<VotanteHabilitado> estudiantes = new ArrayList<>();
        List<VotanteHabilitado> docentes = new ArrayList<>();

        for (AsignacionHabilitado asignado : habilitados) {
            VotanteHabilitado vh = asignado.getVotante_habilitado();
            if (vh.getEstudiante() != null) {
                estudiantes.add(vh);
            } else if (vh.getDocente() != null) {
                docentes.add(vh);
            }
        }

        // Obtener tipos de delegado
        TipoDelegado presidenteTipo = tipoDelegadoService.findOne(1L); // Presidente
        TipoDelegado delegadoDocenteTipo = tipoDelegadoService.findOne(2L);
        TipoDelegado delegadoEstudianteTipo = tipoDelegadoService.findOne(3L);

        Random random = new Random();
        List<Delegado> nuevosDelegados = new ArrayList<>();

        // Elegir presidente (de los docentes)
        if (!docentes.isEmpty()) {
            VotanteHabilitado presidente = docentes.remove(random.nextInt(docentes.size()));
            nuevosDelegados.add(crearDelegado(mesa, presidente, presidenteTipo));
        }

        // Elegir 2 delegados docentes
        for (int i = 0; i < 2 && !docentes.isEmpty(); i++) {
            VotanteHabilitado vh = docentes.remove(random.nextInt(docentes.size()));
            nuevosDelegados.add(crearDelegado(mesa, vh, delegadoDocenteTipo));
        }

        // Elegir 2 delegados estudiantes
        for (int i = 0; i < 2 && !estudiantes.isEmpty(); i++) {
            VotanteHabilitado vh = estudiantes.remove(random.nextInt(estudiantes.size()));
            nuevosDelegados.add(crearDelegado(mesa, vh, delegadoEstudianteTipo));
        }

        // Guardar delegados
        delegadoService.saveAll(nuevosDelegados);

        model.addAttribute("mensaje", "Sorteo realizado correctamente.");
        return "redirect:/admin/sorteo"; // Ajusta según tu vista
    }
*/
    // Método auxiliar para crear delegados
    private Delegado crearDelegado(Mesa mesa, VotanteHabilitado votante, TipoDelegado tipo) {
        Delegado delegado = new Delegado();
        delegado.setMesa(mesa);
        delegado.setVotanteHabilitado(votante);
        delegado.setTipoDelegado(tipo);
        return delegado;
    }

    @PostMapping(value = "/sorteandoss")
    public String sorteando(@RequestParam(name = "id_mesa") Long id_mesa, Model model) {
        Object carre = mesaService.mesaPorCarrera(id_mesa);
        Object[] carreraArray = (Object[]) carre;
        Long idMesa = (Long) carreraArray[0];
        Mesa mesa = mesaService.findOne(idMesa);
        Long idCarrera = (Long) carreraArray[4];
        Carrera carrera = carreraService.findOne(idCarrera);
        Facultad facultad = facultadService.findOne(carrera.getFacultad().getId_facultad());

        List<AsignacionHabilitado> asHabilitados = asignacionHabilitadoService.listaHabilitadosMesas(id_mesa);
        List<Long> idVH = asignacionHabilitadoService.listaDocentesFac(facultad.getId_facultad());

        System.out.println("cantidad votantes estudiantes " + asHabilitados.size());
        System.out.println("cantidad de mesas por fac " + idVH.size());
        List<VotanteHabilitado> vhb = new ArrayList<>();
        try {
            for (Long long1 : idVH) {
                VotanteHabilitado v = votanteHabilitadoService.findOne(long1);
                boolean esDelegado = false;

                for (AsignacionHabilitado ahh : v.getAsignacion_habilitado()) {
                    if ("Delegado".equals(ahh.getDelegado())) {
                        esDelegado = true;
                        break; // Si encontramos un "Delegado", dejamos de buscar en esta lista
                    }
                }
                // Si no encontramos "Delegado", agregamos el votante a la lista
                if (!esDelegado) {
                    if (!"DelegadoOficial".equals(v.getEstado_delegado())) {
                        vhb.add(v);
                    }
                    
                }
            }
            Random random = new Random();
            List<VotanteHabilitado> idsAleatorios = new ArrayList<>();

            int tamañoLista = vhb.size();
            System.out.println("sin sortear  " + tamañoLista);
            List<VotanteHabilitado> copiaLista = new ArrayList<>(vhb); // Copia para evitar modificaciones

            if (tamañoLista >= 3) {
                for (int i = 0; i < 3; i++) {
                    int indiceAleatorio = random.nextInt(tamañoLista);
                    VotanteHabilitado seleccionado = copiaLista.remove(indiceAleatorio); // Eliminar para evitar repetición
                    idsAleatorios.add(seleccionado);

                }
            } else if (tamañoLista < 3) {
                for (int i = 0; i < tamañoLista; i++) {
                    int indiceAleatorio = random.nextInt(tamañoLista);
                    VotanteHabilitado seleccionado = copiaLista.remove(indiceAleatorio); // Eliminar para evitar repetición
                    idsAleatorios.add(seleccionado);
                }
            }
            List<TipoDelegado> tipoDelegados = tipoDelegadoService.findAll();
            
            
            List<VotanteHabilitado> vexistente = new ArrayList<>();
            List<Delegado> nuevos = new ArrayList<>();
            for (AsignacionHabilitado asignacionHabilitado : mesa.getAsignacionHabilitados()) {
                if ("Delegado".equals(asignacionHabilitado.getDelegado()) || "v".equals(asignacionHabilitado.getDelegado())) {
                    vexistente.add(asignacionHabilitado.getVotante_habilitado());
                    Delegado dele = new Delegado();
                    dele.setVotanteHabilitado(asignacionHabilitado.getVotante_habilitado());
                    dele.setMesa(mesa);
                    dele.setTipoDelegado(tipoDelegados.get(2));
                    delegadoService.save(dele);
                    System.out.println("delegados estudiantes  : "+asignacionHabilitado.getVotante_habilitado().getEstudiante().getPersona().getApellidos());
                }
                
            }
            
            for (VotanteHabilitado vh : idsAleatorios) {
                

                Delegado delegado = new Delegado();
                delegado.setVotanteHabilitado(vh);
                delegado.setMesa(mesa);
                delegado.setTipoDelegado(tipoDelegados.get(1));
                delegadoService.save(delegado);
                VotanteHabilitado votHab = votanteHabilitadoService.findOne(vh.getId_votante_habilitado());
                votHab.setEstado_delegado("DelegadoOficial");
                votanteHabilitadoService.save(votHab);
                System.out.println(delegado.getMesa().getNombre_mesa() + " "
                        + delegado.getVotanteHabilitado().getDocente().getPersona().getApellidos() + " "
                        + delegado.getTipoDelegado().getNombre_tipo_delegado());
                      
            }
            

        } catch (Exception e) {
            e.printStackTrace();
        }

        return "redirect:/admin/sorteo";
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
