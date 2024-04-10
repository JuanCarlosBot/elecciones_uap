package uap.elecciones.model.service;

import java.util.List;

import uap.elecciones.model.entity.Nivel;

public interface INivelService {
    public List<Nivel> findAll();

	public void save(Nivel nivel);

	public Nivel findOne(Long id);

	public void delete(Long id);
}
