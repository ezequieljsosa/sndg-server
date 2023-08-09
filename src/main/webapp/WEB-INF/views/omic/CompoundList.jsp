<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page"
	xmlns:spring="http://www.springframework.org/tags"
	xmlns:c="http://java.sun.com/jsp/jstl/core"
	xmlns:fn="http://java.sun.com/jsp/jstl/functions"
	xmlns:form="http://www.springframework.org/tags/form"
	xmlns:sec="http://www.springframework.org/security/tags" version="2.0">
	<jsp:directive.page language="java" contentType="text/html" />


	<html>
<head>
		<c:set var="reqUrl">${pageContext.request.requestURL}</c:set>
	<c:set var="baseURL" value="${fn:replace(reqUrl, pageContext.request.requestURI,pageContext.request.contextPath)}" />

<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta name="header_title" content="Genomes" />
<meta name="header_title_desc" content="" />
<title>Genome List</title>

<!-- DATA TABLES -->
<link
	href="${baseURL}/public/theme/css/datatables/dataTables.bootstrap.css"
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




	<script type="text/javascript">


		function init() {
			var options = {
					 "lengthMenu": [[ 25, 50 ], [ 25, 50, "All"]],
			
				// "ajax" : $.api.url_genomes(),
				//"data": compounds,
				// "processing" : true,
				// "serverSide" : true,
				"searching":true,
				// "language" : {
				// 	"search" : "Filter: "
				// },
				initComplete : function(e) {
					$(".loading-img").remove();
					$(".overlay").remove();

				},
				// searchCols : [ null, null, null, null, null, null ],
				"ajax" : {"url":"${baseURL}/compound/search/${genome}/${gene}" , "type": "POST"   },
				"lengthMenu": [[100, -1], [100, "All"]],
				//"deferLoading": 0,   //Para que no haga la carga inicial
				"processing" : true,
				"serverSide" : true,
				"ordering": false,
				"language" : {
					"search" : "Filter: "
				},
				"columns" : [
						{
							"title" : "Name",

							"data" : "name",
							/*"render" : function(data, type, row) {
								var result = '<a href="' + $.api.url_genome(data)
										 + '">' + row.organism + ' - Overview </a>  '  ;								
								return '<i>' +  result + '</i>' 

							},*/
						},
					{
						"title": "ligand_list",
						"data": "ligand_list"
					},

					{
						"title": "source",
						"data": "source"
					},
					{
						"title": "Smiles",
						"data": "smiles"
					},

					{
						"title": "PFam",
						"data": "pfam"
					},
					{
						"title": "Zincs",
						"data": "zincs"
					},

						 ],
				"order" : [ [ 0, 'asc' ] ]
			};

			$('#genome-table').dataTable(options);

		}
		$(document).ready(init);
	</script>

	<script type="text/javascript">
		// ]]>
	</script>

	
	<div class="row">
		<section class="col-xs-12">

			<div class="box box-primary">

				<div class="box-body no-padding">

					<table id="genome-table" class="table table-bordered table-hover">
						<thead>
							<tr>

								<th>Name</th>
								
								<!-- <th>Status</th> -->
								<!-- 								<th>Size</th>			
								<th>Genes</th> -->
								
								<th># Proteins</th>
								<th># Pathways</th>
								<th># Cristals</th>
								<th># Models </th>
								<th># Models </th>
							</tr>
						</thead>
						<tbody>
						</tbody>

					</table>

				</div>
				<div class="overlay">&#160;</div>
				<div class="loading-img">&#160;</div>


			</div>
		</section>
	</div>
</body>
	</html>

</jsp:root>