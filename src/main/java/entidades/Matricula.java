package entidades;
import java.util.Date;
import java.util.List;

public class Matricula {
	private int idMatricula;
    private Alumno alumno;                 // relación con tabla alumno
    private Curso curso;                   // relación con tabla curso
    private PeriodoAcademico periodo;      // relación con tabla periodo_academico
    private Date fecha;
    
    private List<Nota> notas;
    
	public int getIdMatricula() {
		return idMatricula;
	}
	public void setIdMatricula(int idMatricula) {
		this.idMatricula = idMatricula;
	}
	public Alumno getAlumno() {
		return alumno;
	}
	public void setAlumno(Alumno alumno) {
		this.alumno = alumno;
	}
	public Curso getCurso() {
		return curso;
	}
	public void setCurso(Curso curso) {
		this.curso = curso;
	}
	public PeriodoAcademico getPeriodo() {
		return periodo;
	}
	public void setPeriodo(PeriodoAcademico periodo) {
		this.periodo = periodo;
	}
	public Date getFecha() {
		return fecha;
	}
	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}
    
	public List<Nota> getNotas() {
        return notas;
    }

    public void setNotas(List<Nota> notas) {
        this.notas = notas;
    }
    
    
}
