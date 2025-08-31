package modelos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import db.MySQLConexion;
import entidades.Alumno;
import entidades.Usuario;
import interfaces.AlumnoDAO;

public class AlumnoDAOImp implements AlumnoDAO{
	
	public void insertarAlumnoYUsuario(Alumno alumno, Usuario usuario) throws Exception {
	    Connection cn = null;
	    PreparedStatement psUsuario = null;
	    PreparedStatement psAlumno = null;
	    ResultSet rs = null;

	    try {
	        cn = MySQLConexion.getConexion();
	        MySQLConexion.iniciarTransaccion(cn);

	        String sqlUsuario = "INSERT INTO usuario (username, password, id_rol) VALUES (?, ?, ?)";
	        // Statement.RETURN_GENERATED_KEYS le dice a JDBC que queremos la clave autogenerada
	        psUsuario = cn.prepareStatement(sqlUsuario, PreparedStatement.RETURN_GENERATED_KEYS);
	        psUsuario.setString(1, usuario.getUsername());
	        psUsuario.setString(2, usuario.getPassword());
	        psUsuario.setInt(3, usuario.getRol().getIdRol()); // El rol para un alumno siempre será 3
	        psUsuario.executeUpdate();

	        // Obtenemos la llave generada para el usuario
	        rs = psUsuario.getGeneratedKeys();
	        int idUsuarioGenerado = -1;
	        if (rs.next()) {
	            idUsuarioGenerado = rs.getInt(1);
	        }

	        if (idUsuarioGenerado == -1) {
	            throw new SQLException("No se pudo obtener el ID del usuario generado, la inserción falló.");
	        }

	        // 3. Insertar el Alumno usando el ID del usuario recién creado
	        String sqlAlumno = "INSERT INTO alumno (nombres, apellidos, dni, correo, id_usuario) VALUES (?, ?, ?, ?, ?)";
	        psAlumno = cn.prepareStatement(sqlAlumno);
	        psAlumno.setString(1, alumno.getNombres());
	        psAlumno.setString(2, alumno.getApellidos());
	        psAlumno.setString(3, alumno.getDni());
	        psAlumno.setString(4, alumno.getCorreo());
	        psAlumno.setInt(5, idUsuarioGenerado); // Usamos el ID que obtuvimos
	        psAlumno.executeUpdate();

	        // 4. Si todo fue bien, confirmar la transacción
	        MySQLConexion.confirmarTransaccion(cn);

	    } catch (Exception e) {
	        // 5. Si algo falló, revertir TODOS los cambios
	        System.err.println("Error en la transacción, revirtiendo cambios: " + e.getMessage());
	        MySQLConexion.revertirTransaccion(cn);
	        throw e; // Relanzamos la excepción para que el controlador se entere
	    } finally {
	        // Cerramos todos los recursos
	    	if (rs != null) rs.close();
	    	if (psUsuario != null) psUsuario.close();
	    	if (psAlumno != null) psAlumno.close();
	    	if (cn != null) cn.close();
	    }
	}
	
    public void modificar(Alumno alumno) throws Exception {
        Connection cn = null;
        PreparedStatement ps = null;
        
        try {
            cn = MySQLConexion.getConexion();
            String sql = "UPDATE alumno SET nombres = ?, apellidos = ?, dni = ?, correo = ? WHERE id_alumno = ?";
            ps = cn.prepareStatement(sql);
            
            ps.setString(1, alumno.getNombres());
            ps.setString(2, alumno.getApellidos());
            ps.setString(3, alumno.getDni());
            ps.setString(4, alumno.getCorreo());
            ps.setInt(5, alumno.getIdAlumno());
            
            ps.executeUpdate();
            
        } catch (Exception e) {
            System.err.println("Error al modificar el alumno: " + e.getMessage());
            throw e;
        } finally {
            MySQLConexion.closeConexion(cn, ps);
        }
    }

