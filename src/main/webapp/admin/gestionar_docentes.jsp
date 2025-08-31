<%-- /webapp/admin/gestionar_docentes.jsp --%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<jsp:include page="/includes/header.jsp">
    <jsp:param name="tituloPagina" value="Gestión de Docentes"/>
</jsp:include>

<div class="d-flex justify-content-between align-items-center mb-4">
    <h1 class="display-6"><i class="bi bi-person-video3"></i> Gestión de Docentes</h1>
    
    <%-- Botón para ir al formulario de creación de docentes --%>
    <a href="admin/formulario_docente.jsp" class="btn btn-primary">
        <i class="bi bi-plus-circle-fill"></i> Registrar Nuevo Docente
    </a>
</div>

<div class="card shadow-sm">
    <div class="card-body">
        <div class="table-responsive">
            <table class="table table-striped" id="mytable">
                <thead class="table-dark">
                    <tr>
                        <th>ID</th>
                        <th>Nombres y Apellidos</th>
                        <th>DNI</th>
                        <th>Correo Electrónico</th>
                        <th>Cuenta de Usuario</th>
                        <th class="text-center">Acciones</th>
                    </tr>
                </thead>
                <tbody>
                    <%-- Iteramos sobre la lista de docentes que nos pasará el controlador --%>
                    <c:forEach var="docente" items="${listaDocentes}">
                        <tr>
                            <td><c:out value="${docente.idDocente}" /></td>
                            <td><c:out value="${docente.nombres} ${docente.apellidos}" /></td>
                            <td><c:out value="${docente.dni}" /></td>
                            <td><c:out value="${docente.correo}" /></td>
                            <td>
	                            <span class="badge bg-success">
	                                <i class="bi bi-person-check-fill"></i> ${docente.usuario.username}
	                            </span>
                            </td>
                            <td class="text-center">
                                <%-- Botón para editar --%>
                                <a href="${pageContext.request.contextPath}/AdminDocente?accion=info&id=${docente.idDocente}" class="btn btn-primary btn-sm" title="Editar">
                                    <i class="bi bi-pencil"></i>
                                </a>
                                
                                <%-- Botón para eliminar --%>
                                <a href="${pageContext.request.contextPath}/AdminDocente?accion=eliminar&id=${docente.idDocente}" 
                                   class="btn btn-danger btn-sm" 
                                   title="Eliminar"
                                   onclick="return confirm('¿Está seguro de que desea eliminar a este docente? Esto también eliminará su cuenta de usuario asociada.' );">
                                    <i class="bi bi-trash3-fill"></i>
                                </a>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </div>
    </div>
</div>

<jsp:include page="/includes/footer.jsp" />
