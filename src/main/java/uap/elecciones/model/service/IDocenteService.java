package uap.elecciones.model.service;

import java.util.List;

import uap.elecciones.model.entity.Docente;
import uap.elecciones.model.entity.DocenteDto;

public interface IDocenteService {
    public List<Docente> findAll();

	public void save(Docente docente);

	public Docente findOne(Long id);

	public void delete(Long id);

	Docente buscarDocentePorRd(String rd);

	List<Object[]> listarDocentePorCI(String ci);

	Docente buscarDocentePorIdPersona(Long id);
}
