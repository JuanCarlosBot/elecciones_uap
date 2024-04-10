package uap.elecciones.model.serviceImpl;

import java.util.List;

import org.springframework.stereotype.Service;

import uap.elecciones.model.dao.ICarreraDao;
import uap.elecciones.model.entity.Carrera;
import uap.elecciones.model.service.ICarreraService;

@Service
public class CarreraServiceImpl implements ICarreraService{

    ICarreraDao carreraDao;

    @Override
    public List<Carrera> findAll() {
        // TODO Auto-generated method stub
        return (List<Carrera>) carreraDao.findAll();
    }

    @Override
    public void save(Carrera carrera) {
        // TODO Auto-generated method stub
        carreraDao.save(carrera);
    }

    @Override
    public Carrera findOne(Long id) {
        // TODO Auto-generated method stub
        return carreraDao.findById(id).orElse(null);
    }

    @Override
    public void delete(Long id) {
        // TODO Auto-generated method stub
        carreraDao.deleteById(id);
    }
    
}
