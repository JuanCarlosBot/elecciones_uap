package uap.elecciones.controllers;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import uap.elecciones.model.entity.Asistencia;
import uap.elecciones.model.entity.Docente;
import uap.elecciones.model.entity.DocenteDto;
import uap.elecciones.model.entity.Estudiante;
import uap.elecciones.model.entity.EstudianteDto;
import uap.elecciones.model.entity.Mesa;
import uap.elecciones.model.service.AsistenciaService;
import uap.elecciones.model.service.IDocenteService;
import uap.elecciones.model.service.IEstudianteService;
import uap.elecciones.model.service.IPersonaService;

@Controller
@RequestMapping("/asistencia")
public class AsistenciaController {

    @Autowired
    private IDocenteService docenteService;

    @Autowired
    private IEstudianteService estudianteService;

    @Autowired
    private AsistenciaService asistenciaService;

    @Autowired
    private IPersonaService personaService;

    @GetMapping("/ventana")
    public String ventana(Model model, HttpServletRequest request) {

        return "Asistencia/vista";
    }

    // @PostMapping("/buscarDocenteCi/{ci}")
    // public ResponseEntity<String[][]> modalInfo(Model model, HttpServletRequest
    // request,
    // @PathVariable("ci") String ci) {
    // // List<Docente> listaDocentes = docenteService.listarDocentePorCI(ci);
    // String[][] listaPersona = new String[listaDocentes.size()][2];
    // // int index = 0;
    // // for (Docente docente : listaDocentes) {
    // // listaPersona[index][0] = String.valueOf(docente.getId_docente());
    // // listaPersona[index][1] = docente.getPersona().getApellidos();
    // // index++;
    // // }
    // return ResponseEntity.ok(listaPersona);
    // }

    @GetMapping("buscador/personaCi/{ci}")
    public String buscarPersonaCi(@PathVariable(value = "ci", required = false) String ci, ModelMap model) {
        // System.out.println("des=" + ci);
        List<Object[]> results = docenteService.listarDocentePorCI(ci);
        List<DocenteDto> docentes = new ArrayList<>();
        for (Object[] row : results) {
            DocenteDto docenteDto = new DocenteDto(
                    (Long) row[0], // idDocente
                    (String) row[1], // rd
                    (String) row[2], // nombreCompleto
                    (String) row[3], // ci
                    (Long) row[4] // idPersona
            );
            docentes.add(docenteDto);
        }
        model.addAttribute("personas", docentes);
        return "Asistencia/content";
    }

    @GetMapping("buscador/personaCiEst/{ru}")
    public String personaCiEst(@PathVariable(value = "ru", required = false) String ru, ModelMap model) {
        // System.out.println("des=" + ci);
        List<Object[]> results = estudianteService.listarEstudiantePorRU(ru);
        List<EstudianteDto> estudianteDtos = new ArrayList<>();
        for (Object[] row : results) {
            EstudianteDto estudianteDto = new EstudianteDto(
                    (Long) row[0], // idEstudiante
                    (String) row[1], // ru
                    (String) row[2], // nombreCompleto
                    (String) row[3], // ci
                    (Long) row[4] // idPersona
            );
            estudianteDtos.add(estudianteDto);
        }
        model.addAttribute("personas", estudianteDtos);
        return "Asistencia/contentEst";
    }

    @PostMapping("/buscar")
    public String modalInfo(Model model, HttpServletRequest request,
            @RequestParam("tipo") String tipo,
            @RequestParam("codigo") String codigo) {

        if (tipo.equals("DOCENTE")) {
            Docente docente = docenteService.buscarDocentePorRd(codigo);
            model.addAttribute("person", docente);
            Asistencia asistencia = asistenciaService.asistenciaPorRdDocente(codigo);
            if (asistencia != null) {
                model.addAttribute("asistencia", asistencia);
            }
        }
        if (tipo.equals("ESTUDIANTE")) {
            Estudiante estudiante = estudianteService.buscarEstudiantePorRu(codigo);
            model.addAttribute("person", estudiante);
            Asistencia asistencia = asistenciaService.asistenciaPorRuEstudiante(codigo);
            if (asistencia != null) {
                model.addAttribute("asistencia", asistencia);
            }
        }

        return "Asistencia/modalInfo";
    }

    @PostMapping("/buscarDocenteModal/{id}")
    public String buscarDocenteModal(Model model, HttpServletRequest request,
            @PathVariable("id") Long id) {

        Docente docente = docenteService.buscarDocentePorIdPersona(id);
        model.addAttribute("person", docente);
        Asistencia asistencia = asistenciaService.asistenciaPorRdDocente(docente.getRd());
        if (asistencia != null) {
            model.addAttribute("asistencia", asistencia);
        }
        model.addAttribute("tipo", "DOCENTE");

        return "Asistencia/modalInfo";
    }

    @PostMapping("/buscarEstudianteModal/{ru}")
    public String buscarEstudianteModal(Model model, HttpServletRequest request,
            @PathVariable("ru") String ru) {

        Estudiante estudiante = estudianteService.buscarEstudiantePorRu(ru);
        model.addAttribute("person", estudiante);
        Asistencia asistencia = asistenciaService.asistenciaPorRuEstudiante(estudiante.getRu());
        if (asistencia != null) {
            model.addAttribute("asistencia", asistencia);
        }
        model.addAttribute("tipo", "ESTUDIANTE");

        return "Asistencia/modalInfo";
    }

    @PostMapping("/marcarAsistencia/{codigo}/{tipo}")
    public ResponseEntity<String> marcarAsistencia(Model model, HttpServletRequest request,
            @PathVariable("codigo") String codigo, @PathVariable("tipo") String tipo) {

        if (tipo.equals("DOCENTE")) {
            Docente docente = docenteService.buscarDocentePorRd(codigo);
            model.addAttribute("person", docente);
            Asistencia asistencia = asistenciaService.asistenciaPorRdDocente(codigo);
            if (asistencia == null) {
                asistencia= new Asistencia();
                asistencia.setDocente(docente);
                asistencia.setEstado("MARCADO");
                asistencia.setFechaRegistro(new Date());
                asistenciaService.save(asistencia);
            }
        }
        if (tipo.equals("ESTUDIANTE")) {
            Estudiante estudiante = estudianteService.buscarEstudiantePorRu(codigo);
            model.addAttribute("person", estudiante);
            Asistencia asistencia = asistenciaService.asistenciaPorRuEstudiante(codigo);
            if (asistencia == null) {
                asistencia= new Asistencia();
                asistencia.setEstudiante(estudiante);
                asistencia.setEstado("MARCADO");
                asistencia.setFechaRegistro(new Date());
                asistenciaService.save(asistencia);
            }
        }

        return ResponseEntity.ok("Asistencia Marcada");
    }
}
