package interfaces;

import java.util.List;

import entidades.Rol;

public interface RolDAO {
	// No necesitamos un CRUD completo si solo vamos a leer.
    // Pero es buena práctica tenerlo por si acaso.
    
    Rol obtenerPorId(int id) throws Exception;
    
    List<Rol> listar() throws Exception;
    
    // Métodos de escritura (opcionales si no los vas a usar en la app)
    // void insertar(Rol rol) throws Exception;
    // void modificar(Rol rol) throws Exception;
    // void eliminar(int id) throws Exception;
}
