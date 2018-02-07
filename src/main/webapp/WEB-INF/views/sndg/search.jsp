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
		function init() {

			$('#searchBtn').click(function(evt) {
				evt.preventDefault()
				searchUrl()
			})

			$("#searchInput").keyup(function(evt) {
				if (evt.keyCode == 13) {
					evt.preventDefault()
					searchUrl();

				}
			});

		}
		function searchUrl(){
			window.location.href='${baseURL}/search/results?type=all&query=' + $("#searchInput").val(); 
		}

		$(document).ready(init);
	</script>


	<script type="text/javascript">
		// ]]>
	</script>

	<div class="row">

		<section class="col-lg-12">

			<table width="100%">
				<tr>

					<td width="100%"><input id="searchInput" width="100%"
						type="text" value="${query}" class="form-control" /></td>
					<td><button id="searchBtn" class="btn btn-info">
							<i class="fa fa-search">&#38;#160;</i>
						</button></td>
				</tr>

			</table>
			<br />
		</section>
	</div>


	<div class="row">

		<section class="col-lg-8">
			<div class="panel panel-default">
				<div class="panel-heading">Resultados</div>
				<ul class="list-group">
				<li class="list-group-item"><a
						href="${baseURL}/search/results?type=prot&#38;query=${query}">Proteins</a><span
						class="badge">${prot} </span></li>
					<li class="list-group-item"><span class="badge">${genome}</span>
						<a href="${baseURL}/search/results?type=genome&#38;query=${query}">Genomas</a></li>
					
					<li class="list-group-item"><a
						href="${baseURL}/search/results?type=struct&#38;query=${query}">Estructuras</a><span
						class="badge">${struct} </span></li>
					<li class="list-group-item"><a
						href="${baseURL}/search/results?type=tool&#38;query=${query}">Herramientas</a><span
						class="badge">${tool} </span></li>
					<li class="list-group-item"><a
						href="${baseURL}/search/results?type=barcode&#38;query=${query}">Barcodes</a><span
						class="badge">${barcode} </span></li>
					

				</ul>
			</div>
		</section>
	</div>

</body>
	</html>

</jsp:root>