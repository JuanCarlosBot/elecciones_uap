package uap.elecciones.model.dao;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;



import uap.elecciones.model.entity.ConteoTotalCarrera;

public interface IConteoTotalCarreraDao extends CrudRepository<ConteoTotalCarrera,Long>{



   /*  @Query(value = "select co.* from conteo_total co where co.id_nivel=?1",nativeQuery=true)
   public ConteoTotal  conteoTotalBlacoNulosFul(Long idNivel);*/
}
