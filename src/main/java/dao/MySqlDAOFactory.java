package dao;

import interfaces.AlumnoDAO;
import interfaces.CursoDAO;
import interfaces.DocenteDAO;
import interfaces.EvaluacionDAO;
import interfaces.MatriculaDAO;
import interfaces.NotaDAO;
import interfaces.RolDAO;
import interfaces.UsuarioDAO;
import modelos.AlumnoDAOImp;
import modelos.CursoDAOImp;
import modelos.DocenteDAOImp;
import modelos.EvaluacionDAOImp;
import modelos.MatriculaDAOImp;
import modelos.NotaDAOImp;
import modelos.RolDAOImp;
import modelos.UsuarioDAOImp;

public class MySqlDAOFactory extends DAOFactory{

	@Override
	public AlumnoDAO getAlumno() {
		return new AlumnoDAOImp();
	}
	
	@Override
	public DocenteDAO getDocente() {
		return new DocenteDAOImp();
	}
	
	@Override
	public CursoDAO getCurso() {
		return new CursoDAOImp();
	}
	
	@Override
	public UsuarioDAO getUsuario() {
		return new UsuarioDAOImp();
	}
	
	@Override
	public RolDAO getRol() {
		return new RolDAOImp();
	}

	@Override
	public MatriculaDAO getMatricula() {
		return new MatriculaDAOImp();
	}
	
	@Override
	public NotaDAO getNota() {
		return new NotaDAOImp();
	}
	
	@Override
	public EvaluacionDAO getEvaluacion() {
		return new EvaluacionDAOImp();
	}
	
	
}
