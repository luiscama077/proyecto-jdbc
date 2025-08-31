package modelos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import db.MySQLConexion;
import entidades.Alumno;
import entidades.Curso;
import entidades.Docente;
import entidades.Evaluacion;
import entidades.Matricula;
import entidades.Nota;
import entidades.PeriodoAcademico;
import interfaces.MatriculaDAO;

public class MatriculaDAOImp implements MatriculaDAO{
	
	@Override
	public List<Matricula> listarMatriculasConDetalles(int idAlumno, int idPeriodo) throws Exception {
	    List<Matricula> matriculas = new ArrayList<>();
	    Connection cn = null;
	    PreparedStatement ps = null;
	    ResultSet rs = null;
	    
	    // Esta consulta es el corazón del dashboard. Trae todo lo que necesitamos.
	    String sql = "SELECT " +
	                 "m.id_matricula, m.fecha, " +
	                 "c.id_curso, c.nombre AS curso_nombre, c.creditos, " +
	                 "d.id_docente, d.nombres AS docente_nombres, d.apellidos AS docente_apellidos, " +
	                 "n.id_nota, n.nota, " +
	                 "e.id_evaluacion, e.nombre AS evaluacion_nombre " +
	                 "FROM matricula m " +
	                 "JOIN curso c ON m.id_curso = c.id_curso " +
	                 "LEFT JOIN docente d ON c.id_docente = d.id_docente " +
	                 "LEFT JOIN nota n ON m.id_matricula = n.id_matricula " +
	                 "LEFT JOIN evaluacion e ON n.id_evaluacion = e.id_evaluacion " +
	                 "WHERE m.id_alumno = ? AND m.id_periodo = ? " +
	                 "ORDER BY c.nombre, e.id_evaluacion"; // Ordenar para agrupar correctamente

	    try {
	        cn = MySQLConexion.getConexion();
	        ps = cn.prepareStatement(sql);
	        ps.setInt(1, idAlumno);
	        ps.setInt(2, idPeriodo);
	        rs = ps.executeQuery();

	        Matricula matriculaActual = null;
	        int idMatriculaActual = -1;

	        while (rs.next()) {
	            int idMatricula = rs.getInt("id_matricula");

	            // Si es una nueva matrícula en la lista, la creamos
	            if (idMatricula != idMatriculaActual) {
	                idMatriculaActual = idMatricula;
	                matriculaActual = new Matricula();
	                matriculaActual.setIdMatricula(idMatricula);
	                matriculaActual.setFecha(rs.getDate("fecha"));
	                
	                // Creamos y asignamos el Curso
	                Curso curso = new Curso();
	                curso.setIdCurso(rs.getInt("id_curso"));
	                curso.setNombre(rs.getString("curso_nombre"));
	                curso.setCreditos(rs.getInt("creditos"));
	                
	                // Creamos y asignamos el Docente (si existe)
	                int idDocente = rs.getInt("id_docente");
	                if (!rs.wasNull()) {
	                    Docente docente = new Docente();
	                    docente.setIdDocente(idDocente);
	                    docente.setNombres(rs.getString("docente_nombres"));
	                    docente.setApellidos(rs.getString("docente_apellidos"));
	                    curso.setDocente(docente);
	                }
	                matriculaActual.setCurso(curso);
	                
	                // Inicializamos su lista de notas
	                matriculaActual.setNotas(new ArrayList<>());
	                matriculas.add(matriculaActual);
	            }

	            // Añadimos la nota a la matrícula actual (si existe)
	            int idNota = rs.getInt("id_nota");
	            if (!rs.wasNull() && matriculaActual != null) {
	                Nota nota = new Nota();
	                nota.setIdNota(idNota);
	                nota.setNota(rs.getDouble("nota"));
	                
	                Evaluacion evaluacion = new Evaluacion();
	                evaluacion.setIdEvaluacion(rs.getInt("id_evaluacion"));
	                evaluacion.setNombre(rs.getString("evaluacion_nombre"));
	                nota.setEvaluacion(evaluacion);
	                
	                matriculaActual.getNotas().add(nota);
	            }
	        }
	    } catch (Exception e) {
	        System.err.println("Error al listar matrículas con detalles: " + e.getMessage());
	        throw e;
	    } finally {
	        MySQLConexion.closeConexion(cn, ps, rs);
	    }
	    return matriculas;
	}
	

