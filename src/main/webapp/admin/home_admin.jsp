<%-- /webapp/admin/home_admin.jsp --%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<%-- 1. Incluimos la cabecera. El header.jsp ya tiene el navbar, que adaptaremos. --%>
<jsp:include page="/includes/header.jsp">
    <jsp:param name="tituloPagina" value="Panel del Administrador"/>
</jsp:include>

<%-- 2. Contenido espec�fico de la p�gina de inicio del Admin con dise�o oscuro --%>

<%-- Banner de bienvenida con fondo oscuro --%>
<div class="p-5 mb-4 bg-dark text-white rounded-3 shadow">
    <div class="container-fluid py-4">
        <h1 class="display-5 fw-bold">
            <i class="bi bi-shield-lock-fill"></i>
            Panel de Administraci�n
        </h1>
        <p class="col-md-9 fs-4">
            Bienvenido, <strong><c:out value="${sessionScope.usuarioLogueado.username}" /></strong>.
            Desde este panel central, tiene acceso a todas las funciones de gesti�n del sistema.
        </p>
        <p class="text-white-50">Utilice los accesos directos a continuaci�n o el men� de navegaci�n superior.</p>
    </div>
</div>

<%-- Secci�n de tarjetas de gesti�n en una cuadr�cula de 2x2 --%>
<div class="row g-4">

    <%-- Tarjeta para Gestionar Alumnos --%>
    <div class="col-md-6">
        <div class="card text-white bg-dark h-100 shadow">
            <div class="card-body d-flex flex-column">
                <div class="d-flex align-items-start mb-3">
                    <div class="fs-1 me-3"><i class="bi bi-person-badge"></i></div>
                    <div>
                        <h2 class="card-title">Alumnos</h2>
                        <p class="card-text">Gestione el registro, la edici�n y la eliminaci�n de las cuentas de los alumnos.</p>
                    </div>
                </div>
                <div class="mt-auto text-end">
                    <a class="btn btn-outline-light" href="${pageContext.request.contextPath}/AdminAlumno" role="button">
                        Gestionar Alumnos <i class="bi bi-arrow-right-circle"></i>
                    </a>
                </div>
            </div>
        </div>
    </div>

    <%-- Tarjeta para Gestionar Docentes --%>
    <div class="col-md-6">
        <div class="card text-white bg-dark h-100 shadow">
            <div class="card-body d-flex flex-column">
                <div class="d-flex align-items-start mb-3">
                    <div class="fs-1 me-3"><i class="bi bi-person-video3"></i></div>
                    <div>
                        <h2 class="card-title">Docentes</h2>
                        <p class="card-text">Administre las cuentas de los docentes, incluyendo su asignaci�n a los cursos.</p>
                    </div>
                </div>
                <div class="mt-auto text-end">
                    <a class="btn btn-outline-light" href="${pageContext.request.contextPath}/AdminDocente" role="button">
                        Gestionar Docentes <i class="bi bi-arrow-right-circle"></i>
                    </a>
                </div>
            </div>
        </div>
    </div>

    <%-- Tarjeta para Gestionar Cursos --%>
    <div class="col-md-6">
        <div class="card text-white bg-dark h-100 shadow">
            <div class="card-body d-flex flex-column">
                <div class="d-flex align-items-start mb-3">
                    <div class="fs-1 me-3"><i class="bi bi-book-half"></i></div>
                    <div>
                        <h2 class="card-title">Cursos</h2>
                        <p class="card-text">Cree nuevos cursos, asigne cr�ditos y vincule docentes a cada materia acad�mica.</p>
                    </div>
                </div>
                <div class="mt-auto text-end">
                    <a class="btn btn-outline-light" href="${pageContext.request.contextPath}/AdminCurso" role="button">
                        Gestionar Cursos <i class="bi bi-arrow-right-circle"></i>
                    </a>
                </div>
            </div>
        </div>
    </div>

    <%-- Tarjeta para Gestionar Usuarios (Admins ) --%>
    <div class="col-md-6">
        <div class="card text-white bg-dark h-100 shadow">
            <div class="card-body d-flex flex-column">
                <div class="d-flex align-items-start mb-3">
                    <div class="fs-1 me-3"><i class="bi bi-people-fill"></i></div>
                    <div>
                        <h2 class="card-title">Usuarios</h2>
                        <p class="card-text">Revise todas las cuentas del sistema y gestione los perfiles de administrador.</p>
                    </div>
                </div>
                <div class="mt-auto text-end">
                    <a class="btn btn-outline-light" href="${pageContext.request.contextPath}/AdminUsuario" role="button">
                        Gestionar Usuarios <i class="bi bi-arrow-right-circle"></i>
                    </a>
                </div>
            </div>
        </div>
    </div>

</div>

<%-- 3. Incluimos el pie de p�gina --%>
<jsp:include page="/includes/footer.jsp" />
