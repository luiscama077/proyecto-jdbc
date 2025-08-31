package modelos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import db.MySQLConexion;
import entidades.PeriodoAcademico;
import interfaces.PeriodoAcademicoDAO;

public class PeriodoAcademicoDAOImp implements PeriodoAcademicoDAO{
	
	public void insertar(PeriodoAcademico periodo) throws Exception {
        Connection cn = null;
        PreparedStatement ps = null;
        try {
            cn = MySQLConexion.getConexion();
            String sql = "INSERT INTO periodo_academico (nombre) VALUES (?)";
            ps = cn.prepareStatement(sql);
            ps.setString(1, periodo.getNombre());
            ps.executeUpdate();
        } catch (Exception e) {
            System.err.println("Error al insertar periodo: " + e.getMessage());
            throw e;
        } finally {
            MySQLConexion.closeConexion(cn, ps);
        }
    }

    public void modificar(PeriodoAcademico periodo) throws Exception {
        Connection cn = null;
        PreparedStatement ps = null;
        try {
            cn = MySQLConexion.getConexion();
            String sql = "UPDATE periodo_academico SET nombre = ? WHERE id_periodo = ?";
            ps = cn.prepareStatement(sql);
            ps.setString(1, periodo.getNombre());
            ps.setInt(2, periodo.getIdPeriodo());
            ps.executeUpdate();
        } catch (Exception e) {
            System.err.println("Error al modificar periodo: " + e.getMessage());
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
            String sql = "DELETE FROM periodo_academico WHERE id_periodo = ?";
            ps = cn.prepareStatement(sql);
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (Exception e) {
            System.err.println("Error al eliminar periodo: " + e.getMessage());
            throw e;
        } finally {
            MySQLConexion.closeConexion(cn, ps);
        }
    }

    public PeriodoAcademico obtenerPorId(int id) throws Exception {
        PeriodoAcademico periodo = null;
        Connection cn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            cn = MySQLConexion.getConexion();
            String sql = "SELECT id_periodo, nombre FROM periodo_academico WHERE id_periodo = ?";
            ps = cn.prepareStatement(sql);
            ps.setInt(1, id);
            rs = ps.executeQuery();
            if (rs.next()) {
                periodo = new PeriodoAcademico();
                periodo.setIdPeriodo(rs.getInt("id_periodo"));
                periodo.setNombre(rs.getString("nombre"));
            }
        } catch (Exception e) {
            System.err.println("Error al obtener periodo: " + e.getMessage());
            throw e;
        } finally {
            MySQLConexion.closeConexion(cn, ps, rs);
        }
        return periodo;
    }

    public List<PeriodoAcademico> listar() throws Exception {
        List<PeriodoAcademico> lista = new ArrayList<>();
        Connection cn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            cn = MySQLConexion.getConexion();
            String sql = "SELECT id_periodo, nombre FROM periodo_academico";
            ps = cn.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                PeriodoAcademico periodo = new PeriodoAcademico();
                periodo.setIdPeriodo(rs.getInt("id_periodo"));
                periodo.setNombre(rs.getString("nombre"));
                lista.add(periodo);
            }
        } catch (Exception e) {
            System.err.println("Error al listar periodos: " + e.getMessage());
            throw e;
        } finally {
            MySQLConexion.closeConexion(cn, ps, rs);
        }
        return lista;
    }
}
