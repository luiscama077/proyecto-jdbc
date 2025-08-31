package modelos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import db.MySQLConexion;
import entidades.Docente;
import entidades.Usuario;
import interfaces.DocenteDAO;

public class DocenteDAOImp implements DocenteDAO{
	
	public void insertarDocenteYUsuario(Docente docente, Usuario usuario) throws Exception {
        Connection cn = null;
        PreparedStatement psUsuario = null;
        PreparedStatement psDocente = null;
        ResultSet rs = null;

        try {
            cn = MySQLConexion.getConexion();
            MySQLConexion.iniciarTransaccion(cn);

            // 1. Insertar el Usuario y obtener el ID generado
            String sqlUsuario = "INSERT INTO usuario (username, password, id_rol) VALUES (?, ?, ?)";
            psUsuario = cn.prepareStatement(sqlUsuario, PreparedStatement.RETURN_GENERATED_KEYS);
            psUsuario.setString(1, usuario.getUsername());
            psUsuario.setString(2, usuario.getPassword());
            psUsuario.setInt(3, usuario.getRol().getIdRol()); // El rol para un docente será 2
            psUsuario.executeUpdate();

            rs = psUsuario.getGeneratedKeys();
            int idUsuarioGenerado = -1;
            if (rs.next()) {
                idUsuarioGenerado = rs.getInt(1);
            }

            if (idUsuarioGenerado == -1) {
                throw new SQLException("No se pudo obtener el ID del usuario generado, la inserción falló.");
            }

            // 2. Insertar el Docente usando el ID del usuario recién creado
            String sqlDocente = "INSERT INTO docente (nombres, apellidos, dni, correo, id_usuario) VALUES (?, ?, ?, ?, ?)";
            psDocente = cn.prepareStatement(sqlDocente);
            psDocente.setString(1, docente.getNombres());
            psDocente.setString(2, docente.getApellidos());
            psDocente.setString(3, docente.getDni());
            psDocente.setString(4, docente.getCorreo());
            psDocente.setInt(5, idUsuarioGenerado);
            psDocente.executeUpdate();

            // 3. Si todo fue bien, confirmar la transacción
            MySQLConexion.confirmarTransaccion(cn);

        } catch (Exception e) {
            System.err.println("Error en la transacción, revirtiendo cambios: " + e.getMessage());
            MySQLConexion.revertirTransaccion(cn);
            throw e;
        } finally {
            // Cierre seguro de recursos en el orden correcto
            try { if (rs != null) rs.close(); } catch (SQLException e) { e.printStackTrace(); }
            try { if (psUsuario != null) psUsuario.close(); } catch (SQLException e) { e.printStackTrace(); }
            try { if (psDocente != null) psDocente.close(); } catch (SQLException e) { e.printStackTrace(); }
            try { if (cn != null) cn.close(); } catch (SQLException e) { e.printStackTrace(); }
        }
    }

    public void modificar(Docente docente) throws Exception {
        Connection cn = null;
        PreparedStatement ps = null;
        
        try {
            cn = MySQLConexion.getConexion();
            String sql = "UPDATE docente SET nombres = ?, apellidos = ?, dni = ?, correo = ? WHERE id_docente = ?";
            ps = cn.prepareStatement(sql);
            
            ps.setString(1, docente.getNombres());
            ps.setString(2, docente.getApellidos());
            ps.setString(3, docente.getDni());
            ps.setString(4, docente.getCorreo());
            ps.setInt(5, docente.getIdDocente());
            
            ps.executeUpdate();
            
        } catch (Exception e) {
            System.err.println("Error al modificar el docente: " + e.getMessage());
            throw e;
        } finally {
            MySQLConexion.closeConexion(cn, ps);
        }
    }

    public void eliminar(int idDocente) throws Exception {
        Connection cn = null;
        PreparedStatement psSelect = null;
        PreparedStatement psDeleteDocente = null;
        PreparedStatement psDeleteUsuario = null;
        ResultSet rs = null;
        int idUsuarioAEliminar = -1;

        try {
            cn = MySQLConexion.getConexion();
            
            String sqlSelect = "SELECT id_usuario FROM docente WHERE id_docente = ?";
            psSelect = cn.prepareStatement(sqlSelect);
            psSelect.setInt(1, idDocente);
            rs = psSelect.executeQuery();

            if (rs.next()) {
                idUsuarioAEliminar = rs.getInt("id_usuario");
                if (rs.wasNull()) {
                    idUsuarioAEliminar = -1;
                }
            } else {
                System.out.println("No se encontró ningún docente con el ID: " + idDocente);
                return;
            }

            MySQLConexion.iniciarTransaccion(cn);

            String sqlDeleteDocente = "DELETE FROM docente WHERE id_docente = ?";
            psDeleteDocente = cn.prepareStatement(sqlDeleteDocente);
            psDeleteDocente.setInt(1, idDocente);
            psDeleteDocente.executeUpdate();

            if (idUsuarioAEliminar != -1) {
                String sqlDeleteUsuario = "DELETE FROM usuario WHERE id_usuario = ?";
                psDeleteUsuario = cn.prepareStatement(sqlDeleteUsuario);
                psDeleteUsuario.setInt(1, idUsuarioAEliminar);
                psDeleteUsuario.executeUpdate();
            }

            MySQLConexion.confirmarTransaccion(cn);

        } catch (Exception e) {
            System.err.println("Error en la transacción de eliminación, revirtiendo cambios: " + e.getMessage());
            MySQLConexion.revertirTransaccion(cn);
            throw e;
        } finally {
            try { if (rs != null) rs.close(); } catch (SQLException e) { e.printStackTrace(); }
            try { if (psSelect != null) psSelect.close(); } catch (SQLException e) { e.printStackTrace(); }
            try { if (psDeleteDocente != null) psDeleteDocente.close(); } catch (SQLException e) { e.printStackTrace(); }
            try { if (psDeleteUsuario != null) psDeleteUsuario.close(); } catch (SQLException e) { e.printStackTrace(); }
            try { if (cn != null) cn.close(); } catch (SQLException e) { e.printStackTrace(); }
        }
    }

