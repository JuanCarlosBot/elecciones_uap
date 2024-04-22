package uap.elecciones.controllers;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpServletRequest;
import uap.elecciones.model.entity.Carrera;
import uap.elecciones.model.entity.ConteoTotal;
import uap.elecciones.model.service.IAsignacionEleccionService;
import uap.elecciones.model.service.ICarreraService;
import uap.elecciones.model.service.IConteoTotalCarreraService;
import uap.elecciones.model.service.IConteoTotalService;
import uap.elecciones.model.service.IFacultadService;
import uap.elecciones.model.service.IVotoTotalFrenteService;


@Controller
@RequestMapping(value = "/admin")
public class EstadisticasCentrosController {

      @Autowired
    private IAsignacionEleccionService asignacionEleccionService;

    @Autowired
    private IConteoTotalService conteoTotalService;

    @Autowired
    private IVotoTotalFrenteService votoTotalFrenteService;

    @Autowired
    private IConteoTotalCarreraService conteoTotalCarreraService;

    @Autowired
    private IFacultadService facultadService;

    @Autowired
    private ICarreraService carreraService;
    

      @RequestMapping(value = "/estadisticaCentroCienciasPGP", method = RequestMethod.GET)
    private String estadisticaFULCarrera(Model model, RedirectAttributes flash, HttpServletRequest request,
            @RequestParam(name = "succes", required = false) String succes) {

            Carrera carrera = carreraService.findOne(1L);

            List<Map<Object, String>>votosPorMesaNacer=votoTotalFrenteService.listaMesaFrenteCarrera(8L,carrera.getId_carrera());

            List<Map<Object, String>>votosPorMesaJuridica=votoTotalFrenteService.listaMesaFrenteCarrera(7L,carrera.getId_carrera());

            List<Map<Object, String>>votosBlancosNulosPorMesas=votoTotalFrenteService.listaVotosBlancosNulosPorMesasCarrera(4L,carrera.getId_carrera());
 
            int nacer = 0;
            for (int i = 0; i < votosPorMesaNacer.size(); i++) {
                nacer = nacer + Integer.parseInt(votosPorMesaNacer.get(i).get("cant_votantes"));
            }

            int juridica = 0;
            for (int i = 0; i < votosPorMesaJuridica.size(); i++) {
                juridica = juridica + Integer.parseInt(votosPorMesaJuridica.get(i).get("cant_votantes"));
            }

           
            
            int nulo = 0;
            int blanco = 0;
            for (int i = 0; i < votosBlancosNulosPorMesas.size(); i++) {
                nulo = nulo + Integer.parseInt(votosBlancosNulosPorMesas.get(i).get("cant_voto_nulo"));
                blanco = blanco + Integer.parseInt(votosBlancosNulosPorMesas.get(i).get("cant_voto_blanco"));
            }
            System.out.println(nacer + " " +  " " + nulo + " " + blanco);

            System.out.println("NOMBRE DE FRENTE");
            List<Map<Object, String>> listaFrentes = asignacionEleccionService.getListaFrentes("2024", 4L, 3L);
            String[] frentes = new String[listaFrentes.size() + 2];
            String[] colores = new String[listaFrentes.size() + 2];

            for (int i = 0; i < listaFrentes.size(); i++) {


                System.out.println("NOMBRE DE FRENTE"+listaFrentes.get(i).get("nombre_frente"));
                frentes[i] = listaFrentes.get(i).get("nombre_frente") +" "+ listaFrentes.get(i).get("sigla");
                colores[i] = listaFrentes.get(i).get("color");
            }


            frentes[listaFrentes.size()] = "Blancos";
            frentes[listaFrentes.size() + 1] = "Nulos";
            colores[listaFrentes.size()] = "#DCDCDC";
            colores[listaFrentes.size() + 1] = "#A9A9A9";

            System.out.println("ELECCIONES FUL");
            List<Map<Object, String>> votosFrentesTotal = votoTotalFrenteService.votoTotalFul(4L);
            int [] datos = new int[listaFrentes.size()+2];


            datos[0]=nacer;
            datos[1]=juridica;
            datos[2]=blanco;
            datos[3]=nulo;
           
             int nacerFul=0;
            

            ConteoTotal conteoVotosBlancosNulos = conteoTotalService.conteoTotalBlacoNulosFul(4L);

            // System.out.println(conteoVotosBlancosNulos.getBlanco_total());

            if (conteoVotosBlancosNulos != null) {

                model.addAttribute("blancos", blanco);
                model.addAttribute("nulos", nulo);
                int total=0;


                total=nacerFul+conteoVotosBlancosNulos.getBlanco_total()+conteoVotosBlancosNulos.getNulo_total();
                System.out.println("CONTEO TOTAL1-"+total);
              double  totall = 100 * ((double) total / 9107);

                 System.out.println("CONTEO TOTAL"+total);

                 DecimalFormat df = new DecimalFormat("#.##");
        
                 // Formateamos el número usando el objeto DecimalFormat
                 String totalP = df.format(totall);


                 
                 int habilitadosParaVotar=0;

                 habilitadosParaVotar = carrera.getEstudiantes().size();
                 

                 
                 model.addAttribute("total", (nacer + juridica+ nulo+ blanco));
                 model.addAttribute("habilitadosParaVotar", habilitadosParaVotar);
                 model.addAttribute("totalP", totalP);
                 model.addAttribute("nacerFul", nacer);
                 model.addAttribute("juridica", juridica);
                 model.addAttribute("mesasComputadas", votosPorMesaNacer.size());


                // Lógica adicional aquí
            } else {
                // Inicializar conteoVotosBlancosNulos si es necesario
                conteoVotosBlancosNulos = new ConteoTotal(); // O cualquier otra forma de inicialización que sea
                                                             // necesaria

                datos[3] = 0;
                datos[3] = 0;
            }

            model.addAttribute("votosBlancosNulosPorMesas", votosBlancosNulosPorMesas);
            model.addAttribute("votosPorMesaNacer", votosPorMesaNacer);
            model.addAttribute("votosPorMesaJuridica", votosPorMesaJuridica);
            model.addAttribute("cont_total_carrera", conteoTotalCarreraService.conteoTotalCarreraPorFull());
            model.addAttribute("datos", datos);
            model.addAttribute("frentes", frentes);
            model.addAttribute("colores", colores);
            model.addAttribute("carrera", carrera);

            return "Estadistica/centros/estadisticaCPGP";
        
    }
}
