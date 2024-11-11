package uap.elecciones.model.service;

import java.util.List;

import uap.elecciones.model.entity.Asistencia;

public interface AsistenciaService {
    public List<Asistencia> findAll();

    public void save(Asistencia entidad);

    public Asistencia findOne(Long id);

    public void delete(Long id);

    Asistencia asistenciaPorRdDocente(String rd);

    Asistencia asistenciaPorRuEstudiante(String ru);
}
