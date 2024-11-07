package uap.elecciones.model.service;

import java.util.List;

import uap.elecciones.model.entity.AsignacionHabilitado;
import uap.elecciones.model.entity.DelegadoDto;

public interface IAsignacionHabilitadoService {
    public List<AsignacionHabilitado> findAll();

	public void save(AsignacionHabilitado asignacionHabilitado);

	public AsignacionHabilitado findOne(Long id);

	public void delete(Long id);

	List<Object[]> lista_asignados_habilitados(Long id_carrera);

	List<Object[]> lista_asignados_habilitadosF(Long id_facultad);

	List<Object[]> lista_asignacion_por_mesa(Long id_facultad);

	List<Object[]> lista_asignados_delegados();

	List<Object[]> lista_asignados_delegados_docentes();

	Object asignado_habilitado(String ru);

	Object asignado_habilitadoDocente(String rd);

	List<Object[]> listarDelegadosPorMesa(Long idMesa);
	List<Long> listaDocentesFac(Long id_fac);


	List<AsignacionHabilitado> listaHabilitadosMesas(Long id_mesa);

	public List<Object[]> lista_votantes_por_mesa(Long id_mesa);

}
