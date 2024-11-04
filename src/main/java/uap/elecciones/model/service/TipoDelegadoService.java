package uap.elecciones.model.service;

import java.util.List;

import uap.elecciones.model.entity.TipoDelegado;

public interface TipoDelegadoService {
    public List<TipoDelegado> findAll();

    public void save(TipoDelegado entidad);

    public TipoDelegado findOne(Long id);

    public void delete(Long id);
}
