package uap.elecciones.model.dao;

import org.springframework.data.repository.CrudRepository;

import uap.elecciones.model.entity.Docente;

public interface IDocenteDao extends CrudRepository<Docente,Long>{
    
}
