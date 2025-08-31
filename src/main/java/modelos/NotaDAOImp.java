package modelos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import db.MySQLConexion;
import entidades.Evaluacion;
import entidades.Matricula;
import entidades.Nota;
import interfaces.NotaDAO;

public class NotaDAOImp implements NotaDAO{
	
	public void insertar(Nota nota) throws Exception {
        Connection cn = null;
        PreparedStatement ps = null;
        try {
            cn = MySQLConexion.getConexion();
            String sql = "INSERT INTO nota (id_matricula, id_evaluacion, nota) VALUES (?, ?, ?)";
            ps = cn.prepareStatement(sql);
            
            ps.setInt(1, nota.getMatricula().getIdMatricula());
            ps.setInt(2, nota.getEvaluacion().getIdEvaluacion());
            ps.setDouble(3, nota.getNota());
            
            ps.executeUpdate();
        } catch (Exception e) {
            System.err.println("Error al insertar la nota: " + e.getMessage());
            throw e;
        } finally {
            MySQLConexion.closeConexion(cn, ps);
        }
    }

    public void modificar(Nota nota) throws Exception {
        Connection cn = null;
        PreparedStatement ps = null;
        try {
            cn = MySQLConexion.getConexion();
            String sql = "UPDATE nota SET id_matricula = ?, id_evaluacion = ?, nota = ? WHERE id_nota = ?";
            ps = cn.prepareStatement(sql);
            
            ps.setInt(1, nota.getMatricula().getIdMatricula());
            ps.setInt(2, nota.getEvaluacion().getIdEvaluacion());
            ps.setDouble(3, nota.getNota());
            ps.setInt(4, nota.getIdNota());
            
            ps.executeUpdate();
        } catch (Exception e) {
            System.err.println("Error al modificar la nota: " + e.getMessage());
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
            String sql = "DELETE FROM nota WHERE id_nota = ?";
            ps = cn.prepareStatement(sql);
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (Exception e) {
            System.err.println("Error al eliminar la nota: " + e.getMessage());
            throw e;
        } finally {
            MySQLConexion.closeConexion(cn, ps);
        }
    }

    public List<Nota> listar() throws Exception {
        List<Nota> lista = new ArrayList<>();
        Connection cn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        // Consulta con JOINs a matricula y evaluacion
        String sql = "SELECT n.id_nota, n.nota, " +
                     "m.id_matricula, " +
                     "e.id_evaluacion, e.nombre AS evaluacion_nombre " +
                     "FROM nota n " +
                     "INNER JOIN matricula m ON n.id_matricula = m.id_matricula " +
                     "INNER JOIN evaluacion e ON n.id_evaluacion = e.id_evaluacion";
        
        try {
            cn = MySQLConexion.getConexion();
            ps = cn.prepareStatement(sql);
            rs = ps.executeQuery();
            
            while (rs.next()) {
                lista.add(construirNota(rs));
            }
        } catch (Exception e) {
            System.err.println("Error al listar las notas: " + e.getMessage());
            throw e;
        } finally {
            MySQLConexion.closeConexion(cn, ps, rs);
        }
        return lista;
    }

    public Nota obtenerPorId(int id) throws Exception {
        Nota nota = null;
        Connection cn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        String sql = "SELECT n.id_nota, n.nota, " +
                     "m.id_matricula, " +
                     "e.id_evaluacion, e.nombre AS evaluacion_nombre " +
                     "FROM nota n " +
                     "INNER JOIN matricula m ON n.id_matricula = m.id_matricula " +
                     "INNER JOIN evaluacion e ON n.id_evaluacion = e.id_evaluacion " +
                     "WHERE n.id_nota = ?";
        
        try {
            cn = MySQLConexion.getConexion();
            ps = cn.prepareStatement(sql);
            ps.setInt(1, id);
            rs = ps.executeQuery();
            
            if (rs.next()) {
                nota = construirNota(rs);
            }
        } catch (Exception e) {
            System.err.println("Error al obtener nota por ID: " + e.getMessage());
            throw e;
        } finally {
            MySQLConexion.closeConexion(cn, ps, rs);
        }
        return nota;
    }

    public List<Nota> listarPorMatricula(int idMatricula) throws Exception {
        List<Nota> lista = new ArrayList<>();
        Connection cn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        // Misma consulta que listar(), pero con un WHERE
        String sql = "SELECT n.id_nota, n.nota, " +
                     "m.id_matricula, " +
                     "e.id_evaluacion, e.nombre AS evaluacion_nombre " +
                     "FROM nota n " +
                     "INNER JOIN matricula m ON n.id_matricula = m.id_matricula " +
                     "INNER JOIN evaluacion e ON n.id_evaluacion = e.id_evaluacion " +
                     "WHERE n.id_matricula = ?";
        
        try {
            cn = MySQLConexion.getConexion();
            ps = cn.prepareStatement(sql);
            ps.setInt(1, idMatricula);
            rs = ps.executeQuery();
            
            while (rs.next()) {
                lista.add(construirNota(rs));
            }
        } catch (Exception e) {
            System.err.println("Error al listar notas por matrícula: " + e.getMessage());
            throw e;
        } finally {
            MySQLConexion.closeConexion(cn, ps, rs);
        }
        return lista;
    }

    // Método de utilidad para construir el objeto Nota
    private Nota construirNota(ResultSet rs) throws Exception {
        Matricula matricula = new Matricula();
        matricula.setIdMatricula(rs.getInt("id_matricula"));
        // Nota: No poblamos el resto del objeto matricula para mantener la consulta simple.
        // Si se necesitara, habría que añadir más JOINs.
        
        Evaluacion evaluacion = new Evaluacion();
        evaluacion.setIdEvaluacion(rs.getInt("id_evaluacion"));
        evaluacion.setNombre(rs.getString("evaluacion_nombre"));
        
        Nota nota = new Nota();
        nota.setIdNota(rs.getInt("id_nota"));
        nota.setNota(rs.getDouble("nota"));
        nota.setMatricula(matricula);
        nota.setEvaluacion(evaluacion);
        
        return nota;
    }
    

    @Override
    public void guardarOActualizarNotas(List<Nota> notas) throws Exception {
        Connection cn = null;
        PreparedStatement psUpsert = null;

        // Esta sentencia SQL específica de MySQL intenta hacer un INSERT.
        // Si falla por clave duplicada (id_matricula, id_evaluacion),
        // entonces ejecuta un UPDATE en el campo 'nota'.
        String sql = "INSERT INTO nota (id_matricula, id_evaluacion, nota) VALUES (?, ?, ?) " +
                     "ON DUPLICATE KEY UPDATE nota = VALUES(nota)";

        try {
            cn = MySQLConexion.getConexion();
            MySQLConexion.iniciarTransaccion(cn);

            psUpsert = cn.prepareStatement(sql);

            for (Nota nota : notas) {
                psUpsert.setInt(1, nota.getMatricula().getIdMatricula());
                psUpsert.setInt(2, nota.getEvaluacion().getIdEvaluacion());
                psUpsert.setDouble(3, nota.getNota());
                psUpsert.addBatch();
            }

            psUpsert.executeBatch();
            MySQLConexion.confirmarTransaccion(cn);

        } catch (Exception e) {
            System.err.println("Error en la transacción de guardar notas, revirtiendo cambios: " + e.getMessage());
            MySQLConexion.revertirTransaccion(cn);
            throw e;
        } finally {
            MySQLConexion.closeConexion(cn, psUpsert);
        }
    }

    
}
