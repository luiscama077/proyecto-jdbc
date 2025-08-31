package servlet;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.DAOFactory;
import entidades.Curso;
import entidades.Docente;
import entidades.Usuario;
import interfaces.CursoDAO;
import interfaces.DocenteDAO;
import sesion.GestorSesion;

/**
 * Servlet implementation class AdminCursoServlet
 */
@WebServlet("/AdminCurso")
public class AdminCursoServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AdminCursoServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
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
                case "listar": listarCursos(request, response);
                    break;
                case "info": getCurso(request, response);
                    break;
                case "registrar": registrarCurso(request, response);
                    break;
                case "editar": editarCurso(request, response);
                    break;
                case "eliminar": eliminarCurso(request, response);
                    break;
                case "mostrarFormulario": mostrarFormulario(request, response);
                	break;
                default:
                    request.setAttribute("mensaje", "Acción no válida.");
                    listarCursos(request, response);
            }
        } catch (Exception e) {
            System.err.println("Error en el service de AdminCursoServlet: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Inicializamos los DAOs necesarios
    private DAOFactory daoFactory = DAOFactory.getDaoFactory(DAOFactory.MYSQL);
    private CursoDAO modelCurso = daoFactory.getCurso();
    private DocenteDAO modelDocente = daoFactory.getDocente();

    private void listarCursos(HttpServletRequest request, HttpServletResponse response) throws Exception {
        List<Curso> listaCursos = modelCurso.listar();
        request.setAttribute("listaCursos", listaCursos);
        request.getRequestDispatcher("/admin/gestionar_cursos.jsp").forward(request, response);
    }

    private void getCurso(HttpServletRequest request, HttpServletResponse response) throws Exception {
        int id = Integer.parseInt(request.getParameter("id"));
        Curso curso = modelCurso.obtenerPorId(id);
        
        // También necesitamos la lista de todos los docentes para el <select>
        List<Docente> listaDocentes = modelDocente.listar();
        
        request.setAttribute("curso", curso);
        request.setAttribute("listaDocentes", listaDocentes);
        request.getRequestDispatcher("/admin/formulario_curso.jsp").forward(request, response);
    }
    
    private void mostrarFormulario(HttpServletRequest request, HttpServletResponse response) throws Exception {
    	
    	List<Docente> listaDocentes = modelDocente.listar();
    	
    	request.setAttribute("listaDocentes", listaDocentes);
    	request.getRequestDispatcher("/admin/formulario_curso.jsp").forward(request, response);
    }

    private void registrarCurso(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String nombre = request.getParameter("nombre");
        int creditos = Integer.parseInt(request.getParameter("creditos"));
        String idDocenteParam = request.getParameter("idDocente");

        Curso curso = new Curso();
        curso.setNombre(nombre);
        curso.setCreditos(creditos);

        // Manejar el docente opcional
        if (idDocenteParam != null && !idDocenteParam.isEmpty()) {
            Docente docente = new Docente();
            docente.setIdDocente(Integer.parseInt(idDocenteParam));
            curso.setDocente(docente);
        }

        modelCurso.insertar(curso);
        listarCursos(request, response);
    }

    private void editarCurso(HttpServletRequest request, HttpServletResponse response) throws Exception {
        int id = Integer.parseInt(request.getParameter("id"));
        String nombre = request.getParameter("nombre");
        int creditos = Integer.parseInt(request.getParameter("creditos"));
        String idDocenteParam = request.getParameter("idDocente");

        Curso curso = new Curso();
        curso.setIdCurso(id);
        curso.setNombre(nombre);
        curso.setCreditos(creditos);

        // Manejar el docente opcional
        if (idDocenteParam != null && !idDocenteParam.isEmpty()) {
            Docente docente = new Docente();
            docente.setIdDocente(Integer.parseInt(idDocenteParam));
            curso.setDocente(docente);
        } else {
            // Si se seleccionó "-- Sin Asignar --", nos aseguramos de que el docente sea null
            curso.setDocente(null);
        }

        modelCurso.modificar(curso);
        listarCursos(request, response);
    }

    private void eliminarCurso(HttpServletRequest request, HttpServletResponse response) throws Exception {
        int id = Integer.parseInt(request.getParameter("id"));
        modelCurso.eliminar(id);
        listarCursos(request, response);
    }

}