    public void eliminar(int idAlumno) throws Exception {
        Connection cn = null;
        PreparedStatement psSelect = null;
        PreparedStatement psDeleteAlumno = null;
        PreparedStatement psDeleteUsuario = null;
        ResultSet rs = null;
        int idUsuarioAEliminar = -1;

        try {
            cn = MySQLConexion.getConexion();
            
            // PASO 1: Averiguar el ID del usuario asociado a este alumno (si existe).
            // No necesitamos una transacción para una simple lectura.
            String sqlSelect = "SELECT id_usuario FROM alumno WHERE id_alumno = ?";
            psSelect = cn.prepareStatement(sqlSelect);
            psSelect.setInt(1, idAlumno);
            rs = psSelect.executeQuery();

            if (rs.next()) {
                idUsuarioAEliminar = rs.getInt("id_usuario");
                if (rs.wasNull()) { 
                    idUsuarioAEliminar = -1; 
                }
            } else {
                // Si no se encuentra el alumno, no hay nada que hacer.
                // Podemos lanzar una excepción o simplemente salir.
                System.out.println("No se encontró ningún alumno con el ID: " + idAlumno);
                return;
            }

            // PASO 2: Iniciar la transacción para las operaciones de borrado.
            MySQLConexion.iniciarTransaccion(cn);

            // PASO 3: Eliminar el registro de la tabla 'alumno'.
            // Se elimina primero el registro de la tabla hija para no violar la FK.
            String sqlDeleteAlumno = "DELETE FROM alumno WHERE id_alumno = ?";
            psDeleteAlumno = cn.prepareStatement(sqlDeleteAlumno);
            psDeleteAlumno.setInt(1, idAlumno);
            psDeleteAlumno.executeUpdate();

            if (idUsuarioAEliminar != -1) {
		        String sqlDeleteUsuario = "DELETE FROM usuario WHERE id_usuario = ?";
		        psDeleteUsuario = cn.prepareStatement(sqlDeleteUsuario);
		        psDeleteUsuario.setInt(1, idUsuarioAEliminar);
		        psDeleteUsuario.executeUpdate();
            }
            // PASO 4: Si todo fue bien, confirmar la transacción.
            MySQLConexion.confirmarTransaccion(cn);

        } catch (Exception e) {
            // Si algo falló, revertir TODOS los cambios.
            System.err.println("Error en la transacción de eliminación, revirtiendo cambios: " + e.getMessage());
            MySQLConexion.revertirTransaccion(cn);
            throw e; // Relanzamos la excepción.
        } finally {
            // Cerramos todos los recursos de forma segura.
        	try { if (rs != null) rs.close(); } catch (SQLException e) { e.printStackTrace(); }
        	try { if (psSelect != null) psSelect.close(); } catch (SQLException e) { e.printStackTrace(); }
        	try { if (psDeleteAlumno != null) psDeleteAlumno.close(); } catch (SQLException e) { e.printStackTrace(); }
        	try { if (psDeleteUsuario != null) psDeleteUsuario.close(); } catch (SQLException e) { e.printStackTrace(); }
        	try { if (cn != null) cn.close(); } catch (SQLException e) { e.printStackTrace(); }
        }
    }

