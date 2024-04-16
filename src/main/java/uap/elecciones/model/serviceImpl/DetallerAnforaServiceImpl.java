package uap.elecciones.model.serviceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import uap.elecciones.model.dao.IDetalleAnforaDao;

import uap.elecciones.model.entity.DetalleAnfora;
import uap.elecciones.model.service.IDetalleAnforaService;

@Service
public class DetallerAnforaServiceImpl implements IDetalleAnforaService {
    
    @Autowired
    private IDetalleAnforaDao detalleAnforaDao;

    @Override
    public List<DetalleAnfora> findAll() {
       return (List<DetalleAnfora>) detalleAnforaDao.findAll();
    }

    @Override
    public void save(DetalleAnfora detalleAnfora) {
        detalleAnforaDao.save(detalleAnfora);
    }

    @Override
    public DetalleAnfora findOne(Long id) {
        return detalleAnforaDao.findById(id).orElse(null);
    }

    @Override
    public void delete(Long id) {
        detalleAnforaDao.deleteById(id);
    }
    
}
