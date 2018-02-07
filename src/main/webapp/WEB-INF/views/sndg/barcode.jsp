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
<meta name="header_title" content="Barcode" />

</head>
<body>

	

	<script type="text/javascript">
		//<![CDATA[
	</script>
	
	<script src="${baseURL}/public/jslibs/jquery/jquery-migrate-1.2.1.js"></script>
	<script language="JavaScript" type="text/javascript"
		src="${baseURL}/public/biojs/Biojs.js"></script>

	<script language="JavaScript" type="text/javascript"
		src="${baseURL}/public/biojs/Biojs.Tooltip.js"></script>

	<script language="JavaScript" type="text/javascript"
		src="${baseURL}/public/biojs/Biojs.Sequence.js"></script>


<script src="${baseURL}/public/bia/ui/SequencePanel.js"></script>

	<script>

$(document).ready(function() {
var sp = new $.SequencePanel("sequence_box", '${bc.sequences.sequence[0].nucleotides}', '${bc.processid}',
		'${bc.processid}');
sp.init();
});
</script>

	<script type="text/javascript">
		// ]]>
	</script>


	<div class="row">

		<section class="col-lg-6">

			<div class="panel panel-default">
				<div class="panel-heading">Barcode</div>
				<table class="table">

					<tbody>
						<tr>
							<td><b>Identificador</b></td>
							<td><a class="external"
								href="http://www.boldsystems.org/index.php/Public_RecordView?processid=${bc.processid}">${bc.processid}</a>
								(registro en BOLD)</td>
						</tr>
						<tr>
							<td><b>Provincia</b></td>
							<td>${bc.collection_event.province_state}</td>
						</tr>
						<tr>
							<td><b>Tom&#243; muestra</b></td>
							<td>${bc.collection_event.collectors}</td>
						</tr>
						<tr>
							<td><b>Almacena muestra</b></td>
							<td>${bc.specimen_identifiers.institution_storing}</td>
						</tr>
						<tr>
							<td><b>Marcador</b></td>
							<td>${bc.sequences.sequence[0].markercode}</td>
						</tr>
						<tr>
							<td><b>Secuencia</b></td>
							<td><div id="sequence_box"></div></td>
						</tr>


					</tbody>
				</table>


			</div>

		</section>
		<section class="col-lg-6">
		<img height="400px" src="${bc.specimen_imagery.media[0].image_file}" />
		</section>
	</div>

</body>
	</html>
</jsp:root>
