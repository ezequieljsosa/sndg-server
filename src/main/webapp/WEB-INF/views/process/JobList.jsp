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
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />

<meta name="header_title" content="Jobs" />
<meta name="header_title_desc" content="Runned processes" />

<meta name="_csrf" content="${_csrf.token}" />
<!-- default header name is X-CSRF-TOKEN -->
<meta name="_csrf_header" content="${_csrf.headerName}" />

<title>Blast</title>

<style type="text/css">
.box .box-header {
	padding-bottom: 10px;
}
</style>

<!-- DATA TABLES -->
<link href="${baseURL}/public/theme/css/datatables/dataTables.bootstrap.css"
	rel="stylesheet" type="text/css" />

</head>
<body>

	<script type="text/javascript">
		//<![CDATA[
	</script>

	<!-- DATA TABES SCRIPT -->
	<script
		src="${baseURL}/public/theme/js/plugins/datatables/jquery.dataTables.js"
		type="text/javascript"></script>
	<script
		src="${baseURL}/public/theme/js/plugins/datatables/dataTables.bootstrap.js"
		type="text/javascript"></script>


	<link rel="stylesheet"
		href="${baseURL}/public/biojs/resources/dependencies/jquery/jquery-ui-1.8.2.css" />
	<link rel="stylesheet"
		href="${baseURL}/public/biojs/resources/dependencies/jquery/jquery.tooltip.css" />


	<script src="${baseURL}/public/jslibs/jquery/jquery-migrate-1.2.1.js"></script>

	<script language="JavaScript" type="text/javascript"
		src="${baseURL}/public/biojs/Biojs.js"></script>

	<script language="JavaScript" type="text/javascript"
		src="${baseURL}/public/biojs/Biojs.Tooltip.js"></script>

	<script language="JavaScript" type="text/javascript"
		src="${baseURL}/public/biojs/Biojs.Sequence.js"></script>




	<script type="text/javascript">
		var load_genomes = function(genomes_from_user) {
			$("#seq_overlay").remove();
			$("#seq_loading").remove();

			$.each(genomes_from_user, function(index, genome) {
				if (genome.type == "peptide_collection") {
					$("#genomes_select").append(
							'<option selected="selected" value="' + genome.id + '">'
									+ genome.organism + '</option>');
				}

			});
			$("#option_to_delete").remove();
		};

		function load_jobs(jobs)
		{

			$(".btn").removeAttr("disabled").removeClass(".disabled");
			$("#wait_messages").remove();
			$(".loading-img").remove();
			$(".overlay").remove();

			$("#results_body")
					.html(
							'<table width="100%" id="results_table"><thead><th>ID</th><th>Date</th><th>Type</th><th>Status</th></thead><tbody></tbody></table>');
			$("#results_table")
					.show()
					.dataTable(
							{
								"ordering": false,
								"data" : jobs,
								"columns" : [										
										{
											"title" : "ID",
											"data" : null,
											"orderable": false,
											"render" : function(data, type,
													full, meta) {

													var query = null;
													if (data.result.length > 0){
														query = data.result[0].query;
													}
													
													return '<a href="' + $.api.url_job( data.type,data.id,query) 													
													+ '">'
													+ data.id
													+ '</a>';	
												
												
												
											}
										}, {
											"title" : "Date",
											"data" : "dateTime"
										}, {
											"title" : "Type",
											"data" : "type"
										}, {
											"title" : "Status",
											"data" : "status"
										} ]
							});

		};

		var document_ready_fn = function() {
			$.api.jobList(load_jobs)
		

		};
		$(document).ready(document_ready_fn);
	</script>

	<script type="text/javascript">
		// ]]>
	</script>


	
		<div class="row">
			<section class="col-lg-12 connectedSortable"></section>
			<div id="result_box" class="box box-primary">
			

				<div id="results_body" class="box-body table-responsive no-padding">


				</div>
			</div>
		</div>



</body>
	</html>
</jsp:root>