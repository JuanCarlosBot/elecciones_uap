package uap.elecciones.model.dao;

import org.springframework.data.repository.CrudRepository;

import uap.elecciones.model.entity.DetalleAsignacionMesa;

public interface IDetalleAsignacionMesaDao extends CrudRepository<DetalleAsignacionMesa,Long>{
    
}
