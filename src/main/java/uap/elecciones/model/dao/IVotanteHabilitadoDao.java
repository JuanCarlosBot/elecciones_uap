package uap.elecciones.model.dao;

import org.springframework.data.repository.CrudRepository;

import uap.elecciones.model.entity.VotanteHabilitado;

public interface IVotanteHabilitadoDao extends CrudRepository<VotanteHabilitado,Long>{
    
}
