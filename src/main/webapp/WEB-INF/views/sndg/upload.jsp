<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page"
	xmlns:spring="http://www.springframework.org/tags"
	xmlns:c="http://java.sun.com/jsp/jstl/core"
	xmlns:form="http://www.springframework.org/tags/form"
	xmlns:fn="http://java.sun.com/jsp/jstl/functions"
	xmlns:sec="http://www.springframework.org/security/tags" version="2.0">
	<jsp:directive.page language="java" contentType="text/html" />

	<c:set var="reqUrl">${pageContext.request.requestURL}</c:set>
	<c:set var="baseURL"
		value="${fn:replace(reqUrl, pageContext.request.requestURI,pageContext.request.contextPath)}" />


	<html>
<head>

<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta name="header_title" content="Resultados" />

</head>
<body>

	<script type="text/javascript">
		//<![CDATA[
	</script>

	<script type="text/javascript">
		// ]]>
	</script>

<div class="row">
<h1>Resultados</h1>
<section class="col-lg-6">
	<ul class="list-group">
		<li class="list-group-item">
		<span class="badge">14</span>
		<a>Genomas</a>
		 
		</li>
		<li class="list-group-item">
		<span class="badge">15 </span>
		Secuencias</li>
		<li class="list-group-item">Estructuras</li>
		<li class="list-group-item">Herramientas</li>

	</ul>
</section>
</div>

</body>
	</html>

</jsp:root>