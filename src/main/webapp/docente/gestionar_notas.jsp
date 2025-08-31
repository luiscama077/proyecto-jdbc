<%-- /webapp/docente/gestionar_notas.jsp --%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<jsp:include page="/includes/header.jsp">
    <jsp:param name="tituloPagina" value="Gestión de Notas"/>
</jsp:include>

<%-- ... (el resto del archivo hasta la tabla es igual ) ... --%>

<div class="card shadow-sm bg-dark">
    <div class="card-body">
        <form action="${pageContext.request.contextPath}/Nota" method="post">
            <input type="hidden" name="accion" value="guardarNotas">
            <input type="hidden" name="idCurso" value="${curso.idCurso}">
            
            <div class="table-responsive">
                <table class="table table-bordered table-hover align-middle">
                    <thead class="table-dark text-center">
                        <tr>
                            <th style="width: 30%;">Alumno</th>
                            <%-- 1. Pintamos las cabeceras de las evaluaciones --%>
                            <c:forEach var="evaluacion" items="${evaluaciones}">
                                <th><c:out value="${evaluacion.nombre}" /></th>
                            </c:forEach>
                        </tr>
                    </thead>
                    <tbody>
                        <%-- 2. Iteramos sobre cada fila que nos preparó el controlador --%>
                        <c:forEach var="fila" items="${filas}">
                            <tr>
                                <td>
                                    <c:out value="${fila.alumno.apellidos}, ${fila.alumno.nombres}" />
                                </td>
                                <%-- 3. Iteramos sobre la lista de notas ya ordenadas para esa fila --%>
                                <c:forEach var="nota" items="${fila.notasOrdenadas}" varStatus="loop">
                                    <td class="text-center">
                                        <%-- El nombre del input sigue siendo único --%>
                                        <input type="number" 
                                               class="form-control text-center" 
                                               name="nota_${fila.idMatricula}_${evaluaciones[loop.index].idEvaluacion}"
                                               min="0" max="20" step="0.1"
                                               value="<c:out value='${nota}' />">
                                    </td>
                                </c:forEach>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </div>
            <hr>
            
            <div class="d-flex justify-content-end">
			    <a href="${pageContext.request.contextPath}/DocenteHome" class="btn btn-secondary me-2">
			            <i class="bi bi-x-circle"></i> Cancelar
			    </a>
			    
			    <div class="d-flex justify-content-end">
			        <button type="submit" class="btn btn-primary btn-lg"><i class="bi bi-save-fill"></i> Guardar Cambios</button>
			    </div>
			</div>
        </form>
    </div>
</div>

<jsp:include page="/includes/footer.jsp" />
