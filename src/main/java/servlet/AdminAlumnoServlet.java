package servlet;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.DAOFactory;
import entidades.Alumno;
import entidades.Rol;
import entidades.Usuario;
import interfaces.AlumnoDAO;
import sesion.GestorSesion;

/**
 * Servlet implementation class AdminAlumnoServlet
 */
@WebServlet("/AdminAlumno")
public class AdminAlumnoServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AdminAlumnoServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		Usuario usuarioLogueado = GestorSesion.getUsuarioEnSesion(request);
        if (usuarioLogueado == null) {
            response.sendRedirect("login.jsp");
            return;
        }
		
		String accion;
		try {
			accion = request.getParameter("accion");
			if (accion == null) accion = "listar";
			
			switch (accion) {
			case "listar": listarAlumnos(request,response); break;
			case "registrar": registrarAlumno(request,response); break;
			case "info": getAlumno(request,response); break;
			case "editar": editarAlumno(request,response); break;
			case "eliminar": eliminarAlumno(request,response); break;
			default:
				request.setAttribute("mensaje", "Ocurrio un problema");
				request.getRequestDispatcher("/admin/gestionar_alumnos.jsp").forward(request, response);
			}
		} catch (Exception e) {
            System.err.println("Error en el service: " + e.getMessage());
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
   		
		
	}
	
	private DAOFactory dao = DAOFactory.getDaoFactory(DAOFactory.MYSQL);
   	private AlumnoDAO modelAlumno = dao.getAlumno();
	
   	private void listarAlumnos(HttpServletRequest request, HttpServletResponse response) throws Exception {
		List<Alumno> listaAlumnos = modelAlumno.listar();
		request.setAttribute("listaAlumnos", listaAlumnos);
		request.getRequestDispatcher("/admin/gestionar_alumnos.jsp").forward(request, response);
	}
   	
   	private void registrarAlumno(HttpServletRequest request, HttpServletResponse response) throws Exception {
	    // Datos del Alumno
	    String nombres = request.getParameter("nombres");
	    String apellidos = request.getParameter("apellidos");
	    String dni = request.getParameter("dni");
	    String correo = request.getParameter("correo");

	    // Datos del Usuario asociado
	    String username = request.getParameter("username");
	    String password = request.getParameter("password");
	    
	    // Creamos el objeto Alumno
	    Alumno alumno = new Alumno();
	    alumno.setNombres(nombres);
	    alumno.setApellidos(apellidos);
	    alumno.setDni(dni);
	    alumno.setCorreo(correo);

	    // Creamos el objeto Usuario
	    Usuario usuario = new Usuario();
	    usuario.setUsername(username);
	    usuario.setPassword(password);
	    
	    // Asignamos el rol de Alumno (ID=3)
	    Rol rolAlumno = new Rol();
	    rolAlumno.setIdRol(3);
	    usuario.setRol(rolAlumno);

	    modelAlumno.insertarAlumnoYUsuario(alumno, usuario);
	    listarAlumnos(request,response);
	}
   	
   	private void editarAlumno(HttpServletRequest request, HttpServletResponse response) throws Exception {
	    try {
			// Datos del Alumno
			int id = Integer.parseInt(request.getParameter("id"));
			String nombres = request.getParameter("nombres");
			String apellidos = request.getParameter("apellidos");
			String dni = request.getParameter("dni");
			String correo = request.getParameter("correo");

			// Creamos el objeto Alumno
			Alumno alumno = new Alumno();
			alumno.setIdAlumno(id);
			alumno.setNombres(nombres);
			alumno.setApellidos(apellidos);
			alumno.setDni(dni);
			alumno.setCorreo(correo);

			modelAlumno.modificar(alumno);
			
			listarAlumnos(request,response);
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
   	
   	private void getAlumno(HttpServletRequest request, HttpServletResponse response) throws Exception {
   		int id = Integer.parseInt(request.getParameter("id"));
   		Alumno alumno = modelAlumno.obtenerPorId(id);
   		request.setAttribute("alumno", alumno);
   		request.getRequestDispatcher("/admin/formulario_alumno.jsp").forward(request, response);
  
   	}

   	
   	private void eliminarAlumno(HttpServletRequest request, HttpServletResponse response) throws Exception {
   		int id = Integer.parseInt(request.getParameter("id"));
        modelAlumno.eliminar(id);
	    listarAlumnos(request,response);
   	}


}
