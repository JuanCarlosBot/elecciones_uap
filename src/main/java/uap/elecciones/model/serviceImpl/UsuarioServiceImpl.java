package uap.elecciones.model.serviceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import uap.elecciones.model.dao.IUsuarioDao;
import uap.elecciones.model.entity.Usuario;
import uap.elecciones.model.service.IUsuarioService;

@Service
public class UsuarioServiceImpl implements IUsuarioService{

    @Autowired
    private IUsuarioDao usuarioDao;

    @Override
    public List<Usuario> findAll() {
        // TODO Auto-generated method stub
        return (List<Usuario>) usuarioDao.findAll();
    }

    @Override
    public void save(Usuario usuario) {
        // TODO Auto-generated method stub
        usuarioDao.save(usuario);
    }

    @Override
    public Usuario findOne(Long id) {
        // TODO Auto-generated method stub
        return usuarioDao.findById(id).orElse(null);
    }

    @Override
    public void delete(Long id) {
        // TODO Auto-generated method stub
        usuarioDao.deleteById(id);
    }

    @Override
    public Usuario getUsuario(String usuario, String pass) {
        // TODO Auto-generated method stub
        return usuarioDao.getUsuario(usuario, pass);
    }
    
}
