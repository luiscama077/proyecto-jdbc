<%-- /webapp/admin/formulario_curso.jsp --%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<jsp:include page="/includes/header.jsp">
    <jsp:param name="tituloPagina" value="${not empty curso.idCurso ? 'Editar' : 'Crear'} Curso"/>
</jsp:include>

<h1 class="display-6 mb-4">
    <c:if test="${not empty curso.idCurso}">
        <i class="bi bi-pencil-square"></i> Editar Curso
    </c:if>
    <c:if test="${empty curso.idCurso}">
        <i class="bi bi-plus-circle-fill"></i> Crear Nuevo Curso
    </c:if>
</h1>

<div class="card shadow-sm">
    <div class="card-body">
        <form action="${pageContext.request.contextPath}/AdminCurso">
        
            <%-- Campo oculto para el ID del curso (si estamos editando ) --%>
            <c:if test="${not empty curso.idCurso}">
                <input type="hidden" name="id" value="${curso.idCurso}" />
            </c:if>

            <h4 class="mb-3 border-bottom pb-2">Datos del Curso</h4>

            <div class="row">
                <div class="col-md-8 mb-3">
                    <label for="nombre" class="form-label">Nombre del Curso:</label>
                    <input type="text" class="form-control" id="nombre" name="nombre" 
                           value="${curso.nombre}" required>
                </div>
                <div class="col-md-4 mb-3">
                    <label for="creditos" class="form-label">Créditos:</label>
                    <input type="number" class="form-control" id="creditos" name="creditos" 
                           value="${curso.creditos}" required min="1">
                </div>
            </div>

            <div class="mb-3">
                <label for="idDocente" class="form-label">Docente Asignado:</label>
                <select class="form-select" id="idDocente" name="idDocente">
                    <option value="">-- Sin Asignar --</option>
                    <%-- 
                        Iteramos sobre la lista de docentes que nos pasará el controlador
                        para crear las opciones del menú desplegable.
                    --%>
                    <c:forEach var="docente" items="${listaDocentes}">
                        <%-- 
                            Marcamos como "selected" el docente que ya tiene el curso (si estamos editando).
                        --%>
                        <option value="${docente.idDocente}" ${curso.docente.idDocente == docente.idDocente ? 'selected' : ''}>
                            <c:out value="${docente.nombres} ${docente.apellidos}" />
                        </option>
                    </c:forEach>
                </select>
                <div class="form-text">Puede dejar esta opción en blanco si el curso aún no tiene un docente asignado.</div>
            </div>

            <hr>

            <div class="d-flex justify-content-end">
                <a href="${pageContext.request.contextPath}/AdminCurso" class="btn btn-secondary me-2">
                    <i class="bi bi-x-circle"></i> Cancelar
                </a>
                
                <c:choose>
                    <c:when test="${curso != null}">
                        <input type="hidden" name="accion" value="editar">
                        <button type="submit" class="btn btn-primary">
                            <i class="bi bi-check-circle-fill"></i> Modificar Curso
                        </button>
                    </c:when>
                    <c:otherwise>
                        <input type="hidden" name="accion" value="registrar">
                        <button type="submit" class="btn btn-primary">
                            <i class="bi bi-check-circle-fill"></i> Registrar Curso
                        </button>
                    </c:otherwise>
                </c:choose>
            </div>
            
        </form>
    </div>
</div>

<jsp:include page="/includes/footer.jsp" />
