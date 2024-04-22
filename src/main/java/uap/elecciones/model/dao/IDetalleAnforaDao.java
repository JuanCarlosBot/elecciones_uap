package uap.elecciones.model.dao;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import uap.elecciones.model.entity.DetalleAnfora;
import uap.elecciones.model.entity.Mesa;



public interface IDetalleAnforaDao extends CrudRepository<DetalleAnfora,Long>{
    

     @Query(value = "SELECT f.nombre_frente,CAST(f.id_frente as varchar), SUM(da.cant_votantes) AS voto_t_fre, f.color, CAST(ctc.blanco_total as varchar), CAST(ctc.nulo_total as varchar) \n" + //
                  "               FROM detalle_anfora da \n" + //
                  "               LEFT JOIN anfora a ON a.id_anfora = da.id_anfora\n" + //
                  "               LEFT JOIN conteo_total_carrera ctc ON ctc.id_conteo_total = a.id_conteo_total_carrera\n" + //
                  "               LEFT JOIN mesa m ON a.id_mesa = m.id_mesa\n" + //
                  "               LEFT JOIN frente f ON da.id_frente = f.id_frente\n" + //
                  "               LEFT JOIN carrera c ON m.id_carrera = c.id_carrera\n" + //
                  "               WHERE c.id_carrera = ?1 AND ctc.carrera NOT LIKE '%FULL%'\n" + //
                  "               GROUP BY f.nombre_frente, f.id_frente, ctc.blanco_total, ctc.nulo_total order by f.nombre_frente asc ", nativeQuery = true)
    List<Map<Object, String>> listaVotosPorCarrera(Long idCarrera);


    @Query(value="SELECT f.nombre_frente, f.id_frente, m.nombre_mesa, da.cant_votantes\n" + //
                "               FROM detalle_anfora da \n" + //
                "               LEFT JOIN anfora a ON a.id_anfora = da.id_anfora\n" + //
                "               LEFT JOIN conteo_total_carrera ctc ON ctc.id_conteo_total = a.id_conteo_total_carrera\n" + //
                "               LEFT JOIN frente f ON da.id_frente = f.id_frente\n" + //
                "               LEFT JOIN mesa m ON a.id_mesa = m.id_mesa\n" + //
                "               where ctc.carrera = ?1 ORDER BY m.nombre_mesa", nativeQuery = true)
    List<Object[]> listaVotosPorCarreraFrente(String nombre_carrera);


}
