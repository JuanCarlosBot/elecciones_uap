package uap.elecciones.model.dao;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;


import uap.elecciones.model.entity.VotoTotalFrente;

public interface IVotoTotalFrenteDao  extends CrudRepository<VotoTotalFrente,Long>{


    @Query(value = "select vo.id_voto_total_frente, CAST(vo.voto_total_frente AS varchar), vo.id_conteo_total, vo.id_frente, fe.nombre_frente, ni.nombre_nivel from voto_total_frente vo, frente fe, asignacion_eleccion asig, nivel ni where vo.id_frente=fe.id_frente and fe.id_frente=asig.id_frente and asig.id_nivel=ni.id_nivel and ni.id_nivel=?1 ORDER BY fe.id_frente", nativeQuery = true)
    public List<Map<Object, String>> votoTotalFul(Long idNivel);



    @Query(value = "SELECT da.id_detalle_anfora,fe.nombre_frente,me.nombre_mesa,CAST(da.cant_votantes AS varchar) FROM detalle_anfora da, anfora fo, frente fe,mesa me where da.id_frente=?1 and da.id_anfora=fo.id_anfora and da.id_frente=fe.id_frente and fo.id_mesa=me.id_mesa ORDER BY me.id_mesa",nativeQuery = true)
    public List<Map<Object, String>> listaMesaFrente(Long idFrente);

    @Query(value = "select af.cant_voto_blanco,af.cant_voto_nulo,me.nombre_mesa,co.facultad from conteo_total co ,anfora af,mesa me\r\n" + //
            "where co.id_nivel=?1\r\n" + //
            "and co.id_conteo_total=af.id_conteo_total\r\n" + //
            "and af.id_mesa=me.id_mesa order by me.id_mesa",nativeQuery = true)
    public List<Map<Object, String>> listaVotosBlancosNulosPorMesas(Long idNivel);



    
}
