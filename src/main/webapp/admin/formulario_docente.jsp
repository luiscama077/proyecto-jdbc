<%-- /webapp/admin/formulario_docente.jsp --%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<jsp:include page="/includes/header.jsp">
    <jsp:param name="tituloPagina" value="${not empty docente.idDocente ? 'Editar' : 'Registrar'} Docente"/>
</jsp:include>

<h1 class="display-6 mb-4">
    <c:if test="${not empty docente.idDocente}">
        <i class="bi bi-pencil-square"></i> Editar Datos del Docente
    </c:if>
    <c:if test="${empty docente.idDocente}">
        <i class="bi bi-plus-circle-fill"></i> Registrar Nuevo Docente
    </c:if>
</h1>

<div class="card shadow-sm">
    <div class="card-body">
        <form action="${pageContext.request.contextPath}/AdminDocente">
        
            <%-- Campo oculto para el ID del docente (si estamos editando ) --%>
            <c:if test="${not empty docente.idDocente}">
                <input type="hidden" name="id" value="${docente.idDocente}" />
            </c:if>

            <h4 class="mb-3 border-bottom pb-2">Datos Personales</h4>

            <div class="row">
                <div class="col-md-6 mb-3">
                    <label for="nombres" class="form-label">Nombres:</label>
                    <input type="text" class="form-control" id="nombres" name="nombres" 
                           value="${docente.nombres}" required>
                </div>
                <div class="col-md-6 mb-3">
                    <label for="apellidos" class="form-label">Apellidos:</label>
                    <input type="text" class="form-control" id="apellidos" name="apellidos" 
                           value="${docente.apellidos}" required>
                </div>
            </div>

            <div class="row">
                <div class="col-md-6 mb-3">
                    <label for="dni" class="form-label">DNI:</label>
                    <input type="text" class="form-control" id="dni" name="dni" 
                           value="${docente.dni}" required maxlength="8" pattern="[0-9]{8}">
                </div>
                <div class="col-md-6 mb-3">
                    <label for="correo" class="form-label">Correo Electrónico:</label>
                    <input type="email" class="form-control" id="correo" name="correo" 
                           value="${docente.correo}" required>
                </div>
            </div>

            <%--
                SECCIÓN DE CUENTA DE USUARIO:
                Solo se muestra al crear un nuevo docente.
            --%>
            <c:if test="${empty docente.idDocente}">
                <hr>
                <h4 class="mb-3 border-bottom pb-2">Datos de la Cuenta de Usuario</h4>

                <div class="row">
                    <div class="col-md-6 mb-3">
                        <label for="username" class="form-label">Nombre de Usuario:</label>
                        <input type="text" class="form-control" id="username" name="username" required>
                        <div class="form-text">Este será el usuario con el que el docente iniciará sesión.</div>
                    </div>
                    <div class="col-md-6 mb-3">
                        <label for="password" class="form-label">Contraseña:</label>
                        <input type="password" class="form-control" id="password" name="password" required>
                        <div class="form-text">Establezca una contraseña inicial para el docente.</div>
                    </div>
                </div>
            </c:if>

            <hr>

            <div class="d-flex justify-content-end">
            
                <a href="${pageContext.request.contextPath}/AdminDocente" class="btn btn-secondary me-2">
                    <i class="bi bi-x-circle"></i> Cancelar
                </a>
                
                <%--
                    Lógica para cambiar el botón y el valor de "accion"
                    dependiendo de si estamos en modo de edición o registro.
                --%>
				<c:choose>
				    <c:when test="${docente != null}">
				        <input type="hidden" name="accion" value="editar">
				        <button type="submit" class="btn btn-primary">
				            <i class="bi bi-check-circle-fill"></i> Modificar Docente
				        </button>
				    </c:when>
				    <c:otherwise>
				        <input type="hidden" name="accion" value="registrar">
				        <button type="submit" class="btn btn-primary">
				            <i class="bi bi-check-circle-fill"></i> Registrar Docente
				        </button>
			    	</c:otherwise>
				</c:choose>
            </div>
            
        </form>
    </div>
</div>

<jsp:include page="/includes/footer.jsp" />
