package uap.elecciones.model.service;

import java.util.List;

import uap.elecciones.model.entity.ConteoTotal;



public interface IConteoTotalService {

    public List<ConteoTotal> findAll();

	public void save(ConteoTotal conteoTotal);

	public ConteoTotal findOne(Long id);

	public void delete(Long id);

	public ConteoTotal  conteoTotalBlacoNulosFul(Long idNivel);
}
