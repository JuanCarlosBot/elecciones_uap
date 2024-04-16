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
import uap.elecciones.model.entity.Estudiante;
import uap.elecciones.model.entity.Facultad;
import uap.elecciones.model.entity.Mesa;
import uap.elecciones.model.entity.VotanteHabilitado;
import uap.elecciones.model.service.IAsignacionEleccionService;
import uap.elecciones.model.service.IAsignacionHabilitadoService;
import uap.elecciones.model.service.ICarreraService;
import uap.elecciones.model.service.IFacultadService;
import uap.elecciones.model.service.IFrenteService;
import uap.elecciones.model.service.IMesaService;
import uap.elecciones.model.service.IVotanteHabilitadoService;

@Controller
@RequestMapping(value = "/admin")
public class EstadisticasPruebasController {

    @Autowired
    private IAsignacionEleccionService asignacionEleccionService;

    @RequestMapping(value = "/estadisticaPrueba", method = RequestMethod.GET)
    private String Lista_Facultades(Model model, RedirectAttributes flash, HttpServletRequest request,
            @RequestParam(name = "succes", required = false) String succes) {
        if (request.getSession().getAttribute("usuario") != null) {

            List<Map<Object, String>> listaFrentes = asignacionEleccionService.getListaFrentes("2024", 3L, 1L);
            String[] frentes = new String[listaFrentes.size()+2];
            String[] colores = new String[listaFrentes.size()+2];


            for (int i = 0; i < listaFrentes.size(); i++) {
                frentes[i] = listaFrentes.get(i).get("nombre_frente") +" "+ listaFrentes.get(i).get("sigla");
                colores[i] = listaFrentes.get(i).get("color");
            }

            frentes[listaFrentes.size()] = "Blancos";
            frentes[listaFrentes.size()+1] = "Nulos";
            colores[listaFrentes.size()] = "#33FFFC";
            colores[listaFrentes.size()+1] = "#F0FF33";


            int[] datos = new int[5];

            datos[0] = 2000;
            datos[1] = 3000;
            datos[2] = 3000;
            datos[3] = 1000;
            datos[4] = 3000;


            model.addAttribute("datos", datos);
            model.addAttribute("frentes", frentes);
            model.addAttribute("colores", colores);
            return "estadisticaPruebas";
        } else {
            return "redirect:/login";
        }
    }

}