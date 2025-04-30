package uap.elecciones.model.serviceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import uap.elecciones.model.dao.IAnforaDao;
import uap.elecciones.model.entity.Anfora;
import uap.elecciones.model.service.IAnforaService;

@Service
public class AnforaServiceImpl implements IAnforaService{
    
    @Autowired
    private IAnforaDao anforaDao;

    @Override
    public List<Anfora> findAll() {
         return (List<Anfora>) anforaDao.findAll();
    }

    @Override
    public void save(Anfora anfora) {
        anforaDao.save(anfora);
    }

    @Override
    public Anfora findOne(Long id) {
        return anforaDao.findById(id).orElse(null);
    }

    @Override
    public void delete(Long id) {
        anforaDao.deleteById(id);
    }

    @Override
    public List<Object[]> getDatosDeMesaYFrenteYNivelPorIdMesaYNivel(Long idMesa, Long idNivel) {
        return (List<Object[]>) anforaDao.getDatosDeMesaYFrenteYNivelPorIdMesaYNivel(idMesa, idNivel);
    }

    @Override
    public Object mesaPorFacultad(Long idMesa) {
      return anforaDao.mesaPorFacultad(idMesa);
    }

   
    @Override
    public Object votosGeneral() {
        // TODO Auto-generated method stub
        return anforaDao.votosGeneral();
    }

    @Override
    public Object votosGenerales(boolean esNulo, String nombreMesa) {
        // TODO Auto-generated method stub
        return anforaDao.votosGenerales(esNulo, nombreMesa);
    }

    @Override
    public Object votosGeneralFacultad(Long idFacultad, String sigla, Boolean esNulo) {
        // TODO Auto-generated method stub
        return anforaDao.votosGeneralFacultad(idFacultad, sigla, esNulo);
    }

    @Override
    public Object votosGeneralCarrera(Long id_carrera) {
        // TODO Auto-generated method stub
        return anforaDao.votosGeneralCarrera(id_carrera);
    }

    @Override
    public Object votoGeneralPorfente(Long id_frente) {
        // TODO Auto-generated method stub
        return anforaDao.votoGeneralPorfente(id_frente);
    }


}
