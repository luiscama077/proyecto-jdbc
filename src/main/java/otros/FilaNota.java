package otros;

import java.util.List;

import entidades.Alumno;

public class FilaNota {
	private Alumno alumno;
    private int idMatricula;
    private List<Double> notasOrdenadas; // Una lista de notas que coincide con el orden de las evaluaciones
    
	public Alumno getAlumno() {
		return alumno;
	}
	public void setAlumno(Alumno alumno) {
		this.alumno = alumno;
	}
	public int getIdMatricula() {
		return idMatricula;
	}
	public void setIdMatricula(int idMatricula) {
		this.idMatricula = idMatricula;
	}
	public List<Double> getNotasOrdenadas() {
		return notasOrdenadas;
	}
	public void setNotasOrdenadas(List<Double> notasOrdenadas) {
		this.notasOrdenadas = notasOrdenadas;
	}
    
    
    
}
