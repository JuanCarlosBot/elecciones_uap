package uap.elecciones.model.serviceImpl;

import java.util.List;

import org.springframework.stereotype.Service;

import uap.elecciones.model.dao.IFacultadDao;
import uap.elecciones.model.entity.Facultad;
import uap.elecciones.model.service.IFacultadService;

@Service
public class FacultadServiceImpl implements IFacultadService{

    IFacultadDao facultadDao;

    @Override
    public List<Facultad> findAll() {
        // TODO Auto-generated method stub
        return (List<Facultad>) facultadDao.findAll();
    }

    @Override
    public void save(Facultad facultad) {
        // TODO Auto-generated method stub
        facultadDao.save(facultad);
    }

    @Override
    public Facultad findOne(Long id) {
        // TODO Auto-generated method stub
        return facultadDao.findById(id).orElse(null);
    }

    @Override
    public void delete(Long id) {
        // TODO Auto-generated method stub
        facultadDao.deleteById(id);
    }
    
}
