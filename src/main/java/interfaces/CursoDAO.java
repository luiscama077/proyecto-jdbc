package interfaces;

import java.util.List;

import entidades.Curso;

public interface CursoDAO {
   	void insertar(Curso curso) throws Exception;
    
    void modificar(Curso curso) throws Exception;
    
    void eliminar(int id) throws Exception;
    
    Curso obtenerPorId(int id) throws Exception;
    
    List<Curso> listar() throws Exception;
    
    List<Curso> listarPorDocente(int idDocente) throws Exception;

//    implementacion para matricula
    List<Curso> listarCursosDisponiblesParaMatricula(int idAlumno, int idPeriodo) throws Exception;

//    implementacion para docente
    List<Curso> listarCursosPorDocente(int idDocente, int idPeriodo) throws Exception;

}