	@Override
	public void registrarMatriculas(int idAlumno, int idPeriodo, String[] idsCursos) throws Exception {
	    Connection cn = null;
	    PreparedStatement ps = null;

	    try {
	        cn = MySQLConexion.getConexion();
	        // 1. Iniciar la transacción
	        MySQLConexion.iniciarTransaccion(cn);

	        String sql = "INSERT INTO matricula (id_alumno, id_curso, id_periodo, fecha) VALUES (?, ?, ?, CURDATE())";
	        ps = cn.prepareStatement(sql);

	        // 2. Iterar sobre los cursos seleccionados y añadirlos al batch
	        for (String idCurso : idsCursos) {
	            ps.setInt(1, idAlumno);
	            ps.setInt(2, Integer.parseInt(idCurso));
	            ps.setInt(3, idPeriodo);
	            ps.addBatch(); // Agrega la sentencia al lote
	        }

	        // 3. Ejecutar todas las sentencias del lote
	        ps.executeBatch();

	        // 4. Si todo fue bien, confirmar la transacción
	        MySQLConexion.confirmarTransaccion(cn);

	    } catch (Exception e) {
	        // 5. Si algo falló, revertir TODOS los cambios
	        System.err.println("Error en la transacción de matrícula, revirtiendo cambios: " + e.getMessage());
	        MySQLConexion.revertirTransaccion(cn);
	        throw e;
	    } finally {
	        // Cerramos los recursos
	        MySQLConexion.closeConexion(cn, ps);
	    }
	}

	
	
	
	
	
	
	public void insertar(Matricula matricula) throws Exception {
        Connection cn = null;
        PreparedStatement ps = null;
        
        try {
            cn = MySQLConexion.getConexion();
            String sql = "INSERT INTO matricula (id_alumno, id_curso, id_periodo, fecha) VALUES (?, ?, ?, ?)";
            ps = cn.prepareStatement(sql);
            
            ps.setInt(1, matricula.getAlumno().getIdAlumno());
            ps.setInt(2, matricula.getCurso().getIdCurso());
            ps.setInt(3, matricula.getPeriodo().getIdPeriodo());
            // Convertir java.util.Date a java.sql.Date
            ps.setDate(4, new java.sql.Date(matricula.getFecha().getTime()));
            
            ps.executeUpdate();
            
        } catch (Exception e) {
            System.err.println("Error al insertar la matrícula: " + e.getMessage());
            throw e;
        } finally {
            MySQLConexion.closeConexion(cn, ps);
        }
    }

    public void modificar(Matricula matricula) throws Exception {
        Connection cn = null;
        PreparedStatement ps = null;
        
        try {
            cn = MySQLConexion.getConexion();
            // Generalmente, se modifica la fecha. Cambiar alumno/curso/periodo es más como eliminar y crear una nueva.
            String sql = "UPDATE matricula SET id_alumno = ?, id_curso = ?, id_periodo = ?, fecha = ? WHERE id_matricula = ?";
            ps = cn.prepareStatement(sql);
            
            ps.setInt(1, matricula.getAlumno().getIdAlumno());
            ps.setInt(2, matricula.getCurso().getIdCurso());
            ps.setInt(3, matricula.getPeriodo().getIdPeriodo());
            ps.setDate(4, new java.sql.Date(matricula.getFecha().getTime()));
            ps.setInt(5, matricula.getIdMatricula());
            
            ps.executeUpdate();
            
        } catch (Exception e) {
            System.err.println("Error al modificar la matrícula: " + e.getMessage());
            throw e;
        } finally {
            MySQLConexion.closeConexion(cn, ps);
        }
    }

    public void eliminar(int id) throws Exception {
        Connection cn = null;
        PreparedStatement ps = null;
        
        try {
            cn = MySQLConexion.getConexion();
            String sql = "DELETE FROM matricula WHERE id_matricula = ?";
            ps = cn.prepareStatement(sql);
            ps.setInt(1, id);
            ps.executeUpdate();
            
        } catch (Exception e) {
            System.err.println("Error al eliminar la matrícula: " + e.getMessage());
            throw e;
        } finally {
            MySQLConexion.closeConexion(cn, ps);
        }
    }

