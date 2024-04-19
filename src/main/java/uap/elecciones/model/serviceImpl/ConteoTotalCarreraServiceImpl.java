package uap.elecciones.model.serviceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import uap.elecciones.model.dao.IConteoTotalCarreraDao;
import uap.elecciones.model.entity.ConteoTotalCarrera;
import uap.elecciones.model.service.IConteoTotalCarreraService;


@Service
public class ConteoTotalCarreraServiceImpl implements IConteoTotalCarreraService{
    
@Autowired
private IConteoTotalCarreraDao ConteoTotalCarreraDao;

@Override
public List<ConteoTotalCarrera> findAll() {
     return (List<ConteoTotalCarrera>) ConteoTotalCarreraDao.findAll();
}

@Override
public void save(ConteoTotalCarrera ConteoTotalCarrera) {
    ConteoTotalCarreraDao.save(ConteoTotalCarrera);
}

@Override
public ConteoTotalCarrera findOne(Long id) {
    return ConteoTotalCarreraDao.findById(id).orElse(null);
}

@Override
public void delete(Long id) {
    ConteoTotalCarreraDao.deleteById(id);
}

@Override
public List<ConteoTotalCarrera> conteoTotalCarreraPorFacultad(Long idFacultad) {
    return (List<ConteoTotalCarrera>) ConteoTotalCarreraDao.conteoTotalCarreraPorFacultad(idFacultad);
}






}