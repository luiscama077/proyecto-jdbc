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
import interfaces.MatriculaDAO;
import sesion.GestorSesion;

/**
 * Servlet implementation class MatriculaController
 */
@WebServlet("/Matricula")
public class MatriculaController extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public MatriculaController() {
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
            accion = "verFormulario"; // Acción por defecto
        }

        try {
            switch (accion) {
                case "verFormulario":
                    mostrarFormularioMatricula(request, response);
                    break;
                case "matricular":
                    procesarMatricula(request, response);
                    break;
                default:
                    // Redirigir al home si la acción no es válida
                    response.sendRedirect("AlumnoHome");
            }
        } catch (Exception e) {
            System.err.println("Error en MatriculaController: " + e.getMessage());
            e.printStackTrace();
            request.setAttribute("mensajeError", "Ocurrió un error en el proceso de matrícula.");
            request.getRequestDispatcher("/error.jsp").forward(request, response);
        }
    }

    private void mostrarFormularioMatricula(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Usuario usuarioLogueado = GestorSesion.getUsuarioEnSesion(request);
        int idAlumno = usuarioLogueado.getAlumno().getIdAlumno();
        
        // Período de matrícula (en un sistema real, esto sería dinámico)
        int idPeriodoMatricula = 2; // Matrícula para "2025-I"
        
        // Obtener la lista de cursos disponibles para este alumno en este período
        CursoDAO modelCurso = DAOFactory.getDaoFactory(DAOFactory.MYSQL).getCurso();
        List<Curso> cursosDisponibles = modelCurso.listarCursosDisponiblesParaMatricula(idAlumno, idPeriodoMatricula);
        
        request.setAttribute("cursosDisponibles", cursosDisponibles);
        request.setAttribute("nombrePeriodo", "2025-I");
        
        request.getRequestDispatcher("/alumno/formulario_matricula.jsp").forward(request, response);
    }

    private void procesarMatricula(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Usuario usuarioLogueado = GestorSesion.getUsuarioEnSesion(request);
        int idAlumno = usuarioLogueado.getAlumno().getIdAlumno();
        int idPeriodoMatricula = 2; // Período "2025-I"

        // Obtener los IDs de los cursos seleccionados (checkboxes)
        String[] idsCursosSeleccionados = request.getParameterValues("idCurso");

        if (idsCursosSeleccionados == null || idsCursosSeleccionados.length == 0) {
            // Si no se seleccionó ningún curso, mostrar un mensaje de error
            request.setAttribute("mensajeError", "Debe seleccionar al menos un curso para matricularse.");
            mostrarFormularioMatricula(request, response); // Volver a mostrar el formulario
            return;
        }

        // Llamar al DAO para registrar las matrículas en una transacción
        MatriculaDAO modelMatricula = DAOFactory.getDaoFactory(DAOFactory.MYSQL).getMatricula();
        modelMatricula.registrarMatriculas(idAlumno, idPeriodoMatricula, idsCursosSeleccionados);

        // Redirigir al dashboard del alumno para que vea sus nuevos cursos
        // Añadimos un parámetro para mostrar un mensaje de éxito
        response.sendRedirect("AlumnoHome?matricula=exito");
    }

}
