package servlet;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.DAOFactory;
import entidades.Rol;
import entidades.Usuario;
import interfaces.RolDAO;
import interfaces.UsuarioDAO;
import sesion.GestorSesion;

/**
 * Servlet implementation class AdminUsuarioServlet
 */
@WebServlet("/AdminUsuario")
public class AdminUsuarioServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AdminUsuarioServlet() {
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
    	
        String accion = request.getParameter("accion");
        if (accion == null) {
            accion = "listar";
        }
        
        try {
            switch (accion) {
                case "listar": listarUsuarios(request, response);
                    break;
                case "info": getUsuario(request, response);
                    break;
                case "registrar": registrarUsuarioAdmin(request, response);
                    break;
                case "editar": editarUsuario(request, response);
                    break;
                case "eliminar": eliminarUsuario(request, response);
                    break;
                case "mostrarFormulario": mostrarFormulario(request, response);
                	break;
                default:
                    request.setAttribute("mensaje", "Acción no válida.");
                    listarUsuarios(request, response);
            }
        } catch (Exception e) {
            System.err.println("Error en el service de AdminCursoServlet: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Inicializamos los DAOs necesarios
    private DAOFactory daoFactory = DAOFactory.getDaoFactory(DAOFactory.MYSQL);
    private UsuarioDAO modelUsuario = daoFactory.getUsuario();
    private RolDAO modelRol = daoFactory.getRol();
    
    private void listarUsuarios(HttpServletRequest request, HttpServletResponse response) throws Exception {
        List<Usuario> listaUsuarios = modelUsuario.listar();
        request.setAttribute("listaUsuarios", listaUsuarios);
        request.getRequestDispatcher("admin/gestionar_usuarios.jsp").forward(request, response);
    }
    
    private void mostrarFormulario(HttpServletRequest request, HttpServletResponse response) throws Exception {
        request.getRequestDispatcher("admin/formulario_usuario.jsp").forward(request, response);
    }
    
    private void getUsuario(HttpServletRequest request, HttpServletResponse response) throws Exception {
    	// Verificamos si se está editando un usuario existente o creando uno nuevo.
    	int id = Integer.parseInt(request.getParameter("id"));
    	Usuario usuario = modelUsuario.obtenerPorId(id);
    	
    	// También necesitamos la lista de roles para poblar el <select> en el formulario.
    	List<Rol> listaRoles = modelRol.listar();

    	// Pasamos el usuario (nuevo o existente) al request.
    	request.setAttribute("usuario", usuario);
    	request.setAttribute("listaRoles", listaRoles);
    	
    	// Mostramos el formulario (usaremos el mismo JSP para crear y editar).
    	request.getRequestDispatcher("admin/formulario_usuario.jsp").forward(request, response);
    }

    private void editarUsuario(HttpServletRequest request, HttpServletResponse response) throws Exception {
        // Obtenemos los datos del formulario.
        String idParam = request.getParameter("id");
        String username = request.getParameter("username");
        String clave = request.getParameter("password");
        String idRol = request.getParameter("idRol");

        // Creamos los objetos necesarios.
        
        Usuario usuario = new Usuario();
        usuario.setIdUsuario(Integer.parseInt(idParam));
        usuario.setUsername(username);
        usuario.setPassword(clave);
        
        if (idRol != null && !idRol.isEmpty()) {
        	Rol rol = new Rol();
        	rol.setIdRol(Integer.parseInt(idRol));
            usuario.setRol(rol);
        } else {
            // Si se seleccionó "-- Sin Asignar --", nos aseguramos de que el docente sea null
            usuario.setRol(null);
        }
        
        modelUsuario.modificar(usuario);
        response.sendRedirect("AdminUsuario?accion=listar");
    }
    
    private void registrarUsuarioAdmin(HttpServletRequest request, HttpServletResponse response) throws Exception {
    	// Obtenemos los datos del formulario.
    	String username = request.getParameter("username");
    	String clave = request.getParameter("password");
    	
    	// Creamos los objetos necesarios.
    	
    	Usuario usuario = new Usuario();
    	usuario.setUsername(username);
    	usuario.setPassword(clave);
    	
    	Rol rol = new Rol();
    	rol.setIdRol(1);
    	
    	usuario.setRol(rol);
    	
    	modelUsuario.insertar(usuario);
        response.sendRedirect("AdminUsuario?accion=listar");
    }

    private void eliminarUsuario(HttpServletRequest request, HttpServletResponse response) throws Exception {
        int idUsuario = Integer.parseInt(request.getParameter("id"));
        modelUsuario.eliminar(idUsuario);
        // Redirigimos de vuelta a la lista.
        response.sendRedirect("AdminUsuario?accion=listar");
    }
    
    
    
}
