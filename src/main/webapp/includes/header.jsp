<%-- /webapp/includes/header.jsp --%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
    
    <%--
    FILTRO DE SEGURIDAD BÁSICO:
    Si no hay un usuario en la sesión, se redirige inmediatamente al login.
    Esto protege todas las páginas que incluyan este header.
--%>
<c:if test="${empty sessionScope.usuarioLogueado}">
    <c:redirect url="${pageContext.request.contextPath}/login.jsp" />
</c:if>

    
<!DOCTYPE html>
<html >
<head>
<meta charset="ISO-8859-1">

<%-- El título se pasa como parámetro desde la página que incluye este header --%>
    <title>${param.tituloPagina} - Sistema Académico</title>
    
    <%-- CDN de Bootstrap 5 (CSS ) y Bootstrap Icons --%>

<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css">

 <!-- JQuery -->
<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>

<!-- DataTables con jQuery -->
<link rel="stylesheet" href="https://cdn.datatables.net/1.13.6/css/jquery.dataTables.min.css">
<script src="https://cdn.datatables.net/1.13.6/js/jquery.dataTables.min.js"></script>


</head>
<body style="background-color: gray;" data-bs-theme="dark">
	<header>
    <nav class="navbar navbar-expand-lg navbar-dark bg-dark shadow-sm">
        <div class="container-fluid">
            <a class="navbar-brand fw-bold" href="#">
                <i class="bi bi-journal-bookmark-fill"></i>
                Registro Académico
            </a>
            
            <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#main-nav">
                <span class="navbar-toggler-icon"></span>
            </button>
            
            <div class="collapse navbar-collapse" id="main-nav">
                <%-- MENÚ DE NAVEGACIÓN DINÁMICO --%>
                <ul class="navbar-nav me-auto mb-2 mb-lg-0">
                    
                    <%-- == MENÚ PARA ROL ADMIN == --%>
                    <c:if test="${sessionScope.usuarioLogueado.rol.nombre == 'ADMIN'}">
                        <li class="nav-item">
                            <a class="nav-link active" href="${pageContext.request.contextPath}/admin/home_admin.jsp">Inicio</a>
                        </li>
                    </c:if>
                    
                    <%-- == MENÚ PARA ROL DOCENTE == --%>
                    <c:if test="${sessionScope.usuarioLogueado.rol.nombre == 'DOCENTE'}">
                        <li class="nav-item">
                            <a class="nav-link active" href="${pageContext.request.contextPath}/DocenteHome">Inicio</a>
                        </li>
                    </c:if>
                    
                    <%-- == MENÚ PARA ROL ALUMNO == --%>
                    <c:if test="${sessionScope.usuarioLogueado.rol.nombre == 'ALUMNO'}">
                        <li class="nav-item">
                            <a class="nav-link active" href="${pageContext.request.contextPath}/AlumnoHome">Inicio</a>
                        </li>
                    </c:if>
                </ul>
                
                <%-- INFORMACIÓN DEL USUARIO Y LOGOUT --%>
                <div class="d-flex align-items-center">
                    <span class="navbar-text text-white me-3">
                        <i class="bi bi-person-fill"></i>
                        <c:out value="${sessionScope.usuarioLogueado.username}" />
                        (<c:out value="${sessionScope.usuarioLogueado.rol.nombre}" /> )
                    </span>
                    <a href="${pageContext.request.contextPath}/logout" class="btn btn-danger btn-sm">
                        <i class="bi bi-box-arrow-right"></i> Salir
                    </a>
                </div>
            </div>
        </div>
    </nav>
	</header>
	<%-- Contenedor principal que se cerrará en el footer --%>
	<main class="container mt-4">