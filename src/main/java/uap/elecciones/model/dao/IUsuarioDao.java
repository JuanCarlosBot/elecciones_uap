package uap.elecciones.model.dao;

import org.springframework.data.repository.CrudRepository;

import uap.elecciones.model.entity.Usuario;

public interface IUsuarioDao extends CrudRepository<Usuario,Long>{
    
}
