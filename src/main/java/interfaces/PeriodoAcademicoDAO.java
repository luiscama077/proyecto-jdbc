package interfaces;

import java.util.List;

import entidades.PeriodoAcademico;

public interface PeriodoAcademicoDAO {
	void insertar(PeriodoAcademico periodo) throws Exception;
    void modificar(PeriodoAcademico periodo) throws Exception;
    void eliminar(int id) throws Exception;
    PeriodoAcademico obtenerPorId(int id) throws Exception;
    List<PeriodoAcademico> listar() throws Exception;
}
