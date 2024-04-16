package uap.elecciones.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletRequest;
import uap.elecciones.model.service.IAsignacionHabilitadoService;

@Controller
@RequestMapping(value = "/admin")
public class sorteoController {

    @Autowired
    private IAsignacionHabilitadoService asignacionHabilitadoService;
    
    @RequestMapping(value = "/form_sorteo",method = RequestMethod.POST)
    public String realizar_sorteo(RedirectAttributes flash,
    @RequestParam(name = "ids_lista_asignados",required = false)String ids_lista_asignados,
    @RequestParam(name = "id_car")Long id_car, HttpServletRequest request){
        if (request.getSession().getAttribute("usuario") != null) {
            
            if (ids_lista_asignados != null && !ids_lista_asignados.isEmpty()) {
                try {
                    ObjectMapper mapper = new ObjectMapper();
                    Long[] id_asignacion_habilitado = mapper.readValue(ids_lista_asignados, Long[].class);
                    
                    
                    
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            
            return "redirect:/admin/seleccion_est_carrera/"+id_car;
        } else {
            return "redirect:/login";
        }
    }
}
