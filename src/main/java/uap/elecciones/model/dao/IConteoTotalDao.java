package uap.elecciones.model.dao;


import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;


import uap.elecciones.model.entity.ConteoTotal;

public interface IConteoTotalDao extends CrudRepository<ConteoTotal,Long>{



    @Query(value = "select co.* from conteo_total co where co.id_nivel=?1",nativeQuery=true)
   public ConteoTotal  conteoTotalBlacoNulosFul(Long idNivel);


   @Query(value = "select co.* from conteo_total co where co.id_nivel=?1",nativeQuery=true)
   public List<ConteoTotal>conteoBotosByNAcjyp(Long idNivel);
}
