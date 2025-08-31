<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">

<title>Iniciar Sesión - Sistema Académico</title>
    
    <%-- CDN de Bootstrap 5 (CSS ) y Bootstrap Icons --%>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css">
    
    <%-- Estilos personalizados para centrar el formulario --%>
    <style>
        body {
            display: flex;
            align-items: center;
            justify-content: center;
            height: 100vh;
            background-color: #f8f9fa;
        }
        .login-form {
            width: 100%;
            max-width: 400px;
            padding: 15px;
        }
    </style>

</head>
<body style="background-color: gray;">
	<main class="login-form">
        <form action="${pageContext.request.contextPath}/login" method="post">
            
            <div class="text-center mb-4">
                <i class="bi bi-journal-bookmark-fill" style="font-size: 4rem; color: #0d6efd;"></i>
                <h1 class="h3 mb-3 fw-normal">Sistema Académico</h1>
                <p>Por favor, inicie sesión</p>
            </div>

            <%--
                MANEJO DE MENSAJES DE ERROR:
                Este bloque solo se mostrará si el LoginServlet envía un atributo "mensajeError".
            --%>
            <c:if test="${not empty mensajeError}">
                <div class="alert alert-danger" role="alert">
                    <i class="bi bi-exclamation-triangle-fill"></i>
                    <c:out value="${mensajeError}" />
                </div>
            </c:if>

            <div class="form-floating mb-3">
                <input type="text" class="form-control" id="txtUsername" name="txtUsername" placeholder="Usuario" required autofocus>
                <label for="txtUsername">Nombre de Usuario</label>
            </div>
            
            <div class="form-floating mb-3">
                <input type="password" class="form-control" id="txtClave" name="txtClave" placeholder="Contraseña" required>
                <label for="txtClave">Contraseña</label>
            </div>

            <button class="w-100 btn btn-lg btn-primary" type="submit">
                <i class="bi bi-box-arrow-in-right"></i> Ingresar
            </button>
            
            <p class="mt-5 mb-3 text-muted text-center">&copy; 2025 - Todos los derechos reservados</p>
        </form>
    </main>

	
</body>
</html>