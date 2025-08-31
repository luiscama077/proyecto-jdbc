<%-- /webapp/admin/gestionar_usuarios.jsp --%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<jsp:include page="/includes/header.jsp">
    <jsp:param name="tituloPagina" value="Gestión de Usuarios"/>
</jsp:include>

<div class="d-flex justify-content-between align-items-center mb-4">
    <h1 class="display-6"><i class="bi bi-people-fill"></i> Gestión de Usuarios</h1>
    <%-- Botón para ir al formulario de creación --%>
    <a href="${pageContext.request.contextPath}/AdminUsuario?accion=mostrarFormulario" class="btn btn-primary">
        <i class="bi bi-plus-circle-fill"></i> Registrar un admin
    </a>
</div>

<div class="card shadow-sm">
    <div class="card-body">
        <div class="table-responsive">
            <table class="table table-hover table-striped align-middle" id="mytable">
                <thead class="table-dark">
                    <tr>
                        <th>ID</th>
                        <th>Username</th>
                        <th>Rol</th>
                        <th class="text-center">Acciones</th>
                    </tr>
                </thead>
                <tbody>
                    <%-- 
                        Iteramos sobre la lista de usuarios que nos pasó el UsuarioController.
                        La variable "listaUsuarios" debe coincidir con la que se define en el servlet:
                        request.setAttribute("listaUsuarios", listaUsuarios);
                    --%>
                    <c:forEach var="usuario" items="${listaUsuarios}">
                        <tr>
                            <td><c:out value="${usuario.idUsuario}" /></td>
                            <td><c:out value="${usuario.username}" /></td>
                            <td>
                                <%-- Usamos un badge de Bootstrap para que el rol se vea mejor --%>
                                <c:choose>
                                    <c:when test="${usuario.rol.nombre == 'ADMIN'}">
                                        <span class="badge bg-danger"><c:out value="${usuario.rol.nombre}" /></span>
                                    </c:when>
                                    <c:when test="${usuario.rol.nombre == 'DOCENTE'}">
                                        <span class="badge bg-warning text-dark"><c:out value="${usuario.rol.nombre}" /></span>
                                    </c:when>
                                    <c:otherwise>
                                        <span class="badge bg-info text-dark"><c:out value="${usuario.rol.nombre}" /></span>
                                    </c:otherwise>
                                </c:choose>
                            </td>
                            <td class="text-center">
                                <%-- Botón para editar: llama al servlet con la acción y el ID del usuario --%>
                                <a href="${pageContext.request.contextPath}/AdminUsuario?accion=info&id=${usuario.idUsuario}" class="btn btn-success btn-sm" title="Editar">
                                    <i class="bi bi-pencil-square"></i> Editar
                                </a>
                                
                                <%-- Botón para eliminar: llama al servlet con la acción y el ID --%>
                                <%-- Añadimos un confirm de JavaScript para seguridad --%>
                                
                                <c:if test="${usuario.rol.nombre == 'ADMIN'}">
	                                <a href="${pageContext.request.contextPath}/AdminUsuario?accion=eliminar&id=${usuario.idUsuario}" 
	                                   class="btn btn-danger btn-sm" 
	                                   title="Eliminar"
	                                   onclick="return confirm('¿Está seguro de que desea eliminar a este admin?');">
	                                    <i class="bi bi-trash3-fill"></i> Eliminar
	                                </a>
				            	</c:if>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </div>
    </div>
</div>

<jsp:include page="/includes/footer.jsp" />
