package entidades;

public class Nota {
	private int idNota;
    private Matricula matricula;     // relación con tabla matricula
    private Evaluacion evaluacion;   // relación con tabla evaluacion
    private double nota;
    
	public int getIdNota() {
		return idNota;
	}
	public void setIdNota(int idNota) {
		this.idNota = idNota;
	}
	public Matricula getMatricula() {
		return matricula;
	}
	public void setMatricula(Matricula matricula) {
		this.matricula = matricula;
	}
	public Evaluacion getEvaluacion() {
		return evaluacion;
	}
	public void setEvaluacion(Evaluacion evaluacion) {
		this.evaluacion = evaluacion;
	}
	public double getNota() {
		return nota;
	}
	public void setNota(double nota) {
		this.nota = nota;
	}
    
    
}
