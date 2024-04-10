package uap.elecciones.model.dao;

import org.springframework.data.repository.CrudRepository;

import uap.elecciones.model.entity.Facultad;

public interface IFacultadDao extends CrudRepository<Facultad,Long>{
    
}
