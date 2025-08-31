package interfaces;

import java.util.List;

import entidades.Nota;

public interface NotaDAO {
	void insertar(Nota nota) throws Exception;
    
    void modificar(Nota nota) throws Exception;
    
    void eliminar(int id) throws Exception;
    
    Nota obtenerPorId(int id) throws Exception;
    
    List<Nota> listar() throws Exception;
    
    // Método específico clave: obtener todas las notas de una matrícula
    List<Nota> listarPorMatricula(int idMatricula) throws Exception;
    
//   
    void guardarOActualizarNotas(List<Nota> notas) throws Exception;

}
