package servlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.DAOFactory;
import entidades.Alumno;
import entidades.Docente;
import entidades.Usuario;
import interfaces.AlumnoDAO;
import interfaces.DocenteDAO;
import interfaces.UsuarioDAO;
import sesion.GestorSesion;

/**
 * Servlet implementation class LoginServlet
 */
@WebServlet("/login")
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public LoginServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.sendRedirect("login.jsp");
	}
	
    DAOFactory daoFactory = DAOFactory.getDaoFactory(DAOFactory.MYSQL);
    UsuarioDAO modelUsuario = daoFactory.getUsuario();
	
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String username = request.getParameter("txtUsername");
        String password = request.getParameter("txtClave");
        
        try {
        	
        	Usuario usuario = modelUsuario.verificarInicioSesion(username, password);
        	if (usuario != null) {
        	    // 2. Según el rol, cargar los datos adicionales y añadirlos al objeto usuario.
        	    String rolNombre = usuario.getRol().getNombre();
        	    
        	    if ("ALUMNO".equals(rolNombre)) {
        	        AlumnoDAO modelAlumno = daoFactory.getAlumno();
        	        Alumno alumno = modelAlumno.obtenerPorIdUsuario(usuario.getIdUsuario());
        	        usuario.setAlumno(alumno); // Inyectamos el objeto Alumno en el Usuario
        	        
        	    } else if ("DOCENTE".equals(rolNombre)) {
        	        DocenteDAO modelDocente = daoFactory.getDocente();
        	        Docente docente = modelDocente.obtenerPorIdUsuario(usuario.getIdUsuario());
        	        usuario.setDocente(docente); // Inyectamos el objeto Docente en el Usuario
        	    }
        	    // Si es ADMIN, no necesita datos adicionales.
        	    
        	 // 3. Guardar el objeto Usuario (¡ahora enriquecido!) en la sesión.
        	    GestorSesion gestorSesion = new GestorSesion();
        	    gestorSesion.guardarObjetoEnSesion(request, "usuarioLogueado", usuario);
        	    
                gestorSesion.setTiempoExpiracion(request, 30 * 60);
        	    
        	    // 4. Redirigir al home correspondiente según el rol.
        	    switch (rolNombre) {
        	        case "ADMIN":
        	            response.sendRedirect("admin/home_admin.jsp"); 
        	            break;
        	        case "DOCENTE":
        	            response.sendRedirect("DocenteHome");
        	            break;
        	        case "ALUMNO":
        	            response.sendRedirect("AlumnoHome");
        	            break;
        	        default:
        	            response.sendRedirect("login.jsp");
        	    }

        	} else {
        	    // Error en el login, mostrar mensaje
        	    request.setAttribute("mensajeError", "Usuario o contraseña incorrectos.");
        	    request.getRequestDispatcher("login.jsp").forward(request, response);
        	}
        	
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("mensajeError", "Ocurrió un error inesperado en el servidor.");
            request.getRequestDispatcher("login.jsp").forward(request, response);
        }
	}

}
