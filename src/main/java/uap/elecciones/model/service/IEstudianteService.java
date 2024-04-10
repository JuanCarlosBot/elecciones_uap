package uap.elecciones.model.service;

import java.util.List;

import uap.elecciones.model.entity.Estudiante;

public interface IEstudianteService {
    public List<Estudiante> findAll();

	public void save(Estudiante estudiante);

	public Estudiante findOne(Long id);

	public void delete(Long id);
}
