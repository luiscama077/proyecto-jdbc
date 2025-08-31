<%-- /webapp/docente/home_docente.jsp --%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<jsp:include page="/includes/header.jsp">
    <jsp:param name="tituloPagina" value="Panel del Docente"/>
</jsp:include>

<%-- Banner de Bienvenida --%>
<div class="p-5 mb-4 bg-dark text-white rounded-3 shadow">
    <div class="container-fluid py-4">
        <h1 class="display-5 fw-bold">
            <i class="bi bi-person-video3"></i>
            Panel del Docente
        </h1>
        <p class="col-md-9 fs-4">
            Bienvenido, Profesor(a ) <strong><c:out value="${sessionScope.usuarioLogueado.docente.nombres} ${sessionScope.usuarioLogueado.docente.apellidos}" /></strong>.
        </p>
        <p class="text-white-50">A continuación se muestran los cursos que tiene asignados para el período actual.</p>
    </div>
</div>

<%-- Sección de Cursos Asignados --%>
<h2 class="border-bottom pb-2 mb-4">Mis Cursos Asignados</h2>

<div class="row g-4">
    <c:choose>
        <c:when test="${not empty listaCursosAsignados}">
            <c:forEach var="curso" items="${listaCursosAsignados}">
                <div class="col-md-6">
                    <div class="card h-100 shadow-sm bg-dark">
                        <div class="card-header fw-bold fs-5">
                            <i class="bi bi-book-half"></i> <c:out value="${curso.nombre}" />
                        </div>
                        <div class="card-body d-flex flex-column">
                            <p>
                                <span class="badge bg-primary rounded-pill">
                                    ${curso.alumnosMatriculados} Alumnos Inscritos
                                </span>
                            </p>
                            <p class="card-text">
                                Acceda al registro de calificaciones para ingresar o modificar las notas de los estudiantes de este curso.
                            </p>
                            <div class="mt-auto text-end">
                                <a href="${pageContext.request.contextPath}/Nota?accion=verAlumnosPorCurso&idCurso=${curso.idCurso}" class="btn btn-primary btn-lg">
                                    <i class="bi bi-pencil-square"></i> Gestionar Notas
                                </a>
                            </div>
                        </div>
                    </div>
                </div>
            </c:forEach>
        </c:when>
        <c:otherwise>
            <div class="col-12">
                <div class="alert alert-info">
                    <i class="bi bi-info-circle-fill"></i> Actualmente no tiene cursos asignados para este período.
                </div>
            </div>
        </c:otherwise>
    </c:choose>
</div>

<jsp:include page="/includes/footer.jsp" />
