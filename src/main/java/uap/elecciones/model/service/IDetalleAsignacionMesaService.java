package uap.elecciones.model.service;

import java.util.List;

import uap.elecciones.model.entity.DetalleAsignacionMesa;

public interface IDetalleAsignacionMesaService {
    public List<DetalleAsignacionMesa> findAll();

	public void save(DetalleAsignacionMesa detalleAsignacionMesa);

	public DetalleAsignacionMesa findOne(Long id);

	public void delete(Long id);
}
