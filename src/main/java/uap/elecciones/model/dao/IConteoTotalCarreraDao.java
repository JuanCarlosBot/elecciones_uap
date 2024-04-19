package uap.elecciones.model.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import uap.elecciones.model.entity.ConteoTotal;
import uap.elecciones.model.entity.ConteoTotalCarrera;

public interface IConteoTotalCarreraDao extends CrudRepository<ConteoTotalCarrera,Long>{


@Query(value = "select ct.* from conteo_total_carrera ct\n" + //
            "left join anfora a ON a.id_conteo_total_carrera = ct.id_conteo_total\n" + //
            "left join mesa me ON me.id_mesa = a.id_mesa\n" + //
            "left join facultad f on me.if_facultad = f.id_facultad\n" + //
            "where f.id_facultad = ?1 AND ct.carrera NOT LIKE '%FULL%'",nativeQuery=true)
   public List<ConteoTotalCarrera>conteoTotalCarreraPorFacultad(Long idFacultad);



   @Query(value = "SELECT *\n" + //
                  "FROM conteo_total_carrera\n" + //
                  "WHERE carrera LIKE '%FULL%';",nativeQuery=true)
   public List<ConteoTotalCarrera>conteoTotalCarreraPorFull(Long idFacultad);
   
}
