package uap.elecciones.controllers;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.imageio.ImageIO;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
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
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import uap.elecciones.model.entity.Delegado;
import uap.elecciones.model.entity.DelegadoDto;
import uap.elecciones.model.entity.Docente;
import uap.elecciones.model.entity.Estudiante;
import uap.elecciones.model.entity.Mesa;
import uap.elecciones.model.service.DelegadoService;
import uap.elecciones.model.service.IAsignacionHabilitadoService;
import uap.elecciones.model.service.IDocenteService;
import uap.elecciones.model.service.IEstudianteService;
import uap.elecciones.model.service.IFacultadService;
import uap.elecciones.model.service.IMesaService;
import uap.elecciones.model.service.TipoDelegadoService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.PdfStructTreeController.returnType;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import com.itextpdf.text.Image;
import java.awt.Color;
import java.awt.image.BufferedImage;
import com.itextpdf.text.pdf.PdfContentByte;
import java.awt.Graphics2D;
import java.awt.Color;
import java.io.IOException;
import javax.imageio.ImageIO;

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

    @Autowired
    private IDocenteService docenteService;

    @Autowired
    private IEstudianteService estudianteService;

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
        // System.out.println(mesas.toString()+"-----------");
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

    @GetMapping("/cargarVotantes/{idMesa}")
    public String ventanaVotantes(Model model, @PathVariable(value = "idMesa") Long idMesa,
            HttpServletRequest request) {

        model.addAttribute("mesa", mesaService.findOne(idMesa));

        if (request.getSession().getAttribute("admin") != null) {
            String admin = String.valueOf(request.getSession().getAttribute("admin"));
            model.addAttribute("admin", admin);
        }
        List<Object[]> votantesMesa = habilitadoService.lista_votantes_por_mesa(idMesa);
        for (Object[] objects : votantesMesa) {
            // System.out.println(objects.toString());
        }

        model.addAttribute("votantes", votantesMesa);
        return "Delegado/tabla_votantes";
    }

    @GetMapping(value = "/pruebaE")
    public String pruebaE() {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("x-api-key", "key 70c8b6fc339aa5e6312dd42edf0636558948bb6008f1a0f867885d5e60e26c57");

        String url = "http://190.129.216.246:9993/v1/service/api/ae7ce0054d4c4f38a4a92bf1c0422b55";
        Map<String, String> requestBody = new HashMap<>();
        int j = 1;
        for (Docente docent : docenteService.findAll()) {
            String rd = docent.getRd();// "245";
            requestBody.put("rd", rd);

            HttpEntity<Map<String, String>> requestEntity = new HttpEntity<>(requestBody, headers);
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<Map> resp = restTemplate.exchange(url, HttpMethod.POST, requestEntity, Map.class);

            if (resp.getBody().get("status").toString().equals("200")) {
                Map<String, Object> data = (Map) resp.getBody().get("data");
                System.out.println(j++ + "," + docent.getPersona().getApellidos() + "," + data.get("rd").toString()
                        + "," + data.get("celular").toString());
                // System.out.println("EL CELULAR DE DOCENTE ES " +
                // data.get("celular").toString());
            }
        }
        return "redirect:/delegado/prueba";
    }

    @GetMapping(value = "/pruebaD")
    public ResponseEntity<Map<String, Object>> prueba() {
        Map<String, Object> response = new HashMap<>();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("x-api-key", "key 70c8b6fc339aa5e6312dd42edf0636558948bb6008f1a0f867885d5e60e26c57");

        String url = "http://190.129.216.246:9993/v1/service/api/cee024514f4e4b1f970bfb2b6486b421";
        List<Map<String, String>> lista = new ArrayList<>(); // Lista de mapas para almacenar los datos

        int j = 1;
        for (Estudiante estudiante : estudianteService.findAll()) {
            String ru = estudiante.getRu();
            Map<String, String> requestBody = new HashMap<>();
            requestBody.put("ru", ru);

            HttpEntity<Map<String, String>> requestEntity = new HttpEntity<>(requestBody, headers);
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<Map> resp = restTemplate.exchange(url, HttpMethod.POST, requestEntity, Map.class);

            // Verifica si la respuesta es exitosa y procesa los datos
            if (resp.getBody() != null && "200".equals(resp.getBody().get("status").toString())) {
                Map<String, Object> data = (Map<String, Object>) resp.getBody().get("data");

                // Crear un mapa con los datos del estudiante
                Map<String, String> estudianteData = new HashMap<>();
                estudianteData.put("numero", String.valueOf(j++));
                estudianteData.put("apellidos", estudiante.getPersona().getApellidos());
                estudianteData.put("ru", data.get("ru").toString());
                estudianteData.put("celular", data.get("celular").toString());

                lista.add(estudianteData); // Agrega los datos del estudiante a la lista
            }
        }

        // Añadir la lista a la respuesta JSON
        response.put("estudiantes", lista);
        return ResponseEntity.ok(response);
    }

    @GetMapping(value = "/generarPDFMesa/{idMesa}", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<byte[]> generarPDFMesa(@PathVariable(value = "idMesa") Long idMesa) throws IOException {
        OutputStream outputStream = new ByteArrayOutputStream();
        Document document = new Document(PageSize.LETTER, 45, 40, 110, 70);

        // Obtener la lista de delegados y mapear a DTOs
        List<Object[]> delegadosMesa = habilitadoService.listarDelegadosPorMesa(idMesa);
        List<DelegadoDto> delegadoDto = new ArrayList<>();
        for (Object[] delegad : delegadosMesa) {
            DelegadoDto dD = new DelegadoDto();
            dD.setNombre_tipo_delegado((String) delegad[7]);
            dD.setApellidos((String) delegad[4]);
            dD.setTipo_persona((String) delegad[3]);
            delegadoDto.add(dD);
        }
        // Obtener la lista de votantes y mapear a DTOs
        List<Object[]> votantesMesa = habilitadoService.lista_votantes_por_mesa(idMesa);
        List<DelegadoDto> votantesDto = new ArrayList<>();
        for (Object[] votante : votantesMesa) {
            DelegadoDto dD = new DelegadoDto();
            dD.setNombre_facultad((String) votante[0]);
            dD.setNombre_carrera((String) votante[1]);
            dD.setRu_rd((String) votante[2]);
            dD.setTipo_persona((String) votante[3]);
            dD.setApellidos((String) votante[4]);
            dD.setNombre_mesa((String) votante[5]);
            votantesDto.add(dD);
        }
        Mesa mesaa = mesaService.findOne(idMesa);
        System.out.println("tipoooooooo  " + mesaa.getNombre_mesa().charAt(mesaa.getNombre_mesa().length() - 1));
        String tipo = "" + mesaa.getNombre_mesa().charAt(mesaa.getNombre_mesa().length() - 1);
        String RDRU = "";
        if (tipo.equals("D")) {
            RDRU = "RD";
        } else if (tipo.equals("E")) {
            RDRU = "RU";
        }
        /*
         * // Definir datos del gráfico
         * String[] candidatos = {"MNR", "MAS", "Podemos"};
         * int[] votos = {235, 300, 500};
         * int totalVotos = 1200;
         * String[] colores = {"#000fff", "#000000", "#ffffff"};
         * 
         * // Crear el dataset
         * DefaultCategoryDataset dataset = new DefaultCategoryDataset();
         * for (int i = 0; i < candidatos.length; i++) {
         * dataset.addValue(votos[i], "Votos", candidatos[i]);
         * }
         * 
         * // Crear el gráfico de barras horizontal
         * JFreeChart chart = ChartFactory.createBarChart(
         * "Resultados de Votación", // Título del gráfico
         * "Candidato", // Etiqueta del eje X
         * "Votos", // Etiqueta del eje Y
         * dataset, // Dataset
         * PlotOrientation.HORIZONTAL, // Orientación horizontal
         * true, // Leyenda
         * true, // Herramientas
         * false // URLs
         * );
         */

        try {
            /*
             * // Personalizar colores
             * chart.getCategoryPlot().getRenderer().setSeriesPaint(0,
             * Color.decode(colores[0])); // MNR
             * chart.getCategoryPlot().getRenderer().setSeriesPaint(1,
             * Color.decode(colores[1])); // MAS
             * chart.getCategoryPlot().getRenderer().setSeriesPaint(2,
             * Color.decode(colores[2])); // Podemos
             * // Convertir el gráfico a imagen (PNG)
             * BufferedImage chartImage = chart.createBufferedImage(500, 300);
             * ByteArrayOutputStream imageStream = new ByteArrayOutputStream();
             * ImageIO.write(chartImage, "png", imageStream);
             * Image chartImg = Image.getInstance(imageStream.toByteArray());
             */
            PdfWriter writer = PdfWriter.getInstance(document, outputStream);

            // Definir evento para fondo de cada página
            String rutaImagen = Paths.get("").toAbsolutePath().toString() + "/src/main/resources/static/memo/fondo.png";
            Image imagenFondo = Image.getInstance(rutaImagen);
            writer.setPageEvent(new PdfPageEventHelper() {
                @Override
                public void onEndPage(PdfWriter writer, Document document) {
                    try {
                        imagenFondo.setAbsolutePosition(0, 0);
                        imagenFondo.scaleAbsolute(document.getPageSize());
                        PdfContentByte canvas = writer.getDirectContentUnder();
                        canvas.addImage(imagenFondo);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

            // Configuración de fuente y estilos
            BaseFont base = BaseFont.createFont(
                    Paths.get("").toAbsolutePath().toString() + "/src/main/resources/static/memo/bookman-old-style.ttf",
                    BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
            Font negritaTit = new Font(base, 12, Font.BOLD);
            Font negrita = new Font(base, 8, Font.BOLD);
            Font normal = new Font(base, 8);

            document.open();

            /*
             * // Título del documento
             * String tituu = "Resultados de Votación";
             * Paragraph titulou = new Paragraph(tituu, negritaTit);
             * titulou.setAlignment(Element.ALIGN_CENTER);
             * document.add(titulou);
             * 
             * // Agregar el gráfico como imagen al documento
             * chartImg.scaleToFit(500, 300);
             * document.add(chartImg); // Agregar la imagen del gráfico al documento PDF
             */

            String titu = mesaa.getNombre_mesa() + " - " + mesaa.getUbicacion() + " - " + mesaa.getDescripcion();
            Paragraph titulo = new Paragraph(titu, negritaTit);
            titulo.setAlignment(Element.ALIGN_CENTER);
            titulo.setSpacingBefore(0);// salto de filas antes de parrafo
            titulo.setSpacingAfter(1);// salto de filas despues de parrafo
            document.add(titulo);
            Paragraph titulo2 = new Paragraph("Habilitados: " + votantesDto.size(), negritaTit);
            titulo2.setAlignment(Element.ALIGN_CENTER);
            document.add(titulo2);
            titulo.setAlignment(Element.ALIGN_CENTER);
            // Crear tabla
            if (!votantesMesa.isEmpty()) {
                // titulo y jurados
                PdfPTable table1 = new PdfPTable(3); // Número de columnas
                float[] columnWidths1 = { 0.7f, 2.5f, 5f }; // Ajusta los valores según tus necesidades
                table1.setWidths(columnWidths1);
                table1.setWidthPercentage(70);
                table1.setSpacingBefore(18f);
                int conta = 1;
                for (DelegadoDto vot : delegadoDto) {
                    PdfPCell nro = new PdfPCell(new Phrase(String.valueOf(conta++), negrita));
                    nro.setHorizontalAlignment(Element.ALIGN_CENTER);
                    nro.setFixedHeight(14f);
                    table1.addCell(nro);
                    table1.addCell(new PdfPCell(new Phrase(vot.getNombre_tipo_delegado(), normal)));
                    table1.addCell(new PdfPCell(new Phrase(vot.getApellidos(), normal)));
                }
                document.add(table1);
                // lista de votantes
                PdfPTable table = new PdfPTable(6); // Número de columnas
                float[] columnWidths = { 0.7f, 2.5f, 2.5f, 1.2f, 4f, 2f }; // Ajusta los valores según tus necesidades
                table.setWidths(columnWidths);
                table.setWidthPercentage(100);
                table.setSpacingBefore(20f);

                // Encabezados de la tabla
                String[] headers = { "Nro", "Facultad", "Carrera", RDRU, "Apellidos y Nombres", "Firma" };
                for (String header : headers) {
                    PdfPCell cell = new PdfPCell(new Phrase(header, negrita));
                    cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table.addCell(cell);
                }
                table.setHeaderRows(1);
                // Agregar filas con datos de votantesDto
                int cont = 1;
                for (DelegadoDto vot : votantesDto) {
                    PdfPCell nro = new PdfPCell(new Phrase(String.valueOf(cont++), negrita));
                    nro.setHorizontalAlignment(Element.ALIGN_CENTER);
                    nro.setFixedHeight(24f);
                    table.addCell(nro);
                    table.addCell(new PdfPCell(new Phrase(vot.getNombre_facultad(), normal)));
                    table.addCell(new PdfPCell(new Phrase(vot.getNombre_carrera(), normal)));
                    table.addCell(new PdfPCell(new Phrase(vot.getRu_rd(), normal)));
                    table.addCell(new PdfPCell(new Phrase(vot.getApellidos(), normal)));
                    table.addCell(new PdfPCell(new Phrase("", normal))); // Firma vacía
                }
                document.add(table);
            }

            document.close();
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        byte[] contenidoPDF = ((ByteArrayOutputStream) outputStream).toByteArray();
        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_PDF)
                .body(contenidoPDF);
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
    // public ResponseEntity<InputStreamResource> generarExcelSolicitudBecaR()
    // throws IOException {

    // List<Mesa> listaMesas = mesaService.listarMesasOrdenadas();

    // // Crear un nuevo libro de Excel
    // Workbook workbook = new XSSFWorkbook();

    // Sheet sheet = workbook.createSheet("CONSULTA DE MESAS EN GENERAL");

    // // Crear un estilo para bordes
    // CellStyle borderCellStyle = workbook.createCellStyle();
    // borderCellStyle.setBorderBottom(BorderStyle.THIN);
    // borderCellStyle.setBorderTop(BorderStyle.THIN);
    // borderCellStyle.setBorderLeft(BorderStyle.THIN);
    // borderCellStyle.setBorderRight(BorderStyle.THIN);

    // // Agregar una fila adicional para la descripción
    // Row tituloGeneral = sheet.createRow(0);
    // Cell tituloGeneralCell = tituloGeneral.createCell(0);
    // tituloGeneralCell.setCellValue("CONSULTA DE MESAS EN GENERAL");

    // // Estilo de celda para texto en negrita y centrado
    // Font boldFont = sheet.getWorkbook().createFont();
    // boldFont.setBold(true);
    // CellStyle boldCenterCellStyle = sheet.getWorkbook().createCellStyle();
    // boldCenterCellStyle.setFont(boldFont);
    // boldCenterCellStyle.setAlignment(HorizontalAlignment.CENTER);

    // CellStyle boldCellStyle = sheet.getWorkbook().createCellStyle();
    // boldCellStyle.setFont(boldFont);
    // boldCellStyle.setAlignment(HorizontalAlignment.LEFT);

    // CellStyle boldCenterBorderCellStyle = workbook.createCellStyle();
    // boldCenterBorderCellStyle.setFont(boldFont);
    // boldCenterBorderCellStyle.setAlignment(HorizontalAlignment.CENTER);
    // boldCenterBorderCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
    // boldCenterBorderCellStyle.setBorderBottom(BorderStyle.THIN);
    // boldCenterBorderCellStyle.setBorderTop(BorderStyle.THIN);
    // boldCenterBorderCellStyle.setBorderLeft(BorderStyle.THIN);
    // boldCenterBorderCellStyle.setBorderRight(BorderStyle.THIN);

    // CellStyle delegadoCellStyle = workbook.createCellStyle();
    // delegadoCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
    // delegadoCellStyle.setBorderBottom(BorderStyle.THIN);
    // delegadoCellStyle.setBorderTop(BorderStyle.THIN);
    // delegadoCellStyle.setBorderLeft(BorderStyle.THIN);
    // delegadoCellStyle.setBorderRight(BorderStyle.THIN);

    // // Aplicar estilo a la celda de descripción
    // tituloGeneralCell.setCellStyle(boldCenterCellStyle);

    // int rowNum = 1;

    // String[] headers = { "Nro.", "Apellidos y Nombres", "Tipo Persona", "RU o
    // RD", "Facultad", "Carrera", "Mesa",
    // "Tipo" };
    // sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, headers.length - 1));

    // for (int i = 0; i < listaMesas.size(); i++) {
    // Row tituloMesa = sheet.createRow(rowNum++);
    // Cell cell = tituloMesa.createCell(0);
    // cell.setCellStyle(boldCenterCellStyle);
    // cell.setCellValue(listaMesas.get(i).getNombre_mesa());

    // sheet.addMergedRegion(new CellRangeAddress(rowNum - 1, rowNum - 1, 0,
    // headers.length - 1));

    // Row ubicacionMesa = sheet.createRow(rowNum++);
    // Cell cell2 = ubicacionMesa.createCell(0);
    // cell2.setCellValue(
    // "UBICACIÓN: " + listaMesas.get(i).getUbicacion() + " - " +
    // listaMesas.get(i).getDescripcion());
    // cell2.setCellStyle(boldCenterCellStyle);

    // // Combinar celdas para que la celda de ubicacionMesa ocupe toda la anchura
    // de
    // // las columnas
    // sheet.addMergedRegion(new CellRangeAddress(rowNum - 1, rowNum - 1, 0,
    // headers.length - 1));

    // Row headerRow = sheet.createRow(rowNum++);
    // for (int j = 0; j < headers.length; j++) {
    // Cell cellHeader = headerRow.createCell(j);
    // cellHeader.setCellValue(headers[j]);
    // cellHeader.setCellStyle(boldCenterBorderCellStyle);
    // }

    // List<Object[]> delegados =
    // habilitadoService.listarDelegadosPorMesa(listaMesas.get(i).getId_mesa());
    // List<DelegadoDto> listaDelegadosDto = delegados.stream()
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

    // for (int j = 0; j < listaDelegadosDto.size(); j++) {
    // Row row = sheet.createRow(rowNum++);
    // row.createCell(0).setCellValue(j + 1); // Nro.
    // row.getCell(0).setCellStyle(delegadoCellStyle); // Aplicar bordes

    // row.createCell(1).setCellValue(listaDelegadosDto.get(j).getApellidos());
    // row.getCell(1).setCellStyle(delegadoCellStyle); // Aplicar bordes

    // row.createCell(2).setCellValue(listaDelegadosDto.get(j).getTipo_persona());
    // row.getCell(2).setCellStyle(delegadoCellStyle); // Aplicar bordes

    // row.createCell(3).setCellValue(listaDelegadosDto.get(j).getRu_rd());
    // row.getCell(3).setCellStyle(delegadoCellStyle); // Aplicar bordes

    // row.createCell(4).setCellValue(listaDelegadosDto.get(j).getNombre_facultad());
    // row.getCell(4).setCellStyle(delegadoCellStyle); // Aplicar bordes

    // row.createCell(5).setCellValue(listaDelegadosDto.get(j).getNombre_carrera());
    // row.getCell(5).setCellStyle(delegadoCellStyle); // Aplicar bordes

    // row.createCell(6).setCellValue(listaDelegadosDto.get(j).getNombre_mesa());
    // row.getCell(6).setCellStyle(delegadoCellStyle); // Aplicar bordes

    // row.createCell(7).setCellValue(listaDelegadosDto.get(j).getNombre_tipo_delegado());
    // row.getCell(7).setCellStyle(delegadoCellStyle); // Aplicar bordes
    // }

    // Row saltoLinea = sheet.createRow(rowNum++);
    // Cell cellsaltoLinea = saltoLinea.createCell(0);
    // cellsaltoLinea.setCellValue("");
    // }

    // for (int i = 0; i < headers.length; i++) {
    // sheet.autoSizeColumn(i);
    // }

    // // Escribir el libro de Excel en un flujo de bytes
    // ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    // workbook.write(outputStream);
    // workbook.close();

    // // Crear un InputStreamResource para el flujo de bytes
    // ByteArrayInputStream inputStream = new
    // ByteArrayInputStream(outputStream.toByteArray());
    // InputStreamResource resource = new InputStreamResource(inputStream);

    // // Configurar los encabezados de respuesta para la descarga del archivo
    // HttpHeaders headersRespuesta = new HttpHeaders();
    // headersRespuesta.add(HttpHeaders.CONTENT_DISPOSITION, "attachment;
    // filename=Mesas en General.xlsx");

    // // Devolver la respuesta con el StreamResource y los encabezados
    // return ResponseEntity.ok()
    // .headers(headersRespuesta)
    // .contentType(
    // MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
    // .body(resource);
    // }
}
