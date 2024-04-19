package uap.elecciones.controllers;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletRequest;
import uap.elecciones.model.entity.AsignacionEleccion;
import uap.elecciones.model.entity.AsignacionHabilitado;
import uap.elecciones.model.entity.Carrera;
import uap.elecciones.model.entity.ConteoTotal;
import uap.elecciones.model.entity.ConteoTotalCarrera;
import uap.elecciones.model.entity.Estudiante;
import uap.elecciones.model.entity.Facultad;
import uap.elecciones.model.entity.Mesa;
import uap.elecciones.model.entity.VotanteHabilitado;
import uap.elecciones.model.service.IAsignacionEleccionService;
import uap.elecciones.model.service.IAsignacionHabilitadoService;
import uap.elecciones.model.service.ICarreraService;
import uap.elecciones.model.service.IConteoTotalCarreraService;
import uap.elecciones.model.service.IConteoTotalService;
import uap.elecciones.model.service.IFacultadService;
import uap.elecciones.model.service.IFrenteService;
import uap.elecciones.model.service.IMesaService;
import uap.elecciones.model.service.IVotanteHabilitadoService;
import uap.elecciones.model.service.IVotoTotalFrenteService;

@Controller
@RequestMapping(value = "/admin")
public class EstadisticasPruebasController {

    @Autowired
    private IAsignacionEleccionService asignacionEleccionService;

    @Autowired
    private IConteoTotalService conteoTotalService;

    @Autowired
    private IVotoTotalFrenteService votoTotalFrenteService;

    @Autowired
    private IConteoTotalCarreraService conteoTotalCarreraService;


    @RequestMapping(value = "/estadisticaFUL", method = RequestMethod.GET)
    private String estadisticaFUL(Model model, RedirectAttributes flash, HttpServletRequest request,
            @RequestParam(name = "succes", required = false) String succes) {

            if (request.getSession().getAttribute("usuario") != null) {
            System.out.println("NOMBRE DE FRENTE");
            List<Map<Object, String>> listaFrentes = asignacionEleccionService.getListaFrentes("2024", 3L, 1L);
            String[] frentes = new String[listaFrentes.size() + 2];
            String[] colores = new String[listaFrentes.size() + 2];

            for (int i = 0; i < listaFrentes.size(); i++) {


                System.out.println("NOMBRE DE FRENTE"+listaFrentes.get(i).get("nombre_frente"));
                frentes[i] = listaFrentes.get(i).get("nombre_frente") +" "+ listaFrentes.get(i).get("sigla");
                colores[i] = listaFrentes.get(i).get("color");
            }

            frentes[listaFrentes.size()] = "Blancos";
            frentes[listaFrentes.size() + 1] = "Nulos";
            colores[listaFrentes.size()] = "#fff4ea";
            colores[listaFrentes.size() + 1] = "#fec2ff";

            System.out.println("ELECCIONES FUL");
            List<Map<Object, String>> votosFrentesTotal = votoTotalFrenteService.votoTotalFul(3L);
            int [] datos = new int[listaFrentes.size()+2];
            System.out.println(votosFrentesTotal.size() + "HOLAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");

             int nacerFul=0;
             int FulFul=0;
            for (int i = 0; i < votosFrentesTotal.size(); i++) {

                System.out.println(votosFrentesTotal.get(i).get("voto_total_frente").toString());

                System.out.println( votosFrentesTotal.get(i).get("voto_total_frente").toString());
                System.out.println( votosFrentesTotal.get(i).get("nombre_frente").toString());

             

                   datos[i]=Integer.parseInt(votosFrentesTotal.get(i).get("voto_total_frente").toString());

                   

               if (votosFrentesTotal.get(i).get("nombre_frente").toString().equals("FUL TRANSPARENCIA")) {
                    FulFul=Integer.parseInt(votosFrentesTotal.get(i).get("voto_total_frente").toString());
                     }else{

                  nacerFul=Integer.parseInt(votosFrentesTotal.get(i).get("voto_total_frente").toString());
                    }

          
            }
 
            ConteoTotal conteoVotosBlancosNulos = conteoTotalService.conteoTotalBlacoNulosFul(3L);

            // System.out.println(conteoVotosBlancosNulos.getBlanco_total());

            if (conteoVotosBlancosNulos != null) {

                datos[2] = conteoVotosBlancosNulos.getBlanco_total();
                datos[3] = conteoVotosBlancosNulos.getNulo_total();

                model.addAttribute("blancos", conteoVotosBlancosNulos.getBlanco_total());
                model.addAttribute("nulos", conteoVotosBlancosNulos.getNulo_total());
                int total=0;


                total=FulFul+nacerFul+conteoVotosBlancosNulos.getBlanco_total()+conteoVotosBlancosNulos.getNulo_total();
                System.out.println("CONTEO TOTAL1-"+total);
              double  totall = 100 * ((double) total / 9107);

                 System.out.println("CONTEO TOTAL"+total);

                 DecimalFormat df = new DecimalFormat("#.##");
        
                 // Formateamos el número usando el objeto DecimalFormat
                 String totalP = df.format(totall);


                 int habilitadosParaVotar=9107;
                 model.addAttribute("total", total);
                 model.addAttribute("habilitadosParaVotar", habilitadosParaVotar);
                 model.addAttribute("totalP", totalP);
                 model.addAttribute("nacerFul", nacerFul);
                 model.addAttribute("FulFul", FulFul);


                // Lógica adicional aquí
            } else {
                // Inicializar conteoVotosBlancosNulos si es necesario
                conteoVotosBlancosNulos = new ConteoTotal(); // O cualquier otra forma de inicialización que sea
                                                             // necesaria

                datos[3] = 0;
                datos[3] = 0;
            }


            List<Map<Object, String>>votosPorMesaNacer=votoTotalFrenteService.listaMesaFrente(8L);
            List<Map<Object, String>>votosPorMesaFul=votoTotalFrenteService.listaMesaFrente(9L);
            List<Map<Object, String>>votosBlancosNulosPorMesas=votoTotalFrenteService.listaVotosBlancosNulosPorMesas(3L);
           // datos[2] = conteoVotosBlancosNulos.getBlanco_total();
            //datos[3] = conteoVotosBlancosNulos.getNulo_total();
            model.addAttribute("votosBlancosNulosPorMesas", votosBlancosNulosPorMesas);
            model.addAttribute("votosPorMesaNacer", votosPorMesaNacer);
            model.addAttribute("votosPorMesaFul", votosPorMesaFul);
            model.addAttribute("cont_total_carrera", conteoTotalCarreraService.conteoTotalCarreraPorFull(1L));
            model.addAttribute("datos", datos);
            model.addAttribute("frentes", frentes);
            model.addAttribute("colores", colores);

            return "Estadistica/estadisticaFUL";
        } else {
            return "redirect:/login";
        }
    }



