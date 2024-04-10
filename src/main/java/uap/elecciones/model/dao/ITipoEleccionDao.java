package uap.elecciones.model.dao;

import org.springframework.data.repository.CrudRepository;

import uap.elecciones.model.entity.TipoEleccion;

public interface ITipoEleccionDao  extends CrudRepository<TipoEleccion,Long>{
    
}
