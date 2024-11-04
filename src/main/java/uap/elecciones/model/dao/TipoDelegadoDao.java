package uap.elecciones.model.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import uap.elecciones.model.entity.TipoDelegado;

public interface TipoDelegadoDao extends JpaRepository<TipoDelegado,Long>{
    
}
