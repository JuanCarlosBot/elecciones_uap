package uap.elecciones.controllers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
public class chartController {
    
    @RequestMapping(value = "/resultados",method = RequestMethod.GET)
    private String getResultados(Model model) {
        List<Integer> datos = Arrays.asList(3500, 5500, 150); // Ejemplo de datos de votos
        List<String> frentes = Arrays.asList("MNR", "MAS", "RENOVACION"); // Ejemplo de nombres de frentes
        List<String> colores = Arrays.asList("#ff0000", "#0000ff", "#00ff00"); // Ejemplo de colores
        int nulo=10, blanco=45, valido=5700, emitido=590, habiltiado=7400;
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
        datosTabla.add(nulo);
        datosTabla.add(blanco);
        datosTabla.add(valido);
        datosTabla.add(emitido);
        datosTabla.add(habiltiado);
        
    
    double totalVotos = datos.stream().mapToInt(Integer::intValue).sum();
    List<Double> porcentajes = datosTabla.stream()
    .map(dato -> Math.round((dato / totalVotos) * 100 * 1000.0) / 1000.0) // Redondea a dos decimales
    .collect(Collectors.toList());
    // Añadir al modelo
    model.addAttribute("datos", datos);
    model.addAttribute("datosTabla", datosTabla);
    model.addAttribute("frentes", frentes);
    model.addAttribute("frentesTabla", frentesTabla);
    model.addAttribute("colores", colores);
    model.addAttribute("porcentajes", porcentajes);
        return "chart";
    }
    
}
