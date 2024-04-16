package uap.elecciones.model.serviceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import uap.elecciones.model.dao.IVotoTotalFrenteDao;
import uap.elecciones.model.entity.VotoTotalFrente;
import uap.elecciones.model.service.IVotoTotalFrenteService;

@Service
public class VotoTotalFrenteServiceImpl implements IVotoTotalFrenteService{


     @Autowired
    private IVotoTotalFrenteDao votoTotalFrenteDao;

    @Override
    public List<VotoTotalFrente> findAll() {
     return (List<VotoTotalFrente>) votoTotalFrenteDao.findAll();
    }

    @Override
    public void save(VotoTotalFrente votoTotalFrente) {
        votoTotalFrenteDao.save(votoTotalFrente);
    }

    @Override
    public VotoTotalFrente findOne(Long id) {
        return votoTotalFrenteDao.findById(id).orElse(null);
    }

    @Override
    public void delete(Long id) {
        votoTotalFrenteDao.deleteById(id);
    }
    
}