    /******************************************************************************************************** */


    @RequestMapping(value = "/estadisticaACJYP", method = RequestMethod.GET)
    private String estadisticaACJYP(Model model, RedirectAttributes flash, HttpServletRequest request,
            @RequestParam(name = "succes", required = false) String succes) {
        if (request.getSession().getAttribute("usuario") != null) {




            List<Map<Object, String>> listaFrentes = asignacionEleccionService.getListaFrentes("2024", 4L, 3L);
            String[] frentes = new String[listaFrentes.size()+2];
            String[] colores = new String[listaFrentes.size()+2];


            for (int i = 0; i < listaFrentes.size(); i++) {
                frentes[i] = listaFrentes.get(i).get("nombre_frente") +" "+ listaFrentes.get(i).get("sigla");
                colores[i] = listaFrentes.get(i).get("color");
            }

            frentes[listaFrentes.size()] = "Blancos";
            frentes[listaFrentes.size()+1] = "Nulos";
            colores[listaFrentes.size()] = "#fff4ea";
            colores[listaFrentes.size()+1] = "#fec2ff";

            System.out.println("ELECCIONES FUL");
           List<Map<Object, String>>votosFrentesTotal=votoTotalFrenteService.votoTotalFul(4L);

           

           System.out.println( votosFrentesTotal.size()+"HOLAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");
            
            int [] datos = new int[listaFrentes.size()+2];

            int nacerFul=0;
            int FulFul=0;


            for (int i = 0; i < votosFrentesTotal.size(); i++) {

                System.out.println( votosFrentesTotal.size()+"HOLAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");
                System.out.println( votosFrentesTotal.get(i).get("voto_total_frente").toString());
                System.out.println( votosFrentesTotal.get(i).get("nombre_frente").toString());

                datos[i]=Integer.parseInt(votosFrentesTotal.get(i).get("voto_total_frente").toString());


                if (votosFrentesTotal.get(i).get("nombre_frente").toString().equals("JURIDICA EN ACCIÓN-ACjyP")) {
                    FulFul=Integer.parseInt(votosFrentesTotal.get(i).get("voto_total_frente").toString());
                     }else{

                  nacerFul=Integer.parseInt(votosFrentesTotal.get(i).get("voto_total_frente").toString());
                    }

            }
 

          

            ConteoTotal conteoVotosBlancosNulos= conteoTotalService.conteoTotalBlacoNulosFul(4L);

          

            //System.out.println(conteoVotosBlancosNulos.getBlanco_total());








            if (conteoVotosBlancosNulos != null) {
                

                datos[2] = conteoVotosBlancosNulos.getBlanco_total();
                datos[3] = conteoVotosBlancosNulos.getNulo_total();



                model.addAttribute("blancos", conteoVotosBlancosNulos.getBlanco_total());
                model.addAttribute("nulos", conteoVotosBlancosNulos.getNulo_total());

                int total=0;



                total=FulFul+nacerFul+conteoVotosBlancosNulos.getBlanco_total()+conteoVotosBlancosNulos.getNulo_total();
                System.out.println("CONTEO TOTAL1-"+total);
              double  totall = 100 * ((double) total / 1081);

                 System.out.println("CONTEO TOTAL"+total);

                 DecimalFormat df = new DecimalFormat("#.##");
        
                 // Formateamos el número usando el objeto DecimalFormat
                 String totalP = df.format(totall);

                 int habilitadosParaVotar=1081;
                 model.addAttribute("total", total);
                 model.addAttribute("habilitadosParaVotar", habilitadosParaVotar);
                 model.addAttribute("totalP", totalP);
                 model.addAttribute("nacerFul", nacerFul);
                 model.addAttribute("FulFul", FulFul);


                // Lógica adicional aquí
            } else {
                // Inicializar conteoVotosBlancosNulos si es necesario
                conteoVotosBlancosNulos = new ConteoTotal(); // O cualquier otra forma de inicialización que sea necesaria

                datos[2] = 0;
                datos[3] = 0;
            }

            for (ConteoTotalCarrera c : conteoTotalCarreraService.conteoTotalCarreraPorFacultad(1L)) {
                System.out.println(c.getCarrera());
            }
          
            List<Map<Object, String>>votosPorMesaNacer=votoTotalFrenteService.listaMesaFrente(6L);
            List<Map<Object, String>>votosPorMesaFul=votoTotalFrenteService.listaMesaFrente(7L);
            List<Map<Object, String>>votosBlancosNulosPorMesas=votoTotalFrenteService.listaVotosBlancosNulosPorMesas(4L);
            model.addAttribute("votosPorMesaNacer", votosPorMesaNacer);
            model.addAttribute("votosPorMesaFul", votosPorMesaFul);
            model.addAttribute("votosBlancosNulosPorMesas", votosBlancosNulosPorMesas);
            model.addAttribute("cont_total_carrera", conteoTotalCarreraService.conteoTotalCarreraPorFacultad(1L));
            model.addAttribute("datos", datos);
            model.addAttribute("frentes", frentes);
            model.addAttribute("colores", colores);
            return "Estadistica/estadisticaACJYP";
        } else {
            return "redirect:/login";
        }
    }



/******************************************************************************************************** */



