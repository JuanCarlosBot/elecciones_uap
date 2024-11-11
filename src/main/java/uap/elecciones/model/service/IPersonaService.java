package uap.elecciones.model.service;

import java.util.List;

import uap.elecciones.model.entity.Persona;

public interface IPersonaService {
    public List<Persona> findAll();

	public void save(Persona persona);

	public Persona findOne(Long id);

	public void delete(Long id);

	List<Persona> listarPersonasPorCI(String ci);
}
