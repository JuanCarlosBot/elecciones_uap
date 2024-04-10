package uap.elecciones.model.service;

import java.util.List;

import uap.elecciones.model.entity.Docente;

public interface IDocenteService {
    public List<Docente> findAll();

	public void save(Docente docente);

	public Docente findOne(Long id);

	public void delete(Long id);
}
