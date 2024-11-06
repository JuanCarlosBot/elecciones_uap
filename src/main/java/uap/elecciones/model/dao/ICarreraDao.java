package uap.elecciones.model.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import uap.elecciones.model.entity.Carrera;

public interface ICarreraDao extends CrudRepository<Carrera,Long>{
    
    @Query(value = "select c.* from carrera c where c.id_facultad = ?1",nativeQuery = true)
    public List<Carrera> listaCarrerasPorFacultad(Long id_facultad);
    
}
