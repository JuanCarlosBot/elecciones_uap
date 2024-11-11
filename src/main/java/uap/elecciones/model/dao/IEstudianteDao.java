package uap.elecciones.model.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import uap.elecciones.model.entity.Estudiante;

public interface IEstudianteDao extends CrudRepository<Estudiante,Long>{
    
    @Query("SELECT e FROM Estudiante e WHERE e.ru = ?1 AND e.estado != 'X'")
    Estudiante buscarEstudiantePorRu(String ru);

    @Query(value = """
        SELECT 
            e.id_estudiante as idEstudiante,
            e.ru,
            p.apellidos as nombreCompleto,
            p.ci,
            p.id_persona AS idPersona
        FROM estudiante e
        LEFT JOIN persona p ON e.id_persona = p.id_persona
        WHERE e.estado != 'X' AND e.ru LIKE %?1%
        LIMIT 5
        """, nativeQuery = true)
    List<Object[]> listarEstudiantePorRU(String ru);
}
