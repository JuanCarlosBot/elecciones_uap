package uap.elecciones.model.service;

import java.util.List;

import uap.elecciones.model.entity.Delegado;
import uap.elecciones.model.entity.DelegadoDto;

public interface DelegadoService {
    public List<Delegado> findAll();

    public void save(Delegado entidad);

    public Delegado findOne(Long id);

    public void delete(Long id);

    List<Delegado> listarDelegadosPorIdMesa(Long idMesa);
}
