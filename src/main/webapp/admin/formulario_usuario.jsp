<%-- /webapp/admin/formulario_usuario.jsp --%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

 <%-- El título cambia dinámicamente si estamos creando o editando --%> 
<jsp:include page="/includes/header.jsp">
    <jsp:param name="tituloPagina" value="${not empty usuario.idUsuario ? 'Editar' : 'Crear Admin'} Usuario"/>
</jsp:include>

<%-- Título principal dinámico --%>
<h1 class="display-6 mb-4">
    <c:if test="${not empty usuario.idUsuario}">
        <i class="bi bi-pencil-square"></i> Editar Usuario
    </c:if>
    <c:if test="${empty usuario.idUsuario}">
        <i class="bi bi-plus-circle-fill"></i> Crear Nuevo Admin
    </c:if>
</h1>

<div class="card shadow-sm">
    <div class="card-body">
        <%-- El formulario envía los datos por POST al UsuarioController --%>
        <form action="${pageContext.request.contextPath}/AdminUsuario">
        
            <%-- 
                CAMPO OCULTO PARA EL ID:
                Si estamos editando, necesitamos enviar el ID del usuario para que el servlet
                sepa qué registro actualizar en la base de datos.
                Si estamos creando, este campo no tendrá valor.
            --%>
            <c:if test="${not empty usuario.idUsuario}">
                <input type="hidden" name="id" value="<c:out value='${usuario.idUsuario}' />" />
            </c:if>

            <div class="mb-3">
                <label for="username" class="form-label">Nombre de Usuario:</label>
                <input type="text" class="form-control" id="username" name="username" 
                       value="<c:out value='${usuario.username}' />" required>
            </div>

            <div class="mb-3">
                <label for="password" class="form-label">Contraseña:</label>
                <input type="text" class="form-control" id="password" name="password" 
                       value="<c:out value='${usuario.password}' />" required>
                <c:if test="${not empty usuario.idUsuario}">
                    <div class="form-text">Deje este campo en blanco si no desea cambiar la contraseña.</div>
                </c:if>
            </div>
            
            <c:if test="${not empty usuario.idUsuario}">
                    
	            <div class="mb-3">
	                <label for="idRol" class="form-label">Rol:</label>
	                <select class="form-select" id="idRol" name="idRol" required disabled>
	                    <option value="">-- Seleccione un rol --</option>
	                    <%-- 
	                        Iteramos sobre la lista de roles que nos pasó el servlet
	                        para crear las opciones del menú desplegable.
	                    --%>
	                    <c:forEach var="rol" items="${listaRoles}">
	                        <%-- 
	                            Marcamos como "selected" el rol que ya tiene el usuario (si estamos editando).
	                        --%>
	                        <option value="${rol.idRol}" ${usuario.rol.idRol == rol.idRol ? 'selected' : ''}>
	                            <c:out value="${rol.nombre}" />
	                        </option>
	                    </c:forEach>
	                </select>
	            </div>
            
            </c:if>


            <hr>

            <div class="d-flex justify-content-end">
                <%-- Botón para volver a la lista sin guardar cambios --%>
                <a href="${pageContext.request.contextPath}/AdminUsuario?accion=listar" class="btn btn-secondary me-2">
                    <i class="bi bi-x-circle"></i> Cancelar
                </a>
                
	            <c:choose>
	                  <c:when test="${usuario != null}">
	                      <input type="hidden" name="accion" value="editar">
	                      <button type="submit" class="btn btn-primary">
	                          <i class="bi bi-check-circle-fill"></i> Modificar Usuario
	                      </button>
	                  </c:when>
	                  <c:otherwise>
	                      <input type="hidden" name="accion" value="registrar">
	                      <button type="submit" class="btn btn-primary">
	                          <i class="bi bi-check-circle-fill"></i> Registrar Admin
	                      </button>
	                  </c:otherwise>
	             </c:choose>
              
            </div>
            
            
        </form>
    </div>
</div>

<jsp:include page="/includes/footer.jsp" />
