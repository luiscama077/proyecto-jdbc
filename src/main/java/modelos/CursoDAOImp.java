package modelos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import db.MySQLConexion;
import entidades.Curso;
import entidades.Docente;
import interfaces.CursoDAO;

public class CursoDAOImp implements CursoDAO{
	
	public void insertar(Curso curso) throws Exception {
        Connection cn = null;
        PreparedStatement ps = null;
        
        try {
            cn = MySQLConexion.getConexion();
            String sql = "INSERT INTO curso (nombre, creditos, id_docente) VALUES (?, ?, ?)";
            ps = cn.prepareStatement(sql);
            
            ps.setString(1, curso.getNombre());
            ps.setInt(2, curso.getCreditos());
            
            // Manejar la FK opcional a docente
            if (curso.getDocente() != null && curso.getDocente().getIdDocente() > 0) {
                ps.setInt(3, curso.getDocente().getIdDocente());
            } else {
                ps.setNull(3, Types.INTEGER);
            }
            
            ps.executeUpdate();
            
        } catch (Exception e) {
            System.err.println("Error al insertar el curso: " + e.getMessage());
            throw e;
        } finally {
            MySQLConexion.closeConexion(cn, ps);
        }
    }

    public void modificar(Curso curso) throws Exception {
        Connection cn = null;
        PreparedStatement ps = null;
        
        try {
            cn = MySQLConexion.getConexion();
            String sql = "UPDATE curso SET nombre = ?, creditos = ?, id_docente = ? WHERE id_curso = ?";
            ps = cn.prepareStatement(sql);
            
            ps.setString(1, curso.getNombre());
            ps.setInt(2, curso.getCreditos());
            
            if (curso.getDocente() != null && curso.getDocente().getIdDocente() > 0) {
                ps.setInt(3, curso.getDocente().getIdDocente());
            } else {
                ps.setNull(3, Types.INTEGER);
            }
            
            ps.setInt(4, curso.getIdCurso());
            
            ps.executeUpdate();
            
        } catch (Exception e) {
            System.err.println("Error al modificar el curso: " + e.getMessage());
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
            String sql = "DELETE FROM curso WHERE id_curso = ?";
            ps = cn.prepareStatement(sql);
            ps.setInt(1, id);
            ps.executeUpdate();
            
        } catch (Exception e) {
            System.err.println("Error al eliminar el curso: " + e.getMessage());
            throw e;
        } finally {
            MySQLConexion.closeConexion(cn, ps);
        }
    }

    public List<Curso> listar() throws Exception {
        List<Curso> lista = new ArrayList<>();
        Connection cn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        // LEFT JOIN para traer la información del docente, si existe
        String sql = "SELECT c.id_curso, c.nombre, c.creditos, " +
                     "d.id_docente, d.nombres, d.apellidos " +
                     "FROM curso c " +
                     "LEFT JOIN docente d ON c.id_docente = d.id_docente";
        
        try {
            cn = MySQLConexion.getConexion();
            ps = cn.prepareStatement(sql);
            rs = ps.executeQuery();
            
            while (rs.next()) {
                Curso curso = new Curso();
                curso.setIdCurso(rs.getInt("id_curso"));
                curso.setNombre(rs.getString("nombre"));
                curso.setCreditos(rs.getInt("creditos"));
                
                // Verificar si el curso tiene un docente asociado
                int idDocente = rs.getInt("id_docente");
                if (!rs.wasNull()) {
                    Docente docente = new Docente();
                    docente.setIdDocente(idDocente);
                    docente.setNombres(rs.getString("nombres"));
                    docente.setApellidos(rs.getString("apellidos"));
                    curso.setDocente(docente);
                }
                
                lista.add(curso);
            }
            
        } catch (Exception e) {
            System.err.println("Error al listar los cursos: " + e.getMessage());
            throw e;
        } finally {
            MySQLConexion.closeConexion(cn, ps, rs);
        }
        return lista;
    }

    public Curso obtenerPorId(int id) throws Exception {
        Curso curso = null;
        Connection cn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        String sql = "SELECT c.id_curso, c.nombre, c.creditos, " +
                     "d.id_docente, d.nombres, d.apellidos " +
                     "FROM curso c " +
                     "LEFT JOIN docente d ON c.id_docente = d.id_docente " +
                     "WHERE c.id_curso = ?";
        
        try {
            cn = MySQLConexion.getConexion();
            ps = cn.prepareStatement(sql);
            ps.setInt(1, id);
            rs = ps.executeQuery();
            
            if (rs.next()) {
                curso = new Curso();
                curso.setIdCurso(rs.getInt("id_curso"));
                curso.setNombre(rs.getString("nombre"));
                curso.setCreditos(rs.getInt("creditos"));
                
                int idDocente = rs.getInt("id_docente");
                if (!rs.wasNull()) {
                    Docente docente = new Docente();
                    docente.setIdDocente(idDocente);
                    docente.setNombres(rs.getString("nombres"));
                    docente.setApellidos(rs.getString("apellidos"));
                    curso.setDocente(docente);
                }
            }
            
        } catch (Exception e) {
            System.err.println("Error al obtener curso por ID: " + e.getMessage());
            throw e;
        } finally {
            MySQLConexion.closeConexion(cn, ps, rs);
        }
        return curso;
    }
    
