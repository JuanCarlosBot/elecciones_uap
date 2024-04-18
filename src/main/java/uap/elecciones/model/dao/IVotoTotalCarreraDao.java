package uap.elecciones.model.dao;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import uap.elecciones.model.entity.VotoTotalCarrera;


public interface IVotoTotalCarreraDao  extends CrudRepository<VotoTotalCarrera,Long>{


    /*@Query(value = "select vo.id_voto_total_frente, CAST(vo.voto_total_frente AS varchar), vo.id_conteo_total, vo.id_frente, fe.nombre_frente, ni.nombre_nivel from voto_total_frente vo, frente fe, asignacion_eleccion asig, nivel ni where vo.id_frente=fe.id_frente and fe.id_frente=asig.id_frente and asig.id_nivel=ni.id_nivel and ni.id_nivel=?1", nativeQuery = true)
    public List<Map<Object, String>> votoTotalFul(Long idNivel);*/
    
}
