package uap.elecciones.model.serviceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import uap.elecciones.model.dao.AsistenciaDao;
import uap.elecciones.model.entity.Asistencia;
import uap.elecciones.model.service.AsistenciaService;

@Service
public class AsistenciaServiceImpl implements AsistenciaService{

    @Autowired
    private AsistenciaDao dao;

    @Override
    public List<Asistencia> findAll() {
        // TODO Auto-generated method stub
        return dao.findAll();
    }

    @Override
    public void save(Asistencia entidad) {
        // TODO Auto-generated method stub
        dao.save(entidad);
    }

    @Override
    public Asistencia findOne(Long id) {
        // TODO Auto-generated method stub
        return dao.findById(id).orElse(null);
    }

    @Override
    public void delete(Long id) {
        // TODO Auto-generated method stub
        Asistencia asistencia = dao.findById(id).orElse(null);
        asistencia.setEstado("X");
        dao.save(asistencia);
    }

    @Override
    public Asistencia asistenciaPorRdDocente(String rd) {
        // TODO Auto-generated method stub
        return dao.asistenciaPorRdDocente(rd);
    }

    @Override
    public Asistencia asistenciaPorRuEstudiante(String ru) {
        // TODO Auto-generated method stub
        return dao.asistenciaPorRuEstudiante(ru);
    }
    
}
