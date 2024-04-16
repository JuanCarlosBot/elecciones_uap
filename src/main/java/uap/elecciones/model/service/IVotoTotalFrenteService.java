package uap.elecciones.model.service;

import java.util.List;

import uap.elecciones.model.entity.VotoTotalFrente;

public interface IVotoTotalFrenteService {
    
    public List<VotoTotalFrente> findAll();

	public void save(VotoTotalFrente votoTotalFrente);

	public VotoTotalFrente findOne(Long id);

	public void delete(Long id);
}
