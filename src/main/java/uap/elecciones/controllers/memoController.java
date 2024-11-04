package uap.elecciones.controllers;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.LineSeparator;

import uap.elecciones.model.entity.Carrera;
import uap.elecciones.model.entity.Persona;
import uap.elecciones.model.entity.VotanteHabilitado;
import uap.elecciones.model.service.ICarreraService;
import uap.elecciones.model.service.IPersonaService;
import uap.elecciones.model.service.IVotanteHabilitadoService;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
@Controller
public class memoController {
    
    @Autowired
    IPersonaService personaService;
    @Autowired
    IVotanteHabilitadoService votanteHabilitadoService;
    @Autowired
    ICarreraService carreraService;

    @GetMapping(value = "/generarPDF/{id_vh}/{id_persona}/{numeracion}", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<byte[]> generarPDF(
            @PathVariable(value = "id_vh") Long id_votante_habilitado,
            @PathVariable(value = "id_persona") Long id_persona,
            @PathVariable(value = "numeracion", required = false) String numero) throws IOException {
        VotanteHabilitado vh = votanteHabilitadoService.findOne(id_votante_habilitado);
        Persona persona = personaService.findOne(id_persona);
        Set<Carrera> carreras = new HashSet<>();
        if (vh.getEstudiante() != null) {
            carreras = persona.getEstudiante().getCarreras();
        } else if (vh.getDocente() != null) {
            carreras = persona.getDocente().getCarreras();
        }
        String nomCarrera = "";
        for (Carrera carrera : carreras) {
            nomCarrera = carrera.getNombre_carrera();
        }        
        String rol = "";
        String condi="";
        if (vh.getEstudiante() != null) {
            rol = "Est. - "+nomCarrera;
            condi="E";
        } else if (vh.getDocente() != null) {
            rol = "Doc. - "+nomCarrera;
            condi="D";
        }
        System.out.println("cite:> "+numero);
        
        OutputStream outputStream = new ByteArrayOutputStream();
        // izquierda, derecha, arriba, abajo
        Document document = new Document(PageSize.LETTER, 70, 70, 88, 33);
        
        try {
            
            PdfWriter writer = PdfWriter.getInstance(document, outputStream);
            document.open();

            String rutaImagen = Paths.get("").toAbsolutePath().toString() + "/src/main/resources/static/memo/fondo.png";
            Image imagenFondo = Image.getInstance(rutaImagen);
            imagenFondo.scaleAbsolute(document.getPageSize());
            imagenFondo.setAbsolutePosition(0, 0);
            
            document.add(imagenFondo);
            String nom_completo = persona.getApellidos();
            addTitle(numero, rol, condi, nom_completo, document, writer);
            addCuerpo(document);
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

    public static void addTitle(String numero, String rol, String condi, String nom_completo, Document document, PdfWriter writer) throws DocumentException {
        // 1 pulgada = 25.4mm
        // 1 pulgada 72 puntos
        try {
            BaseFont base = BaseFont.createFont(Paths.get("").toAbsolutePath().toString()+ "/src/main/resources/static/memo/bookman-old-style.ttf",BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
            Font negrita = new Font(base, 11, Font.BOLD);
            Font normal = new Font(base, 11);

            Paragraph title = new Paragraph();

            Chunk ins = new Chunk("MEMORANDUM \n", negrita);
            //ins.setUnderline(0.1f, -1.3f);
            title.add(ins);

            LocalDate ld = LocalDate.now();
            DateTimeFormatter f = DateTimeFormatter.ofPattern("yyyy");
            String anio = ld.format(f);
            Chunk rec = new Chunk();
            if (condi.equals("E")) {
                rec = new Chunk("COM/ELECT/COMP/E  N°"+numero+"/"+anio, negrita);
            }else if (condi.equals("D")) {
                rec = new Chunk("COM/ELECT/COMP/D  N°"+numero+"/"+anio, negrita);
            }
            
            Paragraph recc = new Paragraph(rec);
            recc.setSpacingBefore(10);
            recc.setIndentationLeft(270);// hacer un espacion del borde izquierdo
            //rec.setUnderline(0.1f, -1.5f);
            title.add(recc);

            title.setAlignment(Element.ALIGN_CENTER);
            title.setLeading(15.2f); // salto de lineas de texto en un parrafo
            title.setSpacingBefore(10);// salto de filas antes de parrafo
            title.setSpacingAfter(25);// salto de filas despues de parrafo
            document.add(title);

            PdfContentByte canvas = writer.getDirectContent();
            canvas.saveState();
            canvas.setLineWidth(1f); // Grosor de la línea
            canvas.moveTo(270, document.bottom() + 510); // Ajusta la posición Y para comenzar más arriba
            canvas.lineTo(270, document.bottom() + 610); // Ajusta la posición Y para terminar antes
            canvas.stroke();
            canvas.restoreState();

            Paragraph encavezadoA = new Paragraph();
            encavezadoA.setIndentationLeft(50);// hacer un espacion del borde izquierdo
            encavezadoA.add(new Phrase("Señor(a) \n", normal));
            encavezadoA.add(new Phrase(nom_completo + " \n", normal));
            encavezadoA.add(new Phrase(rol+ " \n", negrita));
            encavezadoA.add(new Phrase("Presente.- ", normal));
            encavezadoA.setLeading(15.2f); // salto de lineas de texto en un parrafo
            encavezadoA.setSpacingBefore(2);// salto de filas antes de parrafo
            encavezadoA.setSpacingAfter(17);// salto de filas despues de parrafo
            encavezadoA.setIndentationLeft(235);
            document.add(encavezadoA);

            LocalDate currentDate = LocalDate.now();
            //LocalDate fechaRestada = currentDate.minusDays(1);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd 'de' MMMM 'de' yyyy");
            String fecha_formateada = currentDate.format(formatter);

            Paragraph fecha = new Paragraph();
            fecha.setIndentationLeft(2);// hacer un espacion del borde izquierdo
            fecha.add(new Phrase("Cobija, " + "31 de octubre de 2024", normal));
            fecha.add(new Phrase("               Ref.: Designación", normal));
            fecha.setIndentationLeft(20);// hacer un espacion del borde izquierdo
            fecha.setIndentationRight(14);// hacer un espacion del borde derecho
            fecha.setLeading(13.2f); // salto de lineas de texto en un parrafo
            fecha.setSpacingBefore(0);// salto de filas antes de parrafo
            fecha.setSpacingAfter(1);// salto de filas despues de parrafo
            document.add(fecha);

            Chunk linea = new Chunk(" _______________________________________________________________________________", negrita);
            linea.setUnderline(1.1f, -1.1f);
            Paragraph lin = new Paragraph(linea);
            lin.setIndentationLeft(20);// hacer un espacion del borde izquierdo
            lin.setIndentationRight(14);// hacer un espacion del borde derecho
            lin.setAlignment(Element.ALIGN_JUSTIFIED); // Justificado
            document.add(lin);

            
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    public static void addCuerpo(Document document) throws DocumentException {
        // 1 pulgada = 25.4mm
        // 1 pulgada 72 puntos
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY); // establece el día de la semana en lunes
        //cal.add(Calendar.WEEK_OF_YEAR, -1); // suma una semana
        Calendar cal2 = Calendar.getInstance();
        cal2.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY); // establece el día de la semana en lunes
        cal2.add(Calendar.DATE, +6); // suma una semana... era +1 en day_of_week
        Calendar cale = cal2;
        //cale.add(Calendar.DATE, +6);
        
        Date fechaLunes = cal.getTime();   int diaLunes = cal.get(Calendar.DAY_OF_MONTH);
        Date fechaDomingo = cale.getTime(); int diaDomingo = cale.get(Calendar.DAY_OF_MONTH);

        SimpleDateFormat sdfMes = new SimpleDateFormat("MMMM", new Locale("es", "ES"));
        String mes = sdfMes.format(fechaDomingo);
        
        // Formatear la fecha completa
        String formatoFecha = String.format("del %d al %d de %s", diaLunes, diaDomingo, mes);
        System.out.println(formatoFecha); // Imprime: del 22 al 28 de Julio

        try {
            BaseFont base = BaseFont.createFont(Paths.get("").toAbsolutePath().toString()+ "/src/main/resources/static/memo/bookman-old-style.ttf",BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
            Font negrita = new Font(base, 12, Font.BOLD);
            Font normal = new Font(base, 11);
            Font cursiva = new Font(base, 11, Font.ITALIC);
            Font ncursiva = new Font(base, 11, Font.BOLDITALIC);
            
            Paragraph cuerpo = new Paragraph(new Phrase("De mi mayor consideración: ", normal));
            cuerpo.setIndentationLeft(20);// hacer un espacion del borde izquierdo
            cuerpo.setIndentationRight(14);// hacer un espacion del borde derecho
            cuerpo.setAlignment(Element.ALIGN_JUSTIFIED); // Justificado
            cuerpo.setLeading(15f); // salto de lineas de texto en un parrafo
            cuerpo.setSpacingBefore(8);// salto de filas antes de parrafo
            cuerpo.setSpacingAfter(8);// salto de filas despues de parrafo
            document.add(cuerpo);

            Paragraph cuerpo1 = new Paragraph();
            cuerpo1.setIndentationLeft(20);// hacer un espacion del borde izquierdo
            cuerpo1.setIndentationRight(14);// hacer un espacion del borde derecho

            cuerpo1.setAlignment(Element.ALIGN_JUSTIFIED); // Justificado
            cuerpo1.add(new Phrase("De acuerdo a lo establecido al Art. 30 numeral 6 del Estatuto Orgánico de la Universidad "+
            "Amazónica de Pando y, el Reglamento Electoral Universitario (Art. 19 y 25) establece: que dentro de las atribuciones del honorable Consejo Universitario es: "+
            "llamar a elecciones de Rector y Vicerrector de la UAP.\n", normal));
            cuerpo1.setLeading(15f); // salto de lineas de texto en un parrafo
            cuerpo1.setSpacingBefore(1);// salto de filas antes de parrafo
            cuerpo1.setSpacingAfter(1);// salto de filas despues de parrafo
            document.add(cuerpo1);
            
            Paragraph cuerpo2 = new Paragraph();
            Chunk jurado = new Chunk("JURADO ELECTORAL:", negrita);
            cuerpo2.add(new Phrase("En cumplimiento a resoluciones N° 534 y 535/2024 que aprueba el Calendario Electoral y Convocatoria a claustro a Rector "+
            "y Vicerrector de la Universidad Amazónica de Pando, gestión 2025 - 2029, tengo el agrado de comunicar a Usted que ha sido designado ", normal));
            cuerpo2.add(jurado);
            cuerpo2.add(new Phrase(" para las elecciones de Rector y Vicerrector de la Universidad amazónica de Pando.\n", normal));
            cuerpo2.setIndentationLeft(20);// hacer un espacion del borde izquierdo
            cuerpo2.setIndentationRight(14);// hacer un espacion del borde derecho
            cuerpo2.setAlignment(Element.ALIGN_JUSTIFIED); // Justificado
            cuerpo2.setLeading(15f); // salto de lineas de texto en un parrafo
            cuerpo2.setSpacingBefore(8);// salto de filas antes de parrafo
            cuerpo2.setSpacingAfter(8);// salto de filas despues de parrafo
            document.add(cuerpo2);

            Paragraph cuerpo3 = new Paragraph();
            cuerpo3.add(new Phrase("La capacitación se realizará el día sábado 02 de noviembre del año en curso, a horas 8:00 am, en el Paraninfo "+
            "Universitario de la Universidad Amazónica de Pando. El incumplimiento será pasible a sanción de acuerdo a normativa vigente.", normal));
            cuerpo3.setIndentationLeft(20);// hacer un espacion del borde izquierdo
            cuerpo3.setIndentationRight(14);// hacer un espacion del borde derecho
            cuerpo3.setAlignment(Element.ALIGN_JUSTIFIED); // Justificado
            cuerpo3.setLeading(15f); // salto de lineas de texto en un parrafo
            cuerpo3.setSpacingBefore(1);// salto de filas antes de parrafo
            cuerpo3.setSpacingAfter(1);// salto de filas despues de parrafo
            document.add(cuerpo3);

            Paragraph cuerpo4 = new Paragraph();
            cuerpo4.add(new Phrase("Agradezco de antemano la atención que ponga al presente. ", normal));
            cuerpo4.setIndentationLeft(20);// hacer un espacion del borde izquierdo
            cuerpo4.setIndentationRight(14);// hacer un espacion del borde derecho
            cuerpo4.setAlignment(Element.ALIGN_JUSTIFIED); // Justificado
            cuerpo4.setLeading(15f); // salto de lineas de texto en un parrafo
            cuerpo4.setSpacingBefore(20);// salto de filas antes de parrafo
            cuerpo4.setSpacingAfter(15);// salto de filas despues de parrafo
            document.add(cuerpo4);

            Paragraph atte = new Paragraph();
            atte.add(new Phrase("Atentamente. " , normal));
            atte.setIndentationLeft(20);// hacer un espacion del borde izquierdo
            atte.setIndentationRight(14);// hacer un espacion del borde derecho
            atte.setAlignment(Element.ALIGN_JUSTIFIED); // Justificado
            atte.setLeading(15f); // salto de lineas de texto en un parrafo
            atte.setSpacingBefore(5);// salto de filas antes de parrafo
            atte.setSpacingAfter(10);// salto de filas despues de parrafo
            document.add(atte);

            Paragraph encavezadoDe = new Paragraph();
            //encavezadoDe.setIndentationLeft(50);// hacer un espacion del borde izquierdo
            
            encavezadoDe.add(new Phrase("Ing. Marcos Vichenzo Abasto Antezana \n", cursiva));
            encavezadoDe.add(new Phrase("PRESIDENTE COMITÉ ELECTORAL", ncursiva));
            encavezadoDe.setAlignment(Element.ALIGN_JUSTIFIED); // Justificado
            encavezadoDe.setAlignment(Element.ALIGN_CENTER); // Centrar el párrafo
            encavezadoDe.setLeading(13.2f); // salto de lineas de texto en un parrafo
            encavezadoDe.setSpacingBefore(44);// salto de filas antes de parrafo
            encavezadoDe.setSpacingAfter(15);// salto de filas despues de parrafo
            document.add(encavezadoDe);

            Font cursivaA = new Font(Font.FontFamily.TIMES_ROMAN, 8, Font.ITALIC);
            Paragraph arch = new Paragraph();
            //arch.setIndentationLeft(5);// hacer un espacion del borde izquierdo
            arch.add(new Phrase("Cc. Arch. \nAHA/mcg." , cursivaA));
            arch.setLeading(10.2f); // salto de lineas de texto en un parrafo
            arch.setSpacingBefore(15);// salto de filas antes de parrafo
            arch.setSpacingAfter(0);// salto de filas despues de parrafo
            document.add(arch);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
