package servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.DAOFactory;
import entidades.Curso;
import entidades.Evaluacion;
import entidades.Matricula;
import entidades.Nota;
import entidades.Usuario;
import interfaces.CursoDAO;
import interfaces.EvaluacionDAO;
import interfaces.MatriculaDAO;
import interfaces.NotaDAO;
import otros.FilaNota;
import sesion.GestorSesion;

/**
 * Servlet implementation class NotaController
 */
@WebServlet("/Nota")
public class NotaController extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public NotaController() {
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
            response.sendRedirect("DocenteHome");
            return;
        }
        try {
            switch (accion) {
                case "verAlumnosPorCurso":
                    verAlumnosPorCurso(request, response);
                    break;
                case "guardarNotas":
                    guardarNotas(request, response);
                    break;
                default:
                    response.sendRedirect("DocenteHome");
            }
        } catch (Exception e) {
            System.err.println("Error en NotaController: " + e.getMessage());
            e.printStackTrace();
            request.setAttribute("mensajeError", "Ocurrió un error en la gestión de notas.");
            request.getRequestDispatcher("/error.jsp").forward(request, response);
        }
    }

    private void verAlumnosPorCurso(HttpServletRequest request, HttpServletResponse response) throws Exception {
        int idCurso = Integer.parseInt(request.getParameter("idCurso"));
        
        DAOFactory daoFactory = DAOFactory.getDaoFactory(DAOFactory.MYSQL);
        MatriculaDAO modelMatricula = daoFactory.getMatricula();
        EvaluacionDAO modelEvaluacion = daoFactory.getEvaluacion();
        CursoDAO modelCurso = daoFactory.getCurso();

        // 1. Obtener los datos base
        List<Matricula> listaMatriculas = modelMatricula.listarAlumnosConNotasPorCurso(idCurso);
        List<Evaluacion> listaEvaluaciones = modelEvaluacion.listar(); // Las cabeceras de la tabla
        Curso curso = modelCurso.obtenerPorId(idCurso);

        // 2. (Paso Clave) Construir la lista de filas para la tabla
        List<FilaNota> filasParaLaTabla = new ArrayList<>();
        
        // Por cada alumno matriculado...
        for (Matricula matricula : listaMatriculas) {
            FilaNota fila = new FilaNota();
            fila.setAlumno(matricula.getAlumno());
            fila.setIdMatricula(matricula.getIdMatricula());
            
            List<Double> notasOrdenadas = new ArrayList<>();
            // ...recorremos las evaluaciones en el orden correcto...
            for (Evaluacion evaluacion : listaEvaluaciones) {
                Double notaEncontrada = null;
                // ...y buscamos si el alumno tiene una nota para esa evaluación.
                for (Nota nota : matricula.getNotas()) {
                    if (nota.getEvaluacion().getIdEvaluacion() == evaluacion.getIdEvaluacion()) {
                        notaEncontrada = nota.getNota();
                        break; // Encontramos la nota, salimos del bucle interior
                    }
                }
                // Añadimos la nota (o null si no se encontró) a la lista ordenada.
                notasOrdenadas.add(notaEncontrada);
            }
            fila.setNotasOrdenadas(notasOrdenadas);
            filasParaLaTabla.add(fila);
        }

        // 3. Enviar los datos preparados al JSP
        request.setAttribute("filas", filasParaLaTabla); // La lista de filas listas para pintar
        request.setAttribute("evaluaciones", listaEvaluaciones); // Las cabeceras
        request.setAttribute("curso", curso);
        
        request.getRequestDispatcher("/docente/gestionar_notas.jsp").forward(request, response);
    }

    private void guardarNotas(HttpServletRequest request, HttpServletResponse response) throws Exception {
        // Este método no necesita cambios, su lógica para leer los parámetros sigue siendo válida.
        int idCurso = Integer.parseInt(request.getParameter("idCurso"));
        
        DAOFactory daoFactory = DAOFactory.getDaoFactory(DAOFactory.MYSQL);
        MatriculaDAO modelMatricula = daoFactory.getMatricula();
        NotaDAO modelNota = daoFactory.getNota();

        List<Matricula> listaMatriculas = modelMatricula.listarAlumnosConNotasPorCurso(idCurso);
        List<Evaluacion> listaEvaluaciones = daoFactory.getEvaluacion().listar();
        
        List<Nota> notasParaGuardar = new ArrayList<>();

        for (Matricula matricula : listaMatriculas) {
            for (Evaluacion evaluacion : listaEvaluaciones) {
                String nombreInput = "nota_" + matricula.getIdMatricula() + "_" + evaluacion.getIdEvaluacion();
                String valorNotaStr = request.getParameter(nombreInput);

                if (valorNotaStr != null && !valorNotaStr.trim().isEmpty()) {
                    try {
                        double valorNota = Double.parseDouble(valorNotaStr);
                        
                        Nota nota = new Nota();
                        // Creamos objetos temporales para la inserción
                        Matricula mTemp = new Matricula();
                        mTemp.setIdMatricula(matricula.getIdMatricula());
                        
                        Evaluacion eTemp = new Evaluacion();
                        eTemp.setIdEvaluacion(evaluacion.getIdEvaluacion());
                        
                        nota.setMatricula(mTemp);
                        nota.setEvaluacion(eTemp);
                        nota.setNota(valorNota);
                        
                        notasParaGuardar.add(nota);
                    } catch (NumberFormatException e) {
                        System.err.println("Valor de nota no válido para el input: " + nombreInput);
                    }
                }
            }
        }

        if (!notasParaGuardar.isEmpty()) {
            modelNota.guardarOActualizarNotas(notasParaGuardar);
        }

        request.setAttribute("mensaje", "Notas guardadas correctamente.");
        verAlumnosPorCurso(request, response);
    }

}
