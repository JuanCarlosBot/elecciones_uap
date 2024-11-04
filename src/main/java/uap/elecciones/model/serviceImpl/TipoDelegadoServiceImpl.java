package uap.elecciones.model.serviceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import uap.elecciones.model.dao.TipoDelegadoDao;
import uap.elecciones.model.entity.TipoDelegado;
import uap.elecciones.model.service.TipoDelegadoService;

@Service
public class TipoDelegadoServiceImpl implements TipoDelegadoService{

    @Autowired
    private TipoDelegadoDao dao;

    @Override
    public List<TipoDelegado> findAll() {
        return dao.findAll();
    }

    @Override
    public void save(TipoDelegado entidad) {
        dao.save(entidad);
    }

    @Override
    public TipoDelegado findOne(Long id) {
        return dao.findById(id).orElse(null);
    }

    @Override
    public void delete(Long id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'delete'");
    }
    
}
