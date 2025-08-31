<%-- /webapp/admin/gestionar_alumnos.jsp --%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<jsp:include page="/includes/header.jsp">
    <jsp:param name="tituloPagina" value="Gesti�n de Alumnos"/>
</jsp:include>

<div class="d-flex justify-content-between align-items-center mb-4">
    <h1 class="display-6"><i class="bi bi-person-badge"></i> Gesti�n de Alumnos</h1>
    
    <%-- Bot�n para ir al formulario de creaci�n --%>
    <a href="admin/formulario_alumno.jsp" class="btn btn-primary">
        <i class="bi bi-plus-circle-fill"></i> Inscribir Nuevo Alumno
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
                        <th>Correo Electr�nico</th>
                        <th>Cuenta de Usuario</th>
                        <th class="text-center">Acciones</th>
                    </tr>
                </thead>
                <tbody>
                    <%-- Iteramos sobre la lista de alumnos que nos pasar� el controlador --%>
                    <c:forEach var="alumno" items="${listaAlumnos}">
                        <tr>
                            <td><c:out value="${alumno.idAlumno}" /></td>
                            <td><c:out value="${alumno.nombres} ${alumno.apellidos}" /></td>
                            <td><c:out value="${alumno.dni}" /></td>
                            <td><c:out value="${alumno.correo}" /></td>
                            <td>
	                            <span class="badge bg-success">
	                                <i class="bi bi-person-check-fill"></i> ${alumno.usuario.username}
	                            </span>
                            </td>
                            <td class="text-center">
                                <%-- Bot�n para editar --%>
                                <a href="${pageContext.request.contextPath}/AdminAlumno?accion=info&id=${alumno.idAlumno}" class="btn btn-primary btn-sm" title="Editar">
                                    <i class="bi bi-pencil"></i>
                                </a>
                                
                                <%-- Bot�n para eliminar --%>
                                <a href="${pageContext.request.contextPath}/AdminAlumno?accion=eliminar&id=${alumno.idAlumno}" 
                                   class="btn btn-danger btn-sm" 
                                   title="Eliminar"
                                   onclick="return confirm('�Est� seguro de que desea eliminar a este alumno? Esto tambi�n eliminar� su cuenta de usuario asociada.');">
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
