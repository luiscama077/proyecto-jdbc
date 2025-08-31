package interfaces;

import java.util.List;

import entidades.Alumno;
import entidades.Usuario;

public interface AlumnoDAO {
	
	void insertarAlumnoYUsuario(Alumno alumno, Usuario usuario) throws Exception;
    
    void modificar(Alumno alumno) throws Exception;
    
    void eliminar(int id) throws Exception;
    
    List<Alumno> listar() throws Exception;
    
    Alumno obtenerPorId(int id) throws Exception;
    
    // Un método específico que podría ser útil
    Alumno obtenerPorDNI(String dni) throws Exception;
    
    // implementacion para la sesion
    public Alumno obtenerPorIdUsuario(int idUsuario) throws Exception;

}
