package uap.elecciones.model.serviceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import uap.elecciones.model.dao.IConteoTotalDao;
import uap.elecciones.model.entity.ConteoTotal;
import uap.elecciones.model.service.IConteoTotalService;

@Service
public class ConteoTotalServiceImpl implements IConteoTotalService{
    
@Autowired
private IConteoTotalDao conteoTotalDao;

@Override
public List<ConteoTotal> findAll() {
     return (List<ConteoTotal>) conteoTotalDao.findAll();
}

@Override
public void save(ConteoTotal conteoTotal) {
    conteoTotalDao.save(conteoTotal);
}

@Override
public ConteoTotal findOne(Long id) {
    return conteoTotalDao.findById(id).orElse(null);
}

@Override
public void delete(Long id) {
    conteoTotalDao.deleteById(id);
}

@Override
public ConteoTotal conteoTotalBlacoNulosFul(Long idNivel) {
    // TODO Auto-generated method stub
    return conteoTotalDao.conteoTotalBlacoNulosFul(idNivel);
}


}