    @RequestMapping(value = "/estadisticaACSYH", method = RequestMethod.GET)
    private String estadisticaACSYH(Model model, RedirectAttributes flash, HttpServletRequest request,
            @RequestParam(name = "succes", required = false) String succes) {
        if (request.getSession().getAttribute("usuario") != null) {
            List<Map<Object, String>> listaFrentes = asignacionEleccionService.getListaFrentes("2024", 8L, 3L);
          
          System.out.println("EL TAMAÑO DE LA LISTA ES : "+listaFrentes.size());
          
            String[] frentes = new String[listaFrentes.size()+2];
            String[] colores = new String[listaFrentes.size()+2];


            for (int i = 0; i < listaFrentes.size(); i++) {
                frentes[i] = listaFrentes.get(i).get("nombre_frente") +" "+ listaFrentes.get(i).get("sigla");
                colores[i] = listaFrentes.get(i).get("color");
            }

            frentes[listaFrentes.size()] = "Blancos";
            frentes[listaFrentes.size()+1] = "Nulos";
            
            colores[listaFrentes.size()+1] = "#fec2ff";

            System.out.println("ELECCIONES FUL");
           List<Map<Object, String>>votosFrentesTotal=votoTotalFrenteService.votoTotalFul(8L);

           

           System.out.println( votosFrentesTotal.size()+"HOLAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");
            
            int [] datos = new int[listaFrentes.size()+2];

            int nacerAcsyh=0;


            for (int i = 0; i < votosFrentesTotal.size(); i++) {


                System.out.println( votosFrentesTotal.get(i).get("voto_total_frente").toString());

                   datos[i]=Integer.parseInt(votosFrentesTotal.get(i).get("voto_total_frente").toString());


                   if (votosFrentesTotal.get(i).get("nombre_frente").toString().equals("NACER-ACSyH")) {
                    nacerAcsyh=Integer.parseInt(votosFrentesTotal.get(i).get("voto_total_frente").toString());
                     }

            }
 
            

            ConteoTotal conteoVotosBlancosNulos= conteoTotalService.conteoTotalBlacoNulosFul(8L);

            //System.out.println(conteoVotosBlancosNulos.getBlanco_total());

          

            if (conteoVotosBlancosNulos != null) {
                

                datos[1] = conteoVotosBlancosNulos.getBlanco_total();
                datos[2] = conteoVotosBlancosNulos.getNulo_total();


                model.addAttribute("blancos", conteoVotosBlancosNulos.getBlanco_total());
                model.addAttribute("nulos", conteoVotosBlancosNulos.getNulo_total());
            
                int total=0;

                total=nacerAcsyh+conteoVotosBlancosNulos.getBlanco_total()+conteoVotosBlancosNulos.getNulo_total();
                System.out.println("CONTEO TOTAL1-"+total);
              double  totall = 100 * ((double) total / 509);

                 System.out.println("CONTEO TOTAL"+total);

                 DecimalFormat df = new DecimalFormat("#.##");
        
                 // Formateamos el número usando el objeto DecimalFormat
                 String totalP = df.format(totall);

                 int habilitadosParaVotar=509;
                 model.addAttribute("total", total);
                 model.addAttribute("habilitadosParaVotar", habilitadosParaVotar);

                 model.addAttribute("totalP", totalP);
                 model.addAttribute("nacerFul", nacerAcsyh);
                 

                // Lógica adicional aquí
            } else {
                // Inicializar conteoVotosBlancosNulos si es necesario
                conteoVotosBlancosNulos = new ConteoTotal(); // O cualquier otra forma de inicialización que sea necesaria

                datos[1] = 0;
                datos[2] = 0;
            }

           // datos[1] = conteoVotosBlancosNulos.getBlanco_total();
           // datos[2] = conteoVotosBlancosNulos.getNulo_total();
           List<Map<Object, String>>votosPorMesaNacer=votoTotalFrenteService.listaMesaFrente(2L);
           List<Map<Object, String>>votosBlancosNulosPorMesas=votoTotalFrenteService.listaVotosBlancosNulosPorMesas(8L);
           model.addAttribute("votosBlancosNulosPorMesas", votosBlancosNulosPorMesas);
            
            model.addAttribute("votosPorMesaNacer", votosPorMesaNacer);
            model.addAttribute("cont_total_carrera", conteoTotalCarreraService.conteoTotalCarreraPorFacultad(5L));
            model.addAttribute("datos", datos);
            model.addAttribute("frentes", frentes);
            model.addAttribute("colores", colores);
            return "Estadistica/estadisticaACSYH";
        } else {
            return "redirect:/login";
        }
    }



/***********************************************************/
    @RequestMapping(value = "/estadisticaACBN", method = RequestMethod.GET)
    private String estadisticaACBN(Model model, RedirectAttributes flash, HttpServletRequest request,
            @RequestParam(name = "succes", required = false) String succes) {
        if (request.getSession().getAttribute("usuario") != null) {
            List<Map<Object, String>> listaFrentes = asignacionEleccionService.getListaFrentes("2024", 5L, 3L);
            String[] frentes = new String[listaFrentes.size()+2];
            String[] colores = new String[listaFrentes.size()+2];


            for (int i = 0; i < listaFrentes.size(); i++) {
                frentes[i] = listaFrentes.get(i).get("nombre_frente") +" "+ listaFrentes.get(i).get("sigla");
                colores[i] = listaFrentes.get(i).get("color");
            }

            frentes[listaFrentes.size()] = "Blancos";
            frentes[listaFrentes.size()+1] = "Nulos";
            colores[listaFrentes.size()] = "#fff4ea";
            colores[listaFrentes.size()+1] = "#fec2ff";

            System.out.println("ELECCIONES FUL");
           List<Map<Object, String>>votosFrentesTotal=votoTotalFrenteService.votoTotalFul(5L);

           System.out.println( votosFrentesTotal.size()+"HOLAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");
            
            int [] datos = new int[listaFrentes.size()+2];
            int nacerAcbn=0;

            for (int i = 0; i < votosFrentesTotal.size(); i++) {

                System.out.println( votosFrentesTotal.get(i).get("voto_total_frente").toString());

                   datos[i]=Integer.parseInt(votosFrentesTotal.get(i).get("voto_total_frente").toString());

                   if (votosFrentesTotal.get(i).get("nombre_frente").toString().equals("NACER-ACBN")) {
                    nacerAcbn=Integer.parseInt(votosFrentesTotal.get(i).get("voto_total_frente").toString());
                     }
          
            }
 
            ConteoTotal conteoVotosBlancosNulos= conteoTotalService.conteoTotalBlacoNulosFul(5L);

            //System.out.println(conteoVotosBlancosNulos.getBlanco_total());

          
            if (conteoVotosBlancosNulos != null) {
                

                datos[1] = conteoVotosBlancosNulos.getBlanco_total();
                datos[2] = conteoVotosBlancosNulos.getNulo_total();

                model.addAttribute("blancos", conteoVotosBlancosNulos.getBlanco_total());
                model.addAttribute("nulos", conteoVotosBlancosNulos.getNulo_total());


                int total=0;

                total=nacerAcbn+conteoVotosBlancosNulos.getBlanco_total()+conteoVotosBlancosNulos.getNulo_total();
                System.out.println("CONTEO TOTAL1-"+total);
              double  totall = 100 * ((double) total / 637);

                 System.out.println("CONTEO TOTAL"+total);

                 DecimalFormat df = new DecimalFormat("#.##");
        
                 // Formateamos el número usando el objeto DecimalFormat
                 String totalP = df.format(totall);

                 int habilitadosParaVotar=637;
                 model.addAttribute("total", total);
                 model.addAttribute("habilitadosParaVotar", habilitadosParaVotar);
                 model.addAttribute("totalP", totalP);
                 model.addAttribute("nacerFul", nacerAcbn);



                // Lógica adicional aquí
            } else {
                // Inicializar conteoVotosBlancosNulos si es necesario
                conteoVotosBlancosNulos = new ConteoTotal(); // O cualquier otra forma de inicialización que sea necesaria

                datos[1] = 0;
                datos[2] = 0;
            }




            //datos[1] = conteoVotosBlancosNulos.getBlanco_total();
           // datos[2] = conteoVotosBlancosNulos.getNulo_total();
           List<Map<Object, String>>votosPorMesaNacer=votoTotalFrenteService.listaMesaFrente(4L);
           List<Map<Object, String>>votosBlancosNulosPorMesas=votoTotalFrenteService.listaVotosBlancosNulosPorMesas(5L);
            
            model.addAttribute("votosPorMesaNacer", votosPorMesaNacer);
            model.addAttribute("votosBlancosNulosPorMesas", votosBlancosNulosPorMesas);
            model.addAttribute("cont_total_carrera", conteoTotalCarreraService.conteoTotalCarreraPorFacultad(5L));
            model.addAttribute("datos", datos);
            model.addAttribute("frentes", frentes);
            model.addAttribute("colores", colores);
            return "Estadistica/estadisticaACBN";
        } else {
            return "redirect:/login";
        }
    }
/*//////////////////////////////////////////////////////////////// */

