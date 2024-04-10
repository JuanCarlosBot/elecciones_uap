package uap.elecciones.model.dao;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import uap.elecciones.model.entity.Usuario;

public interface IUsuarioDao extends CrudRepository<Usuario,Long>{
    
    @Query("select us from Usuario us where us.usuario=?1 and us.pass=?2")
    public Usuario getUsuario(String usuario,String pass);
}
