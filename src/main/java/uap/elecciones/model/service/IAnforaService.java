package uap.elecciones.model.service;

import java.util.List;

import uap.elecciones.model.entity.Anfora;


public interface IAnforaService {
    
    public List<Anfora> findAll();

	public void save(Anfora anfora);

	public Anfora findOne(Long id);

	public void delete(Long id);

	List<Object[]> getDatosDeMesaYFrenteYNivelPorIdMesaYNivel(Long idMesa, Long idNivel);
}
