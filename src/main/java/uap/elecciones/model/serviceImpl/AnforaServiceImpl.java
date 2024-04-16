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
}
