<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page"
	xmlns:spring="http://www.springframework.org/tags"
	xmlns:c="http://java.sun.com/jsp/jstl/core"
	xmlns:form="http://www.springframework.org/tags/form" version="2.0">
	<jsp:directive.page language="java" contentType="text/html" />

<html>
	<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
	<title>Variants</title>

	<meta name="header_title" content="Variant analysis" />
	<meta name="header_title_desc" content="Gene level" />

	<!-- DATA TABLES -->
	<link href="../public/theme/css/datatables/dataTables.bootstrap.css" rel="stylesheet" type="text/css" />

	</head>

	<body>

	<script type="text/javascript">
		//<![CDATA[
	</script>

	<!-- DATA TABES SCRIPT -->
	<script
		src="../public/theme/js/plugins/datatables/jquery.dataTables.js"
		type="text/javascript">
	</script>
	<script
		src="../public/theme/js/plugins/datatables/dataTables.bootstrap.js"
		type="text/javascript">
	</script>

	<script type="text/javascript">
		//Refresca la tabla (por ahora, llamado al cargar y al actualizar la seleccion de strains)
		function refreshtable() {
			var strain_list_raw = $('#strain_list').val();
			var strain_list = strain_list_raw.join();
			alert(strain_list+"ddd");
			var async_response = $
					.getJSON("../rest/variant/Meloidogyne%20incognita?strains="
							+ strain_list);
			async_response
					.done(function(variant_list) {
						
						//Crea el encabezado de la tabla
						var variant_list_control_header = $('#variants-table-head');
						variant_list_control_header.html('')
						header = '<tr><th>Proteins</th><th>Description</th>'
						for ( var key in strain_list_raw) {
							header = header + '<th>' + strain_list_raw[key]
									+ '</th>'
						}
						header = header + '</tr>'
						variant_list_control_header.append(header);

						
						//Crea el cuerpo de la tabla
						var variant_list_control = $('#variants-table-body');
						variant_list_control.html(''); 
						
					/*							
												 
						$.each(variant_list, function(index, element) {
						 var option = '<tr>'
						 element.gen = '<a href="#">' + element.gen + '</a>';
						 for ( var key in element) {
						 var option = option + '<td>' + element[key] + '</td>'
						 }
						 var option = option + '</tr>'

						 variant_list_control.append(option);

						 });
*/

						$('#variants-table')
								.dataTable(
										{
											"processing" : true,
											"serverSide" : true,
											"ajax" : "../rest/variant/Meloidogyne%20incognita?strains=" + strain_list,
											"language" : {
												"search" : "Filter: "
											},

											"columns" : [ {
												"data" : "gen",
												"render" : function(data, type, row) {
																return '<a href="Protein.jsp?protein='
																+ data.id
																+ '"><i class="fa fa-link">&#160;</i>  </a>';
															},
												"defaultContent" : ''
											}, {
												"data" : "gene",
												"defaultContent" : "?",
												"render" : function(data, type, row) {
													if (row["gene_id"] != "#" && row["gene_id"] != null) {
														return '<a href="../rest/redirect/?type=gene&key=_id&value='
																+ row["gene_id"] + '">' + data + '</a>';
													} else {
														return data;
													}
												}
											}, {
												"data" : "description",
												"defaultContent" : '-'
											}, {
												"data" : "strain",
												"defaultContent" : '-'
											}, {
												"data" : "strain",
												"defaultContent" : '-'
											} ],
											"order" : [ [ 2, 'asc' ] ]
										});

					});

			async_response.fail(function(jqxhr, textStatus, error) {
				var err = textStatus + ", " + error;
				console.log("Request Failed: " + err);
			});
		}

		function init_variants() {
			//load_strains http://localhost:8080/xomeq/rest/variant/Meloidogyne%20incognita/strains
			console.log("ready!");

			var async_response = $
					.getJSON("../rest/variant/Meloidogyne%20incognita/strains");

			async_response.done(function(strain_list_query) {
				var strain_list_control = $('#strain_list');
				
				$.each(strain_list_query, function(index, element) {
					var option_sel = '<option>';
					if (index < 2) {
						option_sel = '<option  selected="selected">';
					}
					var option = option_sel + element.strain + '</option>';
					strain_list_control.append(option);
				});
				$("#firstoption").remove();
				refreshtable();
				//$('#variants-table').dataTable(table_options);

			});

			async_response.fail(function(jqxhr, textStatus, error) {
				var err = textStatus + ", " + error;
				console.log("Request Failed: " + err);
			});

			$('#button-update').click(function(evt) {
				refreshtable();
			});
			console.log("FINISH!");
		}

		$(document).ready(init_variants);
	</script>

	<script type="text/javascript">
		// ]]>
	</script>

	<div class="row">
		<section class="col-lg-12 connectedSortable">
			<div class="box">
				<div class="box-body">
					<select id="strain_list" multiple="multiple" class="form-control">
						<option id="firstoption">Strain 1</option>

					</select>
				</div>
				<div class="box-footer">
					<button id="button-update">Update</button>
				</div>

		</div>
		</section>

		</div>
		<div class="row">
			<section class="col-lg-12">
				<div class="box">

					<div class="box-body no-padding">
	
						<table id="variants-table" class="table table-bordered table-hover">
	
							<thead id="variants-table-head">
	
							</thead>
							<tbody id="variants-table-body">
	
							</tbody>
	
						</table>
					</div>
				</div>
			</section>
		</div>

	</body>
</html>
</jsp:root>