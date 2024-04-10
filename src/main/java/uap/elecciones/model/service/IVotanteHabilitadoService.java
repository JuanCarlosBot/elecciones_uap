package uap.elecciones.model.service;

import java.util.List;

import uap.elecciones.model.entity.VotanteHabilitado;

public interface IVotanteHabilitadoService {
    public List<VotanteHabilitado> findAll();

	public void save(VotanteHabilitado votanteHabilitado);

	public VotanteHabilitado findOne(Long id);

	public void delete(Long id);
}
