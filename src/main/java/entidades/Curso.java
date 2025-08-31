package entidades;

public class Curso {
	private int idCurso;
    private String nombre;
    private int creditos;
    private Docente docente;
    
	public int getIdCurso() {
		return idCurso;
	}
	public void setIdCurso(int idCurso) {
		this.idCurso = idCurso;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public int getCreditos() {
		return creditos;
	}
	public void setCreditos(int creditos) {
		this.creditos = creditos;
	}
	public Docente getDocente() {
		return docente;
	}
	public void setDocente(Docente docente) {
		this.docente = docente;
	}
	
	// implementacion para docente
	private int alumnosMatriculados;

	public int getAlumnosMatriculados() {
		return alumnosMatriculados;
	}
	public void setAlumnosMatriculados(int alumnosMatriculados) {
		this.alumnosMatriculados = alumnosMatriculados;
	}
    
}
