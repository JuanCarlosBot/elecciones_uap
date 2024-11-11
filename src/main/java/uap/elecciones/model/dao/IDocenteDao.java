package uap.elecciones.model.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import uap.elecciones.model.entity.Docente;
import uap.elecciones.model.entity.DocenteDto;

public interface IDocenteDao extends CrudRepository<Docente,Long>{
    @Query("SELECT d FROM Docente d WHERE d.rd = ?1 AND d.estado != 'X'")
    Docente buscarDocentePorRd(String rd);

    @Query(value = """
        SELECT 
            d.id_docente as idDocente,
            d.rd,
            p.apellidos as nombreCompleto,
            p.ci,
            p.id_persona AS idPersona
        FROM docente d
        LEFT JOIN persona p ON d.id_persona = p.id_persona
        WHERE d.estado != 'X' AND p.ci LIKE %?1%
        LIMIT 5
        """, nativeQuery = true)
    List<Object[]> listarDocentePorCI(String ci);

    @Query(value = "SELECT d FROM Docente d WHERE d.estado != 'X' AND d.persona.id_persona = ?1")
    Docente buscarDocentePorIdPersona(Long idPersona);
}
