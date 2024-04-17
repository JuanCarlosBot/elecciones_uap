package uap.elecciones.model.service;

import java.util.List;

import uap.elecciones.model.entity.AsignacionHabilitado;

public interface IAsignacionHabilitadoService {
    public List<AsignacionHabilitado> findAll();

	public void save(AsignacionHabilitado asignacionHabilitado);

	public AsignacionHabilitado findOne(Long id);

	public void delete(Long id);

	List<Object[]> lista_asignados_habilitados(Long id_carrera);

	List<Object[]> lista_asignados_delegados();


}
