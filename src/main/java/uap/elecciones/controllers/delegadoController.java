package uap.elecciones.controllers;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import uap.elecciones.model.entity.Delegado;
import uap.elecciones.model.entity.DelegadoDto;
import uap.elecciones.model.entity.Mesa;
import uap.elecciones.model.service.DelegadoService;
import uap.elecciones.model.service.IAsignacionHabilitadoService;
import uap.elecciones.model.service.IFacultadService;
import uap.elecciones.model.service.IMesaService;
import uap.elecciones.model.service.TipoDelegadoService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/delegado")
public class delegadoController {

    @Autowired
    private DelegadoService delegadoService;

    @Autowired
    private IMesaService mesaService;

    @Autowired
    private IFacultadService facultadService;

    @Autowired
    private TipoDelegadoService tipoDelegadoService;

    @Autowired
    private IAsignacionHabilitadoService habilitadoService;

    @GetMapping("/ventana")
    public String ventana(Model model, HttpServletRequest request) {

        
        if (request.getSession().getAttribute("admin") != null) {
            HttpSession session = request.getSession(false);
            session.invalidate();
        }
        model.addAttribute("facultades", facultadService.findAll());
        // model.addAttribute("mesas", mesaService.findAll());
        return "Delegado/mesa_delegado";
    }

    @GetMapping("/ventana/{admin}")
    public String ventana(Model model, @PathVariable(value = "admin") String admin, HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        session = request.getSession(true);
        session.setAttribute("admin", "true");
        model.addAttribute("facultades", facultadService.findAll());
        // model.addAttribute("mesas", mesaService.findAll());
        return "Delegado/mesa_delegado";
    }

    @GetMapping("/mesas")
    public ResponseEntity<List<Mesa>> listaMesas() {
        return ResponseEntity.ok(mesaService.listarMesasOrdenadas());
    }

    @PostMapping("/cargarMesasPorFacultad/{idFacultdad}")
    public ResponseEntity<String[][]> cargarMesasPorFacultad(@PathVariable(value = "idFacultdad") Long idFacultdad) {
        List<Mesa> mesas = mesaService.listarMesasPorIdFacultad(idFacultdad);
        String[][] materiaArray = new String[mesas.size()][2];
        int index = 0;
        for (Mesa mesa : mesas) {
            materiaArray[index][0] = String.valueOf(mesa.getId_mesa());
            materiaArray[index][1] = mesa.getNombre_mesa();
            index++;
        }
        return ResponseEntity.ok(materiaArray);
    }

    @GetMapping("/tablaDelegados/{idMesa}")
    public String ventana(Model model, @PathVariable(value = "idMesa") Long idMesa, HttpServletRequest request) {

        model.addAttribute("mesa", mesaService.findOne(idMesa));

        if (request.getSession().getAttribute("admin") != null) {
            String admin = String.valueOf(request.getSession().getAttribute("admin"));
            model.addAttribute("admin", admin);
        }

        // System.out.println("AAAAAAAAAAAAAAAAAAA");
        List<Object[]> delegados = habilitadoService.listarDelegadosPorMesa(idMesa);
        List<DelegadoDto> delegadosDto = delegados.stream()
                .map(arr -> new DelegadoDto(
                        (String) arr[0], // nombre_facultad
                        (String) arr[1], // nombre_carrera
                        (String) arr[2], // ru_rd
                        (String) arr[3], // tipo
                        (String) arr[4], // apellidos
                        (Long) arr[5], // id_persona
                        (String) arr[6], // nombre_mesa
                        (String) arr[7], // nombre_tipo_delegado
                        (Long) arr[8] // id_votante_habilitado
                ))
                .collect(Collectors.toList());

        model.addAttribute("delegados", delegadosDto);
        return "Delegado/tabla_registros";
    }

    // @GetMapping("/tablaDelegadosGeneral/{idMesa}")
    // public ResponseEntity<List<DelegadoDto>> tablaDelegadosGeneral(@PathVariable(value = "idMesa") Long idMesa,
    //         HttpServletRequest request) {

    //     // System.out.println("AAAAAAAAAAAAAAAAAAA");
    //     List<Object[]> delegados = habilitadoService.listarDelegadosPorMesa(idMesa);
    //     List<DelegadoDto> delegadosDto = delegados.stream()
    //             .map(arr -> new DelegadoDto(
    //                     (String) arr[0], // nombre_facultad
    //                     (String) arr[1], // nombre_carrera
    //                     (String) arr[2], // ru_rd
    //                     (String) arr[3], // tipo
    //                     (String) arr[4], // apellidos
    //                     (Long) arr[5], // id_persona
    //                     (String) arr[6], // nombre_mesa
    //                     (String) arr[7], // nombre_tipo_delegado
    //                     (Long) arr[8] // id_votante_habilitado
    //             ))
    //             .collect(Collectors.toList());

    //     return ResponseEntity.ok(delegadosDto);
    // }

    @GetMapping("/tablaDelegadosGeneral/{idMesa}")
    public String tablaDelegadosGeneral(Model model, @PathVariable(value = "idMesa") Long idMesa) {

        model.addAttribute("mesa", mesaService.findOne(idMesa));

        List<Object[]> delegados = habilitadoService.listarDelegadosPorMesa(idMesa);
        List<DelegadoDto> delegadosDto = delegados.stream()
                .map(arr -> new DelegadoDto(
                        (String) arr[0], // nombre_facultad
                        (String) arr[1], // nombre_carrera
                        (String) arr[2], // ru_rd
                        (String) arr[3], // tipo
                        (String) arr[4], // apellidos
                        (Long) arr[5], // id_persona
                        (String) arr[6], // nombre_mesa
                        (String) arr[7], // nombre_tipo_delegado
                        (Long) arr[8] // id_votante_habilitado
                ))
                .collect(Collectors.toList());

        model.addAttribute("delegados", delegadosDto);
        return "Delegado/tabla_registrosMesas";
    }
}
