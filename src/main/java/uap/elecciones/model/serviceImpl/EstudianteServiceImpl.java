package uap.elecciones.model.serviceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import uap.elecciones.model.dao.IEstudianteDao;
import uap.elecciones.model.entity.Estudiante;
import uap.elecciones.model.service.IEstudianteService;

@Service
public class EstudianteServiceImpl implements IEstudianteService{

    @Autowired
    private IEstudianteDao estudianteDao;

    @Override
    public List<Estudiante> findAll() {
        // TODO Auto-generated method stub
        return (List<Estudiante>) estudianteDao.findAll();
    }

    @Override
    public void save(Estudiante estudiante) {
        // TODO Auto-generated method stub
        estudianteDao.save(estudiante);
    }

    @Override
    public Estudiante findOne(Long id) {
        // TODO Auto-generated method stub
        return estudianteDao.findById(id).orElse(null);
    }

    @Override
    public void delete(Long id) {
        // TODO Auto-generated method stub
        estudianteDao.deleteById(id);
    }
    
}