    @RequestMapping(value = "/estadisticaACEF", method = RequestMethod.GET)
    private String estadisticaACEF(Model model, RedirectAttributes flash, HttpServletRequest request,
            @RequestParam(name = "succes", required = false) String succes) {
        if (request.getSession().getAttribute("usuario") != null) {
            List<Map<Object, String>> listaFrentes = asignacionEleccionService.getListaFrentes("2024", 7L, 3L);
            String[] frentes = new String[listaFrentes.size()+2];
            String[] colores = new String[listaFrentes.size()+2];


            for (int i = 0; i < listaFrentes.size(); i++) {
                frentes[i] = listaFrentes.get(i).get("nombre_frente") +" "+ listaFrentes.get(i).get("sigla");
                colores[i] = listaFrentes.get(i).get("color");
            }

            frentes[listaFrentes.size()] = "Blancos";
            frentes[listaFrentes.size()+1] = "Nulos";
            colores[listaFrentes.size()] = "#fff4ea";
            colores[listaFrentes.size()+1] = "#fec2ff";

            System.out.println("ELECCIONES FUL");
           List<Map<Object, String>>votosFrentesTotal=votoTotalFrenteService.votoTotalFul(7L);

           

           System.out.println( votosFrentesTotal.size()+"HOLAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");
            
            int [] datos = new int[listaFrentes.size()+2];
            int nacerAcef=0;

            for (int i = 0; i < votosFrentesTotal.size(); i++) {


                System.out.println( votosFrentesTotal.get(i).get("voto_total_frente").toString());

                   datos[i]=Integer.parseInt(votosFrentesTotal.get(i).get("voto_total_frente").toString());

                   if (votosFrentesTotal.get(i).get("nombre_frente").toString().equals("NACER-ACEF")) {
                    nacerAcef=Integer.parseInt(votosFrentesTotal.get(i).get("voto_total_frente").toString());
                     }

          
            }
 
            ConteoTotal conteoVotosBlancosNulos= conteoTotalService.conteoTotalBlacoNulosFul(7L);

            //System.out.println(conteoVotosBlancosNulos.getBlanco_total());




            if (conteoVotosBlancosNulos!= null) {
                

                datos[1] = conteoVotosBlancosNulos.getBlanco_total();
                datos[2] = conteoVotosBlancosNulos.getNulo_total();

                model.addAttribute("blancos", conteoVotosBlancosNulos.getBlanco_total());
                model.addAttribute("nulos", conteoVotosBlancosNulos.getNulo_total());


                int total=0;

                total=nacerAcef+conteoVotosBlancosNulos.getBlanco_total()+conteoVotosBlancosNulos.getNulo_total();
                System.out.println("CONTEO TOTAL1-"+total);
              double  totall = 100 * ((double) total / 1741);

                 System.out.println("CONTEO TOTAL"+total);

                 DecimalFormat df = new DecimalFormat("#.##");
        
                 // Formateamos el número usando el objeto DecimalFormat
                 String totalP = df.format(totall);

                 int habilitadosParaVotar=1741;
                 model.addAttribute("total", total);
                 model.addAttribute("habilitadosParaVotar", habilitadosParaVotar);

                 model.addAttribute("totalP", totalP);
                 model.addAttribute("nacerFul", nacerAcef);


                // Lógica adicional aquí
            } else {
                // Inicializar conteoVotosBlancosNulos si es necesario
                conteoVotosBlancosNulos = new ConteoTotal(); // O cualquier otra forma de inicialización que sea necesaria

                datos[1] = 0;
                datos[2] = 0;
            }
          
            //datos[1] = conteoVotosBlancosNulos.getBlanco_total();
          //  datos[2] = conteoVotosBlancosNulos.getNulo_total();
           
          List<Map<Object, String>>votosPorMesaNacer=votoTotalFrenteService.listaMesaFrente(3L);
          List<Map<Object, String>>votosBlancosNulosPorMesas=votoTotalFrenteService.listaVotosBlancosNulosPorMesas(7L);
          model.addAttribute("votosPorMesaNacer", votosPorMesaNacer);
          model.addAttribute("votosBlancosNulosPorMesas", votosBlancosNulosPorMesas);
            model.addAttribute("datos", datos);
            model.addAttribute("frentes", frentes);
            model.addAttribute("colores", colores);
            model.addAttribute("cont_total_carrera", conteoTotalCarreraService.conteoTotalCarreraPorFacultad(4L));
            return "Estadistica/estadisticaACEF";
        } else {
            return "redirect:/login";
        }
    }

    /*+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++ */

