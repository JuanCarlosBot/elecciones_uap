package uap.elecciones.model.service;

import java.util.List;

import uap.elecciones.model.entity.AsignacionEleccion;

public interface IAsignacionEleccionService {
    public List<AsignacionEleccion> findAll();

	public void save(AsignacionEleccion asignacionEleccion);

	public AsignacionEleccion findOne(Long id);

	public void delete(Long id);
}
