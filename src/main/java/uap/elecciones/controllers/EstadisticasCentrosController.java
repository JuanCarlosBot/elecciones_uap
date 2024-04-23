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
import uap.elecciones.model.dao.IVotoTotalFrenteDao;
import uap.elecciones.model.entity.Carrera;
import uap.elecciones.model.service.ICarreraService;
import uap.elecciones.model.service.IConteoTotalCarreraService;
import uap.elecciones.model.service.IDetalleAnforaService;
import uap.elecciones.model.service.IMesaService;

@Controller
@RequestMapping(value = "/admin")
public class EstadisticasCentrosController {

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
    @Autowired
    private IVotoTotalFrenteDao votoTotalFrenteDao;

      @RequestMapping(value = "/estadisticaCentros/{idCarrera}", method = RequestMethod.GET)
    private String estadisticaFULCarrera(@PathVariable("idCarrera") Long idCarrera,Model model, RedirectAttributes flash, HttpServletRequest request,
            @RequestParam(name = "succes", required = false) String succes) {

            Carrera carrera = carreraService.findOne(idCarrera);
            List<Map<Object, String>>votosPorFrentes= detalleAnforaService.listaVotosPorCarrera(idCarrera);
            List<Object[]>votosBlancosNulosPorMesas=votoTotalFrenteDao.blancosNulosPorMesaCarrera(idCarrera, carrera.getNombre_carrera());
            List<Object[]> danforas = detalleAnforaDao.listaVotosPorCarreraFrente(carrera.getNombre_carrera());
            String[] frentes = new String[votosPorFrentes.size() + 2], colores = new String[votosPorFrentes.size() + 2];
            int [] datos = new int[votosPorFrentes.size()+2];

            frentes[votosPorFrentes.size()] = "Blancos"; frentes[votosPorFrentes.size() + 1] = "Nulos";
            colores[votosPorFrentes.size()] = "#DCDCDC"; colores[votosPorFrentes.size() + 1] = "#A9A9A9";
            int total=0;
            String bla="", nul="";
            for (int i = 0; i < votosPorFrentes.size(); i++) {
                frentes[i] = votosPorFrentes.get(i).get("nombre_frente");
                colores[i] = votosPorFrentes.get(i).get("color");
                bla=votosPorFrentes.get(i).get("blanco_total");
                nul=votosPorFrentes.get(i).get("nulo_total");
            }
            total=Integer.parseInt(nul)+Integer.parseInt(bla);
            for (int i = 0; i < frentes.length; i++) {
                int valor = 0;
                for (Object[] frente : danforas) {
                    if (frentes[i].equals((String)frente[0])) {valor+=(int)frente[3];}
                }
                total+=valor; datos[i]=valor;
                datos[votosPorFrentes.size()]=Integer.parseInt(bla);
                datos[votosPorFrentes.size()+1]=Integer.parseInt(nul);
                valor=0;
            }
            model.addAttribute("blancos", bla);
            model.addAttribute("nulos", nul);
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
