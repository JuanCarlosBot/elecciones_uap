package uap.elecciones.model.serviceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import uap.elecciones.model.dao.IPersonaDao;
import uap.elecciones.model.entity.Persona;
import uap.elecciones.model.service.IPersonaService;

@Service
public class PersonaServiceImpl implements IPersonaService{

    @Autowired
    private IPersonaDao personaDao;

    @Override
    public List<Persona> findAll() {
        // TODO Auto-generated method stub
        return (List<Persona>) personaDao.findAll();
    }

    @Override
    public void save(Persona persona) {
        // TODO Auto-generated method stub
        personaDao.save(persona);
    }

    @Override
    public Persona findOne(Long id) {
        // TODO Auto-generated method stub
        return personaDao.findById(id).orElse(null);
    }

    @Override
    public void delete(Long id) {
        // TODO Auto-generated method stub
        personaDao.deleteById(id);
    }

    @Override
    public List<Persona> listarPersonasPorCI(String ci) {
        // TODO Auto-generated method stub
        return personaDao.listarPersonasPorCI(ci);
    }
    
}
