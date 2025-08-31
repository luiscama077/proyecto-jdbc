<%-- /webapp/admin/gestionar_cursos.jsp --%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<jsp:include page="/includes/header.jsp">
    <jsp:param name="tituloPagina" value="Gestión de Cursos"/>
</jsp:include>

<div class="d-flex justify-content-between align-items-center mb-4">
    <h1 class="display-6"><i class="bi bi-book-half"></i> Gestión de Cursos</h1>
    
    <%-- Botón para ir al formulario de creación de cursos --%>
    <a href="${pageContext.request.contextPath}/AdminCurso?accion=mostrarFormulario" class="btn btn-primary">
        <i class="bi bi-plus-circle-fill"></i> Crear Nuevo Curso
    </a>
</div>

<div class="card shadow-sm">
    <div class="card-body">
        <div class="table-responsive">
            <table class="table table-striped" id="mytable">
                <thead class="table-dark">
                    <tr>
                        <th>ID</th>
                        <th>Nombre del Curso</th>
                        <th>Créditos</th>
                        <th>Docente Asignado</th>
                        <th class="text-center">Acciones</th>
                    </tr>
                </thead>
                <tbody>
                    <%-- Iteramos sobre la lista de cursos que nos pasará el controlador --%>
                    <c:forEach var="curso" items="${listaCursos}">
                        <tr>
                            <td><c:out value="${curso.idCurso}" /></td>
                            <td><c:out value="${curso.nombre}" /></td>
                            <td><c:out value="${curso.creditos}" /></td>
                            <td>
                                <%-- Verificamos si el curso tiene un docente asignado --%>
                                <c:choose>
                                    <c:when test="${not empty curso.docente}">
                                        <span class="badge bg-info text-dark">
                                            <i class="bi bi-person-video"></i>
                                            <c:out value="${curso.docente.nombres} ${curso.docente.apellidos}" />
                                        </span>
                                    </c:when>
                                    <c:otherwise>
                                        <span class="badge bg-secondary">Sin Asignar</span>
                                    </c:otherwise>
                                </c:choose>
                            </td>
                            <td class="text-center">
                                <%-- Botón para editar --%>
                                <a href="${pageContext.request.contextPath}/AdminCurso?accion=info&id=${curso.idCurso}" class="btn btn-primary btn-sm" title="Editar">
                                    <i class="bi bi-pencil"></i>
                                </a>
                                
                                <%-- Botón para eliminar --%>
                                <a href="${pageContext.request.contextPath}/AdminCurso?accion=eliminar&id=${curso.idCurso}" 
                                   class="btn btn-danger btn-sm" 
                                   title="Eliminar"
                                   onclick="return confirm('¿Está seguro de que desea eliminar este curso?' );">
                                    <i class="bi bi-trash3-fill"></i>
                                </a>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </div>
    </div>
</div>

<jsp:include page="/includes/footer.jsp" />
