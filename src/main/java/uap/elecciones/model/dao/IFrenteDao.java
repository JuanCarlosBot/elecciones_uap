package uap.elecciones.model.dao;

import org.springframework.data.repository.CrudRepository;

import uap.elecciones.model.entity.Frente;

public interface IFrenteDao extends CrudRepository<Frente,Long>{
    
}
