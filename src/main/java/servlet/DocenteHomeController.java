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
import entidades.Usuario;
import interfaces.CursoDAO;
import sesion.GestorSesion;

/**
 * Servlet implementation class DocenteHomeController
 */
@WebServlet("/DocenteHome")
public class DocenteHomeController extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public DocenteHomeController() {
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
    	
        try {
            mostrarDashboard(request, response);
        } catch (Exception e) {
            System.err.println("Error al mostrar el dashboard del docente: " + e.getMessage());
            e.printStackTrace();
            request.setAttribute("mensajeError", "Ocurrió un error inesperado al cargar su información.");
            request.getRequestDispatcher("/error.jsp").forward(request, response);
        }
    }

    private void mostrarDashboard(HttpServletRequest request, HttpServletResponse response) throws Exception {
        // 1. Obtener el usuario de la sesión y su ID de docente
        Usuario usuarioLogueado = GestorSesion.getUsuarioEnSesion(request);
        if (usuarioLogueado == null || usuarioLogueado.getDocente() == null) {
            response.sendRedirect("login.jsp");
            return;
        }
        int idDocente = usuarioLogueado.getDocente().getIdDocente();
        
        // 2. Definir el período académico actual (esto podría ser dinámico)
        int idPeriodoActual = 2; // Asumimos "2025-I"
        
        // 3. Obtener los datos del DAO
        CursoDAO modelCurso = DAOFactory.getDaoFactory(DAOFactory.MYSQL).getCurso();
        List<Curso> cursosAsignados = modelCurso.listarCursosPorDocente(idDocente, idPeriodoActual);
        
        // 4. Pasar los datos a la vista
        request.setAttribute("listaCursosAsignados", cursosAsignados);
        
        // 5. Mostrar el dashboard
        request.getRequestDispatcher("/docente/home_docente.jsp").forward(request, response);
    }

}
