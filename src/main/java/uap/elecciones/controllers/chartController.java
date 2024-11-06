package uap.elecciones.controllers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import jakarta.servlet.http.HttpServletRequest;
import uap.elecciones.model.entity.Carrera;
import uap.elecciones.model.entity.Frente;
import uap.elecciones.model.entity.Usuario;
import uap.elecciones.model.service.IAnforaService;
import uap.elecciones.model.service.ICarreraService;
import uap.elecciones.model.service.IFacultadService;
import uap.elecciones.model.service.IFrenteService;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;



@Controller
public class chartController {

    @Autowired
    private IFacultadService facultadService;

    @Autowired
    private ICarreraService carreraService;

    @Autowired
    private IFrenteService frenteService;

    @Autowired
    private IAnforaService anforaService;
    
    @RequestMapping(value = "/resultados",method = RequestMethod.GET)
    private String getResultados(Model model) {


        Object[] resultado = (Object[]) anforaService.votosGeneral();
        List<Integer> datos = Arrays.asList(Integer.parseInt(resultado[2].toString()));
        Integer total = Integer.parseInt(resultado[3].toString());
        List<String> frentes = new ArrayList<>(); // Ejemplo de nombres de frentes
        for (Frente f : frenteService.findAll()) {
            frentes.add(f.getNombre_frente());
        }
        List<String> colores = Arrays.asList("#00FF00"); // Ejemplo de colores
        String nulos="Nulos", blancos="Blancos", validos="Válidos", emitidos="Emitidos", habiltiados="Habilitados";
        
        List<String> frentesTabla = new ArrayList<>(); // Ejemplo de datos de votos
        for (int i = 0; i < frentes.size(); i++) {
            frentesTabla.add(frentes.get(i));
        }
        frentesTabla.add(nulos);
        frentesTabla.add(blancos);
        frentesTabla.add(validos);
        frentesTabla.add(emitidos);
        frentesTabla.add(habiltiados);

        List<Integer> datosTabla = new ArrayList<>(); // Ejemplo de datos de votos

        for (int i = 0; i < datos.size(); i++) {
            datosTabla.add(datos.get(i));
        }
        datosTabla.add(Integer.parseInt(resultado[0].toString()));
        datosTabla.add(Integer.parseInt(resultado[1].toString()));
        datosTabla.add(Integer.parseInt(resultado[2].toString()));
        datosTabla.add(Integer.parseInt(resultado[4].toString()));
        datosTabla.add(Integer.parseInt(resultado[5].toString()));
        
    
    // double totalVotos = datosTabla.stream().mapToInt(Integer::intValue).sum();
    double totalVotos =  Integer.parseInt(resultado[3].toString());// Total Habilitados
    List<Double> porcentajes = datosTabla.stream().map(dato -> Math.round((dato / totalVotos) * 100 * 1000.0) / 1000.0) // Redondea a dos decimales
    .collect(Collectors.toList());
    // Añadir al modelo
    model.addAttribute("datos", datos);
    model.addAttribute("datosTabla", datosTabla);
    model.addAttribute("frentes", frentes);
    model.addAttribute("frentesTabla", frentesTabla);
    model.addAttribute("colores", colores);
    model.addAttribute("porcentajes", porcentajes);

    model.addAttribute("facultades", facultadService.findAll());
        return "chart";
    }
    
    @PostMapping("/cargarCarreras/{id_facultad}")
    public ResponseEntity<List<String[]>> cargarCarreras(@PathVariable("id_facultad")Long id_facultad) {
        
        List<String[]> listaCarrera = new ArrayList<>();
        List <Carrera> carreras = carreraService.listaCarrerasPorFacultad(id_facultad);

        for (Carrera c : carreras) {
            String[] car = { c.getId_carrera().toString(), c.getNombre_carrera()};
          
            listaCarrera.add(car);
        }
        return ResponseEntity.ok(listaCarrera);
    }

    @PostMapping("path")
    public String postMethodName(@RequestBody String entity) {
        // TODO: process POST request

        return entity;
    }
   
    
}
