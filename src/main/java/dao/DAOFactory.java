package dao;

import interfaces.AlumnoDAO;
import interfaces.CursoDAO;
import interfaces.DocenteDAO;
import interfaces.EvaluacionDAO;
import interfaces.MatriculaDAO;
import interfaces.NotaDAO;
import interfaces.RolDAO;
import interfaces.UsuarioDAO;

public abstract class DAOFactory {
	public static final int MYSQL = 1;
	public static final int SQLSERVER = 2;
	public static final int ORACLE = 3;
	
	public abstract AlumnoDAO getAlumno();
	public abstract DocenteDAO getDocente();
	public abstract CursoDAO getCurso();
	public abstract UsuarioDAO getUsuario();
	public abstract RolDAO getRol();
	public abstract MatriculaDAO getMatricula();
	public abstract NotaDAO getNota();
	public abstract EvaluacionDAO getEvaluacion();
	
	public static DAOFactory getDaoFactory(int tipo) {
		switch(tipo) {
		case MYSQL:
			return new MySqlDAOFactory();
		case SQLSERVER:
			return new SQLServerDAOFactory();
		case ORACLE:
//			return new OracleDAOFactory();
			return null;
		default:
			return null;
		}
	}

	
}
