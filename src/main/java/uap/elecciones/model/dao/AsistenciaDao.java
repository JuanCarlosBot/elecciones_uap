package uap.elecciones.model.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import uap.elecciones.model.entity.Asistencia;

public interface AsistenciaDao extends JpaRepository<Asistencia,Long>{
    
    @Query("SELECT a FROM Asistencia a WHERE a.docente.rd = ?1 AND a.estado = 'MARCADO'")
    Asistencia asistenciaPorRdDocente(String rd);

    @Query("SELECT a FROM Asistencia a WHERE a.estudiante.ru = ?1 AND a.estado = 'MARCADO'")
    Asistencia asistenciaPorRuEstudiante(String ru);
    
}
