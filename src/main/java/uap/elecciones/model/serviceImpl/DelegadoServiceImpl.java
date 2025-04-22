package uap.elecciones.model.serviceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import uap.elecciones.model.dao.DelegadoDao;
import uap.elecciones.model.entity.Delegado;
import uap.elecciones.model.service.DelegadoService;

@Service
public class DelegadoServiceImpl implements DelegadoService{

    @Autowired
    private DelegadoDao dao;

    @Override
    public List<Delegado> findAll() {
        return dao.findAll();
    }

    @Override
    public void save(Delegado entidad) {
        dao.save(entidad);
    }

    @Override
    public Delegado findOne(Long id) {
        return dao.findById(id).orElse(null);
    }

    @Override
    public void delete(Long id) {
        
    }

    @Override
    public List<Delegado> listarDelegadosPorIdMesa(Long idMesa) {
        return dao.listarDelegadosPorIdMesa(idMesa);
    }

    @Override
    public void saveAll(List<Delegado> delegados) {
        dao.saveAll(delegados);
    }

    @Override
    public List<Delegado> findAllOrderByMesaAndTipo() {
        return dao.findAllOrderByMesaAndTipo();
    }
    
}
