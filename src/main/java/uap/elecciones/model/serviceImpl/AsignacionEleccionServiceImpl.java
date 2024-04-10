package uap.elecciones.model.serviceImpl;

import java.util.List;

import org.springframework.stereotype.Service;

import uap.elecciones.model.dao.IAsignacionEleccionDao;
import uap.elecciones.model.entity.AsignacionEleccion;
import uap.elecciones.model.service.IAsignacionEleccionService;

@Service
public class AsignacionEleccionServiceImpl implements IAsignacionEleccionService{

    IAsignacionEleccionDao asignacionEleccionDao;

    @Override
    public List<AsignacionEleccion> findAll() {
        // TODO Auto-generated method stub
        return (List<AsignacionEleccion>) asignacionEleccionDao.findAll();
    }

    @Override
    public void save(AsignacionEleccion asignacionEleccion) {
        // TODO Auto-generated method stub
        asignacionEleccionDao.save(asignacionEleccion);
    }

    @Override
    public AsignacionEleccion findOne(Long id) {
        // TODO Auto-generated method stub
        return asignacionEleccionDao.findById(id).orElse(null);
    }

    @Override
    public void delete(Long id) {
        // TODO Auto-generated method stub
        asignacionEleccionDao.deleteById(id);
    }
    
}
