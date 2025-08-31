package interfaces;

import java.util.List;

import entidades.Matricula;

public interface MatriculaDAO {
	List<Matricula> listarMatriculasConDetalles(int idAlumno, int idPeriodo) throws Exception;
	
	
	void registrarMatriculas(int idAlumno, int idPeriodo, String[] idsCursos) throws Exception;

	
	
	
	void insertar(Matricula matricula) throws Exception;
    
    void modificar(Matricula matricula) throws Exception;
    
    void eliminar(int id) throws Exception;
    
    Matricula obtenerPorId(int id) throws Exception;
    
    List<Matricula> listar() throws Exception;
    
    // Métodos específicos que podrían ser muy útiles
    List<Matricula> listarPorAlumno(int idAlumno) throws Exception;
    List<Matricula> listarPorCursoYPeriodo(int idCurso, int idPeriodo) throws Exception;
    
    List<Matricula> listarPorCurso(int idCurso) throws Exception;
    
    
//    implementacion para docente
    List<Matricula> listarAlumnosConNotasPorCurso(int idCurso) throws Exception;

    

}
