package uap.elecciones.model.dao;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import uap.elecciones.model.entity.AsignacionEleccion;

public interface IAsignacionEleccionDao extends CrudRepository<AsignacionEleccion, Long> {

    @Query(value = "select ae.id_asignacion_eleccion,f.nombre_frente,f.color,f.sigla from asignacion_eleccion ae,nivel n , tipo_eleccion te, frente f where ae.estado='A' AND ae.gestion=?1 AND ae.id_frente=f.id_frente and ae.id_nivel=n.id_nivel and n.id_nivel=?2 and ae.id_tipo_eleccion=te.id_tipo_eleccion and ae.id_tipo_eleccion=?3 ORDER BY f.id_frente", nativeQuery = true)
    public List<Map<Object, String>> getListaFrentes(String gestion, Long idNivel, Long idTipo);
}
