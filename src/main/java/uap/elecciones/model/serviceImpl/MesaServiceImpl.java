package uap.elecciones.model.serviceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import uap.elecciones.model.dao.IMesaDao;
import uap.elecciones.model.entity.Mesa;
import uap.elecciones.model.service.IMesaService;

@Service
public class MesaServiceImpl implements IMesaService{

    @Autowired
    private IMesaDao mesaDao;

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

    @Override
    public List<Object[]> lista_mesas_por_carrera(Long id_carrera) {
        // TODO Auto-generated method stub
        return mesaDao.lista_mesas_por_carrera(id_carrera);
    }

    @Override
    public Object mesaPorCarrera(Long idMesa) {
        return mesaDao.mesaPorCarrera(idMesa);
    }

    
    
}
