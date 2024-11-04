package uap.elecciones.model.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import uap.elecciones.model.entity.Delegado;

public interface DelegadoDao extends JpaRepository<Delegado,Long>{
    @Query("select d from Delegado d where d.mesa.id_mesa = ?1")
    List<Delegado> listarDelegadosPorIdMesa(Long idMesa);
}