    public List<Docente> listar() throws Exception {
        List<Docente> lista = new ArrayList<>();
        Connection cn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        String sql = "SELECT d.id_docente, d.nombres, d.apellidos, d.dni, d.correo, " +
                     "u.id_usuario, u.username " +
                     "FROM docente d " +
                     "LEFT JOIN usuario u ON d.id_usuario = u.id_usuario";
        
        try {
            cn = MySQLConexion.getConexion();
            ps = cn.prepareStatement(sql);
            rs = ps.executeQuery();
            
            while (rs.next()) {
                Docente docente = new Docente();
                docente.setIdDocente(rs.getInt("id_docente"));
                docente.setNombres(rs.getString("nombres"));
                docente.setApellidos(rs.getString("apellidos"));
                docente.setDni(rs.getString("dni"));
                docente.setCorreo(rs.getString("correo"));
                
                int idUsuario = rs.getInt("id_usuario");
                if (!rs.wasNull()) {
                    Usuario usuario = new Usuario();
                    usuario.setIdUsuario(idUsuario);
                    usuario.setUsername(rs.getString("username"));
                    docente.setUsuario(usuario);
                }
                
                lista.add(docente);
            }
        } catch (Exception e) {
            System.err.println("Error al listar los docentes: " + e.getMessage());
            throw e;
        } finally {
            MySQLConexion.closeConexion(cn, ps, rs);
        }
        return lista;
    }

    public Docente obtenerPorId(int id) throws Exception {
        Docente docente = null;
        Connection cn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        String sql = "SELECT d.id_docente, d.nombres, d.apellidos, d.dni, d.correo, " +
                     "u.id_usuario, u.username " +
                     "FROM docente d " +
                     "LEFT JOIN usuario u ON d.id_usuario = u.id_usuario " +
                     "WHERE d.id_docente = ?";
        
        try {
            cn = MySQLConexion.getConexion();
            ps = cn.prepareStatement(sql);
            ps.setInt(1, id);
            rs = ps.executeQuery();
            
            if (rs.next()) {
                docente = new Docente();
                docente.setIdDocente(rs.getInt("id_docente"));
                docente.setNombres(rs.getString("nombres"));
                docente.setApellidos(rs.getString("apellidos"));
                docente.setDni(rs.getString("dni"));
                docente.setCorreo(rs.getString("correo"));
                
                int idUsuario = rs.getInt("id_usuario");
                if (!rs.wasNull()) {
                    Usuario usuario = new Usuario();
                    usuario.setIdUsuario(idUsuario);
                    usuario.setUsername(rs.getString("username"));
                    docente.setUsuario(usuario);
                }
            }
        } catch (Exception e) {
            System.err.println("Error al obtener docente por ID: " + e.getMessage());
            throw e;
        } finally {
            MySQLConexion.closeConexion(cn, ps, rs);
        }
        return docente;
    }
    
//    implementacion para la sesion
    
    public Docente obtenerPorIdUsuario(int idUsuario) throws Exception {
        Docente docente = null;
        Connection cn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        String sql = "SELECT id_docente, nombres, apellidos, dni, correo FROM docente WHERE id_usuario = ?";
        
        try {
            cn = MySQLConexion.getConexion();
            ps = cn.prepareStatement(sql);
            ps.setInt(1, idUsuario);
            rs = ps.executeQuery();
            
            if (rs.next()) {
                docente = new Docente();
                docente.setIdDocente(rs.getInt("id_docente"));
                docente.setNombres(rs.getString("nombres"));
                docente.setApellidos(rs.getString("apellidos"));
                docente.setDni(rs.getString("dni"));
                docente.setCorreo(rs.getString("correo"));
            }
        } catch (Exception e) {
            System.err.println("Error al obtener docente por ID de Usuario: " + e.getMessage());
            throw e;
        } finally {
            MySQLConexion.closeConexion(cn, ps, rs);
        }
        return docente;
    }

    
    
}
