package modelos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import db.MySQLConexion;
import entidades.Rol;
import interfaces.RolDAO;

public class RolDAOImp implements RolDAO{
	public Rol obtenerPorId(int id) throws Exception {
        Rol rol = null;
        Connection cn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        try {
            cn = MySQLConexion.getConexion();
            String sql = "SELECT id_rol, nombre FROM rol WHERE id_rol = ?";
            ps = cn.prepareStatement(sql);
            ps.setInt(1, id);
            rs = ps.executeQuery();
            
            if (rs.next()) {
                rol = new Rol();
                rol.setIdRol(rs.getInt("id_rol"));
                rol.setNombre(rs.getString("nombre"));
            }
        } catch (Exception e) {
            System.err.println("Error al obtener rol por ID: " + e.getMessage());
            throw e;
        } finally {
            MySQLConexion.closeConexion(cn, ps, rs);
        }
        return rol;
    }

    public List<Rol> listar() throws Exception {
        List<Rol> lista = new ArrayList<>();
        Connection cn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        try {
            cn = MySQLConexion.getConexion();
            String sql = "SELECT id_rol, nombre FROM rol";
            ps = cn.prepareStatement(sql);
            rs = ps.executeQuery();
            
            while (rs.next()) {
                Rol rol = new Rol();
                rol.setIdRol(rs.getInt("id_rol"));
                rol.setNombre(rs.getString("nombre"));
                lista.add(rol);
            }
        } catch (Exception e) {
            System.err.println("Error al listar roles: " + e.getMessage());
            throw e;
        } finally {
            MySQLConexion.closeConexion(cn, ps, rs);
        }
        return lista;
    }
}
