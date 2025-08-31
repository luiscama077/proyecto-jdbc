package servlet;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.DAOFactory;
import entidades.Matricula;
import entidades.Usuario;
import interfaces.MatriculaDAO;
import sesion.GestorSesion;

/**
 * Servlet implementation class AlumnoHomeController
 */
@WebServlet("/AlumnoHome")
public class AlumnoHomeController extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AlumnoHomeController() {
        super();
        // TODO Auto-generated constructor stub
    }

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Por ahora, este servlet solo tiene una acción: mostrar el dashboard.
        // Podríamos añadir más acciones en el futuro si fuera necesario.
    	
    	Usuario usuarioLogueado = GestorSesion.getUsuarioEnSesion(request);
        if (usuarioLogueado == null) {
            response.sendRedirect("login.jsp");
            return;
        }
    	
        try {
            mostrarDashboard(request, response);
        } catch (Exception e) {
            System.err.println("Error al mostrar el dashboard del alumno: " + e.getMessage());
            e.printStackTrace();
            // Redirigir a una página de error genérica
            request.setAttribute("mensajeError", "Ocurrió un error inesperado al cargar su información.");
            request.getRequestDispatcher("/error.jsp").forward(request, response);
        }
    }
    
    DAOFactory daoFactory = DAOFactory.getDaoFactory(DAOFactory.MYSQL);
    MatriculaDAO modelMatricula = daoFactory.getMatricula();

    private void mostrarDashboard(HttpServletRequest request, HttpServletResponse response) throws Exception {
        // 1. Obtener el usuario de la sesión
        Usuario usuarioLogueado = GestorSesion.getUsuarioEnSesion(request);
        if (usuarioLogueado == null || usuarioLogueado.getAlumno() == null) {
            // Si no hay usuario o no es un alumno, redirigir al login
            response.sendRedirect("login.jsp");
            return;
        }
        int idAlumno = usuarioLogueado.getAlumno().getIdAlumno();
        
        // 2. Definir el período académico actual (esto podría venir de la BD en un futuro)
        int idPeriodoActual = 2; // Asumimos que el período "2025-I" es el actual
        
        // 3. Obtener los datos del DAO
        List<Matricula> matriculasDelPeriodo = modelMatricula.listarMatriculasConDetalles(idAlumno, idPeriodoActual);
        
        if (matriculasDelPeriodo == null) {
            System.out.println("La lista de matrículas es NULL");
        } else if (matriculasDelPeriodo.isEmpty()) {
            System.out.println("La lista de matrículas está vacía");
        } else {
            System.out.println("Se recuperaron " + matriculasDelPeriodo.size() + " matrículas");
        }
        
        // 4. Pasar los datos a la vista
        request.setAttribute("matriculas", matriculasDelPeriodo);
        request.setAttribute("nombrePeriodo", "2025-I"); // Pasamos el nombre para mostrarlo
        
        // 5. Mostrar el dashboard
        request.getRequestDispatcher("/alumno/home_alumno.jsp").forward(request, response);
    }

}
