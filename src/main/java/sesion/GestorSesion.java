package sesion;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import entidades.Usuario;

public class GestorSesion {
	
	 /**
     * Guarda un objeto completo en la sesión.
     * Es mejor práctica guardar el objeto entero que sus atributos por separado.
     * @param request El HttpServletRequest actual, del cual se obtendrá la sesión.
     * @param key La clave con la que se guardará el objeto (ej. "USUARIO_LOGUEADO" ).
     * @param value El objeto a guardar en la sesión.
     */
    public void guardarObjetoEnSesion(HttpServletRequest request, String key, Object value) {
        // true: crea una sesión si no existe una.
        HttpSession session = request.getSession(true); 
        session.setAttribute(key, value);
    }

    /**
     * Obtiene un objeto de la sesión usando su clave.
     * @param request El HttpServletRequest actual.
     * @param key La clave del objeto que se quiere recuperar.
     * @return El objeto guardado, o null si no se encuentra o no hay sesión.
     */
    public Object obtenerObjetoDeSesion(HttpServletRequest request, String key) {
        HttpSession session = request.getSession(false); // false: no crea una sesión si no existe.
        if (session != null) {
            return session.getAttribute(key);
        }
        return null;
    }

    /**
     * Invalida (cierra) la sesión actual.
     * Elimina todos los atributos y la sesión misma.
     * @param request El HttpServletRequest actual.
     */
    public void invalidarSesion(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
    }

    /**
     * Establece el tiempo máximo de inactividad de la sesión en segundos.
     * Si el usuario no hace ninguna petición en este tiempo, la sesión se invalida automáticamente.
     * @param request El HttpServletRequest actual.
     * @param segundos El tiempo de vida de la sesión en segundos.
     */
    public void setTiempoExpiracion(HttpServletRequest request, int segundos) {
        HttpSession session = request.getSession(true);
        session.setMaxInactiveInterval(segundos);
    }
    
    /**
     * Método de conveniencia para obtener el Usuario logueado directamente.
     * @param request El HttpServletRequest actual.
     * @return El objeto Usuario si está en sesión, o null en caso contrario.
     */
    public static Usuario getUsuarioEnSesion(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            // Se hace un "casting" del objeto guardado a la clase Usuario.
            return (Usuario) session.getAttribute("usuarioLogueado");
        }
        return null;
    }
	
	
	
}
