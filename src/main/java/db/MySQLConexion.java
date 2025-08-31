package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MySQLConexion {
	public static Connection getConexion() {
		Connection con = null;
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			String url = "jdbc:mysql://localhost:3306/registro_academico?useSSL=false&useTimezone=true&serverTimezone=UTC";
			String usr = "root";
			String psw = "mysql";
			con = DriverManager.getConnection(url, usr, psw);
		} catch (ClassNotFoundException e) {
			System.out.println("Error >> Driver no Instalado!!" + e.getMessage());
		} catch (SQLException e) {
			System.out.println("Error >> de conexi�n con la BD" + e.getMessage());
		} catch (Exception e) {
			System.out.println("Error >> general : " + e.getMessage());
		}
		return con;
	}
	
	 public static void closeConexion(Connection cn, PreparedStatement ps, ResultSet rs) {
	        try {
	        	if (rs != null) rs.close();
	        	if (ps != null) ps.close();
	        	if (cn != null) cn.close();
	        } catch (SQLException e2) {
	            System.err.println("Error al cerrar recursos: " + e2.getMessage());
	        }
    }
    
    public static void closeConexion(Connection cn, PreparedStatement ps) {
    	try {
    		if (ps != null) ps.close();
    		if (cn != null) cn.close();
    	} catch (SQLException e2) {
    		System.err.println("Error al cerrar recursos: " + e2.getMessage());
    	}
    }
	
    
    /**
     * Inicia una transacción desactivando el auto-commit.
     * @param cn La conexión sobre la que se iniciará la transacción.
     * @throws SQLException
     */
    public static void iniciarTransaccion(Connection cn) throws SQLException {
        if (cn != null) {
            cn.setAutoCommit(false);
        }
    }

    /**
     * Confirma la transacción, haciendo permanentes todos los cambios.
     * @param cn La conexión cuya transacción se va a confirmar.
     * @throws SQLException
     */
    public static void confirmarTransaccion(Connection cn) throws SQLException {
        if (cn != null) {
            cn.commit();
            cn.setAutoCommit(true); // Se restaura el modo auto-commit
        }
    }

    /**
     * Deshace la transacción, revirtiendo todos los cambios.
     * @param cn La conexión cuya transacción se va a deshacer.
     * @throws SQLException
     */
    public static void revertirTransaccion(Connection cn) throws SQLException {
        if (cn != null) {
            cn.rollback();
            cn.setAutoCommit(true); // Se restaura el modo auto-commit
        }
    }
	
}
