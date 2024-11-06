package uap.elecciones.model.serviceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import uap.elecciones.model.dao.ActaDao;
import uap.elecciones.model.entity.Acta;
import uap.elecciones.model.service.ActaService;

@Service
public class ActaServiceImpl implements ActaService{

     @Autowired
     private ActaDao dao;

    @Override
    public List<Acta> findAll() {
        // TODO Auto-generated method stub
        return (List<Acta>) dao.findAll();
    }

    @Override
    public void save(Acta entidad) {
        // TODO Auto-generated method stub
        dao.save(entidad);
    }

    @Override
    public Acta findOne(Long id) {
        // TODO Auto-generated method stub
        return dao.findById(id).orElse(null);
    }

    @Override
    public void delete(Long id) {
        // TODO Auto-generated method stub
        Acta acta = dao.findById(id).orElse(null);
        acta.setEstado("X");
        dao.save(acta);
    }

    @Override
    public Acta actaPorIdMesa(Long idMesa) {
        return dao.actaPorIdMesa(idMesa);
    }

    @Override
    public Acta actaPorIdMesa2(Long idMesa, Long idMesa2) {
        return dao.actaPorIdMesa2(idMesa, idMesa2);
    }

    @Override
    public List<Acta> listarActasPorOdenMesa() {
        return dao.listarActasPorOdenMesa();
    }
    
}