    @RequestMapping(value = "/estadisticaACYT", method = RequestMethod.GET)
    private String estadisticaACYT(Model model, RedirectAttributes flash, HttpServletRequest request,
            @RequestParam(name = "succes", required = false) String succes) {
        if (request.getSession().getAttribute("usuario") != null) {
            List<Map<Object, String>> listaFrentes = asignacionEleccionService.getListaFrentes("2024", 9L, 3L);
            String[] frentes = new String[listaFrentes.size()+2];
            String[] colores = new String[listaFrentes.size()+2];


            for (int i = 0; i < listaFrentes.size(); i++) {
                frentes[i] = listaFrentes.get(i).get("nombre_frente") +" "+ listaFrentes.get(i).get("sigla");
                colores[i] = listaFrentes.get(i).get("color");
            }

            frentes[listaFrentes.size()] = "Blancos";
            frentes[listaFrentes.size()+1] = "Nulos";
            colores[listaFrentes.size()] = "#fff4ea";
            colores[listaFrentes.size()+1] = "#fec2ff";

            System.out.println("ELECCIONES FUL");
           List<Map<Object, String>>votosFrentesTotal=votoTotalFrenteService.votoTotalFul(9L);

           

           System.out.println( votosFrentesTotal.size()+"HOLAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");
            
            int [] datos = new int[listaFrentes.size()+2];
            int nacerAcyt=0;


            for (int i = 0; i < votosFrentesTotal.size(); i++) {


                System.out.println( votosFrentesTotal.get(i).get("voto_total_frente").toString());

                   datos[i]=Integer.parseInt(votosFrentesTotal.get(i).get("voto_total_frente").toString());

                   if (votosFrentesTotal.get(i).get("nombre_frente").toString().equals("NACER-ACyT")) {
                    nacerAcyt=Integer.parseInt(votosFrentesTotal.get(i).get("voto_total_frente").toString());
                     }
          
            }
 
            ConteoTotal conteoVotosBlancosNulos= conteoTotalService.conteoTotalBlacoNulosFul(9L);



            if (conteoVotosBlancosNulos != null) {
                

                datos[1] = conteoVotosBlancosNulos.getBlanco_total();
                datos[2] = conteoVotosBlancosNulos.getNulo_total();


                model.addAttribute("blancos", conteoVotosBlancosNulos.getBlanco_total());
                model.addAttribute("nulos", conteoVotosBlancosNulos.getNulo_total());


                int total=0;

                total=nacerAcyt+conteoVotosBlancosNulos.getBlanco_total()+conteoVotosBlancosNulos.getNulo_total();
                System.out.println("CONTEO TOTAL1-"+total);
              double  totall = 100 * ((double) total / 1242);

                 System.out.println("CONTEO TOTAL"+total);

                 DecimalFormat df = new DecimalFormat("#.##");
        
                 // Formateamos el número usando el objeto DecimalFormat
                 String totalP = df.format(totall);

                 
                 int habilitadosParaVotar=1242;
                 model.addAttribute("total", total);
                 model.addAttribute("habilitadosParaVotar", habilitadosParaVotar);
                 model.addAttribute("totalP", totalP);
                 model.addAttribute("nacerFul", nacerAcyt);



                // Lógica adicional aquí
            } else {
                // Inicializar conteoVotosBlancosNulos si es necesario
                conteoVotosBlancosNulos = new ConteoTotal(); // O cualquier otra forma de inicialización que sea necesaria

                datos[1] = 0;
                datos[2] = 0;
            }


            //System.out.println(conteoVotosBlancosNulos.getBlanco_total());

            
    
            List<Map<Object, String>>votosPorMesaNacer=votoTotalFrenteService.listaMesaFrente(1L);
            List<Map<Object, String>>votosBlancosNulosPorMesas=votoTotalFrenteService.listaVotosBlancosNulosPorMesas(9L);
            
             model.addAttribute("votosPorMesaNacer", votosPorMesaNacer);
             model.addAttribute("votosBlancosNulosPorMesas", votosBlancosNulosPorMesas);
            model.addAttribute("datos", datos);
            model.addAttribute("frentes", frentes);
            model.addAttribute("colores", colores);
            model.addAttribute("cont_total_carrera", conteoTotalCarreraService.conteoTotalCarreraPorFacultad(6L));
            return "Estadistica/estadisticaACYT";
        } else {
            return "redirect:/login";
        }
    }

/**********************************************************************************************/
    @RequestMapping(value = "/estadisticaACS", method = RequestMethod.GET)
    private String estadisticaACS(Model model, RedirectAttributes flash, HttpServletRequest request,
            @RequestParam(name = "succes", required = false) String succes) {
        if (request.getSession().getAttribute("usuario") != null) {
            List<Map<Object, String>> listaFrentes = asignacionEleccionService.getListaFrentes("2024", 6L, 3L);
            String[] frentes = new String[listaFrentes.size()+2];
            String[] colores = new String[listaFrentes.size()+2];


            for (int i = 0; i < listaFrentes.size(); i++) {
                frentes[i] = listaFrentes.get(i).get("nombre_frente") +" "+ listaFrentes.get(i).get("sigla");
                colores[i] = listaFrentes.get(i).get("color");
            }

            frentes[listaFrentes.size()] = "Blancos";
            frentes[listaFrentes.size()+1] = "Nulos";
            colores[listaFrentes.size()] = "#fff4ea";
            colores[listaFrentes.size()+1] = "#fec2ff";

            System.out.println("ELECCIONES FUL");
           List<Map<Object, String>>votosFrentesTotal=votoTotalFrenteService.votoTotalFul(6L);

           

           System.out.println( votosFrentesTotal.size()+"HOLAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");
            
            int [] datos = new int[listaFrentes.size()+2];
            int nacerAcs=0;

            for (int i = 0; i < votosFrentesTotal.size(); i++) {


                System.out.println( votosFrentesTotal.get(i).get("voto_total_frente").toString());

                   datos[i]=Integer.parseInt(votosFrentesTotal.get(i).get("voto_total_frente").toString());


                   if (votosFrentesTotal.get(i).get("nombre_frente").toString().equals("NACER-ACS")) {
                    nacerAcs=Integer.parseInt(votosFrentesTotal.get(i).get("voto_total_frente").toString());
                     }
          
            }
 
            ConteoTotal conteoVotosBlancosNulos= conteoTotalService.conteoTotalBlacoNulosFul(6L);

            //System.out.println(conteoVotosBlancosNulos.getBlanco_total());


            if (conteoVotosBlancosNulos != null) {
                

                datos[1] = conteoVotosBlancosNulos.getBlanco_total();
                datos[2] = conteoVotosBlancosNulos.getNulo_total();



                model.addAttribute("blancos", conteoVotosBlancosNulos.getBlanco_total());
                model.addAttribute("nulos", conteoVotosBlancosNulos.getNulo_total());


                int total=0;

                total=nacerAcs+conteoVotosBlancosNulos.getBlanco_total()+conteoVotosBlancosNulos.getNulo_total();
                System.out.println("CONTEO TOTAL1-"+total);
              double  totall = 100 * ((double) total / 3092);

                 System.out.println("CONTEO TOTAL"+total);

                 DecimalFormat df = new DecimalFormat("#.##");
        
                 // Formateamos el número usando el objeto DecimalFormat
                 String totalP = df.format(totall);
                 
                 int habilitadosParaVotar=3092;
                 model.addAttribute("total", total);
                 model.addAttribute("habilitadosParaVotar", habilitadosParaVotar);
                 model.addAttribute("totalP", totalP);
                 model.addAttribute("nacerFul", nacerAcs);
                // Lógica adicional aquí
            } else {
                // Inicializar conteoVotosBlancosNulos si es necesario
                conteoVotosBlancosNulos = new ConteoTotal(); // O cualquier otra forma de inicialización que sea necesaria

                datos[1] = 0;
                datos[2] = 0;
            }
          
           // datos[1] = conteoVotosBlancosNulos.getBlanco_total();
            //datos[2] = conteoVotosBlancosNulos.getNulo_total();
            List<Map<Object, String>>votosPorMesaNacer=votoTotalFrenteService.listaMesaFrente(5L);
            List<Map<Object, String>>votosBlancosNulosPorMesas=votoTotalFrenteService.listaVotosBlancosNulosPorMesas(6L);
            model.addAttribute("votosPorMesaNacer", votosPorMesaNacer);
            model.addAttribute("votosBlancosNulosPorMesas", votosBlancosNulosPorMesas);
            model.addAttribute("datos", datos);
            model.addAttribute("frentes", frentes);
            model.addAttribute("colores", colores);
            model.addAttribute("cont_total_carrera", conteoTotalCarreraService.conteoTotalCarreraPorFacultad(3L));
            return "Estadistica/estadisticaACS";
        } else {
            return "redirect:/login";
        }
    }





