package uap.elecciones.model.dao;

import org.springframework.data.repository.CrudRepository;


import uap.elecciones.model.entity.ConteoTotal;

public interface IConteoTotalDao extends CrudRepository<ConteoTotal,Long>{
    
}
