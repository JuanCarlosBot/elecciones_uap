package uap.elecciones.model.serviceImpl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import uap.elecciones.model.dao.IVotoTotalCarreraDao;
import uap.elecciones.model.entity.VotoTotalCarrera;
import uap.elecciones.model.service.IVotoTotalCarreraService;


@Service
public class VotoTotalCarreraServiceImpl implements IVotoTotalCarreraService{


     @Autowired
    private IVotoTotalCarreraDao votoTotalCarreraDao;

    @Override
    public List<VotoTotalCarrera> findAll() {
     return (List<VotoTotalCarrera>) votoTotalCarreraDao.findAll();
    }

    @Override
    public void save(VotoTotalCarrera votoTotalCarrera) {
        votoTotalCarreraDao.save(votoTotalCarrera);
    }

    @Override
    public VotoTotalCarrera findOne(Long id) {
        return votoTotalCarreraDao.findById(id).orElse(null);
    }

    @Override
    public void delete(Long id) {
        votoTotalCarreraDao.deleteById(id);
    }


    
}