    @RequestMapping(value = "/estadisticaPTO", method = RequestMethod.GET)
    private String estadisticaPTO(Model model, RedirectAttributes flash, HttpServletRequest request,
            @RequestParam(name = "succes", required = false) String succes) {
        if (request.getSession().getAttribute("usuario") != null) {
            List<Map<Object, String>> listaFrentes = asignacionEleccionService.getListaFrentes("2024", 10L, 3L);
            String[] frentes = new String[listaFrentes.size()+2];
            String[] colores = new String[listaFrentes.size()+2];


            for (int i = 0; i < listaFrentes.size(); i++) {
                frentes[i] = listaFrentes.get(i).get("nombre_frente") +" "+ listaFrentes.get(i).get("sigla");
                colores[i] = listaFrentes.get(i).get("color");
            }

            frentes[listaFrentes.size()] = "Blancos";
            frentes[listaFrentes.size()+1] = "Nulos";
            colores[listaFrentes.size()] = "#fff4ea";
            colores[listaFrentes.size()+1] = "#fec2ff";

            System.out.println("ELECCIONES FUL");
           List<Map<Object, String>>votosFrentesTotal=votoTotalFrenteService.votoTotalFul(10L);

           

           System.out.println( votosFrentesTotal.size()+"HOLAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");
            
            int [] datos = new int[listaFrentes.size()+2];
            int nacerPto=0;

            for (int i = 0; i < votosFrentesTotal.size(); i++) {


                System.out.println( votosFrentesTotal.get(i).get("voto_total_frente").toString());

                   datos[i]=Integer.parseInt(votosFrentesTotal.get(i).get("voto_total_frente").toString());


                   if (votosFrentesTotal.get(i).get("nombre_frente").toString().equals("NACER-UA-PTO")) {
                    nacerPto=Integer.parseInt(votosFrentesTotal.get(i).get("voto_total_frente").toString());
                     }
          
            }
 
            ConteoTotal conteoVotosBlancosNulos= conteoTotalService.conteoTotalBlacoNulosFul(10L);

            //System.out.println(conteoVotosBlancosNulos.getBlanco_total());


            if (conteoVotosBlancosNulos != null) {
                

                datos[1] = conteoVotosBlancosNulos.getBlanco_total();
                datos[2] = conteoVotosBlancosNulos.getNulo_total();



                model.addAttribute("blancos", conteoVotosBlancosNulos.getBlanco_total());
                model.addAttribute("nulos", conteoVotosBlancosNulos.getNulo_total());


                int total=0;

                total=nacerPto+conteoVotosBlancosNulos.getBlanco_total()+conteoVotosBlancosNulos.getNulo_total();
                System.out.println("CONTEO TOTAL1-"+total);
              double  totall = 100 * ((double) total / 252);

                 System.out.println("CONTEO TOTAL"+total);

                 DecimalFormat df = new DecimalFormat("#.##");
        
                 // Formateamos el número usando el objeto DecimalFormat
                 String totalP = df.format(totall);


                 int habilitadosParaVotar=252;                 
                 model.addAttribute("habilitadosParaVotar", habilitadosParaVotar);
                 model.addAttribute("total", total);
                 model.addAttribute("totalP", totalP);
                 model.addAttribute("nacerFul", nacerPto);
                // Lógica adicional aquí
            } else {
                // Inicializar conteoVotosBlancosNulos si es necesario
                conteoVotosBlancosNulos = new ConteoTotal(); // O cualquier otra forma de inicialización que sea necesaria

                datos[1] = 0;
                datos[2] = 0;
            }
          
           // datos[1] = conteoVotosBlancosNulos.getBlanco_total();
            //datos[2] = conteoVotosBlancosNulos.getNulo_total();
            List<Map<Object, String>>votosPorMesaNacer=votoTotalFrenteService.listaMesaFrente(10L);
            List<Map<Object, String>>votosBlancosNulosPorMesas=votoTotalFrenteService.listaVotosBlancosNulosPorMesas(10L);
            
            model.addAttribute("votosPorMesaNacer", votosPorMesaNacer);
            model.addAttribute("votosBlancosNulosPorMesas", votosBlancosNulosPorMesas);
            model.addAttribute("cont_total_carrera", conteoTotalCarreraService.conteoTotalCarreraPorFacultad(7L));
            model.addAttribute("datos", datos);
            model.addAttribute("frentes", frentes);
            model.addAttribute("colores", colores);
            return "Estadistica/estadisticaPTO";
        } else {
            return "redirect:/login";
        }
    }






