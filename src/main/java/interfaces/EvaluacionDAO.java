package interfaces;

import java.util.List;

import entidades.Evaluacion;

public interface EvaluacionDAO {
	List<Evaluacion> listar() throws Exception;
}
