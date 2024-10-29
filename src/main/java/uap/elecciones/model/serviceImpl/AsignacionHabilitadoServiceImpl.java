package uap.elecciones.model.serviceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import uap.elecciones.model.dao.IAsignacionHabilitadoDao;
import uap.elecciones.model.entity.AsignacionHabilitado;
import uap.elecciones.model.service.IAsignacionHabilitadoService;

@Service
public class AsignacionHabilitadoServiceImpl implements IAsignacionHabilitadoService{

    @Autowired
    private IAsignacionHabilitadoDao asignacionHabilitadoDao;

    @Override
    public List<AsignacionHabilitado> findAll() {
        // TODO Auto-generated method stub
        return (List<AsignacionHabilitado>) asignacionHabilitadoDao.findAll();
    }

    @Override
    public void save(AsignacionHabilitado asignacionHabilitado) {
        // TODO Auto-generated method stub
        asignacionHabilitadoDao.save(asignacionHabilitado);
    }

    @Override
    public AsignacionHabilitado findOne(Long id) {
        // TODO Auto-generated method stub
        return asignacionHabilitadoDao.findById(id).orElse(null);
    }

    @Override
    public void delete(Long id) {
        // TODO Auto-generated method stub
        asignacionHabilitadoDao.deleteById(id);
    }

    @Override
    public List<Object[]> lista_asignados_habilitados(Long id_carrera) {
        // TODO Auto-generated method stub
        return asignacionHabilitadoDao.lista_asignados_habilitados(id_carrera);
    }

    @Override
    public List<Object[]> lista_asignados_delegados() {
        // TODO Auto-generated method stub
        return asignacionHabilitadoDao.lista_asignados_delegados();
    }

    @Override
    public Object asignado_habilitado(String ru) {
        // TODO Auto-generated method stub
        return asignacionHabilitadoDao.asignado_habilitado(ru);
    }

    @Override
    public List<Object[]> lista_asignados_habilitadosF(Long id_facultad) {
        return (List<Object[]>) asignacionHabilitadoDao.lista_asignados_habilitadosF(id_facultad);
    }

    @Override
    public List<Object[]> lista_asignacion_por_mesa(Long id_facultadd) {
        // TODO Auto-generated method stub
        return asignacionHabilitadoDao.lista_asignacion_por_mesa(id_facultadd);
    }

    @Override
    public Object asignado_habilitadoDocente(String rd) {
        return asignacionHabilitadoDao.asignado_habilitadoDocente(rd);
    }

    @Override
    public List<Object[]> lista_asignados_delegados_docentes() {
        return asignacionHabilitadoDao.lista_asignados_delegados_docentes();
    }

    

}
