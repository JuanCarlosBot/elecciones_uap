package uap.elecciones.controllers;
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
import uap.elecciones.model.dao.IDetalleAnforaDao;
import uap.elecciones.model.dao.IMesaDao;
import uap.elecciones.model.entity.Carrera;
import uap.elecciones.model.entity.Mesa;
import uap.elecciones.model.service.ICarreraService;
import uap.elecciones.model.service.IConteoTotalCarreraService;
import uap.elecciones.model.service.IDetalleAnforaService;
import uap.elecciones.model.service.IMesaService;
import uap.elecciones.model.service.IVotoTotalFrenteService;

@Controller
@RequestMapping(value = "/admin")
public class EstadisticasCentrosController {

    @Autowired
    private IVotoTotalFrenteService votoTotalFrenteService;

    @Autowired
    private IConteoTotalCarreraService conteoTotalCarreraService;

    @Autowired
    private ICarreraService carreraService;

    @Autowired
    private IDetalleAnforaDao detalleAnforaDao;

    @Autowired
    private IDetalleAnforaService detalleAnforaService;

    @Autowired
    private IMesaService mesaService;

      @RequestMapping(value = "/estadisticaCentros/{idCarrera}", method = RequestMethod.GET)
    private String estadisticaFULCarrera(@PathVariable("idCarrera") Long idCarrera,Model model, RedirectAttributes flash, HttpServletRequest request,
            @RequestParam(name = "succes", required = false) String succes) {

            Carrera carrera = carreraService.findOne(idCarrera);

            List<Map<Object, String>>votosPorFrentes= detalleAnforaService.listaVotosPorCarrera(idCarrera);

            List<Map<Object, String>>votosBlancosNulosPorMesas=votoTotalFrenteService.listaVotosBlancosNulosPorMesasCarrera(4L,carrera.getId_carrera());

            String[] frentes = new String[votosPorFrentes.size() + 2];
            String[] colores = new String[votosPorFrentes.size() + 2];
            int [] datos = new int[votosPorFrentes.size()+2];

            frentes[votosPorFrentes.size()] = "Blancos";
            frentes[votosPorFrentes.size() + 1] = "Nulos";
            colores[votosPorFrentes.size()] = "#DCDCDC";
            colores[votosPorFrentes.size() + 1] = "#A9A9A9";
            int total=0;
            int nulo = 0;
            int blanco = 0;
            for (int i = 0; i < votosBlancosNulosPorMesas.size(); i++) {
                nulo = nulo + Integer.parseInt(votosBlancosNulosPorMesas.get(i).get("cant_voto_nulo"));
                blanco = blanco + Integer.parseInt(votosBlancosNulosPorMesas.get(i).get("cant_voto_blanco"));
            }
            total=nulo+blanco;
            String bla="";
            String nul="";
            for (int i = 0; i < votosPorFrentes.size(); i++) {
                frentes[i] = votosPorFrentes.get(i).get("nombre_frente");
                colores[i] = votosPorFrentes.get(i).get("color");
                bla=votosPorFrentes.get(i).get("blanco_total");
                nul=votosPorFrentes.get(i).get("nulo_total");
            }
            for (int i = 0; i < frentes.length; i++) {
                System.out.println("Frente >> "+frentes[i]);
               // System.out.println(votosPorFrentes.get(i).get("blanco_total"));
            }

            List<Object[]> danforas = detalleAnforaDao.listaVotosPorCarreraFrente(carrera.getNombre_carrera());
            int j = 0;
            
            for (int i = 0; i < frentes.length; i++) {
                int valor = 0;
                for (Object[] frente : danforas) {
                    String nom= frentes[i];
                    
                    if (nom.equals((String)frente[0])) {
                        System.out.println("----------------"+nom);
                        valor+=(int)frente[3];
                    }
                }
                total+=valor;
                datos[j]=valor;
                datos[votosPorFrentes.size()]=Integer.parseInt(bla);
                datos[votosPorFrentes.size()+1]=Integer.parseInt(nul);
                j++;
                valor=0;
            }
           
            for (int i = 0; i < datos.length; i++) {
                System.out.println("valores "+datos[i]);
            }

           
       
            model.addAttribute("blancos", blanco);
            model.addAttribute("nulos", nulo);
            model.addAttribute("votosBlancosNulosPorMesas", votosBlancosNulosPorMesas);
            model.addAttribute("total", (total));
            model.addAttribute("habilitadosParaVotar", carrera.getEstudiantes().size());

            model.addAttribute("votosPorFrentes", votosPorFrentes);
            model.addAttribute("cont_total_carrera", conteoTotalCarreraService.conteoTotalCarreraPorFull());
            model.addAttribute("datos", datos);
            model.addAttribute("frentes", frentes);
            model.addAttribute("colores", colores);
            model.addAttribute("carrera", carrera);
            model.addAttribute("danforas", danforas);
            model.addAttribute("mesasComputadas",  mesaService.mesasPorCarrera(idCarrera).size());

            
            return "Estadistica/centros/estadisticaCENTRO";
        
    }
}
