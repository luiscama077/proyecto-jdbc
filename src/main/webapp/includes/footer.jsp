<%-- /webapp/includes/footer.jsp --%>

</main> <%-- Cierra la etiqueta <main> abierta en header.jsp --%>

<footer class="container text-center mt-5 py-4 border-top">
    <p class="text-muted">&copy; 2025 Sistema de Registro Académico. Desarrollado con JSP, Servlets y Bootstrap.</p>
</footer>

<%-- CDN de Bootstrap 5 (JavaScript Bundle) --%>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
<script>
	$(document).ready(function(){
		$('#mytable').DataTable();
	});
</script>
</body>
</html>