package uap.elecciones.model.dao;

import org.springframework.data.repository.CrudRepository;

import uap.elecciones.model.entity.Estudiante;

public interface IEstudianteDao extends CrudRepository<Estudiante,Long>{
    
}