    public List<Matricula> listar() throws Exception {
        List<Matricula> lista = new ArrayList<>();
        Connection cn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        // La consulta con todos los JOINs necesarios
        String sql = "SELECT m.id_matricula, m.fecha, " +
                     "a.id_alumno, a.nombres AS alumno_nombres, a.apellidos AS alumno_apellidos, " +
                     "c.id_curso, c.nombre AS curso_nombre, " +
                     "p.id_periodo, p.nombre AS periodo_nombre " +
                     "FROM matricula m " +
                     "INNER JOIN alumno a ON m.id_alumno = a.id_alumno " +
                     "INNER JOIN curso c ON m.id_curso = c.id_curso " +
                     "INNER JOIN periodo_academico p ON m.id_periodo = p.id_periodo";
        
        try {
            cn = MySQLConexion.getConexion();
            ps = cn.prepareStatement(sql);
            rs = ps.executeQuery();
            
            while (rs.next()) {
                // Construir el objeto Matricula a partir del ResultSet
                Matricula matricula = construirMatricula(rs);
                lista.add(matricula);
            }
            
        } catch (Exception e) {
            System.err.println("Error al listar las matrículas: " + e.getMessage());
            throw e;
        } finally {
            MySQLConexion.closeConexion(cn, ps, rs);
        }
        return lista;
    }

    public Matricula obtenerPorId(int id) throws Exception {
        Matricula matricula = null;
        Connection cn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        String sql = "SELECT m.id_matricula, m.fecha, " +
                     "a.id_alumno, a.nombres AS alumno_nombres, a.apellidos AS alumno_apellidos, " +
                     "c.id_curso, c.nombre AS curso_nombre, " +
                     "p.id_periodo, p.nombre AS periodo_nombre " +
                     "FROM matricula m " +
                     "INNER JOIN alumno a ON m.id_alumno = a.id_alumno " +
                     "INNER JOIN curso c ON m.id_curso = c.id_curso " +
                     "INNER JOIN periodo_academico p ON m.id_periodo = p.id_periodo " +
                     "WHERE m.id_matricula = ?";
        
        try {
            cn = MySQLConexion.getConexion();
            ps = cn.prepareStatement(sql);
            ps.setInt(1, id);
            rs = ps.executeQuery();
            
            if (rs.next()) {
                matricula = construirMatricula(rs);
            }
            
        } catch (Exception e) {
            System.err.println("Error al obtener matrícula por ID: " + e.getMessage());
            throw e;
        } finally {
            MySQLConexion.closeConexion(cn, ps, rs);
        }
        return matricula;
    }

    // Método de utilidad para no repetir el código de construcción de objetos
    private Matricula construirMatricula(ResultSet rs) throws Exception {
        Alumno alumno = new Alumno();
        alumno.setIdAlumno(rs.getInt("id_alumno"));
        alumno.setNombres(rs.getString("alumno_nombres"));
        alumno.setApellidos(rs.getString("alumno_apellidos"));
        
        Curso curso = new Curso();
        curso.setIdCurso(rs.getInt("id_curso"));
        curso.setNombre(rs.getString("curso_nombre"));
        
        PeriodoAcademico periodo = new PeriodoAcademico();
        periodo.setIdPeriodo(rs.getInt("id_periodo"));
        periodo.setNombre(rs.getString("periodo_nombre"));
        
        Matricula matricula = new Matricula();
        matricula.setIdMatricula(rs.getInt("id_matricula"));
        matricula.setFecha(rs.getDate("fecha"));
        matricula.setAlumno(alumno);
        matricula.setCurso(curso);
        matricula.setPeriodo(periodo);
        
        return matricula;
    }
    
    // Implementación de los métodos específicos (opcional)
    public List<Matricula> listarPorAlumno(int idAlumno) throws Exception {
        // La implementación sería similar a listar(), pero añadiendo "WHERE m.id_alumno = ?"
        return new ArrayList<>(); // Implementación pendiente
    }

    public List<Matricula> listarPorCursoYPeriodo(int idCurso, int idPeriodo) throws Exception {
        // Similar a listar(), con "WHERE m.id_curso = ? AND m.id_periodo = ?"
        return new ArrayList<>(); // Implementación pendiente
    }
    
    //Implementacion
    
