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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.thymeleaf.spring6.SpringTemplateEngine;

import uap.elecciones.model.entity.Acta;
import uap.elecciones.model.entity.Carrera;
import uap.elecciones.model.entity.Frente;
import uap.elecciones.model.entity.Mesa;
import uap.elecciones.model.service.ActaService;
import uap.elecciones.model.service.IAnforaService;
import uap.elecciones.model.service.ICarreraService;
import uap.elecciones.model.service.IFacultadService;
import uap.elecciones.model.service.IFrenteService;
import uap.elecciones.model.service.IMesaService;


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

    @Autowired
    private IMesaService mesaService;

    @Autowired
    private ActaService actaService;

    @RequestMapping(value = "/resultados", method = RequestMethod.GET)
    private String getResultados(Model model) {

        // Object[] resultado = (Object[]) anforaService.votosGeneral();
        // List<Integer> datos = Arrays.asList(Integer.parseInt(resultado[2].toString()),
        //         Integer.parseInt(resultado[0].toString()), Integer.parseInt(resultado[1].toString()));
        List<String> frentes = new ArrayList<>(); // Ejemplo de nombres de frentes
        for (Frente f : frenteService.findAll()) {
            frentes.add(f.getNombre_frente());
        }
        frentes.add("NULOS");
        frentes.add("BLANCOS");
        List<String> colores = Arrays.asList("#008000", "#dedede", "#f1f592"); // Ejemplo de colores
        String nulos = "Nulos", blancos = "Blancos", validos = "Válidos", emitidos = "Emitidos",
                habiltiados = "Habilitados";

        List<Mesa> mesas = mesaService.findAll();
        List<String> frentesTabla = new ArrayList<>(); // Ejemplo de datos de votos
        for (int i = 0; i < frentes.size(); i++) {
            frentesTabla.add(frentes.get(i));
        }
        // frentesTabla.add(nulos);
        // frentesTabla.add(blancos);
        // frentesTabla.add(validos.toUpperCase());
        // frentesTabla.add(emitidos.toUpperCase());
        // frentesTabla.add(habiltiados.toUpperCase());

        List<Integer> datosTabla = new ArrayList<>(); // Ejemplo de datos de votos

        // for (int i = 0; i < datos.size(); i++) {
        //     datosTabla.add(datos.get(i));
        // }

        // datosTabla.add(Integer.parseInt(resultado[0].toString()));
        // datosTabla.add(Integer.parseInt(resultado[1].toString()));
        // datosTabla.add(Integer.parseInt(resultado[2].toString()));
        // datosTabla.add(Integer.parseInt(resultado[4].toString()));
        // datosTabla.add(Integer.parseInt(resultado[5].toString()));

        // double totalVotos = datosTabla.stream().mapToInt(Integer::intValue).sum();
        // double totalVotos = Integer.parseInt(resultado[5].toString());// Total Habilitados
        // List<Double> porcentajes = datosTabla.stream()
        //         .map(dato -> Math.round((dato / totalVotos) * 100 * 1000.0) / 1000.0) // Redondea a dos decimales
        //         .collect(Collectors.toList());
        // Añadir al modelo
        model.addAttribute("mesas", mesas);
        // model.addAttribute("datos", datos);
        // model.addAttribute("datosTabla", datosTabla);
        // model.addAttribute("frentes", frentes);
        // model.addAttribute("frentesTabla", frentesTabla);
        // model.addAttribute("colores", colores);
        // model.addAttribute("porcentajes", porcentajes);

        model.addAttribute("facultades", facultadService.findAll());
        return "chart";
    }

    @PostMapping("/cargarGeneral")
    public ResponseEntity<Map<String, Object>> cargarGeneral(@RequestBody String entity) {

        // Generación de los datos
        
        Object[] resultadoDocentes = (Object[]) anforaService.votosGenerales(true, "D");
        Object[] resultadoEstudiantes = (Object[]) anforaService.votosGenerales(false, "E");

        // Validar que los resultados no sean nulos y asignar valores predeterminados en caso necesario
        List<Integer> datosDocentes = Arrays.asList(
            resultadoDocentes[2] != null ? Integer.parseInt(resultadoDocentes[2].toString()) : 0,
            resultadoDocentes[0] != null ? Integer.parseInt(resultadoDocentes[0].toString()) : 0,
            resultadoDocentes[1] != null ? Integer.parseInt(resultadoDocentes[1].toString()) : 0
        );

        List<Integer> datosEstudiantes = Arrays.asList(
            resultadoEstudiantes[2] != null ? Integer.parseInt(resultadoEstudiantes[2].toString()) : 0,
            resultadoEstudiantes[0] != null ? Integer.parseInt(resultadoEstudiantes[0].toString()) : 0,
            resultadoEstudiantes[1] != null ? Integer.parseInt(resultadoEstudiantes[1].toString()) : 0
        );

        List<String> frentesDocentes = new ArrayList<>();
        List<String> frentesEstudiantes = new ArrayList<>();
        for (Frente f : frenteService.findAll()) {
            frentesDocentes.add(f.getNombre_frente());
        }

        frentesDocentes.add("NULOS");
        frentesDocentes.add("BLANCOS");

        for (Frente f : frenteService.findAll()) {
            frentesEstudiantes.add(f.getNombre_frente());
        }

        frentesEstudiantes.add("NULOS");
        frentesEstudiantes.add("BLANCOS");

        List<String> colores = Arrays.asList("#008000", "#dedede", "#f1f592");

        String nulos = "Nulos", blancos = "Blancos", validos = "Válidos", emitidos = "Emitidos",
                habilitados = "Habilitados", actas = "Actas Computadas" , actas_habilitadas = "Actas Habilitadas";

        List<String> frentesTablaDocentes = new ArrayList<>(frentesDocentes);
        List<String> frentesTablaEstudiantes = new ArrayList<>(frentesEstudiantes);

        frentesTablaDocentes.addAll(Arrays.asList(validos.toUpperCase(), emitidos.toUpperCase(), habilitados.toUpperCase(), actas.toUpperCase(), actas_habilitadas.toUpperCase()));
        frentesTablaEstudiantes.addAll(Arrays.asList(validos.toUpperCase(), emitidos.toUpperCase(), habilitados.toUpperCase(),actas.toUpperCase(),actas_habilitadas.toUpperCase()));

        List<Integer> datosTablaDocentes = new ArrayList<>(datosDocentes);
        List<Integer> datosTablaEstudiantes = new ArrayList<>(datosEstudiantes);
        // datosTabla.add(Integer.parseInt(resultado[0].toString()));
        // datosTabla.add(Integer.parseInt(resultado[1].toString()));
        datosTablaDocentes.add(resultadoDocentes[2] != null ? Integer.parseInt(resultadoDocentes[2].toString()) : 0);
        datosTablaDocentes.add(resultadoDocentes[4] != null ? Integer.parseInt(resultadoDocentes[4].toString()) : 0);
        datosTablaDocentes.add(resultadoDocentes[5] != null ? Integer.parseInt(resultadoDocentes[5].toString()) : 0);
        datosTablaDocentes.add(resultadoDocentes[6] != null ? Integer.parseInt(resultadoDocentes[6].toString()) : 0);
        datosTablaDocentes.add(resultadoDocentes[7] != null ? Integer.parseInt(resultadoDocentes[7].toString()) : 0);
    
        datosTablaEstudiantes.add(resultadoEstudiantes[2] != null ? Integer.parseInt(resultadoEstudiantes[2].toString()) : 0);
        datosTablaEstudiantes.add(resultadoEstudiantes[4] != null ? Integer.parseInt(resultadoEstudiantes[4].toString()) : 0);
        datosTablaEstudiantes.add(resultadoEstudiantes[5] != null ? Integer.parseInt(resultadoEstudiantes[5].toString()) : 0);
        datosTablaEstudiantes.add(resultadoEstudiantes[6] != null ? Integer.parseInt(resultadoEstudiantes[6].toString()) : 0);
        datosTablaEstudiantes.add(resultadoEstudiantes[7] != null ? Integer.parseInt(resultadoEstudiantes[7].toString()) : 0);
    

        double totalVotosDocentes = (resultadoDocentes[4] != null) ? Double.parseDouble(resultadoDocentes[4].toString()) : 0;
        double totalVotosEstudiantes = (resultadoEstudiantes[4] != null) ? Double.parseDouble(resultadoEstudiantes[4].toString()) : 0;
        double totalVotosDocentesHabilitados = (resultadoDocentes[5] != null) ? Double.parseDouble(resultadoDocentes[5].toString()) : Double.parseDouble(resultadoEstudiantes[5].toString());
        double totalVotosEstudiantesHabilitados = (resultadoEstudiantes[5] != null) ? Double.parseDouble(resultadoEstudiantes[5].toString()) : Double.parseDouble(resultadoEstudiantes[5].toString());


        List<Double> porcentajesDocentes = datosTablaDocentes.stream()
                .map(dato -> Math.round((dato / totalVotosDocentes) * 50 * 1000.0) / 1000.0)
                .collect(Collectors.toList());

                // System.out.println(porcentajesDocentes);
        List<Double> porcentajesEstudiantes = datosTablaEstudiantes.stream()
                .map(dato -> Math.round((dato / totalVotosEstudiantes) * 50 * 1000.0) / 1000.0)
                .collect(Collectors.toList());

        List<Double> porcentajesDocentesHabilitados = datosTablaDocentes.stream()
                .map(dato -> Math.round((dato / totalVotosDocentesHabilitados) * 50 * 1000.0) / 1000.0)
                .collect(Collectors.toList());

                // System.out.println(porcentajesDocentes);
        List<Double> porcentajesEstudiantesHabilitados = datosTablaEstudiantes.stream()
                .map(dato -> Math.round((dato / totalVotosEstudiantesHabilitados) * 50 * 1000.0) / 1000.0)
                .collect(Collectors.toList());

        List<Double> porcentajesDocentesHabilitados100 = datosTablaDocentes.stream()
                .map(dato -> Math.round((dato / totalVotosDocentesHabilitados) * 100 * 1000.0) / 1000.0)
                .collect(Collectors.toList());

                // System.out.println(porcentajesDocentes);
        List<Double> porcentajesEstudiantesHabilitados100 = datosTablaEstudiantes.stream()
                .map(dato -> Math.round((dato / totalVotosEstudiantesHabilitados) * 100 * 1000.0) / 1000.0)
                .collect(Collectors.toList());

        List<Double> porcentajesDocentes100 = datosTablaDocentes.stream()
                .map(dato -> Math.round((dato / totalVotosDocentes) * 100 * 1000.0) / 1000.0)
                .collect(Collectors.toList());

                // System.out.println(porcentajesDocentes);
        List<Double> porcentajesEstudiantes100 = datosTablaEstudiantes.stream()
                .map(dato -> Math.round((dato / totalVotosEstudiantes) * 100 * 1000.0) / 1000.0)
                .collect(Collectors.toList());

        // Obtener el valor que se usará para el último índice
        int totalEstudiantesBase = Integer.parseInt(resultadoEstudiantes[6].toString());
        int totalDocentesBase = Integer.parseInt(resultadoDocentes[6].toString());

        int totalActasEstudiantesBase = Integer.parseInt(resultadoEstudiantes[7].toString());
        int totalActasDocentesBase = Integer.parseInt(resultadoDocentes[7].toString());

        // Reemplazar el último índice en ambas listas con el cálculo basado en totalEstudiantesBase
        int lastIndexDocentesHabilitados = porcentajesDocentesHabilitados.size() - 2;
        int lastIndexEstudiantesHabiltiados = porcentajesEstudiantesHabilitados.size() - 2;

        int lastIndexDocentesActasHabiltiados = porcentajesDocentesHabilitados.size() - 1;
        int lastIndexEstudiantesActasHabiltiados = porcentajesEstudiantesHabilitados.size() - 1;

        int lastIndexDocentes = porcentajesDocentes.size() - 2;
        int lastIndexEstudiantes = porcentajesEstudiantes.size() - 2;

        int lastIndexDocentesActas = porcentajesDocentes.size() - 1;
        int lastIndexEstudiantesActas = porcentajesEstudiantes.size() - 1;

        int lastIndexDocentes100 = porcentajesDocentes100.size() - 2;
        int lastIndexEstudiantes100 = porcentajesEstudiantes100.size() - 2;

        int lastIndexDocentesActas100 = porcentajesDocentes100.size() - 1;
        int lastIndexEstudiantesActas100 = porcentajesEstudiantes100.size() - 1;

        int lastIndexDocentesHabilitados100 = porcentajesDocentesHabilitados100.size() - 2;
        int lastIndexEstudiantesHabilitados100 = porcentajesEstudiantesHabilitados100.size() - 2;

        int lastIndexDocentesActasHabilitados100 = porcentajesDocentesHabilitados100.size() - 1;
        int lastIndexEstudiantesActasHabilitados100 = porcentajesEstudiantesHabilitados100.size() - 1;

        porcentajesDocentesHabilitados.set(lastIndexDocentesHabilitados,
            Math.round((datosTablaDocentes.get(lastIndexDocentesHabilitados) / (double) totalActasDocentesBase) * 100 * 1000.0) / 1000.0);

        porcentajesDocentesHabilitados.set(lastIndexDocentesActasHabiltiados,
            Math.round((datosTablaDocentes.get(lastIndexDocentesActasHabiltiados) / (double) totalActasDocentesBase) * 100 * 1000.0) / 1000.0);

        porcentajesEstudiantesHabilitados.set(lastIndexEstudiantesHabiltiados,
            Math.round((datosTablaEstudiantes.get(lastIndexEstudiantesHabiltiados) / (double) totalActasEstudiantesBase) * 100 * 1000.0) / 1000.0);

        porcentajesEstudiantesHabilitados.set(lastIndexEstudiantesActasHabiltiados,
            Math.round((datosTablaEstudiantes.get(lastIndexEstudiantesActasHabiltiados) / (double) totalActasEstudiantesBase) * 100 * 1000.0) / 1000.0);

        porcentajesDocentesHabilitados100.set(lastIndexDocentesHabilitados100,
            Math.round((datosTablaDocentes.get(lastIndexDocentesHabilitados100) / (double) totalActasDocentesBase) * 100 * 1000.0) / 1000.0);

        porcentajesDocentesHabilitados100.set(lastIndexDocentesActasHabilitados100,
            Math.round((datosTablaDocentes.get(lastIndexDocentesActasHabilitados100) / (double) totalActasDocentesBase) * 100 * 1000.0) / 1000.0);

        porcentajesEstudiantesHabilitados100.set(lastIndexEstudiantesHabilitados100,
            Math.round((datosTablaEstudiantes.get(lastIndexEstudiantesHabilitados100) / (double) totalActasEstudiantesBase) * 100 * 1000.0) / 1000.0);

        porcentajesEstudiantesHabilitados100.set(lastIndexEstudiantesActasHabilitados100,
            Math.round((datosTablaEstudiantes.get(lastIndexEstudiantesActasHabilitados100) / (double) totalActasEstudiantesBase) * 100 * 1000.0) / 1000.0);

        porcentajesDocentes.set(lastIndexDocentes,
            Math.round((datosTablaDocentes.get(lastIndexDocentes) / (double) totalActasDocentesBase) * 100 * 1000.0) / 1000.0);

        porcentajesDocentes.set(lastIndexDocentesActas,
            Math.round((datosTablaDocentes.get(lastIndexDocentesActas) / (double) totalActasDocentesBase) * 100 * 1000.0) / 1000.0);

        porcentajesEstudiantes.set(lastIndexEstudiantes,
            Math.round((datosTablaEstudiantes.get(lastIndexEstudiantes) / (double) totalActasEstudiantesBase) * 100 * 1000.0) / 1000.0);

        porcentajesEstudiantes.set(lastIndexEstudiantesActas,
            Math.round((datosTablaEstudiantes.get(lastIndexEstudiantesActas) / (double) totalActasEstudiantesBase) * 100 * 1000.0) / 1000.0);

        porcentajesDocentes100.set(lastIndexDocentes100,
            Math.round((datosTablaDocentes.get(lastIndexDocentes100) / (double) totalActasDocentesBase) * 100 * 1000.0) / 1000.0);

        porcentajesDocentes100.set(lastIndexDocentesActas100,
            Math.round((datosTablaDocentes.get(lastIndexDocentesActas100) / (double) totalActasDocentesBase) * 100 * 1000.0) / 1000.0);

        porcentajesEstudiantes100.set(lastIndexEstudiantes100,
            Math.round((datosTablaEstudiantes.get(lastIndexEstudiantes100) / (double) totalActasEstudiantesBase) * 100 * 1000.0) / 1000.0);

        porcentajesEstudiantes100.set(lastIndexEstudiantesActas100,
            Math.round((datosTablaEstudiantes.get(lastIndexEstudiantesActas100) / (double) totalActasEstudiantesBase) * 100 * 1000.0) / 1000.0);
        
        
        List<Double> totalPorcentaje = new ArrayList<>();
        List<Integer> totalDatos = new ArrayList<>();

        // Construir el HTML de la tabla
        StringBuilder htmlTabla = new StringBuilder();
        htmlTabla.append("<h3 class='text-center'>Tabla de Resultados (Docente) / 50 %</h3>");
        htmlTabla.append("<table class='table table-striped'>");
        htmlTabla.append("<thead><tr><th>Frente</th><th>Votos</th><th> 50 %</th><th> 100 %</th></tr></thead>");
        htmlTabla.append("<tbody>");

        for (int i = 0; i < datosTablaDocentes.size(); i++) {
            htmlTabla.append("<tr>");
            htmlTabla.append("<td>").append(frentesTablaDocentes.get(i)).append("</td>");
            htmlTabla.append("<td>").append(datosTablaDocentes.get(i)).append("</td>");
            htmlTabla.append("<td>").append(porcentajesDocentesHabilitados.get(i)+" %").append("</td>");
            htmlTabla.append("<td>").append(porcentajesDocentesHabilitados100.get(i)+" %").append("</td>");
            htmlTabla.append("</tr>");
            
        }

        htmlTabla.append("</tbody>");
        htmlTabla.append("</table>");

        StringBuilder htmlTablaEst = new StringBuilder();
        htmlTablaEst.append("<h3 class='text-center'>Tabla de Resultados (Estudiantes) / 50 %</h3>");
        htmlTablaEst.append("<table class='table table-striped'>");
        htmlTablaEst.append("<thead><tr><th>Frente</th><th>Votos</th><th> 50 %</th><th> 100 %</th></tr></thead>");
        htmlTablaEst.append("<tbody>");

        for (int i = 0; i < datosTablaEstudiantes.size(); i++) {
            htmlTablaEst.append("<tr>");
            htmlTablaEst.append("<td>").append(frentesTablaEstudiantes.get(i)).append("</td>");
            htmlTablaEst.append("<td>").append(datosTablaEstudiantes.get(i)).append("</td>");
            htmlTablaEst.append("<td>").append(porcentajesEstudiantesHabilitados.get(i) + " %").append("</td>");
            htmlTablaEst.append("<td>").append(porcentajesEstudiantesHabilitados100.get(i) + " %").append("</td>");
            htmlTablaEst.append("</tr>");
        
            // Sumar datos normalmente
            totalDatos.add(datosTablaDocentes.get(i) + datosTablaEstudiantes.get(i));
        
            // Evitar la suma en totalPorcentaje solo para los últimos dos índices
            if (i < datosTablaEstudiantes.size() - 2) {
                totalPorcentaje.add(porcentajesDocentes.get(i) + porcentajesEstudiantes.get(i));
            } else {
                // Solo agregar el porcentaje de estudiantes sin sumar
                totalPorcentaje.add(porcentajesEstudiantes.get(i));
            }
        }

        // System.out.println(totalPorcentaje);
        // System.out.println(totalDatos);
        htmlTablaEst.append("</tbody>");
        htmlTablaEst.append("</table>");

        StringBuilder htmlTablaTotalGeneral = new StringBuilder();
        htmlTablaTotalGeneral.append("<h3 class='text-center'>Tabla de Resultados</h3>");
        htmlTablaTotalGeneral.append("<table class='table table-striped'>");
        htmlTablaTotalGeneral.append("<thead><tr><th>Frente</th><th> 100 %</th></tr></thead>");
        htmlTablaTotalGeneral.append("<tbody>");

        for (int i = 0; i < totalPorcentaje.size() -3 ; i++) {
            htmlTablaTotalGeneral.append("<tr>");
            htmlTablaTotalGeneral.append("<td>").append(frentesTablaEstudiantes.get(i)).append("</td>");
            // htmlTablaTotalGeneral.append("<td>").append(totalDatos.get(i)).append("</td>");
            htmlTablaTotalGeneral.append("<td>").append(String.format("%.3f", totalPorcentaje.get(i))).append(" %").append("</td>");
            htmlTablaTotalGeneral.append("</tr>");
            // System.out.println(totalPorcentaje.get(i));
        }

        htmlTablaTotalGeneral.append("</tbody>");
        htmlTablaTotalGeneral.append("</table>");

        // Datos para el gráfico
        List<Map<String, Object>> chartData = new ArrayList<>();
        for (int i = 0; i < frentesDocentes.size(); i++) {
            Map<String, Object> dataPoint = new HashMap<>();
            dataPoint.put("frente", frentesDocentes.get(i));
            dataPoint.put("votos", datosDocentes.get(i));
            dataPoint.put("color", colores.get(i % colores.size()));
            chartData.add(dataPoint);
        }

        List<Map<String, Object>> chartDataEst = new ArrayList<>();
        for (int i = 0; i < frentesDocentes.size(); i++) {
            Map<String, Object> dataPoint = new HashMap<>();
            dataPoint.put("frente", frentesEstudiantes.get(i));
            dataPoint.put("votos", datosEstudiantes.get(i));
            dataPoint.put("color", colores.get(i % colores.size()));
            chartDataEst.add(dataPoint);
        }

        // Crear la respuesta JSON
        Map<String, Object> response = new HashMap<>();
        response.put("htmlDoc", htmlTabla.toString()); // HTML de la tabla generado
        response.put("htmlEst", htmlTablaEst.toString()); // HTML de la tabla generado
        response.put("htmlTotalGeneral", htmlTablaTotalGeneral.toString()); // HTML de la tabla generado
        response.put("chartDataDoc", chartData); // Datos para el gráfico
        response.put("chartDataEst", chartDataEst); // Datos para el gráfico
        response.put("totalHabilitadosDoc", Integer.parseInt(resultadoDocentes[5].toString())); // Total de Habilitados
        response.put("totalHabilitadosEst", Integer.parseInt(resultadoEstudiantes[5].toString())); // Total de Habilitados
        
        return ResponseEntity.ok(response);
    }
    
    @PostMapping("/cargarGeneralTotal")
    public ResponseEntity<Map<String, Object>> cargarGeneralTotal(Model model) {

        // Generación de los datos
        Object[] resultado = (Object[]) anforaService.votosGeneral();
        List<Integer> datos = Arrays.asList(
            resultado != null && resultado.length > 2 && resultado[2] != null ? Integer.parseInt(resultado[2].toString()) : 0,
            resultado != null && resultado.length > 0 && resultado[0] != null ? Integer.parseInt(resultado[0].toString()) : 0,
            resultado != null && resultado.length > 1 && resultado[1] != null ? Integer.parseInt(resultado[1].toString()) : 0
        );
        List<String> frentes = new ArrayList<>();
        for (Frente f : frenteService.findAll()) {
            frentes.add(f.getNombre_frente());
        }
        frentes.add("NULOS");
        frentes.add("BLANCOS");
        List<String> colores = Arrays.asList("#008000", "#dedede", "#f1f592");

        String nulos = "Nulos", blancos = "Blancos", validos = "Válidos", emitidos = "Emitidos",
                habilitados = "Habilitados",actas = "Actas Computadas" , actas_habilitadas = "Actas Habilitadas";

        List<String> frentesTabla = new ArrayList<>(frentes);
        frentesTabla.addAll(Arrays.asList(validos.toUpperCase(), emitidos.toUpperCase(), habilitados.toUpperCase(),
        actas.toUpperCase(), actas_habilitadas.toUpperCase()));

        List<Integer> datosTabla = new ArrayList<>(datos);
        // datosTabla.add(Integer.parseInt(resultado[0].toString()));
        // datosTabla.add(Integer.parseInt(resultado[1].toString()));
        datosTabla.add(resultado != null && resultado.length > 2 && resultado[2] != null ? Integer.parseInt(resultado[2].toString()) : 0);
        datosTabla.add(resultado != null && resultado.length > 4 && resultado[4] != null ? Integer.parseInt(resultado[4].toString()) : 0);
        datosTabla.add(resultado != null && resultado.length > 5 && resultado[5] != null ? Integer.parseInt(resultado[5].toString()) : 0);
        datosTabla.add(resultado != null && resultado.length > 6 && resultado[6] != null ? Integer.parseInt(resultado[6].toString()) : 0);
        datosTabla.add(resultado != null && resultado.length > 7 && resultado[7] != null ? Integer.parseInt(resultado[7].toString()) : 0);


        double totalVotos = (resultado != null && resultado.length > 4 && resultado[4] != null) ? Integer.parseInt(resultado[5].toString()) : 0.0;

        List<Double> porcentajes = datosTabla.stream()
                .map(dato -> Math.round((dato / totalVotos) * 100 * 1000.0) / 1000.0)
                .collect(Collectors.toList());

        int totalActas = (resultado != null && resultado.length > 7 && resultado[7] != null) ? Integer.parseInt(resultado[7].toString()) : 1;

        int lastIndexActasComputadas = porcentajes.size() - 2;

        int lastIndexActasHabilitas = porcentajes.size() - 1;

        porcentajes.set(lastIndexActasComputadas,
            Math.round((datosTabla.get(lastIndexActasComputadas) / (double) totalActas) * 100 * 1000.0) / 1000.0);

        porcentajes.set(lastIndexActasHabilitas,
            Math.round((datosTabla.get(lastIndexActasHabilitas) / (double) totalActas) * 100 * 1000.0) / 1000.0);

        // Construir el HTML de la tabla
        StringBuilder htmlTabla = new StringBuilder();
        htmlTabla.append("<h3 class='text-center'>Tabla de Resultados General ( 100 % )</h3>");
        htmlTabla.append("<table class='table table-striped'>");
        htmlTabla.append("<thead><tr><th>Frente</th><th>Votos</th><th>%</th></tr></thead>");
        htmlTabla.append("<tbody>");

        for (int i = 0; i < datosTabla.size(); i++) {
            htmlTabla.append("<tr>");
            htmlTabla.append("<td>").append(frentesTabla.get(i)).append("</td>");
            htmlTabla.append("<td>").append(datosTabla.get(i)).append("</td>");
            htmlTabla.append("<td>").append(porcentajes.get(i)+" %").append("</td>");
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

    @PostMapping("/cargarCarreras/{id_facultad}")
    public ResponseEntity<List<String[]>> cargarCarreras(@PathVariable(name = "id_facultad",required = false) Long id_facultad) {

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
        List<Integer> datos = Arrays.asList(
            resultado != null && resultado.length > 2 && resultado[2] != null ? Integer.parseInt(resultado[2].toString()) : 0,
            resultado != null && resultado.length > 0 && resultado[0] != null ? Integer.parseInt(resultado[0].toString()) : 0,
            resultado != null && resultado.length > 1 && resultado[1] != null ? Integer.parseInt(resultado[1].toString()) : 0
        );
        List<String> frentes = new ArrayList<>();
        for (Frente f : frenteService.findAll()) {
            frentes.add(f.getNombre_frente());
        }
        frentes.add("NULOS");
        frentes.add("BLANCOS");
        List<String> colores = Arrays.asList("#008000", "#dedede", "#f1f592");

        String nulos = "Nulos", blancos = "Blancos", validos = "Válidos", emitidos = "Emitidos",
                habilitados = "Habilitados",actas = "Actas Computadas" , actas_habilitadas = "Actas Habilitadas";

        List<String> frentesTabla = new ArrayList<>(frentes);
        frentesTabla.addAll(Arrays.asList(validos.toUpperCase(), emitidos.toUpperCase(), habilitados.toUpperCase(),
        actas.toUpperCase(), actas_habilitadas.toUpperCase()));

        List<Integer> datosTabla = new ArrayList<>(datos);
        // datosTabla.add(Integer.parseInt(resultado[0].toString()));
        // datosTabla.add(Integer.parseInt(resultado[1].toString()));
        datosTabla.add(resultado != null && resultado.length > 2 && resultado[2] != null ? Integer.parseInt(resultado[2].toString()) : 0);
        datosTabla.add(resultado != null && resultado.length > 4 && resultado[4] != null ? Integer.parseInt(resultado[4].toString()) : 0);
        datosTabla.add(resultado != null && resultado.length > 5 && resultado[5] != null ? Integer.parseInt(resultado[5].toString()) : 0);
        datosTabla.add(resultado != null && resultado.length > 6 && resultado[6] != null ? Integer.parseInt(resultado[6].toString()) : 0);
        datosTabla.add(resultado != null && resultado.length > 7 && resultado[7] != null ? Integer.parseInt(resultado[7].toString()) : 0);


        double totalVotos = (resultado != null && resultado.length > 4 && resultado[4] != null) ? Integer.parseInt(resultado[5].toString()) : Integer.parseInt(resultado[5].toString());

        List<Double> porcentajes = datosTabla.stream()
                .map(dato -> Math.round((dato / totalVotos) * 100 * 1000.0) / 1000.0)
                .collect(Collectors.toList());

        int totalActas = (resultado != null && resultado.length > 7 && resultado[7] != null) ? Integer.parseInt(resultado[7].toString()) : 0;

        int lastIndexActasComputadas = porcentajes.size() - 2;

        int lastIndexActasHabilitas = porcentajes.size() - 1;

        porcentajes.set(lastIndexActasComputadas,
            Math.round((datosTabla.get(lastIndexActasComputadas) / (double) totalActas) * 100 * 1000.0) / 1000.0);

        porcentajes.set(lastIndexActasHabilitas,
            Math.round((datosTabla.get(lastIndexActasHabilitas) / (double) totalActas) * 100 * 1000.0) / 1000.0);

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
            htmlTabla.append("<td>").append(porcentajes.get(i)+" %").append("</td>");
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

    @PostMapping("/cargarDatosFacultad/{id_facultad}/{sigla}/{isNul}")
    public ResponseEntity<Map<String, Object>> cargarDatosFacultad(@PathVariable("id_facultad") Long id_facultad,
            @PathVariable("sigla") String sigla,
            @PathVariable("isNul") Boolean isNul) {

        // Generación de los datos
        Object[] resultado = (Object[]) anforaService.votosGeneralFacultad(id_facultad, sigla, isNul);
        List<Integer> datos = Arrays.asList(
            resultado != null && resultado.length > 2 && resultado[2] != null ? Integer.parseInt(resultado[2].toString()) : 0,
            resultado != null && resultado.length > 0 && resultado[0] != null ? Integer.parseInt(resultado[0].toString()) : 0,
            resultado != null && resultado.length > 1 && resultado[1] != null ? Integer.parseInt(resultado[1].toString()) : 0
        );
        List<String> frentes = new ArrayList<>();
        for (Frente f : frenteService.findAll()) {
            frentes.add(f.getNombre_frente());
        }
        frentes.add("NULOS");
        frentes.add("BLANCOS");
        List<String> colores = Arrays.asList("#008000", "#dedede", "#f1f592");

        String nulos = "Nulos", blancos = "Blancos", validos = "Válidos", emitidos = "Emitidos",
                habilitados = "Habilitados" , actas = "Actas Computadas", actas_habilitadas = "Actas Habilitadas";

        List<String> frentesTabla = new ArrayList<>(frentes);
        frentesTabla.addAll(Arrays.asList(validos.toUpperCase(), emitidos.toUpperCase(), habilitados.toUpperCase(), actas.toUpperCase(),actas_habilitadas.toUpperCase()));

        List<Integer> datosTabla = new ArrayList<>(datos);
        // datosTabla.add(Integer.parseInt(resultado[0].toString()));
        // datosTabla.add(Integer.parseInt(resultado[1].toString()));
        datosTabla.add(resultado != null && resultado.length > 2 && resultado[2] != null ? Integer.parseInt(resultado[2].toString()) : 0);
        datosTabla.add(resultado != null && resultado.length > 4 && resultado[4] != null ? Integer.parseInt(resultado[4].toString()) : 0);
        datosTabla.add(resultado != null && resultado.length > 5 && resultado[5] != null ? Integer.parseInt(resultado[5].toString()) : 0);
        datosTabla.add(resultado != null && resultado.length > 6 && resultado[6] != null ? Integer.parseInt(resultado[6].toString()) : 0);
        datosTabla.add(resultado != null && resultado.length > 7 && resultado[7] != null ? Integer.parseInt(resultado[7].toString()) : 0);

        double totalVotos = (resultado != null && resultado.length > 4 && resultado[4] != null) ? Integer.parseInt(resultado[5].toString()) : Integer.parseInt(resultado[5].toString());
        List<Double> porcentajes = datosTabla.stream()
                .map(dato -> Math.round((dato / totalVotos) * 100 * 1000.0) / 1000.0)
                .collect(Collectors.toList());

        int totalActas = (resultado != null && resultado.length > 7 && resultado[7] != null) ? Integer.parseInt(resultado[7].toString()) : 0;

        int lastIndexActasComputadas = porcentajes.size() - 2;

        int lastIndexActasHabilitas = porcentajes.size() - 1;

        porcentajes.set(lastIndexActasComputadas,
            Math.round((datosTabla.get(lastIndexActasComputadas) / (double) totalActas) * 100 * 1000.0) / 1000.0);

        porcentajes.set(lastIndexActasHabilitas,
            Math.round((datosTabla.get(lastIndexActasHabilitas) / (double) totalActas) * 100 * 1000.0) / 1000.0);

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
            htmlTabla.append("<td>").append(porcentajes.get(i)+" %").append("</td>");
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

    @PostMapping("/cargarDatosCarrera/{id_carrera}")
    public ResponseEntity<Map<String, Object>> cargarDatosCarrera(@PathVariable(name = "id_carrera",required = false) Long id_carrera) {

        // Generación de los datos
        Object[] resultado = (Object[]) anforaService.votosGeneralCarrera(id_carrera);
        List<Integer> datos = Arrays.asList(
            resultado != null && resultado.length > 2 && resultado[2] != null ? Integer.parseInt(resultado[2].toString()) : 0,
            resultado != null && resultado.length > 0 && resultado[0] != null ? Integer.parseInt(resultado[0].toString()) : 0,
            resultado != null && resultado.length > 1 && resultado[1] != null ? Integer.parseInt(resultado[1].toString()) : 0
        );
        List<String> frentes = new ArrayList<>();
        for (Frente f : frenteService.findAll()) {
            frentes.add(f.getNombre_frente());
        }
        frentes.add("NULOS");
        frentes.add("BLANCOS");
        List<String> colores = Arrays.asList("#008000", "#dedede", "#f1f592");

        String nulos = "Nulos", blancos = "Blancos", validos = "Válidos", emitidos = "Emitidos",
                habilitados = "Habilitados", actas = "Actas Computadas" , actas_habilitadas = "Actas Habilitadas";

        List<String> frentesTabla = new ArrayList<>(frentes);
        frentesTabla.addAll(Arrays.asList(validos.toUpperCase(), emitidos.toUpperCase(), habilitados.toUpperCase(),
        actas.toUpperCase(), actas_habilitadas.toUpperCase()));

        List<Integer> datosTabla = new ArrayList<>(datos);
        // datosTabla.add(Integer.parseInt(resultado[0].toString()));
        // datosTabla.add(Integer.parseInt(resultado[1].toString()));
        datosTabla.add(resultado != null && resultado.length > 2 && resultado[2] != null ? Integer.parseInt(resultado[2].toString()) : 0);
        datosTabla.add(resultado != null && resultado.length > 4 && resultado[4] != null ? Integer.parseInt(resultado[4].toString()) : 0);
        datosTabla.add(resultado != null && resultado.length > 5 && resultado[5] != null ? Integer.parseInt(resultado[5].toString()) : 0);
        datosTabla.add(resultado != null && resultado.length > 6 && resultado[6] != null ? Integer.parseInt(resultado[6].toString()) : 0);
        datosTabla.add(resultado != null && resultado.length > 7 && resultado[7] != null ? Integer.parseInt(resultado[7].toString()) : 0);


        double totalVotos = (resultado != null && resultado.length > 4 && resultado[4] != null) ? Integer.parseInt(resultado[5].toString()) : Integer.parseInt(resultado[5].toString());
        List<Double> porcentajes = datosTabla.stream()
                .map(dato -> Math.round((dato / totalVotos) * 100 * 1000.0) / 1000.0)
                .collect(Collectors.toList());

        int totalActas = (resultado != null && resultado.length > 7 && resultado[7] != null) ? Integer.parseInt(resultado[7].toString()) : 0;

        int lastIndexActasComputadas = porcentajes.size() - 2;

        int lastIndexActasHabilitas = porcentajes.size() - 1;

        porcentajes.set(lastIndexActasComputadas,
            Math.round((datosTabla.get(lastIndexActasComputadas) / (double) totalActas) * 100 * 1000.0) / 1000.0);

        porcentajes.set(lastIndexActasHabilitas,
            Math.round((datosTabla.get(lastIndexActasHabilitas) / (double) totalActas) * 100 * 1000.0) / 1000.0);

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
            htmlTabla.append("<td>").append(porcentajes.get(i)+" %").append("</td>");
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

    @PostMapping("/tabla-mesa/{mesa}")
    public String cargarTablaPorMesa(Model model, @PathVariable Long mesa) throws Exception {

        Object[] resultadoMesa = (Object[]) mesaService.listarMesasyActas(mesa);

        List<String> frentes = new ArrayList<>(); // Ejemplo de nombres de frentes
        for (Frente f : frenteService.findAll()) {
            frentes.add(f.getNombre_frente());
        }
        Acta acta = actaService.actaPorIdMesa(mesa);
        // System.out.println(acta.getRutaArchivo() + "DOCUMENTO");

        String nulos = "Nulos", blancos = "Blancos", validos = "Válidos", emitidos = "Emitidos",
        noEmitido = "No Emitido", habiltiados = "Habilitados";

        List<String> frentesTabla = new ArrayList<>(); // Ejemplo de datos de votos
        for (int i = 0; i < frentes.size(); i++) {
            frentesTabla.add(frentes.get(i));
        }
        frentesTabla.add(nulos);
        frentesTabla.add(blancos);
        frentesTabla.add(validos);
        frentesTabla.add(emitidos);
        frentesTabla.add(noEmitido);
        frentesTabla.add(habiltiados);
        

        List<Integer> datosTabla = new ArrayList<>(); // Ejemplo de datos de votos

        datosTabla.add(Integer.parseInt(resultadoMesa[2].toString()));
        datosTabla.add(Integer.parseInt(resultadoMesa[0].toString()));
        datosTabla.add(Integer.parseInt(resultadoMesa[1].toString()));
        datosTabla.add(Integer.parseInt(resultadoMesa[2].toString()));
        datosTabla.add(Integer.parseInt(resultadoMesa[4].toString()));
        datosTabla.add(Integer.parseInt(resultadoMesa[5].toString()));
        datosTabla.add(Integer.parseInt(resultadoMesa[3].toString()));
        
        System.out.println(frentesTabla+"frente");
        System.out.println(datosTabla+"resultados");
        model.addAttribute("acta", acta);
        model.addAttribute("datosTabla", datosTabla);
        model.addAttribute("frentesTabla", frentesTabla);
        return "tabla-resultado-mesa";
    }

}
