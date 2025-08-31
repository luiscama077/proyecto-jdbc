package modelos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import db.MySQLConexion;
import entidades.Evaluacion;
import interfaces.EvaluacionDAO;

public class EvaluacionDAOImp implements EvaluacionDAO{
	public List<Evaluacion> listar() throws Exception {
        List<Evaluacion> lista = new ArrayList<>();
        Connection cn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        try {
            cn = MySQLConexion.getConexion();
            String sql = "SELECT id_evaluacion, nombre FROM evaluacion ORDER BY id_evaluacion";
            ps = cn.prepareStatement(sql);
            rs = ps.executeQuery();
            
            while (rs.next()) {
                Evaluacion eval = new Evaluacion();
                eval.setIdEvaluacion(rs.getInt("id_evaluacion"));
                eval.setNombre(rs.getString("nombre"));
                lista.add(eval);
            }
        } catch (Exception e) {
            System.err.println("Error al listar evaluaciones: " + e.getMessage());
            throw e;
        } finally {
            MySQLConexion.closeConexion(cn, ps, rs);
        }
        return lista;
    }
}	
