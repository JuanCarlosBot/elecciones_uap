package uap.elecciones.controllers;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

// import org.apache.poi.ss.usermodel.BorderStyle;
// import org.apache.poi.ss.usermodel.Cell;
// import org.apache.poi.ss.usermodel.CellStyle;
// import org.apache.poi.ss.usermodel.FillPatternType;
// import org.apache.poi.ss.usermodel.Font;
// import org.apache.poi.ss.usermodel.HorizontalAlignment;
// import org.apache.poi.ss.usermodel.IndexedColors;
// import org.apache.poi.ss.usermodel.Row;
// import org.apache.poi.ss.usermodel.Sheet;
// import org.apache.poi.ss.usermodel.VerticalAlignment;
// import org.apache.poi.ss.usermodel.Workbook;
// import org.apache.poi.ss.util.CellRangeAddress;
// import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
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
    // public ResponseEntity<List<DelegadoDto>>
    // tablaDelegadosGeneral(@PathVariable(value = "idMesa") Long idMesa,
    // HttpServletRequest request) {

    // // System.out.println("AAAAAAAAAAAAAAAAAAA");
    // List<Object[]> delegados = habilitadoService.listarDelegadosPorMesa(idMesa);
    // List<DelegadoDto> delegadosDto = delegados.stream()
    // .map(arr -> new DelegadoDto(
    // (String) arr[0], // nombre_facultad
    // (String) arr[1], // nombre_carrera
    // (String) arr[2], // ru_rd
    // (String) arr[3], // tipo
    // (String) arr[4], // apellidos
    // (Long) arr[5], // id_persona
    // (String) arr[6], // nombre_mesa
    // (String) arr[7], // nombre_tipo_delegado
    // (Long) arr[8] // id_votante_habilitado
    // ))
    // .collect(Collectors.toList());

    // return ResponseEntity.ok(delegadosDto);
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

    // @GetMapping("/generarExcelConsultasMesasGeneral")
    // public ResponseEntity<InputStreamResource> generarExcelSolicitudBecaR() throws IOException {

    //     List<Mesa> listaMesas = mesaService.listarMesasOrdenadas();

    //     // Crear un nuevo libro de Excel
    //     Workbook workbook = new XSSFWorkbook();

    //     Sheet sheet = workbook.createSheet("CONSULTA DE MESAS EN GENERAL");

    //     // Crear un estilo para bordes
    //     CellStyle borderCellStyle = workbook.createCellStyle();
    //     borderCellStyle.setBorderBottom(BorderStyle.THIN);
    //     borderCellStyle.setBorderTop(BorderStyle.THIN);
    //     borderCellStyle.setBorderLeft(BorderStyle.THIN);
    //     borderCellStyle.setBorderRight(BorderStyle.THIN);

    //     // Agregar una fila adicional para la descripción
    //     Row tituloGeneral = sheet.createRow(0);
    //     Cell tituloGeneralCell = tituloGeneral.createCell(0);
    //     tituloGeneralCell.setCellValue("CONSULTA DE MESAS EN GENERAL");

    //     // Estilo de celda para texto en negrita y centrado
    //     Font boldFont = sheet.getWorkbook().createFont();
    //     boldFont.setBold(true);
    //     CellStyle boldCenterCellStyle = sheet.getWorkbook().createCellStyle();
    //     boldCenterCellStyle.setFont(boldFont);
    //     boldCenterCellStyle.setAlignment(HorizontalAlignment.CENTER);

    //     CellStyle boldCellStyle = sheet.getWorkbook().createCellStyle();
    //     boldCellStyle.setFont(boldFont);
    //     boldCellStyle.setAlignment(HorizontalAlignment.LEFT);

    //     CellStyle boldCenterBorderCellStyle = workbook.createCellStyle();
    //     boldCenterBorderCellStyle.setFont(boldFont);
    //     boldCenterBorderCellStyle.setAlignment(HorizontalAlignment.CENTER);
    //     boldCenterBorderCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
    //     boldCenterBorderCellStyle.setBorderBottom(BorderStyle.THIN);
    //     boldCenterBorderCellStyle.setBorderTop(BorderStyle.THIN);
    //     boldCenterBorderCellStyle.setBorderLeft(BorderStyle.THIN);
    //     boldCenterBorderCellStyle.setBorderRight(BorderStyle.THIN);

    //     CellStyle delegadoCellStyle = workbook.createCellStyle();
    //     delegadoCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
    //     delegadoCellStyle.setBorderBottom(BorderStyle.THIN);
    //     delegadoCellStyle.setBorderTop(BorderStyle.THIN);
    //     delegadoCellStyle.setBorderLeft(BorderStyle.THIN);
    //     delegadoCellStyle.setBorderRight(BorderStyle.THIN);

    //     // Aplicar estilo a la celda de descripción
    //     tituloGeneralCell.setCellStyle(boldCenterCellStyle);

    //     int rowNum = 1;

    //     String[] headers = { "Nro.", "Apellidos y Nombres", "Tipo Persona", "RU o RD", "Facultad", "Carrera", "Mesa",
    //             "Tipo" };
    //     sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, headers.length - 1));

    //     for (int i = 0; i < listaMesas.size(); i++) {
    //         Row tituloMesa = sheet.createRow(rowNum++);
    //         Cell cell = tituloMesa.createCell(0);
    //         cell.setCellStyle(boldCenterCellStyle);
    //         cell.setCellValue(listaMesas.get(i).getNombre_mesa());

    //         sheet.addMergedRegion(new CellRangeAddress(rowNum - 1, rowNum - 1, 0, headers.length - 1));

    //         Row ubicacionMesa = sheet.createRow(rowNum++);
    //         Cell cell2 = ubicacionMesa.createCell(0);
    //         cell2.setCellValue(
    //                 "UBICACIÓN: " + listaMesas.get(i).getUbicacion() + " - " + listaMesas.get(i).getDescripcion());
    //         cell2.setCellStyle(boldCenterCellStyle);

    //         // Combinar celdas para que la celda de ubicacionMesa ocupe toda la anchura de
    //         // las columnas
    //         sheet.addMergedRegion(new CellRangeAddress(rowNum - 1, rowNum - 1, 0, headers.length - 1));

    //         Row headerRow = sheet.createRow(rowNum++);
    //         for (int j = 0; j < headers.length; j++) {
    //             Cell cellHeader = headerRow.createCell(j);
    //             cellHeader.setCellValue(headers[j]);
    //             cellHeader.setCellStyle(boldCenterBorderCellStyle);
    //         }

    //         List<Object[]> delegados = habilitadoService.listarDelegadosPorMesa(listaMesas.get(i).getId_mesa());
    //         List<DelegadoDto> listaDelegadosDto = delegados.stream()
    //                 .map(arr -> new DelegadoDto(
    //                         (String) arr[0], // nombre_facultad
    //                         (String) arr[1], // nombre_carrera
    //                         (String) arr[2], // ru_rd
    //                         (String) arr[3], // tipo
    //                         (String) arr[4], // apellidos
    //                         (Long) arr[5], // id_persona
    //                         (String) arr[6], // nombre_mesa
    //                         (String) arr[7], // nombre_tipo_delegado
    //                         (Long) arr[8] // id_votante_habilitado
    //                 ))
    //                 .collect(Collectors.toList());

    //                 for (int j = 0; j < listaDelegadosDto.size(); j++) {
    //                     Row row = sheet.createRow(rowNum++);
    //                     row.createCell(0).setCellValue(j + 1);  // Nro.
    //                     row.getCell(0).setCellStyle(delegadoCellStyle);  // Aplicar bordes
                
    //                     row.createCell(1).setCellValue(listaDelegadosDto.get(j).getApellidos());
    //                     row.getCell(1).setCellStyle(delegadoCellStyle);  // Aplicar bordes
                
    //                     row.createCell(2).setCellValue(listaDelegadosDto.get(j).getTipo_persona());
    //                     row.getCell(2).setCellStyle(delegadoCellStyle);  // Aplicar bordes
                
    //                     row.createCell(3).setCellValue(listaDelegadosDto.get(j).getRu_rd());
    //                     row.getCell(3).setCellStyle(delegadoCellStyle);  // Aplicar bordes
                
    //                     row.createCell(4).setCellValue(listaDelegadosDto.get(j).getNombre_facultad());
    //                     row.getCell(4).setCellStyle(delegadoCellStyle);  // Aplicar bordes
                
    //                     row.createCell(5).setCellValue(listaDelegadosDto.get(j).getNombre_carrera());
    //                     row.getCell(5).setCellStyle(delegadoCellStyle);  // Aplicar bordes
                
    //                     row.createCell(6).setCellValue(listaDelegadosDto.get(j).getNombre_mesa());
    //                     row.getCell(6).setCellStyle(delegadoCellStyle);  // Aplicar bordes
                
    //                     row.createCell(7).setCellValue(listaDelegadosDto.get(j).getNombre_tipo_delegado());
    //                     row.getCell(7).setCellStyle(delegadoCellStyle);  // Aplicar bordes
    //                 }

    //         Row saltoLinea = sheet.createRow(rowNum++);
    //         Cell cellsaltoLinea = saltoLinea.createCell(0);
    //         cellsaltoLinea.setCellValue("");
    //     }

    //     for (int i = 0; i < headers.length; i++) {
    //         sheet.autoSizeColumn(i);
    //     }

    //     // Escribir el libro de Excel en un flujo de bytes
    //     ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    //     workbook.write(outputStream);
    //     workbook.close();

    //     // Crear un InputStreamResource para el flujo de bytes
    //     ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
    //     InputStreamResource resource = new InputStreamResource(inputStream);

    //     // Configurar los encabezados de respuesta para la descarga del archivo
    //     HttpHeaders headersRespuesta = new HttpHeaders();
    //     headersRespuesta.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=Mesas en General.xlsx");

    //     // Devolver la respuesta con el StreamResource y los encabezados
    //     return ResponseEntity.ok()
    //             .headers(headersRespuesta)
    //             .contentType(
    //                     MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
    //             .body(resource);
    // }
}
