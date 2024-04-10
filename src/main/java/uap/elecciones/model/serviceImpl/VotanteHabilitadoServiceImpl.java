package uap.elecciones.model.serviceImpl;

import java.util.List;

import org.springframework.stereotype.Service;

import uap.elecciones.model.dao.IVotanteHabilitadoDao;
import uap.elecciones.model.entity.VotanteHabilitado;
import uap.elecciones.model.service.IVotanteHabilitadoService;

@Service
public class VotanteHabilitadoServiceImpl implements IVotanteHabilitadoService{

    IVotanteHabilitadoDao votanteHabilitadoDao;

    @Override
    public List<VotanteHabilitado> findAll() {
        // TODO Auto-generated method stub
        return (List<VotanteHabilitado>) votanteHabilitadoDao.findAll();
    }

    @Override
    public void save(VotanteHabilitado votanteHabilitado) {
        // TODO Auto-generated method stub
        votanteHabilitadoDao.save(votanteHabilitado);
    }

    @Override
    public VotanteHabilitado findOne(Long id) {
        // TODO Auto-generated method stub
        return votanteHabilitadoDao.findById(id).orElse(null);
    }

    @Override
    public void delete(Long id) {
        // TODO Auto-generated method stub
        votanteHabilitadoDao.deleteById(id);
    }
    
}
