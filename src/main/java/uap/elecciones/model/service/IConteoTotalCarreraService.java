package uap.elecciones.model.service;

import java.util.List;


import uap.elecciones.model.entity.ConteoTotalCarrera;



public interface IConteoTotalCarreraService {

    public List<ConteoTotalCarrera> findAll();

	public void save(ConteoTotalCarrera conteoTotalCarrera);

	public ConteoTotalCarrera findOne(Long id);

	public void delete(Long id);


}
