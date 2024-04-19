package uap.elecciones.model.service;

import java.util.List;
import java.util.Map;

import uap.elecciones.model.entity.VotoTotalFrente;

public interface IVotoTotalFrenteService {
    
    public List<VotoTotalFrente> findAll();

	public void save(VotoTotalFrente votoTotalFrente);

	public VotoTotalFrente findOne(Long id);

	public void delete(Long id);

	public List<Map<Object, String>> votoTotalFul(Long idNivel);

	public List<Map<Object, String>> listaMesaFrente(Long idFrente);

	public List<Map<Object, String>> listaVotosBlancosNulosPorMesas(Long idNivel);
}
