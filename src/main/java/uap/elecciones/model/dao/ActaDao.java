package uap.elecciones.model.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import uap.elecciones.model.entity.Acta;

public interface ActaDao extends JpaRepository<Acta,Long>{
    
    @Query("SELECT a from Acta a where a.estado != 'X' order by a.mesa.id_mesa asc")
    List<Acta> listarActasPorOdenMesa();

    @Query("SELECT a from Acta a where a.mesa.id_mesa = ?1 and a.estado != 'X'")
    Acta actaPorIdMesa(Long idMesa);

    @Query("SELECT a from Acta a where a.mesa.id_mesa = ?1 and a.mesa.id_mesa != ?2 and a.estado != 'X'")
    Acta actaPorIdMesa2(Long idMesa, Long idMesa2);
}
