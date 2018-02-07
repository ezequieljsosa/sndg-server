<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page"
	xmlns:spring="http://www.springframework.org/tags"
	xmlns:c="http://java.sun.com/jsp/jstl/core"
	xmlns:fn="http://java.sun.com/jsp/jstl/functions"
	xmlns:form="http://www.springframework.org/tags/form" version="2.0">
	<jsp:directive.page language="java" contentType="text/html" />
		<c:set var="reqUrl">${pageContext.request.requestURL}</c:set>
	<c:set var="baseURL" value="${fn:replace(reqUrl, pageContext.request.requestURI,pageContext.request.contextPath)}" />

	<html>
<head>

<title>Gene Details</title>
</head>
<body>

	<script type="text/javascript">
		//<![CDATA[
	</script>

	<script src="${baseURL}/public/jslibs/jquery/jquery-migrate-1.2.1.js"></script>

	<script src="${baseURL}/public/bia/ui/SequencePanel.js"></script>
	<script src="${baseURL}/public/bia/model/Localization.js"></script>

	<script src="${baseURL}/public/bia/ui/JBrowseWrapper.js"
		type="text/javascript"></script>


	<!-- BIOJS SCRIPT -->

	<script language="JavaScript" type="text/javascript"
		src="${baseURL}/public/biojs/Biojs.js"></script>

	<script language="JavaScript" type="text/javascript"
		src="${baseURL}/public/biojs/Biojs.Tooltip.js"></script>

	<script language="JavaScript" type="text/javascript"
		src="${baseURL}/public/biojs/Biojs.Sequence.js"></script>

<script type="text/javascript">


var genome_id = '${genome_id}';

</script>

	<script type="text/javascript">
		function setJbrowseURL(genome_name) {
			var loc = new $.Localization('${contig}'.split(".")[0], ${start} , ${end} );
			
			var jbrowse = new $.JBrowseWrapper($("#jbrowse"),
					"${baseURL}/public/jbrowse/?data=data/");			
			jbrowse.init(genome_name, true, loc.expand().expand(), loc);
		
		}
		

		$(document).ready(function() {
			
			setJbrowseURL('${genome_id}')
			$("<a/>",{"href":"${baseURL}/genome"}).html("Genomes").appendTo( $("#base_breadcrumb") );
			var li = $("<li/>").appendTo( $(".breadcrumb") );
			$("<a/>",{"href":"${baseURL}/genome/" + '${genome_id}'}).html("<i>" + '${genome_desc}' + "</i>").appendTo(li);
		});
	</script>

	<script type="text/javascript">
		// ]]>
	</script>

	<div id="jbrowse_row" class="row">
		<div class="col-xs-12">
			<div class="box box-primary">
				<div class="box-body">
					<![CDATA[
		<iframe id="jbrowse" style="border: 1px solid black"
			
			height="400px" width="100%"> </iframe>
	]]>

				</div>
			
			</div>
		</div>
	</div>





	


</body>
	</html>
</jsp:root>