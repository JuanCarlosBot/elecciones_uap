package uap.elecciones.model.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import uap.elecciones.model.entity.Frente;

public interface IFrenteDao extends CrudRepository<Frente,Long>{

    @Query(value = "SELECT f.id_frente, m.nombre_mesa, f.nombre_frente, n.nombre_nivel " +
    "FROM mesa m " +
    "LEFT JOIN detalle_asignacion_mesa dam ON m.id_mesa = dam.id_mesa " +
    "LEFT JOIN asignacion_eleccion ae ON dam.id_asignacion_eleccion = ae.id_asignacion_eleccion " +
    "LEFT JOIN nivel n ON ae.id_nivel = n.id_nivel " +
    "LEFT JOIN frente f ON ae.id_frente = f.id_frente " +
    "WHERE m.id_mesa = ?1 AND n.id_nivel = ?2", nativeQuery = true)
    public List<Object[]> frentesPorMesaYNivel(Long id_mesa, Long id_nivel);
    
}
