package uap.elecciones.model.service;

import java.util.List;

import uap.elecciones.model.entity.Mesa;

public interface IMesaService {
    public List<Mesa> findAll();

	public void save(Mesa mesa);

	public Mesa findOne(Long id);

	public void delete(Long id);
}
