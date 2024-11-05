package uap.elecciones.model.service;

import java.util.List;

import uap.elecciones.model.entity.Facultad;
import uap.elecciones.model.entity.Mesa;

public interface IMesaService {
    public List<Mesa> findAll();

	public void save(Mesa mesa);

	public Mesa findOne(Long id);

	public void delete(Long id);

	public List<Object[]> lista_mesas_por_carrera(Long id_carrera);

	public List<Object[]> lista_mesas_por_facultad_docente(Long id_facultad);

	Object mesaPorCarrera(Long idMesa);

	public List<Mesa>mesasPorCarrera(Long idCarrera);

	List<Mesa> listarMesasPorIdFacultad(Long idMesa);

	List<Mesa> listarMesasOrdenadas();

}
