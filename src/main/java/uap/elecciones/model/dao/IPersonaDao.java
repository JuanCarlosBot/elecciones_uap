package uap.elecciones.model.dao;

import org.springframework.data.repository.CrudRepository;

import uap.elecciones.model.entity.Persona;

public interface IPersonaDao extends CrudRepository<Persona,Long>{
    
}
