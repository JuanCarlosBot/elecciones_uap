package uap.elecciones.controllers;

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
import uap.elecciones.model.entity.Estudiante;
import uap.elecciones.model.entity.Facultad;
import uap.elecciones.model.entity.Mesa;
import uap.elecciones.model.entity.VotanteHabilitado;
import uap.elecciones.model.service.IAsignacionEleccionService;
import uap.elecciones.model.service.IAsignacionHabilitadoService;
import uap.elecciones.model.service.ICarreraService;
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



    @RequestMapping(value = "/estadisticaFUL", method = RequestMethod.GET)
    private String estadisticaFUL(Model model, RedirectAttributes flash, HttpServletRequest request,
            @RequestParam(name = "succes", required = false) String succes) {
        if (request.getSession().getAttribute("usuario") != null) {

            List<Map<Object, String>> listaFrentes = asignacionEleccionService.getListaFrentes("2024", 3L, 1L);
            String[] frentes = new String[listaFrentes.size() + 2];
            String[] colores = new String[listaFrentes.size() + 2];

            for (int i = 0; i < listaFrentes.size(); i++) {
                frentes[i] = listaFrentes.get(i).get("nombre_frente") + " " + listaFrentes.get(i).get("sigla");
                colores[i] = listaFrentes.get(i).get("color");
            }

            frentes[listaFrentes.size()] = "Blancos";
            frentes[listaFrentes.size() + 1] = "Nulos";
            colores[listaFrentes.size()] = "#fff4ea";
            colores[listaFrentes.size() + 1] = "#fec2ff";

            System.out.println("ELECCIONES FUL");
            List<Map<Object, String>> votosFrentesTotal = votoTotalFrenteService.votoTotalFul(3L);

            System.out.println(votosFrentesTotal.size() + "HOLAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");

            int[] datos = new int[listaFrentes.size() + 2];

            for (int i = 0; i < votosFrentesTotal.size(); i++) {

                System.out.println(votosFrentesTotal.get(i).get("voto_total_frente").toString());

                datos[i] = Integer.parseInt(votosFrentesTotal.get(i).get("voto_total_frente").toString());

            }

            ConteoTotal conteoVotosBlancosNulos = conteoTotalService.conteoTotalBlacoNulosFul(3L);

            // System.out.println(conteoVotosBlancosNulos.getBlanco_total());

            if (conteoVotosBlancosNulos != null) {

                datos[2] = conteoVotosBlancosNulos.getBlanco_total();
                datos[3] = conteoVotosBlancosNulos.getNulo_total();
                // Lógica adicional aquí
            } else {
                // Inicializar conteoVotosBlancosNulos si es necesario
                conteoVotosBlancosNulos = new ConteoTotal(); // O cualquier otra forma de inicialización que sea
                                                             // necesaria

                datos[3] = 0;
                datos[3] = 0;
            }

            model.addAttribute("datos", datos);
            model.addAttribute("frentes", frentes);
            model.addAttribute("colores", colores);
            return "Estadistica/estadisticaFUL";
        } else {
            return "redirect:/login";
        }
    }


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


            for (int i = 0; i < votosFrentesTotal.size(); i++) {


                System.out.println( votosFrentesTotal.get(i).get("voto_total_frente").toString());

                   datos[i]=Integer.parseInt(votosFrentesTotal.get(i).get("voto_total_frente").toString());
          
            }
 
            ConteoTotal conteoVotosBlancosNulos= conteoTotalService.conteoTotalBlacoNulosFul(4L);

            //System.out.println(conteoVotosBlancosNulos.getBlanco_total());

            if (conteoVotosBlancosNulos != null) {
                

                datos[2] = conteoVotosBlancosNulos.getBlanco_total();
                datos[3] = conteoVotosBlancosNulos.getNulo_total();
                // Lógica adicional aquí
            } else {
                // Inicializar conteoVotosBlancosNulos si es necesario
                conteoVotosBlancosNulos = new ConteoTotal(); // O cualquier otra forma de inicialización que sea necesaria

                datos[2] = 0;
                datos[3] = 0;
            }

          
            //datos[2] = conteoVotosBlancosNulos.getBlanco_total();
            //datos[3] = conteoVotosBlancosNulos.getNulo_total();
           
            model.addAttribute("datos", datos);
            model.addAttribute("frentes", frentes);
            model.addAttribute("colores", colores);
            return "Estadistica/estadisticaACJYP";
        } else {
            return "redirect:/login";
        }
    }



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


            for (int i = 0; i < votosFrentesTotal.size(); i++) {


                System.out.println( votosFrentesTotal.get(i).get("voto_total_frente").toString());

                   datos[i]=Integer.parseInt(votosFrentesTotal.get(i).get("voto_total_frente").toString());
          
            }
 
            ConteoTotal conteoVotosBlancosNulos= conteoTotalService.conteoTotalBlacoNulosFul(8L);

            //System.out.println(conteoVotosBlancosNulos.getBlanco_total());

          

            if (conteoVotosBlancosNulos != null) {
                

                datos[1] = conteoVotosBlancosNulos.getBlanco_total();
                datos[2] = conteoVotosBlancosNulos.getNulo_total();
                // Lógica adicional aquí
            } else {
                // Inicializar conteoVotosBlancosNulos si es necesario
                conteoVotosBlancosNulos = new ConteoTotal(); // O cualquier otra forma de inicialización que sea necesaria

                datos[1] = 0;
                datos[2] = 0;
            }

           // datos[1] = conteoVotosBlancosNulos.getBlanco_total();
           // datos[2] = conteoVotosBlancosNulos.getNulo_total();
           
            model.addAttribute("datos", datos);
            model.addAttribute("frentes", frentes);
            model.addAttribute("colores", colores);
            return "Estadistica/estadisticaACSYH";
        } else {
            return "redirect:/login";
        }
    }



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


            for (int i = 0; i < votosFrentesTotal.size(); i++) {


                System.out.println( votosFrentesTotal.get(i).get("voto_total_frente").toString());

                   datos[i]=Integer.parseInt(votosFrentesTotal.get(i).get("voto_total_frente").toString());
          
            }
 
            ConteoTotal conteoVotosBlancosNulos= conteoTotalService.conteoTotalBlacoNulosFul(5L);

            //System.out.println(conteoVotosBlancosNulos.getBlanco_total());

          
            if (conteoVotosBlancosNulos != null) {
                

                datos[1] = conteoVotosBlancosNulos.getBlanco_total();
                datos[2] = conteoVotosBlancosNulos.getNulo_total();
                // Lógica adicional aquí
            } else {
                // Inicializar conteoVotosBlancosNulos si es necesario
                conteoVotosBlancosNulos = new ConteoTotal(); // O cualquier otra forma de inicialización que sea necesaria

                datos[1] = 0;
                datos[2] = 0;
            }




            //datos[1] = conteoVotosBlancosNulos.getBlanco_total();
           // datos[2] = conteoVotosBlancosNulos.getNulo_total();
           
            model.addAttribute("datos", datos);
            model.addAttribute("frentes", frentes);
            model.addAttribute("colores", colores);
            return "Estadistica/estadisticaACBN";
        } else {
            return "redirect:/login";
        }
    }


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


            for (int i = 0; i < votosFrentesTotal.size(); i++) {


                System.out.println( votosFrentesTotal.get(i).get("voto_total_frente").toString());

                   datos[i]=Integer.parseInt(votosFrentesTotal.get(i).get("voto_total_frente").toString());
          
            }
 
            ConteoTotal conteoVotosBlancosNulos= conteoTotalService.conteoTotalBlacoNulosFul(7L);

            //System.out.println(conteoVotosBlancosNulos.getBlanco_total());




            if (conteoVotosBlancosNulos != null) {
                

                datos[1] = conteoVotosBlancosNulos.getBlanco_total();
                datos[2] = conteoVotosBlancosNulos.getNulo_total();
                // Lógica adicional aquí
            } else {
                // Inicializar conteoVotosBlancosNulos si es necesario
                conteoVotosBlancosNulos = new ConteoTotal(); // O cualquier otra forma de inicialización que sea necesaria

                datos[1] = 0;
                datos[2] = 0;
            }
          
            //datos[1] = conteoVotosBlancosNulos.getBlanco_total();
          //  datos[2] = conteoVotosBlancosNulos.getNulo_total();
           
            model.addAttribute("datos", datos);
            model.addAttribute("frentes", frentes);
            model.addAttribute("colores", colores);
            return "Estadistica/estadisticaACEF";
        } else {
            return "redirect:/login";
        }
    }


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


            for (int i = 0; i < votosFrentesTotal.size(); i++) {


                System.out.println( votosFrentesTotal.get(i).get("voto_total_frente").toString());

                   datos[i]=Integer.parseInt(votosFrentesTotal.get(i).get("voto_total_frente").toString());
          
            }
 
            ConteoTotal conteoVotosBlancosNulos= conteoTotalService.conteoTotalBlacoNulosFul(9L);



            if (conteoVotosBlancosNulos != null) {
                

                datos[1] = conteoVotosBlancosNulos.getBlanco_total();
                datos[2] = conteoVotosBlancosNulos.getNulo_total();
                // Lógica adicional aquí
            } else {
                // Inicializar conteoVotosBlancosNulos si es necesario
                conteoVotosBlancosNulos = new ConteoTotal(); // O cualquier otra forma de inicialización que sea necesaria

                datos[1] = 0;
                datos[2] = 0;
            }


            //System.out.println(conteoVotosBlancosNulos.getBlanco_total());

          
    
           
            model.addAttribute("datos", datos);
            model.addAttribute("frentes", frentes);
            model.addAttribute("colores", colores);
            return "Estadistica/estadisticaACYT";
        } else {
            return "redirect:/login";
        }
    }


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


            for (int i = 0; i < votosFrentesTotal.size(); i++) {


                System.out.println( votosFrentesTotal.get(i).get("voto_total_frente").toString());

                   datos[i]=Integer.parseInt(votosFrentesTotal.get(i).get("voto_total_frente").toString());
          
            }
 
            ConteoTotal conteoVotosBlancosNulos= conteoTotalService.conteoTotalBlacoNulosFul(6L);

            //System.out.println(conteoVotosBlancosNulos.getBlanco_total());







            if (conteoVotosBlancosNulos != null) {
                

                datos[1] = conteoVotosBlancosNulos.getBlanco_total();
                datos[2] = conteoVotosBlancosNulos.getNulo_total();
                // Lógica adicional aquí
            } else {
                // Inicializar conteoVotosBlancosNulos si es necesario
                conteoVotosBlancosNulos = new ConteoTotal(); // O cualquier otra forma de inicialización que sea necesaria

                datos[1] = 0;
                datos[2] = 0;
            }
          
           // datos[1] = conteoVotosBlancosNulos.getBlanco_total();
            //datos[2] = conteoVotosBlancosNulos.getNulo_total();
           
            model.addAttribute("datos", datos);
            model.addAttribute("frentes", frentes);
            model.addAttribute("colores", colores);
            return "Estadistica/estadisticaACS";
        } else {
            return "redirect:/login";
        }
    }

































}