    //Implementacion
    
    
    public List<Curso> listarPorDocente(int idDocente) throws Exception {
        List<Curso> lista = new ArrayList<>();
        Connection cn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        // La consulta es la misma que listar(), pero con un WHERE.
        // No necesitamos el JOIN aquí si solo queremos la info del curso.
        String sql = "SELECT id_curso, nombre, creditos FROM curso WHERE id_docente = ?";
        
        try {
            cn = MySQLConexion.getConexion();
            ps = cn.prepareStatement(sql);
            ps.setInt(1, idDocente);
            rs = ps.executeQuery();
            
            while (rs.next()) {
                Curso curso = new Curso();
                curso.setIdCurso(rs.getInt("id_curso"));
                curso.setNombre(rs.getString("nombre"));
                curso.setCreditos(rs.getInt("creditos"));
                // No asignamos el docente para mantener la consulta simple.
                lista.add(curso);
            }
        } catch (Exception e) {
            System.err.println("Error al listar cursos por docente: " + e.getMessage());
            throw e;
        } finally {
            MySQLConexion.closeConexion(cn, ps, rs);
        }
        return lista;
    }
    
//    implementacion para matricula
    
 // En CursoDAOImp.java

    @Override
    public List<Curso> listarCursosDisponiblesParaMatricula(int idAlumno, int idPeriodo) throws Exception {
        List<Curso> cursos = new ArrayList<>();
        Connection cn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        // La subconsulta en el WHERE es la clave:
        // Selecciona todos los cursos cuyo ID no esté en la lista de cursos
        // en los que el alumno ya está matriculado para ese período.
        String sql = "SELECT c.id_curso, c.nombre, c.creditos, d.nombres, d.apellidos " +
                     "FROM curso c " +
                     "LEFT JOIN docente d ON c.id_docente = d.id_docente " +
                     "WHERE c.id_curso NOT IN (" +
                     "  SELECT m.id_curso FROM matricula m WHERE m.id_alumno = ? AND m.id_periodo = ?" +
                     ") ORDER BY c.nombre";

        try {
            cn = MySQLConexion.getConexion();
            ps = cn.prepareStatement(sql);
            ps.setInt(1, idAlumno);
            ps.setInt(2, idPeriodo);
            rs = ps.executeQuery();

            while (rs.next()) {
                Curso curso = new Curso();
                curso.setIdCurso(rs.getInt("id_curso"));
                curso.setNombre(rs.getString("nombre"));
                curso.setCreditos(rs.getInt("creditos"));

                // Asignar docente si existe
                String nombresDocente = rs.getString("nombres");
                if (nombresDocente != null) {
                    Docente docente = new Docente();
                    docente.setNombres(nombresDocente);
                    docente.setApellidos(rs.getString("apellidos"));
                    curso.setDocente(docente);
                }
                cursos.add(curso);
            }
        } catch (Exception e) {
            System.err.println("Error al listar cursos disponibles: " + e.getMessage());
            throw e;
        } finally {
            MySQLConexion.closeConexion(cn, ps, rs);
        }
        return cursos;
    }

//    implementacion para docente
    public List<Curso> listarCursosPorDocente(int idDocente, int idPeriodo) throws Exception {
        List<Curso> cursos = new ArrayList<>();
        Connection cn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        // Esta consulta usa una subconsulta en el SELECT para contar los alumnos
        // matriculados en cada curso para el período especificado.
        String sql = "SELECT " +
                     "  c.id_curso, c.nombre, c.creditos, " +
                     "  (SELECT COUNT(*) FROM matricula m WHERE m.id_curso = c.id_curso AND m.id_periodo = ?) AS alumnos_matriculados " +
                     "FROM curso c " +
                     "WHERE c.id_docente = ?";

        try {
            cn = MySQLConexion.getConexion();
            ps = cn.prepareStatement(sql);
            ps.setInt(1, idPeriodo);
            ps.setInt(2, idDocente);
            rs = ps.executeQuery();

            while (rs.next()) {
                Curso curso = new Curso();
                curso.setIdCurso(rs.getInt("id_curso"));
                curso.setNombre(rs.getString("nombre"));
                curso.setCreditos(rs.getInt("creditos"));
                
                // Asignamos el conteo de alumnos a un campo que debes añadir en tu entidad Curso.java
                curso.setAlumnosMatriculados(rs.getInt("alumnos_matriculados"));
                
                cursos.add(curso);
            }
        } catch (Exception e) {
            System.err.println("Error al listar cursos por docente: " + e.getMessage());
            throw e;
        } finally {
            MySQLConexion.closeConexion(cn, ps, rs);
        }
        return cursos;
    }
    
    
}
