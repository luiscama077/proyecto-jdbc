<%-- /webapp/alumno/formulario_matricula.jsp --%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<jsp:include page="/includes/header.jsp">
    <jsp:param name="tituloPagina" value="Proceso de Matrícula"/>
</jsp:include>

<h1 class="display-6 mb-4">
    <i class="bi bi-pencil-square"></i> Proceso de Matrícula para el Período ${nombrePeriodo}
</h1>

<%-- Mensaje de error si no se selecciona ningún curso --%>
<c:if test="${not empty mensajeError}">
    <div class="alert alert-danger" role="alert">
        <i class="bi bi-exclamation-triangle-fill"></i> <c:out value="${mensajeError}" />
    </div>
</c:if>

<div class="card shadow-sm bg-secondary">
    <div class="card-body">
        <form action="${pageContext.request.contextPath}/Matricula" method="post">
            <input type="hidden" name="accion" value="matricular">
            
            <p class="lead">Seleccione los cursos en los que desea matricularse:</p>
            
            <div class="table-responsive">
                <table class="table table-hover align-middle">
                    <thead class="table-dark">
                        <tr>
                            <th style="width: 5%;" class="text-center">Seleccionar</th>
                            <th>Nombre del Curso</th>
                            <th>Docente Asignado</th>
                            <th class="text-center">Créditos</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:choose>
                            <c:when test="${not empty cursosDisponibles}">
                                <c:forEach var="curso" items="${cursosDisponibles}">
                                    <tr>
                                        <td class="text-center">
                                            <div class="form-check d-flex justify-content-center">
                                                <input class="form-check-input" type="checkbox" name="idCurso" value="${curso.idCurso}" id="curso_${curso.idCurso}">
                                            </div>
                                        </td>
                                        <td>
                                            <label class="form-check-label w-100" for="curso_${curso.idCurso}">
                                                <c:out value="${curso.nombre}" />
                                            </label>
                                        </td>
                                        <td>
                                            <c:choose>
                                                <c:when test="${not empty curso.docente.nombres}">
                                                    <c:out value="${curso.docente.nombres} ${curso.docente.apellidos}" />
                                                </c:when>
                                                <c:otherwise>
                                                    <span class="text-white-50">Por definir</span>
                                                </c:otherwise>
                                            </c:choose>
                                        </td>
                                        <td class="text-center"><c:out value="${curso.creditos}" /></td>
                                    </tr>
                                </c:forEach>
                            </c:when>
                            <c:otherwise>
                                <tr>
                                    <td colspan="4" class="text-center p-4">
                                        ¡Felicidades! Ya se encuentra matriculado en todos los cursos ofertados para este período.
                                    </td>
                                </tr>
                            </c:otherwise>
                        </c:choose>
                    </tbody>
                </table>
            </div>

            <hr>

            <div class="d-flex justify-content-end">
                <a href="${pageContext.request.contextPath}/AlumnoHome" class="btn btn-secondary me-2">
                    <i class="bi bi-x-circle"></i> Volver al Panel
                </a>
                <c:if test="${not empty cursosDisponibles}">
                    <button type="submit" class="btn btn-primary btn-lg">
                        <i class="bi bi-check-circle-fill"></i> Confirmar Matrícula
                    </button>
                </c:if>
            </div>
        </form>
    </div>
</div>

<jsp:include page="/includes/footer.jsp" />
