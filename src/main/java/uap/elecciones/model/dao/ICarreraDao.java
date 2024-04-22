package uap.elecciones.model.dao;

import org.springframework.data.repository.CrudRepository;

import uap.elecciones.model.entity.Carrera;

public interface ICarreraDao extends CrudRepository<Carrera,Long>{
    
    
}
