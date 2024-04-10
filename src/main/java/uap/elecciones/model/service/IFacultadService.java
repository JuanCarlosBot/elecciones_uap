package uap.elecciones.model.service;

import java.util.List;

import uap.elecciones.model.entity.Facultad;

public interface IFacultadService {
    public List<Facultad> findAll();

	public void save(Facultad facultad);

	public Facultad findOne(Long id);

	public void delete(Long id);
}
