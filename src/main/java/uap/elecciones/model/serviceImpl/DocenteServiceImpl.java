package uap.elecciones.model.serviceImpl;

import java.util.List;

import org.springframework.stereotype.Service;

import uap.elecciones.model.dao.IDocenteDao;
import uap.elecciones.model.entity.Docente;
import uap.elecciones.model.service.IDocenteService;

@Service
public class DocenteServiceImpl implements IDocenteService{

    IDocenteDao docenteDao;

    @Override
    public List<Docente> findAll() {
        // TODO Auto-generated method stub
        return (List<Docente>) docenteDao.findAll();
    }

    @Override
    public void save(Docente docente) {
        // TODO Auto-generated method stub
        docenteDao.save(docente);
    }

    @Override
    public Docente findOne(Long id) {
        // TODO Auto-generated method stub
        return docenteDao.findById(id).orElse(null);
    }

    @Override
    public void delete(Long id) {
        // TODO Auto-generated method stub
        docenteDao.deleteById(id);
    }
    
}