    public List<Matricula> listarPorCurso(int idCurso) throws Exception {
        List<Matricula> lista = new ArrayList<>();
        Connection cn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        // Necesitamos un JOIN con alumno para mostrar su nombre.
        String sql = "SELECT m.id_matricula, m.fecha, " +
                     "a.id_alumno, a.nombres, a.apellidos " +
                     "FROM matricula m " +
                     "INNER JOIN alumno a ON m.id_alumno = a.id_alumno " +
                     "WHERE m.id_curso = ?";
        
        try {
            cn = MySQLConexion.getConexion();
            ps = cn.prepareStatement(sql);
            ps.setInt(1, idCurso);
            rs = ps.executeQuery();
            
            while (rs.next()) {
                Alumno alumno = new Alumno();
                alumno.setIdAlumno(rs.getInt("id_alumno"));
                alumno.setNombres(rs.getString("nombres"));
                alumno.setApellidos(rs.getString("apellidos"));
                
                Matricula matricula = new Matricula();
                matricula.setIdMatricula(rs.getInt("id_matricula"));
                matricula.setFecha(rs.getDate("fecha"));
                matricula.setAlumno(alumno);
                
                lista.add(matricula);
            }
        } catch (Exception e) {
            System.err.println("Error al listar matrículas por curso: " + e.getMessage());
            throw e;
        } finally {
            MySQLConexion.closeConexion(cn, ps, rs);
        }
        return lista;
    }

//    implementacion para docente
    

    @Override
    public List<Matricula> listarAlumnosConNotasPorCurso(int idCurso) throws Exception {
        List<Matricula> matriculas = new ArrayList<>();
        Connection cn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        String sql = "SELECT " +
                     "m.id_matricula, " +
                     "a.id_alumno, a.nombres AS alumno_nombres, a.apellidos AS alumno_apellidos, " +
                     "n.id_nota, n.nota, " +
                     "e.id_evaluacion, e.nombre AS evaluacion_nombre " +
                     "FROM matricula m " +
                     "JOIN alumno a ON m.id_alumno = a.id_alumno " +
                     "LEFT JOIN nota n ON m.id_matricula = n.id_matricula " +
                     "LEFT JOIN evaluacion e ON n.id_evaluacion = e.id_evaluacion " +
                     "WHERE m.id_curso = ? " +
                     "ORDER BY a.apellidos, a.nombres, e.id_evaluacion";

        try {
            cn = MySQLConexion.getConexion();
            ps = cn.prepareStatement(sql);
            ps.setInt(1, idCurso);
            rs = ps.executeQuery();

            Matricula matriculaActual = null;
            int idMatriculaActual = -1;

            while (rs.next()) { // Itera sobre cada fila del resultado SQL
                int idMatricula = rs.getInt("id_matricula");
             // ¿Es una nueva matrícula (un nuevo alumno en la lista)?
                if (idMatricula != idMatriculaActual) {
                	// Si es así, crea un nuevo objeto Matricula
                    idMatriculaActual = idMatricula;
                    matriculaActual = new Matricula();
                    matriculaActual.setIdMatricula(idMatricula);
                    
                 // Crea y asigna el objeto Alumno
                    Alumno alumno = new Alumno();
                    alumno.setIdAlumno(rs.getInt("id_alumno"));
                    alumno.setNombres(rs.getString("alumno_nombres"));
                    alumno.setApellidos(rs.getString("alumno_apellidos"));
                    matriculaActual.setAlumno(alumno);
                 // Inicializa su lista de notas (muy importante)
                    matriculaActual.setNotas(new ArrayList<>());
                 // Añade la nueva matrícula a la lista final
                    matriculas.add(matriculaActual);
                }
             // Ahora, procesa la nota de la fila actual
                int idNota = rs.getInt("id_nota");
                if (!rs.wasNull() && matriculaActual != null) {
                	// Si hay una nota en esta fila, crea el objeto Nota
                    Nota nota = new Nota();
                    nota.setIdNota(idNota);
                    nota.setNota(rs.getDouble("nota"));
                 // Crea y asigna el objeto Evaluacion
                    Evaluacion evaluacion = new Evaluacion();
                    evaluacion.setIdEvaluacion(rs.getInt("id_evaluacion"));
                    evaluacion.setNombre(rs.getString("evaluacion_nombre"));
                    nota.setEvaluacion(evaluacion);
                 // Añade la nota a la lista de notas de la matrícula actual
                    matriculaActual.getNotas().add(nota);
                }
            }
        } catch (Exception e) {
            System.err.println("Error al listar alumnos con notas por curso: " + e.getMessage());
            throw e;
        } finally {
            MySQLConexion.closeConexion(cn, ps, rs);
        }
        return matriculas;
    }

    
}
