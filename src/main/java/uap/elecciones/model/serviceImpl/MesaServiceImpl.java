package uap.elecciones.model.serviceImpl;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import uap.elecciones.model.dao.IMesaDao;
import uap.elecciones.model.entity.Mesa;
import uap.elecciones.model.service.IMesaService;

@Service
public class MesaServiceImpl implements IMesaService{

    @Autowired
    private IMesaDao mesaDao;

    @Override
    public List<Mesa> findAll() {
        // TODO Auto-generated method stub
        return (List<Mesa>) mesaDao.findAll();
    }

    @Override
    public void save(Mesa mesa) {
        // TODO Auto-generated method stub
        mesaDao.save(mesa);
    }

    @Override
    public Mesa findOne(Long id) {
        // TODO Auto-generated method stub
        return mesaDao.findById(id).orElse(null);
    }

    @Override
    public void delete(Long id) {
        // TODO Auto-generated method stub
        mesaDao.deleteById(id);
    }

    @Override
    public List<Object[]> lista_mesas_por_carrera(Long id_carrera) {
        // TODO Auto-generated method stub
        return mesaDao.lista_mesas_por_carrera(id_carrera);
    }

    @Override
    public Object mesaPorCarrera(Long idMesa) {
        return mesaDao.mesaPorCarrera(idMesa);
    }

    @Override
    public List<Mesa> mesasPorCarrera(Long idCarrera) {
        return (List<Mesa>) mesaDao.mesasPorCarrera(idCarrera);
    }

    @Override
    public List<Object[]> lista_mesas_por_facultad_docente(Long id_facultad) {
        return mesaDao.lista_mesas_por_facultad_docente(id_facultad);
    }

    @Override
    public List<Mesa> listarMesasPorIdFacultad(Long idMesa) {
        return mesaDao.listarMesasPorIdFacultad(idMesa);
    }

    @Override
    public List<Mesa> listarMesasOrdenadas() {
        // TODO Auto-generated method stub
        List<Mesa> mesas = mesaDao.listarMesasOrdenadas();
        Collections.sort(mesas, new Comparator<Mesa>() {
            @Override
            public int compare(Mesa m1, Mesa m2) {
                return m1.getId_mesa().compareTo(m2.getId_mesa()); // Comparar por id_mesa
            }
        });
        return mesas;
    }

    @Override
    public Object listarMesasyActas(Long idMesa) {
        return mesaDao.listarMesasyActas(idMesa);
    }

    @Override
    public List<Mesa> findByCarrera(Long idCarrera) {
        return mesaDao.mesasPorCarrera(idCarrera);
    }
    
    
}
