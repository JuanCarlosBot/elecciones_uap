package uap.elecciones.model.dao;

import org.springframework.data.repository.CrudRepository;

import uap.elecciones.model.entity.Mesa;

public interface IMesaDao extends CrudRepository<Mesa,Long>{
    
}
