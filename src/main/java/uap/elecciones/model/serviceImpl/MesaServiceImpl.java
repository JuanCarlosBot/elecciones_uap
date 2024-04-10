package uap.elecciones.model.serviceImpl;

import java.util.List;

import org.springframework.stereotype.Service;

import uap.elecciones.model.dao.IMesaDao;
import uap.elecciones.model.entity.Mesa;
import uap.elecciones.model.service.IMesaService;

@Service
public class MesaServiceImpl implements IMesaService{

    IMesaDao mesaDao;

    @Override
    public List<Mesa> findAll() {
        // TODO Auto-generated method stub
        return (List<Mesa>) mesaDao.findAll();
    }

    @Override
    public void save(Mesa mesa) {
        // TODO Auto-generated method stub
        mesaDao.save(mesa);
    }

    @Override
    public Mesa findOne(Long id) {
        // TODO Auto-generated method stub
        return mesaDao.findById(id).orElse(null);
    }

    @Override
    public void delete(Long id) {
        // TODO Auto-generated method stub
        mesaDao.deleteById(id);
    }
    
}
