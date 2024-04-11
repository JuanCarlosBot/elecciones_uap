package uap.elecciones.model.serviceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import uap.elecciones.model.dao.IUniversidadDao;
import uap.elecciones.model.entity.Universidad;
import uap.elecciones.model.service.IUniversidadService;

@Service
public class UniversidadServiceImpl implements IUniversidadService{

    @Autowired
    private IUniversidadDao universidadDao;

    @Override
    public List<Universidad> findAll() {
        // TODO Auto-generated method stub
        return (List<Universidad>) universidadDao.findAll();
    }

    @Override
    public void save(Universidad universidad) {
        // TODO Auto-generated method stub
        universidadDao.save(universidad);
    }

    @Override
    public Universidad findOne(Long id) {
        // TODO Auto-generated method stub
        return universidadDao.findById(id).orElse(null);
    }

    @Override
    public void delete(Long id) {
        // TODO Auto-generated method stub
        universidadDao.deleteById(id);
    }
    
}
