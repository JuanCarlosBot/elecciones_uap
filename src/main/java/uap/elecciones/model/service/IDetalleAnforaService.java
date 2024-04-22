package uap.elecciones.model.service;

import java.util.List;
import java.util.Map;

import uap.elecciones.model.entity.DetalleAnfora;

public interface IDetalleAnforaService {

     public List<DetalleAnfora> findAll();

	public void save(DetalleAnfora detalleAnfora);

	public DetalleAnfora findOne(Long id);

	public void delete(Long id);

	List<Map<Object, String>> listaVotosPorCarrera(Long idCarrera);
}
