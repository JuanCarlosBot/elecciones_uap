package uap.elecciones.model.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import uap.elecciones.model.entity.Persona;

public interface IPersonaDao extends CrudRepository<Persona, Long> {

    @Query(value = """
            SELECT * FROM persona p WHERE p.ci LIKE %?1% limit 5
            """, nativeQuery = true)
    List<Persona> listarPersonasPorCI(String ci);

}
