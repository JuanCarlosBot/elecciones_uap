package uap.elecciones.model.service;

import java.util.List;

import uap.elecciones.model.entity.TipoEleccion;

public interface ITipoEleccionService {
    public List<TipoEleccion> findAll();

	public void save(TipoEleccion tipoEleccion);

	public TipoEleccion findOne(Long id);

	public void delete(Long id);
}
