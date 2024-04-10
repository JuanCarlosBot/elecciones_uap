package uap.elecciones.model.dao;

import org.springframework.data.repository.CrudRepository;

import uap.elecciones.model.entity.AsignacionEleccion;

public interface IAsignacionEleccionDao extends CrudRepository<AsignacionEleccion,Long>{
    
}
