package uap.elecciones.model.dao;

import org.springframework.data.repository.CrudRepository;

import uap.elecciones.model.entity.Universidad;

public interface IUniversidadDao extends CrudRepository<Universidad,Long>{
    
}
