package uap.elecciones.model.serviceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import uap.elecciones.model.dao.ITipoEleccionDao;
import uap.elecciones.model.entity.TipoEleccion;
import uap.elecciones.model.service.ITipoEleccionService;

@Service
public class TipoEleccionServiceImpl implements ITipoEleccionService{

    @Autowired
    private ITipoEleccionDao tipoEleccionDao;

    @Override
    public List<TipoEleccion> findAll() {
        // TODO Auto-generated method stub
        return (List<TipoEleccion>) tipoEleccionDao.findAll();
    }

    @Override
    public void save(TipoEleccion tipoEleccion) {
        // TODO Auto-generated method stub
        tipoEleccionDao.save(tipoEleccion);
    }

    @Override
    public TipoEleccion findOne(Long id) {
        // TODO Auto-generated method stub
        return tipoEleccionDao.findById(id).orElse(null);
    }

    @Override
    public void delete(Long id) {
        // TODO Auto-generated method stub
        tipoEleccionDao.deleteById(id);
    }
    
}
