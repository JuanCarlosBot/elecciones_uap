package uap.elecciones.model.service;

import java.util.List;

import uap.elecciones.model.entity.Carrera;

public interface ICarreraService {
    public List<Carrera> findAll();

	public void save(Carrera carrera);

	public Carrera findOne(Long id);

	public void delete(Long id);
}