    @RequestMapping(value = "/estadisticaES", method = RequestMethod.GET)
    private String estadisticaES(Model model, RedirectAttributes flash, HttpServletRequest request,
            @RequestParam(name = "succes", required = false) String succes) {
        if (request.getSession().getAttribute("usuario") != null) {
            List<Map<Object, String>> listaFrentes = asignacionEleccionService.getListaFrentes("2024", 11L, 3L);
            String[] frentes = new String[listaFrentes.size()+2];
            String[] colores = new String[listaFrentes.size()+2];


            for (int i = 0; i < listaFrentes.size(); i++) {
                frentes[i] = listaFrentes.get(i).get("nombre_frente") +" "+ listaFrentes.get(i).get("sigla");
                colores[i] = listaFrentes.get(i).get("color");
            }

            frentes[listaFrentes.size()] = "Blancos";
            frentes[listaFrentes.size()+1] = "Nulos";
            colores[listaFrentes.size()] = "#fff4ea";
            colores[listaFrentes.size()+1] = "#fec2ff";

            System.out.println("ELECCIONES FUL");
           List<Map<Object, String>>votosFrentesTotal=votoTotalFrenteService.votoTotalFul(11L);

           

           System.out.println( votosFrentesTotal.size()+"HOLAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");
            
            int [] datos = new int[listaFrentes.size()+2];
            int nacerEs=0;

            for (int i = 0; i < votosFrentesTotal.size(); i++) {


                System.out.println( votosFrentesTotal.get(i).get("voto_total_frente").toString());

                   datos[i]=Integer.parseInt(votosFrentesTotal.get(i).get("voto_total_frente").toString());


                   if (votosFrentesTotal.get(i).get("nombre_frente").toString().equals("NACER-UA-ES")) {
                    nacerEs=Integer.parseInt(votosFrentesTotal.get(i).get("voto_total_frente").toString());
                     }
          
            }
 
            ConteoTotal conteoVotosBlancosNulos= conteoTotalService.conteoTotalBlacoNulosFul(11L);

            //System.out.println(conteoVotosBlancosNulos.getBlanco_total());


            if (conteoVotosBlancosNulos != null) {
                

                datos[1] = conteoVotosBlancosNulos.getBlanco_total();
                datos[2] = conteoVotosBlancosNulos.getNulo_total();



                model.addAttribute("blancos", conteoVotosBlancosNulos.getBlanco_total());
                model.addAttribute("nulos", conteoVotosBlancosNulos.getNulo_total());


                int total=0;

                total=nacerEs+conteoVotosBlancosNulos.getBlanco_total()+conteoVotosBlancosNulos.getNulo_total();
                System.out.println("CONTEO TOTAL1-"+total);
              double  totall = 100 * ((double) total / 131);

                 System.out.println("CONTEO TOTAL"+total);

                 DecimalFormat df = new DecimalFormat("#.##");
        
                 // Formateamos el número usando el objeto DecimalFormat
                 String totalP = df.format(totall);

                 int habilitadosParaVotar=131;
                 model.addAttribute("habilitadosParaVotar", habilitadosParaVotar);
                 model.addAttribute("total", total);
                 model.addAttribute("totalP", totalP);
                 model.addAttribute("nacerFul", nacerEs);
                // Lógica adicional aquí
            } else {
                // Inicializar conteoVotosBlancosNulos si es necesario
                conteoVotosBlancosNulos = new ConteoTotal(); // O cualquier otra forma de inicialización que sea necesaria

                datos[1] = 0;
                datos[2] = 0;
            }
          
           // datos[1] = conteoVotosBlancosNulos.getBlanco_total();
            //datos[2] = conteoVotosBlancosNulos.getNulo_total();
            List<Map<Object, String>>votosPorMesaNacer=votoTotalFrenteService.listaMesaFrente(11L);
            List<Map<Object, String>>votosBlancosNulosPorMesas=votoTotalFrenteService.listaVotosBlancosNulosPorMesas(11L);
            model.addAttribute("votosPorMesaNacer", votosPorMesaNacer);
            model.addAttribute("votosBlancosNulosPorMesas", votosBlancosNulosPorMesas);
            model.addAttribute("cont_total_carrera", conteoTotalCarreraService.conteoTotalCarreraPorFacultad(8L));
            model.addAttribute("datos", datos);
            model.addAttribute("frentes", frentes);
            model.addAttribute("colores", colores);
            return "Estadistica/estadisticaES";
        } else {
            return "redirect:/login";
        }
    }







    @RequestMapping(value = "/estadisticaLP", method = RequestMethod.GET)
    private String estadisticaLP(Model model, RedirectAttributes flash, HttpServletRequest request,
            @RequestParam(name = "succes", required = false) String succes) {
        if (request.getSession().getAttribute("usuario") != null) {
            List<Map<Object, String>> listaFrentes = asignacionEleccionService.getListaFrentes("2024", 12L, 3L);
            String[] frentes = new String[listaFrentes.size()+2];
            String[] colores = new String[listaFrentes.size()+2];


            for (int i = 0; i < listaFrentes.size(); i++) {
                frentes[i] = listaFrentes.get(i).get("nombre_frente") +" "+ listaFrentes.get(i).get("sigla");
                colores[i] = listaFrentes.get(i).get("color");
            }

            frentes[listaFrentes.size()] = "Blancos";
            frentes[listaFrentes.size()+1] = "Nulos";
            colores[listaFrentes.size()] = "#fff4ea";
            colores[listaFrentes.size()+1] = "#fec2ff";

            System.out.println("ELECCIONES FUL");
           List<Map<Object, String>>votosFrentesTotal=votoTotalFrenteService.votoTotalFul(12L);

           

           System.out.println( votosFrentesTotal.size()+"HOLAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");
            
            int [] datos = new int[listaFrentes.size()+2];
            int nacerLp=0;

            for (int i = 0; i < votosFrentesTotal.size(); i++) {


                System.out.println( votosFrentesTotal.get(i).get("voto_total_frente").toString());

                   datos[i]=Integer.parseInt(votosFrentesTotal.get(i).get("voto_total_frente").toString());


                   if (votosFrentesTotal.get(i).get("nombre_frente").toString().equals("NACER-UA-LP")) {
                    nacerLp=Integer.parseInt(votosFrentesTotal.get(i).get("voto_total_frente").toString());
                     }
          
            }
 
            ConteoTotal conteoVotosBlancosNulos= conteoTotalService.conteoTotalBlacoNulosFul(12L);

            //System.out.println(conteoVotosBlancosNulos.getBlanco_total());


            if (conteoVotosBlancosNulos != null) {
                

                datos[1] = conteoVotosBlancosNulos.getBlanco_total();
                datos[2] = conteoVotosBlancosNulos.getNulo_total();



                model.addAttribute("blancos", conteoVotosBlancosNulos.getBlanco_total());
                model.addAttribute("nulos", conteoVotosBlancosNulos.getNulo_total());


                int total=0;

                total=nacerLp+conteoVotosBlancosNulos.getBlanco_total()+conteoVotosBlancosNulos.getNulo_total();
                System.out.println("CONTEO TOTAL1-"+total);
              double  totall = 100 * ((double) total / 409);

                 System.out.println("CONTEO TOTAL"+total);

                 DecimalFormat df = new DecimalFormat("#.##");
        
                 // Formateamos el número usando el objeto DecimalFormat
                 String totalP = df.format(totall);

               int habilitadosParaVotar=409;
               model.addAttribute("total", total);
                 model.addAttribute("totalP", totalP);
                 model.addAttribute("habilitadosParaVotar", habilitadosParaVotar);
                 model.addAttribute("nacerFul", nacerLp);
                 
                 
                // Lógica adicional aquí
            } else {
                // Inicializar conteoVotosBlancosNulos si es necesario
                conteoVotosBlancosNulos = new ConteoTotal(); // O cualquier otra forma de inicialización que sea necesaria

                datos[1] = 0;
                datos[2] = 0;
            }
          
           // datos[1] = conteoVotosBlancosNulos.getBlanco_total();
            //datos[2] = conteoVotosBlancosNulos.getNulo_total();
            List<Map<Object, String>>votosPorMesaNacer=votoTotalFrenteService.listaMesaFrente(12L);
            List<Map<Object, String>>votosBlancosNulosPorMesas=votoTotalFrenteService.listaVotosBlancosNulosPorMesas(12L);
            model.addAttribute("votosPorMesaNacer", votosPorMesaNacer);
            model.addAttribute("votosBlancosNulosPorMesas", votosBlancosNulosPorMesas);
            model.addAttribute("cont_total_carrera", conteoTotalCarreraService.conteoTotalCarreraPorFacultad(9L));
            model.addAttribute("datos", datos);
            model.addAttribute("frentes", frentes);
            model.addAttribute("colores", colores);
            return "Estadistica/estadisticaLP";
        } else {
            return "redirect:/login";
        }
    }


