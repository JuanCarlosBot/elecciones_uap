package uap.elecciones.controllers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

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
    private SpringTemplateEngine templateEngine;

    @Autowired
    private IFacultadService facultadService;

    @Autowired
    private ICarreraService carreraService;

    @Autowired
    private IFrenteService frenteService;

    @Autowired
    private IAnforaService anforaService;

    @RequestMapping(value = "/resultados", method = RequestMethod.GET)
    private String getResultados(Model model) {

        Object[] resultado = (Object[]) anforaService.votosGeneral();
        List<Integer> datos = Arrays.asList(Integer.parseInt(resultado[2].toString()));
        List<String> frentes = new ArrayList<>(); // Ejemplo de nombres de frentes
        for (Frente f : frenteService.findAll()) {
            frentes.add(f.getNombre_frente());
        }
        List<String> colores = Arrays.asList("#00FF00"); // Ejemplo de colores
        String nulos = "Nulos", blancos = "Blancos", validos = "Válidos", emitidos = "Emitidos",
                habiltiados = "Habilitados";

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
        double totalVotos = Integer.parseInt(resultado[5].toString());// Total Habilitados
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

        model.addAttribute("facultades", facultadService.findAll());
        return "chart";
    }

    @PostMapping("/cargarCarreras/{id_facultad}")
    public ResponseEntity<List<String[]>> cargarCarreras(@PathVariable("id_facultad") Long id_facultad) {

        List<String[]> listaCarrera = new ArrayList<>();
        List<Carrera> carreras = carreraService.listaCarrerasPorFacultad(id_facultad);

        for (Carrera c : carreras) {
            String[] car = { c.getId_carrera().toString(), c.getNombre_carrera() };

            listaCarrera.add(car);
        }
        return ResponseEntity.ok(listaCarrera);
    }

    @PostMapping("/cargarDatosEstDoc/{sigla}/{isNul}")
    public ResponseEntity<Map<String, Object>> cargarDatosEstDoc(@PathVariable("sigla") String sigla,
            @PathVariable("isNul") Boolean isNul) {

        // Generación de los datos
        Object[] resultado = (Object[]) anforaService.votosGenerales(isNul, sigla);
        List<Integer> datos = Arrays.asList(Integer.parseInt(resultado[2].toString()));
        List<String> frentes = new ArrayList<>();
        for (Frente f : frenteService.findAll()) {
            frentes.add(f.getNombre_frente());
        }
        List<String> colores = Arrays.asList("#00FF00");

        String nulos = "Nulos", blancos = "Blancos", validos = "Válidos", emitidos = "Emitidos",
                habilitados = "Habilitados";

        List<String> frentesTabla = new ArrayList<>(frentes);
        frentesTabla.addAll(Arrays.asList(nulos, blancos, validos, emitidos, habilitados));

        List<Integer> datosTabla = new ArrayList<>(datos);
        datosTabla.add(Integer.parseInt(resultado[0].toString()));
        datosTabla.add(Integer.parseInt(resultado[1].toString()));
        datosTabla.add(Integer.parseInt(resultado[2].toString()));
        datosTabla.add(Integer.parseInt(resultado[4].toString()));
        datosTabla.add(Integer.parseInt(resultado[5].toString()));

        double totalVotos = Integer.parseInt(resultado[5].toString());
        List<Double> porcentajes = datosTabla.stream()
                .map(dato -> Math.round((dato / totalVotos) * 100 * 1000.0) / 1000.0)
                .collect(Collectors.toList());

        // Construir el HTML de la tabla
        StringBuilder htmlTabla = new StringBuilder();
        htmlTabla.append("<h3 class='text-center'>Tabla de Resultados</h3>");
        htmlTabla.append("<table class='table table-striped'>");
        htmlTabla.append("<thead><tr><th>Frente</th><th>Votos</th><th>%</th></tr></thead>");
        htmlTabla.append("<tbody>");

        for (int i = 0; i < datosTabla.size(); i++) {
            htmlTabla.append("<tr>");
            htmlTabla.append("<td>").append(frentesTabla.get(i)).append("</td>");
            htmlTabla.append("<td>").append(datosTabla.get(i)).append("</td>");
            htmlTabla.append("<td>").append(porcentajes.get(i)).append("</td>");
            htmlTabla.append("</tr>");
        }

        htmlTabla.append("</tbody>");
        htmlTabla.append("</table>");

        // Datos para el gráfico
        List<Map<String, Object>> chartData = new ArrayList<>();
        for (int i = 0; i < frentes.size(); i++) {
            Map<String, Object> dataPoint = new HashMap<>();
            dataPoint.put("frente", frentes.get(i));
            dataPoint.put("votos", datos.get(i));
            dataPoint.put("color", colores.get(i % colores.size()));
            chartData.add(dataPoint);
        }

        // Crear la respuesta JSON
        Map<String, Object> response = new HashMap<>();
        response.put("html", htmlTabla.toString()); // HTML de la tabla generado
        response.put("chartData", chartData); // Datos para el gráfico
        response.put("totalHabilitados", Integer.parseInt(resultado[5].toString())); // Total de Habilitados

        return ResponseEntity.ok(response); // Devuelve la respuesta como JSON
    }
    

}
