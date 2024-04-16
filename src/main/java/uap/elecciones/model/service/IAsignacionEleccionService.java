package uap.elecciones.model.service;

import java.util.List;
import java.util.Map;

import uap.elecciones.model.entity.AsignacionEleccion;

public interface IAsignacionEleccionService {
    public List<AsignacionEleccion> findAll();

	public void save(AsignacionEleccion asignacionEleccion);

	public AsignacionEleccion findOne(Long id);

	public void delete(Long id);

	public List<Map<Object, String>> getListaFrentes(String gestion,Long idNivel,Long idTipo);
}
