package uap.elecciones.model.service;

import java.util.List;

import org.springframework.data.repository.query.Param;

import uap.elecciones.model.entity.Anfora;


public interface IAnforaService {
    
    public List<Anfora> findAll();

	public void save(Anfora anfora);

	public Anfora findOne(Long id);

	public void delete(Long id);

	List<Object[]> getDatosDeMesaYFrenteYNivelPorIdMesaYNivel(Long idMesa, Long idNivel);

	public Object mesaPorFacultad(Long idMesa);

	public Object votosGenerales(@Param("mesa") String mesa);

	public Object votosGeneral();


}
