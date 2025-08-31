<%-- /webapp/admin/formulario_alumno.jsp --%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<jsp:include page="/includes/header.jsp">
    <jsp:param name="tituloPagina" value="${not empty alumno.idAlumno ? 'Editar' : 'Registrar'} Alumno"/>
</jsp:include>

<h1 class="display-6 mb-4">
    <c:if test="${not empty alumno.idAlumno}">
        <i class="bi bi-pencil-square"></i> Editar Datos del Alumno
    </c:if>
    <c:if test="${empty alumno.idAlumno}">
        <i class="bi bi-plus-circle-fill"></i> Registrar Nuevo Alumno
    </c:if>
</h1>

<div class="card shadow-sm">
    <div class="card-body">
        <form action="${pageContext.request.contextPath}/AdminAlumno">
        
            <%-- Campo oculto para el ID del alumno (si estamos editando) --%>
            <c:if test="${not empty alumno.idAlumno}">
                <input type="hidden" name="id" value="${alumno.idAlumno}" />
            </c:if>

            <h4 class="mb-3 border-bottom pb-2">Datos Personales</h4>

            <div class="row">
                <div class="col-md-6 mb-3">
                    <label for="nombres" class="form-label">Nombres:</label>
                    <input type="text" class="form-control" id="nombres" name="nombres" 
                           value="${alumno.nombres}" required>
                </div>
                <div class="col-md-6 mb-3">
                    <label for="apellidos" class="form-label">Apellidos:</label>
                    <input type="text" class="form-control" id="apellidos" name="apellidos" 
                           value="${alumno.apellidos}" required>
                </div>
            </div>

            <div class="row">
                <div class="col-md-6 mb-3">
                    <label for="dni" class="form-label">DNI:</label>
                    <input type="text" class="form-control" id="dni" name="dni" 
                           value="${alumno.dni}" required maxlength="8" pattern="[0-9]{8}">
                </div>
                <div class="col-md-6 mb-3">
                    <label for="correo" class="form-label">Correo Electrónico:</label>
                    <input type="email" class="form-control" id="correo" name="correo" 
                           value="${alumno.correo}" required>
                </div>
            </div>

            <c:if test="${empty alumno.idAlumno}">
                <hr>
                <h4 class="mb-3 border-bottom pb-2">Datos de la Cuenta de Usuario</h4>

                <div class="row">
                    <div class="col-md-6 mb-3">
                        <label for="username" class="form-label">Nombre de Usuario:</label>
                        <input type="text" class="form-control" id="username" name="username" required>
                        <div class="form-text">Este será el usuario con el que el alumno iniciará sesión.</div>
                    </div>
                    <div class="col-md-6 mb-3">
                        <label for="password" class="form-label">Contraseña:</label>
                        <input type="password" class="form-control" id="password" name="password" required>
                        <div class="form-text">Establezca una contraseña inicial para el alumno.</div>
                    </div>
                </div>
            </c:if>

            <hr>

            <div class="d-flex justify-content-end">
            
                <a href="${pageContext.request.contextPath}/AdminAlumno" class="btn btn-secondary me-2">
                    <i class="bi bi-x-circle"></i> Cancelar
                </a>
                
				<c:choose>
				    <c:when test="${alumno != null}">
				        <input type="hidden" name="accion" value="editar">
				        <button type="submit" class="btn btn-primary">
				            <i class="bi bi-check-circle-fill"></i> Modificar Alumno
				        </button>
				    </c:when>
				    <c:otherwise>
				        <input type="hidden" name="accion" value="registrar">
				        <button type="submit" class="btn btn-primary">
				            <i class="bi bi-check-circle-fill"></i> Registrar Alumno
				        </button>
			    	</c:otherwise>
				</c:choose>
            </div>
            
            
        </form>
    </div>
</div>

<jsp:include page="/includes/footer.jsp" />
