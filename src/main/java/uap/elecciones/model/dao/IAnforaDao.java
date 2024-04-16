package uap.elecciones.model.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import uap.elecciones.model.entity.Anfora;



public interface IAnforaDao extends CrudRepository<Anfora,Long>{
    
    @Query(value = "select m.nombre_mesa, f.nombre_frente, n.nombre_nivel " +
               "from mesa m " +
               "left join detalle_asignacion_mesa d on m.id_mesa = d.id_mesa " +
               "left join asignacion_eleccion ae on d.id_asignacion_eleccion = ae.id_asignacion_eleccion " +
               "left join frente f on ae.id_frente = f.id_frente " +
               "left join nivel n on ae.id_nivel = n.id_nivel " +
               "where m.id_mesa = ?1 and n.id_nivel = ?2", nativeQuery = true)
List<Object[]> getDatosDeMesaYFrenteYNivelPorIdMesaYNivel(Long idMesa, Long idNivel);

}
