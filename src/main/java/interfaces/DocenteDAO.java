package interfaces;

import java.util.List;

import entidades.Docente;
import entidades.Usuario;

public interface DocenteDAO {
	void insertarDocenteYUsuario(Docente docente, Usuario usuario) throws Exception;

    void modificar(Docente docente) throws Exception;

    void eliminar(int idDocente) throws Exception;

    List<Docente> listar() throws Exception;

    Docente obtenerPorId(int id) throws Exception;
    
    // implementacion para la sesion
    
    public Docente obtenerPorIdUsuario(int idUsuario) throws Exception;

    
}
