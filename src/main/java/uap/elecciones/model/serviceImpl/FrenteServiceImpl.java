package uap.elecciones.model.serviceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import uap.elecciones.model.dao.IFrenteDao;
import uap.elecciones.model.entity.Frente;
import uap.elecciones.model.service.IFrenteService;

@Service
public class FrenteServiceImpl implements IFrenteService{

    @Autowired
    private IFrenteDao frenteDao;

    @Override
    public List<Frente> findAll() {
        // TODO Auto-generated method stub
        return (List<Frente>) frenteDao.findAll();
    }

    @Override
    public void save(Frente frente) {
        // TODO Auto-generated method stub
        frenteDao.save(frente);
    }

    @Override
    public Frente findOne(Long id) {
        // TODO Auto-generated method stub
        return frenteDao.findById(id).orElse(null);
    }

    @Override
    public void delete(Long id) {
        // TODO Auto-generated method stub
        frenteDao.deleteById(id);
    }

    @Override
    public List<Object[]> frentesPorMesaYNivel(Long id_mesa, Long id_nivel) {
        return (List<Object[]>) frenteDao.frentesPorMesaYNivel(id_mesa, id_nivel);
    }

}
