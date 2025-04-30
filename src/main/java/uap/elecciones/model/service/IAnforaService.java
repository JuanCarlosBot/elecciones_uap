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

	public Object votosGenerales(@Param("esNulo") boolean esNulo, @Param("nombreMesa") String nombreMesa);

	public Object votosGeneral();

	public Object votosGeneralFacultad(@Param("idFacultad") Long idFacultad, @Param("sigla") String sigla , @Param("esNulo")Boolean esNulo);

	public Object votosGeneralCarrera(@Param("Idcarrera")Long id_carrera);

	public Object votoGeneralPorfente(@Param("id_frente")Long id_frente);

}
