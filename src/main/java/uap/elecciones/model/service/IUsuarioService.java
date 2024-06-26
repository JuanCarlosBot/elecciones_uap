package uap.elecciones.model.service;

import java.util.List;

import uap.elecciones.model.entity.Usuario;

public interface IUsuarioService {
    public List<Usuario> findAll();

	public void save(Usuario usuario);

	public Usuario findOne(Long id);

	public void delete(Long id);

	public Usuario getUsuario(String usuario,String pass);
}
