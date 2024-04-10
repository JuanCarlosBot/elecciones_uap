package uap.elecciones.model.service;

import java.util.List;

import uap.elecciones.model.entity.Universidad;

public interface IUniversidadService {
    public List<Universidad> findAll();

	public void save(Universidad universidad);

	public Universidad findOne(Long id);

	public void delete(Long id);
}
