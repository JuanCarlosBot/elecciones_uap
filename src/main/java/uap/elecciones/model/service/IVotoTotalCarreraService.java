package uap.elecciones.model.service;

import java.util.List;
import java.util.Map;

import uap.elecciones.model.entity.VotoTotalCarrera;


public interface IVotoTotalCarreraService {
    
    public List<VotoTotalCarrera> findAll();

	public void save(VotoTotalCarrera votoTotalCarrera);

	public VotoTotalCarrera findOne(Long id);

	public void delete(Long id);


}
