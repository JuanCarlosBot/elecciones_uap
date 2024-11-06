package uap.elecciones.model.service;

import java.util.List;

import uap.elecciones.model.entity.Acta;

public interface ActaService {
    public List<Acta> findAll();

    public void save(Acta entidad);

    public Acta findOne(Long id);

    public void delete(Long id);

    Acta actaPorIdMesa(Long idMesa);

    Acta actaPorIdMesa2(Long idMesa, Long idMesa2);

    List<Acta> listarActasPorOdenMesa();
}
