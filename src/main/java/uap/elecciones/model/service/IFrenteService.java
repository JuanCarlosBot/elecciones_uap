package uap.elecciones.model.service;

import java.util.List;

import uap.elecciones.model.entity.Frente;

public interface IFrenteService {
    public List<Frente> findAll();

	public void save(Frente frente);

	public Frente findOne(Long id);

	public void delete(Long id);
}
