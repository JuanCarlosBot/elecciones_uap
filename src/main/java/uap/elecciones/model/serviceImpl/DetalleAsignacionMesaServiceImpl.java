package uap.elecciones.model.serviceImpl;

import java.util.List;

import org.springframework.stereotype.Service;

import uap.elecciones.model.dao.IDetalleAsignacionMesaDao;
import uap.elecciones.model.entity.DetalleAsignacionMesa;
import uap.elecciones.model.service.IDetalleAsignacionMesaService;

@Service
public class DetalleAsignacionMesaServiceImpl implements IDetalleAsignacionMesaService{

    IDetalleAsignacionMesaDao detalleAsignacionMesaDao;

    @Override
    public List<DetalleAsignacionMesa> findAll() {
        // TODO Auto-generated method stub
        return (List<DetalleAsignacionMesa>) detalleAsignacionMesaDao.findAll();
    }

    @Override
    public void save(DetalleAsignacionMesa detalleAsignacionMesa) {
        // TODO Auto-generated method stub
        detalleAsignacionMesaDao.save(detalleAsignacionMesa);
    }

    @Override
    public DetalleAsignacionMesa findOne(Long id) {
        // TODO Auto-generated method stub
        return detalleAsignacionMesaDao.findById(id).orElse(null);
    }

    @Override
    public void delete(Long id) {
        // TODO Auto-generated method stub
        detalleAsignacionMesaDao.deleteById(id);
    }
    
}