    public List<Alumno> listar() throws Exception {
        List<Alumno> lista = new ArrayList<>();
        Connection cn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        // Usamos LEFT JOIN para incluir alumnos que no tienen un usuario asignado
        String sql = "SELECT a.id_alumno, a.nombres, a.apellidos, a.dni, a.correo, " +
                     "u.id_usuario, u.username " +
                     "FROM alumno a " +
                     "LEFT JOIN usuario u ON a.id_usuario = u.id_usuario";
        
        try {
            cn = MySQLConexion.getConexion();
            ps = cn.prepareStatement(sql);
            rs = ps.executeQuery();
            
            while (rs.next()) {
                Alumno alumno = new Alumno();
                alumno.setIdAlumno(rs.getInt("id_alumno"));
                alumno.setNombres(rs.getString("nombres"));
                alumno.setApellidos(rs.getString("apellidos"));
                alumno.setDni(rs.getString("dni"));
                alumno.setCorreo(rs.getString("correo"));
                
                int idUsuario = rs.getInt("id_usuario");
                if (!rs.wasNull()) {
		            Usuario usuario = new Usuario();
		            usuario.setIdUsuario(idUsuario);
		            usuario.setUsername(rs.getString("username"));
		            alumno.setUsuario(usuario);
		            
                }
                lista.add(alumno);
            }
            
        } catch (Exception e) {
            System.err.println("Error al listar los alumnos: " + e.getMessage());
            throw e;
        } finally {
            MySQLConexion.closeConexion(cn, ps, rs);
        }
        return lista;
    }

    public Alumno obtenerPorId(int id) throws Exception {
        Alumno alumno = null;
        Connection cn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        String sql = "SELECT a.id_alumno, a.nombres, a.apellidos, a.dni, a.correo, " +
                     "u.id_usuario, u.username " +
                     "FROM alumno a " +
                     "LEFT JOIN usuario u ON a.id_usuario = u.id_usuario " +
                     "WHERE a.id_alumno = ?";
        
        try {
            cn = MySQLConexion.getConexion();
            ps = cn.prepareStatement(sql);
            ps.setInt(1, id);
            rs = ps.executeQuery();
            
            if (rs.next()) {
                alumno = new Alumno();
                alumno.setIdAlumno(rs.getInt("id_alumno"));
                alumno.setNombres(rs.getString("nombres"));
                alumno.setApellidos(rs.getString("apellidos"));
                alumno.setDni(rs.getString("dni"));
                alumno.setCorreo(rs.getString("correo"));
                
                int idUsuario = rs.getInt("id_usuario");
                if (!rs.wasNull()) {
	                Usuario usuario = new Usuario();
	                usuario.setIdUsuario(idUsuario);
	                usuario.setUsername(rs.getString("username"));
	                alumno.setUsuario(usuario);
                }
            }
            
        } catch (Exception e) {
            System.err.println("Error al obtener alumno por ID: " + e.getMessage());
            throw e;
        } finally {
            MySQLConexion.closeConexion(cn, ps, rs);
        }
        return alumno;
    }

    public Alumno obtenerPorDNI(String dni) throws Exception {
        // La implementación sería casi idéntica a obtenerPorId,
        // pero cambiando "WHERE a.id_alumno = ?" por "WHERE a.dni = ?".
        // Te lo dejo como ejercicio o lo puedo desarrollar si lo necesitas.
        return null; // Implementación pendiente
    }
    
 // Implementacion para la sesion
    public Alumno obtenerPorIdUsuario(int idUsuario) throws Exception {
        // La lógica es casi idéntica a obtenerPorId, pero con un WHERE diferente.
        Alumno alumno = null;
        Connection cn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        String sql = "SELECT id_alumno, nombres, apellidos, dni, correo FROM alumno WHERE id_usuario = ?";
        
        try {
            cn = MySQLConexion.getConexion();
            ps = cn.prepareStatement(sql);
            ps.setInt(1, idUsuario);
            rs = ps.executeQuery();
            
            if (rs.next()) {
                alumno = new Alumno();
                alumno.setIdAlumno(rs.getInt("id_alumno"));
                alumno.setNombres(rs.getString("nombres"));
                alumno.setApellidos(rs.getString("apellidos"));
                alumno.setDni(rs.getString("dni"));
                alumno.setCorreo(rs.getString("correo"));
            }
        } catch (Exception e) {
            System.err.println("Error al obtener alumno por ID de Usuario: " + e.getMessage());
            throw e;
        } finally {
            MySQLConexion.closeConexion(cn, ps, rs);
        }
        return alumno;
    }

    
    
    
}
