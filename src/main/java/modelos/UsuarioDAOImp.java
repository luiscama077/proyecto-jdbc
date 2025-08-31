package modelos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import db.MySQLConexion;
import entidades.Rol;
import entidades.Usuario;
import interfaces.UsuarioDAO;

public class UsuarioDAOImp implements UsuarioDAO{

	public void insertar(Usuario usuario) throws Exception {
        Connection cn = null;
        PreparedStatement ps = null;
        
        try {
            cn = MySQLConexion.getConexion();
            String sql = "INSERT INTO usuario (username, password, id_rol) VALUES (?, ?, ?)";
            ps = cn.prepareStatement(sql);
            
            ps.setString(1, usuario.getUsername());
            ps.setString(2, usuario.getPassword());
            // Obtenemos el ID del objeto Rol contenido en Usuario
            ps.setInt(3, usuario.getRol().getIdRol()); 
            ps.executeUpdate();
        } catch (Exception e) {
            System.err.println("Error al insertar el usuario: " + e.getMessage());
            throw e;
        } finally {
        	MySQLConexion.closeConexion(cn, ps);
        }
    }

    public void modificar(Usuario usuario) throws Exception {
        Connection cn = null;
        PreparedStatement ps = null;
        
        try {
            cn = MySQLConexion.getConexion();
            // Opcional: Decidir si se puede cambiar el password o el rol aquí
            String sql = "UPDATE usuario SET username = ?, password = ? WHERE id_usuario = ?";
            ps = cn.prepareStatement(sql);
            
            ps.setString(1, usuario.getUsername());
            ps.setString(2, usuario.getPassword());
            
            ps.setInt(3, usuario.getIdUsuario());
            
            ps.executeUpdate();
            
        } catch (Exception e) {
            System.err.println("Error al modificar el usuario: " + e.getMessage());
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
            String sql = "DELETE FROM usuario WHERE id_usuario = ?";
            ps = cn.prepareStatement(sql);
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (Exception e) {
            System.err.println("Error al eliminar el usuario: " + e.getMessage());
            throw e;
        } finally {
        	MySQLConexion.closeConexion(cn, ps);
        }
    }

    public List<Usuario> listar() throws Exception {
        List<Usuario> lista = new ArrayList<>();
        Connection cn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        // Usamos un JOIN para traer los datos del usuario y su rol en una sola consulta
        String sql = "SELECT u.id_usuario, u.username, u.password, r.id_rol, r.nombre AS rol_nombre " +
                     "FROM usuario u " +
                     "INNER JOIN rol r ON u.id_rol = r.id_rol";
        
        try {
            cn = MySQLConexion.getConexion();
            ps = cn.prepareStatement(sql);
            rs = ps.executeQuery();
            
            while (rs.next()) {
                Usuario usuario = new Usuario();
                usuario.setIdUsuario(rs.getInt("id_usuario"));
                usuario.setUsername(rs.getString("username"));
                usuario.setPassword(rs.getString("password"));
                
                Rol rol = new Rol();
                rol.setIdRol(rs.getInt("id_rol"));
                rol.setNombre(rs.getString("rol_nombre"));
                
                usuario.setRol(rol); // Asignamos el objeto Rol completo
                lista.add(usuario);
            }
            
        } catch (Exception e) {
            System.err.println("Error al listar los usuarios: " + e.getMessage());
            throw e;
        } finally {
        	MySQLConexion.closeConexion(cn, ps, rs);
        }
        return lista;
    }

    public Usuario obtenerPorId(int id) throws Exception {
        Usuario usuario = null;
        Connection cn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        String sql = "SELECT u.id_usuario, u.username, u.password, r.id_rol, r.nombre AS rol_nombre " +
                     "FROM usuario u " +
                     "INNER JOIN rol r ON u.id_rol = r.id_rol " +
                     "WHERE u.id_usuario = ?";
        
        try {
            cn = MySQLConexion.getConexion();
            ps = cn.prepareStatement(sql);
            ps.setInt(1, id);
            rs = ps.executeQuery();
            
            if (rs.next()) {
                usuario = new Usuario();
                usuario.setIdUsuario(rs.getInt("id_usuario"));
                usuario.setUsername(rs.getString("username"));
                usuario.setPassword(rs.getString("password"));
                
                Rol rol = new Rol();
                rol.setIdRol(rs.getInt("id_rol"));
                rol.setNombre(rs.getString("rol_nombre"));
                
                usuario.setRol(rol);
            }
            
        } catch (Exception e) {
            System.err.println("Error al obtener usuario por ID: " + e.getMessage());
            throw e;
        } finally {
        	MySQLConexion.closeConexion(cn, ps, rs);
        }
        return usuario;
    }
    
    public Usuario obtenerPorUsername(String username) throws Exception {
        // Este método es muy similar a obtenerPorId, pero busca por username.
        // Es crucial para la funcionalidad de login.
        Usuario usuario = null;
        Connection cn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        String sql = "SELECT u.id_usuario, u.username, u.password, r.id_rol, r.nombre AS rol_nombre " +
                     "FROM usuario u " +
                     "INNER JOIN rol r ON u.id_rol = r.id_rol " +
                     "WHERE u.username = ?";
        
        try {
            cn = MySQLConexion.getConexion();
            ps = cn.prepareStatement(sql);
            ps.setString(1, username);
            rs = ps.executeQuery();
            
            if (rs.next()) {
                usuario = new Usuario();
                usuario.setIdUsuario(rs.getInt("id_usuario"));
                usuario.setUsername(rs.getString("username"));
                usuario.setPassword(rs.getString("password"));
                
                Rol rol = new Rol();
                rol.setIdRol(rs.getInt("id_rol"));
                rol.setNombre(rs.getString("rol_nombre"));
                
                usuario.setRol(rol);
            }
            
        } catch (Exception e) {
            System.err.println("Error al obtener usuario por username: " + e.getMessage());
            throw e;
        } finally {
        	MySQLConexion.closeConexion(cn, ps, rs);
        }
        return usuario;
    }
    
    
 // implementacion para la sesion

    public Usuario verificarInicioSesion(String username, String password) throws Exception {
        Usuario usuario = null;
        Connection cn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        // La consulta busca un usuario que coincida exactamente con el username Y la password.
        // También une la tabla 'rol' para traer el nombre del rol en la misma consulta.
        String sql = "SELECT u.id_usuario, u.username, u.password, r.id_rol, r.nombre AS rol_nombre " +
                     "FROM usuario u " +
                     "INNER JOIN rol r ON u.id_rol = r.id_rol " +
                     "WHERE u.username = ? AND u.password = ?";
        
        try {
            cn = MySQLConexion.getConexion();
            ps = cn.prepareStatement(sql);
            ps.setString(1, username);
            ps.setString(2, password); // En un sistema real, la contraseña estaría hasheada.
            rs = ps.executeQuery();
            
            // Si rs.next() es true, significa que se encontró exactamente una fila,
            // lo que indica que el login es correcto.
            if (rs.next()) {
                usuario = new Usuario();
                usuario.setIdUsuario(rs.getInt("id_usuario"));
                usuario.setUsername(rs.getString("username"));
                usuario.setPassword(rs.getString("password")); // No es ideal guardar la pass, pero lo mantenemos por simplicidad.
                
                Rol rol = new Rol();
                rol.setIdRol(rs.getInt("id_rol"));
                rol.setNombre(rs.getString("rol_nombre"));
                
                usuario.setRol(rol);
            }
            // Si no se encuentra ninguna fila, el método simplemente devolverá null.
            
        } catch (Exception e) {
            System.err.println("Error al verificar inicio de sesión: " + e.getMessage());
            throw e; // Relanzamos la excepción para que el servlet se entere.
        } finally {
            MySQLConexion.closeConexion(cn, ps, rs);
        }
        
        return usuario; // Devuelve el objeto usuario si el login fue exitoso, o null si no lo fue.
    }


}