    @RequestMapping(value = "/estadisticaSR", method = RequestMethod.GET)
    private String estadisticaSR(Model model, RedirectAttributes flash, HttpServletRequest request,
            @RequestParam(name = "succes", required = false) String succes) {
        if (request.getSession().getAttribute("usuario") != null) {
            List<Map<Object, String>> listaFrentes = asignacionEleccionService.getListaFrentes("2024", 13L, 3L);
            String[] frentes = new String[listaFrentes.size()+2];
            String[] colores = new String[listaFrentes.size()+2];


            for (int i = 0; i < listaFrentes.size(); i++) {
                frentes[i] = listaFrentes.get(i).get("nombre_frente") +" "+ listaFrentes.get(i).get("sigla");
                colores[i] = listaFrentes.get(i).get("color");
            }

            frentes[listaFrentes.size()] = "Blancos";
            frentes[listaFrentes.size()+1] = "Nulos";
            colores[listaFrentes.size()] = "#fff4ea";
            colores[listaFrentes.size()+1] = "#fec2ff";

            System.out.println("ELECCIONES FUL");
           List<Map<Object, String>>votosFrentesTotal=votoTotalFrenteService.votoTotalFul(13L);

           

           System.out.println( votosFrentesTotal.size()+"HOLAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");
            
            int [] datos = new int[listaFrentes.size()+2];
            int nacerSr=0;

            for (int i = 0; i < votosFrentesTotal.size(); i++) {


                System.out.println( votosFrentesTotal.get(i).get("voto_total_frente").toString());

                   datos[i]=Integer.parseInt(votosFrentesTotal.get(i).get("voto_total_frente").toString());


                   if (votosFrentesTotal.get(i).get("nombre_frente").toString().equals("NACER-UA-SR")) {
                    nacerSr=Integer.parseInt(votosFrentesTotal.get(i).get("voto_total_frente").toString());
                     }
          
            }
 
            ConteoTotal conteoVotosBlancosNulos= conteoTotalService.conteoTotalBlacoNulosFul(13L);

            //System.out.println(conteoVotosBlancosNulos.getBlanco_total());


            if (conteoVotosBlancosNulos != null) {
                

                datos[1] = conteoVotosBlancosNulos.getBlanco_total();
                datos[2] = conteoVotosBlancosNulos.getNulo_total();



                model.addAttribute("blancos", conteoVotosBlancosNulos.getBlanco_total());
                model.addAttribute("nulos", conteoVotosBlancosNulos.getNulo_total());


                int total=0;

                total=nacerSr+conteoVotosBlancosNulos.getBlanco_total()+conteoVotosBlancosNulos.getNulo_total();
                System.out.println("CONTEO TOTAL1-"+total);
              double  totall = 100 * ((double) total / 13);

                 System.out.println("CONTEO TOTAL"+total);

                 DecimalFormat df = new DecimalFormat("#.##");
        
                 // Formateamos el número usando el objeto DecimalFormat
                 String totalP = df.format(totall);

               int habilitadosParaVotar=13;
               model.addAttribute("total", total);
                 model.addAttribute("totalP", totalP);
                 model.addAttribute("habilitadosParaVotar", habilitadosParaVotar);
                 model.addAttribute("nacerFul", nacerSr);
                 
                 
                // Lógica adicional aquí
            } else {
                // Inicializar conteoVotosBlancosNulos si es necesario
                conteoVotosBlancosNulos = new ConteoTotal(); // O cualquier otra forma de inicialización que sea necesaria

                datos[1] = 0;
                datos[2] = 0;
            }
          
           // datos[1] = conteoVotosBlancosNulos.getBlanco_total();
            //datos[2] = conteoVotosBlancosNulos.getNulo_total();
            List<Map<Object, String>>votosPorMesaNacer=votoTotalFrenteService.listaMesaFrente(13L);
            
            List<Map<Object, String>>votosBlancosNulosPorMesas=votoTotalFrenteService.listaVotosBlancosNulosPorMesas(13L);

            
            model.addAttribute("votosPorMesaNacer", votosPorMesaNacer);
            model.addAttribute("votosBlancosNulosPorMesas", votosBlancosNulosPorMesas);
            model.addAttribute("cont_total_carrera", conteoTotalCarreraService.conteoTotalCarreraPorFacultad(10L));
            model.addAttribute("datos", datos);
            model.addAttribute("frentes", frentes);
            model.addAttribute("colores", colores);
            return "Estadistica/estadisticaSR";
        } else {
            return "redirect:/login";
        }
    }



    


    































}
