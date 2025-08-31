package servlet;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.DAOFactory;
import entidades.Docente;
import entidades.Rol;
import entidades.Usuario;
import interfaces.DocenteDAO;
import sesion.GestorSesion;

/**
 * Servlet implementation class AdminDocenteServlet
 */
@WebServlet("/AdminDocente")
public class AdminDocenteServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AdminDocenteServlet() {
        super();
        // TODO Auto-generated constructor stub
    }
    
    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	
    	Usuario usuarioLogueado = GestorSesion.getUsuarioEnSesion(request);
        if (usuarioLogueado == null) {
            response.sendRedirect("login.jsp");
            return;
        }
    	
        String accion;
        try {
            accion = request.getParameter("accion");
            if (accion == null) {
                accion = "listar";
            }
            
            switch (accion) {
                case "listar": listarDocentes(request, response); break;
                case "registrar": registrarDocente(request, response); break;
                case "info": getDocente(request, response); break;
                case "editar": editarDocente(request, response); break;
                case "eliminar": eliminarDocente(request, response); break;
                default:
                    request.setAttribute("mensaje", "Ocurrió un problema");
                    request.getRequestDispatcher("/admin/gestionar_docentes.jsp").forward(request, response);
            }
        } catch (Exception e) {
            System.err.println("Error en el service de AdminDocenteServlet: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    // Inicializamos el DAO usando la Factory
    private DAOFactory daoFactory = DAOFactory.getDaoFactory(DAOFactory.MYSQL);
    private DocenteDAO modelDocente = daoFactory.getDocente();
    
    private void listarDocentes(HttpServletRequest request, HttpServletResponse response) throws Exception {
        List<Docente> listaDocentes = modelDocente.listar();
        request.setAttribute("listaDocentes", listaDocentes);
        request.getRequestDispatcher("/admin/gestionar_docentes.jsp").forward(request, response);
    }
    
    private void registrarDocente(HttpServletRequest request, HttpServletResponse response) throws Exception {
        // Datos del Docente
        String nombres = request.getParameter("nombres");
        String apellidos = request.getParameter("apellidos");
        String dni = request.getParameter("dni");
        String correo = request.getParameter("correo");

        // Datos del Usuario asociado
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        
        // Creamos el objeto Docente
        Docente docente = new Docente();
        docente.setNombres(nombres);
        docente.setApellidos(apellidos);
        docente.setDni(dni);
        docente.setCorreo(correo);

        // Creamos el objeto Usuario
        Usuario usuario = new Usuario();
        usuario.setUsername(username);
        usuario.setPassword(password);
        
        // Asignamos el rol de Docente (ID=2)
        Rol rolDocente = new Rol();
        rolDocente.setIdRol(2);
        usuario.setRol(rolDocente);

        modelDocente.insertarDocenteYUsuario(docente, usuario);
        listarDocentes(request, response);
    }
    
    private void editarDocente(HttpServletRequest request, HttpServletResponse response) throws Exception {
        // Datos del Docente
        int id = Integer.parseInt(request.getParameter("id"));
        String nombres = request.getParameter("nombres");
        String apellidos = request.getParameter("apellidos");
        String dni = request.getParameter("dni");
        String correo = request.getParameter("correo");

        // Creamos el objeto Docente
        Docente docente = new Docente();
        docente.setIdDocente(id);
        docente.setNombres(nombres);
        docente.setApellidos(apellidos);
        docente.setDni(dni);
        docente.setCorreo(correo);

        modelDocente.modificar(docente);
        listarDocentes(request, response);
    }
    
    private void getDocente(HttpServletRequest request, HttpServletResponse response) throws Exception {
        int id = Integer.parseInt(request.getParameter("id"));
        Docente docente = modelDocente.obtenerPorId(id);
        request.setAttribute("docente", docente);
        request.getRequestDispatcher("/admin/formulario_docente.jsp").forward(request, response);
    }
    
    private void eliminarDocente(HttpServletRequest request, HttpServletResponse response) throws Exception {
        int id = Integer.parseInt(request.getParameter("id"));
        modelDocente.eliminar(id);
        listarDocentes(request, response);
    }


}
