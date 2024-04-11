package uap.elecciones.model.serviceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import uap.elecciones.model.dao.INivelDao;
import uap.elecciones.model.entity.Nivel;
import uap.elecciones.model.service.INivelService;

@Service
public class NivelServiceImpl implements INivelService{

    @Autowired
    private INivelDao nivelDao;

    @Override
    public List<Nivel> findAll() {
        // TODO Auto-generated method stub
        return (List<Nivel>) nivelDao.findAll();
    }

    @Override
    public void save(Nivel nivel) {
        // TODO Auto-generated method stub
        nivelDao.save(nivel);
    }

    @Override
    public Nivel findOne(Long id) {
        // TODO Auto-generated method stub
        return nivelDao.findById(id).orElse(null);
    }

    @Override
    public void delete(Long id) {
        // TODO Auto-generated method stub
        nivelDao.deleteById(id);
    }
    
}
