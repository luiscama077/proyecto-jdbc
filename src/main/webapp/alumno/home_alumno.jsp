<%-- /webapp/alumno/home_alumno.jsp --%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<jsp:include page="/includes/header.jsp">
    <jsp:param name="tituloPagina" value="Mi Panel de Alumno"/>
</jsp:include>

<%-- Banner de Bienvenida --%>
<div class="p-5 mb-4 bg-dark text-white rounded-3 shadow">
    <div class="container-fluid py-4">
        <h1 class="display-5 fw-bold">
            <i class="bi bi-person-badge-fill"></i>
            Mi Panel de Estudiante
        </h1>
        <p class="col-md-9 fs-4">
            Bienvenido, <strong><c:out value="${sessionScope.usuarioLogueado.alumno.nombres}" /></strong>.
        </p>
        <p class="text-white-50">Resumen del período académico: <strong><c:out value="${nombrePeriodo}" /></strong></p>
    </div>
</div>

<%-- Sección de Cursos Matriculados --%>
<h2 class="border-bottom pb-2 mb-4">Mis Cursos Matriculados</h2>

<div class="row g-4">
    <c:choose>
        <c:when test="${not empty matriculas}">
            <c:forEach var="matricula" items="${matriculas}">
                <div class="col-md-6">
                    <div class="card h-100 shadow-sm bg-dark">
                        <div class="card-header fw-bold fs-5">
                            <i class="bi bi-book"></i> <c:out value="${matricula.curso.nombre}" />
                        </div>
                        <div class="card-body">
                            <h6 class="card-subtitle mb-2 text-white-50">
                                Docente: 
                                <c:choose>
                                    <c:when test="${not empty matricula.curso.docente}">
                                        <c:out value="${matricula.curso.docente.nombres} ${matricula.curso.docente.apellidos}" />
                                    </c:when>
                                    <c:otherwise>
                                        Sin asignar
                                    </c:otherwise>
                                </c:choose>
                            </h6>
                            
                            <p class="card-text"><strong>Notas Registradas:</strong></p>
                            <ul class="list-group list-group-flush">
                                <c:choose>
                                    <c:when test="${not empty matricula.notas}">
                                        <c:forEach var="nota" items="${matricula.notas}">
                                            <li class="list-group-item bg-transparent d-flex justify-content-between align-items-center">
                                                <c:out value="${nota.evaluacion.nombre}" />
                                                <span class="badge bg-primary rounded-pill fs-6">
                                                    <fmt:formatNumber type="number" maxFractionDigits="2" minFractionDigits="2" value="${nota.nota}" />
                                                </span>
                                            </li>
                                        </c:forEach>
                                    </c:when>
                                    <c:otherwise>
                                        <li class="list-group-item bg-transparent">Aún no hay notas registradas.</li>
                                    </c:otherwise>
                                </c:choose>
                            </ul>
                        </div>
                    </div>
                </div>
            </c:forEach>
            
        </c:when>
        
        <c:otherwise>
            <div class="col-12">
                <div class="alert alert-info">
                    <i class="bi bi-info-circle-fill"></i> No se encontraron cursos matriculados para el período actual.
                </div>
            </div>
        </c:otherwise>
    </c:choose>
</div>

<%-- Sección de Acciones --%>	
<div class="mt-5 p-4 bg-secondary rounded-3 text-center shadow">
    <h3 class="text-white">Proceso de Matrícula</h3>
    <p class="lead text-white-50">Inscríbete en los cursos para el próximo período académico.</p>
    <%-- Aquí iría la lógica para habilitar/deshabilitar el botón según las fechas --%>
    <a href="#" class="btn btn-primary btn-lg disabled" aria-disabled="true">
        <i class="bi bi-pencil-square"></i> Matrícula para 2025-II (Cerrada )
    </a>
    
    <a href="${pageContext.request.contextPath}/Matricula?accion=verFormulario" class="btn btn-success btn-lg">
        <i class="bi bi-pencil-square"></i> Matricularme para 2025-II (Abierta)
    </a>
    
</div>

<jsp:include page="/includes/footer.jsp" />
