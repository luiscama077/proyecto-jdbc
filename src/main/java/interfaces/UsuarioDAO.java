package interfaces;

import java.util.List;

import entidades.Usuario;

public interface UsuarioDAO {
	void insertar(Usuario usuario) throws Exception;
    
    Usuario obtenerPorId(int id) throws Exception;
    
    List<Usuario> listar() throws Exception;
    
    void modificar(Usuario usuario) throws Exception;
    
    void eliminar(int id) throws Exception;
    
    // Un método específico útil para el login
    Usuario obtenerPorUsername(String username) throws Exception;
    
//    implementacion para la sesion
    public Usuario verificarInicioSesion(String username, String password) throws Exception;

}
