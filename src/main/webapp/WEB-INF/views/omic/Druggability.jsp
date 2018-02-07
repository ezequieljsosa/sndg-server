<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page"
	xmlns:spring="http://www.springframework.org/tags"
	xmlns:c="http://java.sun.com/jsp/jstl/core"
	xmlns:form="http://www.springframework.org/tags/form" version="2.0">
	<jsp:directive.page language="java" contentType="text/html" />

	<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />

<meta name="header_title" content="Druggability" />
<meta name="header_title_desc" content="Custom druggability score" />

<meta name="_csrf" content="${_csrf.token}" />
<!-- default header name is X-CSRF-TOKEN -->
<meta name="_csrf_header" content="${_csrf.headerName}" />

<title>Custom Druggability Score</title>


<!-- DATA TABLES -->
<link href="/xomeq/public/theme/css/datatables/dataTables.bootstrap.css"
	rel="stylesheet" type="text/css" />

</head>
<body>

	<script type="text/javascript">
		//<![CDATA[
	</script>

	<!-- DATA TABES SCRIPT -->
	<script
		src="/xomeq/public/theme/js/plugins/datatables/jquery.dataTables.js"
		type="text/javascript"></script>
	<script
		src="/xomeq/public/theme/js/plugins/datatables/dataTables.bootstrap.js"
		type="text/javascript"></script>


	<link rel="stylesheet"
		href="/xomeq/public/biojs/resources/dependencies/jquery/jquery-ui-1.8.2.css" />
	<link rel="stylesheet"
		href="/xomeq/public/biojs/resources/dependencies/jquery/jquery.tooltip.css" />



	<script type="text/javascript">
		var genome_name = '${genome_name}';
		$.parameters = ${genomeDruggabilityParams};
		var isUpload = ($.isDefAndNotNull( $.QueryString["isUpload"] )) ? true : false;
		
		var uploadErrors = ($.isDefAndNotNull( $.QueryString["uploadErrors"] )) ? $.QueryString["uploadErrors"] : [];
	</script>


	<script type="text/javascript">
		function run_score(evt) {

			$(this).prop('disabled', true);
			//<div class="overlay">&#160;</div>
			//<div class="loading-img">&#160;</div>
			$("<div/>").addClass("loading-img").appendTo($("#result_box"))
			$("<div/>").addClass("overlay").appendTo($("#result_box"))

			var data_sent = {
				formula : [],
				filters : [],
				genes : []
			};

			if (!$("#gene_list").val().isEmpty()) {
				var gene_list = $("#gene_list").val().replace(/\n/g, " ")
						.replace(/[ ]+/g, " ").split(" ");
				$.each(gene_list, function(i, line) {
					data_sent.genes.push(line.trim())
				});
			}

			$.each($("#filters_tbody").children(), function(i, child) {
				if ($.isDefAndNotNull($(child).data().name)) {
					data_sent.filters.push($(child).data());
				}
			});
			$.each($("#parameters_tbody").children(), function(i, child) {
				if ($.isDefAndNotNull($(child).data().coefficient)) {
					data_sent.formula.push($(child).data());
				}
			});

			if (data_sent.formula.length == 0) {
				alert("no druggability criteria was selected")
			} else {

				$
						.ajax(
								{
									url : '/xomeq/druggability/' + genome_name,
									type : 'post',
									data : JSON.stringify(data_sent),
									headers : {
										'Accept' : 'application/json',
										'Content-Type' : 'application/json',
										//'${_csrf.headerName}' : '${_csrf.token}'
									},
									dataType : 'json',
									success : function(data) {
										
										//if($.isDefAndNotNull($('#result_table').dataTable())){
										try {
											$('#result_table').dataTable()
													.api().destroy()
										} catch (ex) {
										}
										//}

										var parent = $('#result_table')
												.parent();
										$('#result_table').remove()
										$("<table/>", {
											id : "result_table"
										}).appendTo(parent).addClass("table")
												.addClass("table-striped")

										$('#result_table')
												.DataTable(
														{
															order : [ [
																	data_sent.formula.length + 1,
																	"desc" ] ],
															data : data,
															columns : createTableColumns(data_sent.formula)
														})
									}
								}).always(function(data) {
							
							$("#run_score").prop('disabled', false);
							$(".loading-img").remove();
							$(".overlay").remove();
						}).fail(function(data){
							alert("Query failed: "  + data.statusText)
						});
			}

		}
		function reload_weigths() {
			var total = 0;
			$.each($("#parameters_tbody").children(), function(i, child) {
				if ($(child).data().coefficient > 0) {
					total = $(child).data().coefficient + total;
				}

			});
			$.each($("#parameters_tbody").children(), function(i, child) {
				var norm_weigth = $(child).data().coefficient * 1.0 / total;
				$($(child).children()[$(child).children().length - 1]).html(
						norm_weigth.toFixed(2))
			});
		}

		function category_change(evt) {
			var selected = [];
			$.each($("#parameters_tbody").children(), function(i, child) {
				var data = $(child).data();
				if ($.isDefAndNotNull(data.coefficient)) {
					selected.push(data.type + "_" + data.name)
				}
			});

			var property_select = $("#property_select");
			$("#property_select").empty()

			$.each([ "has", "variable", "choises" ], function(i, var_name) {

				$.each($.parameters[this.selectedIndex][var_name], function(j,
						prop) {
					var prop_value = var_name + "_" + prop.name;
					if (selected.indexOf(prop_value) == -1) {
						var html = prop.name + " (" + var_name + ")";
						$("<option/>", {
							value : prop_value
						}).html(html).appendTo(property_select);
					}
				}.bind(this));
			}.bind(this));

		}
		function f_category_change(evt) {
			var selected = [];
			$.each($("#filters_tbody").children(), function(i, child) {
				var data = $(child).data();
				if ($.isDefAndNotNull(data.coefficient)) {
					selected.push(data.type + "_" + data.name)
				}
			});

			var property_select = $("#f_property_select");
			$("#f_property_select").empty()

			$.each([ "has", "variable", "choises" ], function(i, var_name) {

				$.each($.parameters[this.selectedIndex][var_name], function(j,
						prop) {
					var prop_value = var_name + "_" + prop.name;
					if (selected.indexOf(prop_value) == -1) {
						var html = prop.name + " (" + var_name + ")";
						$("<option/>", {
							value : prop_value
						}).html(html).appendTo(property_select);
					}
				}.bind(this));
			}.bind(this));

			$("#f_property_select").trigger("change");
		}
		function f_property_select_change(evt) {
			var property_select = $("#f_property_select");
			var filter_select = $("#f_category_select");
			var category = filter_select.val()
			var name = $("#f_property_select").val().split("_").slice(1).join(
					"_");
			var type = $("#f_property_select").val().split("_")[0];
			var options = []

			if (type == "has") {
				options = [ "has", "has not" ]
			} else if (type == "variable") {
				options = [ ">", "<" ]
			} else if (type == "choises") {
				var parameter = $.grep($.parameters, function(x) {
					return x.name == category
				})[0];
				options = $.grep(parameter.choises, function(x) {
					return x.name == name
				})[0].options
			}

			$("#f_operation_select").empty();
			$.each(options, function(i, option) {
				$("<option/>", {
					value : option
				}).html(option).appendTo($("#f_operation_select"));
			});
		}

		function createTableColumns(formula) {
			/* data = {
				category : "prot",
				name : "drug",
				coefficient : 1,
				type : "variable",
						choise: "algo"
			}; */
			var columns = [ {
				title : "Gene",
				data : "name",
				render : function(data,type, row) {
					return '<a href="' + $.api.url_protein_gene(data) + '" >'
							+ data + '</a>';
				}
			}, {
				title : "Structure",
				data : "name",
				render : function(data,type, row) {
					
					if( $.isDefAndNotNull(row.structure.name) ){
						return '<a href="' + $.api.url_structure(row.structure.name) + '" >'
						+ row.structure.name + '</a>';
					} else {
						var val = "";
						$.each(row.search.structures,function(i,structure){
							val += '<a href="' + $.api.url_structure(structure_name) + '>' + structure.name + '</a> - ' 
						});
						return val;
					}
					
					
				}
			},  ];

			$.each(formula, function(i, f_term) {
				var levels = 0;
				if (f_term.category == "structure") {
					levels = 1;
				}
				if (f_term.category == "pocket") {
					levels = 2;
				}
				columns.push({
					title : f_term.name.capitalize(),
					
					render : function( data, type, row ){
						var value = null;
						if (levels == 0 ){
							value = row[f_term.name]	
						} else if (levels == 1 ){
							value =  row.structure[f_term.name]	
						} else {
							value =  row.structure.pocket[f_term.name]
						}
						if( $.isDefAndNotNull(value) ){
							return value;
						} else {
							return "NO DATA";
						}
						
						
					}
				});
			});
			columns.push({
				title : "Score",
				data : "score"
			});
			return columns;
		}

		function init() {
			
			$("<a/>",{href: $.api.url_genome(genome_name) }).html(genome_name).appendTo($("#h3_ref")).css("color","blue");	
			var refcontainer = $('#props_ref');
			var box = $("#props_ref_box");
			var bf = box.find(".box-body, .box-footer");
			if (!box.hasClass("collapsed-box")) {
				box.addClass("collapsed-box");
				bf.slideUp();
			}

			var main_ul = $("<ul/>").appendTo(refcontainer);
			$.each($.parameters, function(x, param) {

				var li = $("<li/>").html( "<b>" + param.name.capitalize() + "</b>").appendTo(main_ul);
				var ul = $("<ul/>").appendTo(li);
				$.each([ "has", "choises", "variable" ], function(y, attr) {
					$.each(param[attr], function(j, prop) {
						$("<li/>").html(prop.name + ": " + prop.description)
								.appendTo(ul);
					});
				});

			});
			
			if(isUpload){
				if($.isArray(uploadErrors)){
					if(uploadErrors.length > 0){
						$("<h1/>").html("Errors loading file:").appendTo( $("#upload_error_div")).css("color","red");
						var ul = $("<ul/>").appendTo($("#upload_error_div"));
						$.each(uploadErrors,function(i,error){
							$("<li/>").html(error).appendTo(ul);
							
						});	
					} else  {
						$("<h1/>").html("File uploaded ok").appendTo( $("#upload_error_div")).css("color","green");
					}
						
					
				} else {
					if(uploadErrors != ''){
						$("<h1/>").html("Error loading file:").appendTo( $("#upload_error_div")).css("color","red");
						$("<h2/>").html(uploadErrors).appendTo( $("#upload_error_div")).css("color","red");
					} else  {
						$("<h1/>").html("File uploaded ok").appendTo( $("#upload_error_div")).css("color","green");
					}
					
				}
			}
			
			

			var category_select = $("#category_select");

			$.each($.parameters, function(i, parameter) {
				$("<option/>", {
					value : parameter.name
				}).html(parameter.name).appendTo(category_select);
				$.each(parameter.variable, function(j, prop) {
					$("<option/>", {
						value : "variable_" + prop.name
					}).html(prop.name + " (variable)").appendTo(
							$("#property_select"));
				});
			});

			category_select.change(category_change);

			var filter_select = $("#f_category_select");
			$.each($.parameters, function(i, parameter) {
				$("<option/>", {
					value : parameter.name
				}).html(parameter.name).appendTo(filter_select);
				$.each(parameter.variable, function(j, prop) {
					$("<option/>", {
						value : "variable_" + prop.name
					}).html(prop.name + " (variable)").appendTo(
							$("#f_property_select"));
				});
			});

			filter_select.change(f_category_change)
			$("#f_property_select").change(f_property_select_change)
			$("#f_property_select").trigger("change");

			$("#add_filter_btn").click(
					function() {
						var tr = $("<tr/>").appendTo($("#filters_tbody"));

						var data = {
							category : $("#f_category_select").val(),
							name : $("#f_property_select").val().split("_")
									.slice(1).join("_"),

							type : $("#f_property_select").val().split("_")[0]
						};

						var td = $("<td/>").appendTo(tr)
						$("<a/>", {
							href : "#"
						}).html("Delete").appendTo(td).click(function(e) {
							tr.remove();
						});

						$("<td/>").html($("#f_category_select").val())
								.appendTo(tr)
						$("<td/>").html(data.type).appendTo(tr)
						$("<td/>").html(data.name).appendTo(tr)
						td = $("<td/>").html(data.operation).appendTo(tr)
						if (data.type == "variable") {
							data.coefficient = 0;
							$("<input/>", {
								value : 0
							}).appendTo(td).keyup(function(evt) {

								$(tr).data().coefficient = $(this).val();
							});
						}

						if (data.type == "choises") {
							data.choise = $("#f_operation_select").val();
						} else {
							data.operation = $("#f_operation_select").val();
						}
						tr.data(data);

					});

			$("#add_param_btn").click(
					function(evt) {
						var tr = $("<tr/>").appendTo($("#parameters_tbody"));

						var data = {
							category : $("#category_select").val(),
							name : $("#property_select").val().split("_")
									.slice(1).join("_"),
							coefficient : 1,
							type : $("#property_select").val().split("_")[0]
						};
						tr.data(data);

						var td = $("<td/>").appendTo(tr)
						$("<a/>", {
							href : "#"
						}).html("Delete").appendTo(td).click(function(e) {
							tr.remove();
							$("#category_select").trigger("change");
						});

						$("<td/>").html($("#category_select").val()).appendTo(
								tr)
						$("<td/>").html(data.type).appendTo(tr)
						$("<td/>").html(data.name).appendTo(tr)

						td = $("<td/>").appendTo(tr)
						if (data.type == "choises") {
							var select = $("<select/>").appendTo(td).change(
									function(evt) {
										data.choise = $(this).val();
									});
							var params = $.grep($.parameters, function(x) {
								return x.name == data.category
							})[0];
							var prop = $.grep(params.choises, function(x) {
								return x.name == data.name
							})[0];
							$.each(prop.options, function(i, choise) {
								$("<option/>").html(choise).appendTo(select);
							});
						}

						td = $("<td/>").appendTo(tr);
						$("<input/>", {
							value : 1
						}).appendTo(td).keyup(function(evt) {
							tr.data().coefficient = parseInt($(this).val());
							reload_weigths()
						});
						$("<td/>").html("0.1").appendTo(tr)

						$("#category_select").trigger("change");
						reload_weigths()

					});

			$("#run_score").click(run_score);
		};

		$(document).ready(init);
	</script>

	<script type="text/javascript">
		// ]]>
	</script>

	<div>

		<div class="row">
			<section class="col-lg-6">
				<div id="props_ref_box" class="box box-primary no-padding">
				<div class="box-header no-padding">
				
				<div class="pull-right box-tools">

						<button class="btn btn-info btn-sm" data-widget="collapse"
							data-toggle="tooltip" title="" data-original-title="Collapse">
							<i class="fa fa-minus"> &#160;</i>
						</button>

					</div>
					
				<h3 id="h3_ref" >Search properties Reference   </h3>
				
				</div>
				
				
					<div class="box-body no-padding">
						<div id="props_ref">&#160;</div>


						
					</div>
				</div>
			</section><section class="col-lg-6">
				<div class="box box-primary no-padding">
				
					<div class="box-body no-padding">						

						<form action="/xomeq/druggability/properties/${genome_name}"
							method="POST" enctype="multipart/form-data">
							<table>
								<tr>
									<td><input type="file" name="props_file" /></td>
									<td><input class="btn" type="submit" value="Upload File" />
									</td>
								</tr>
								<input title="Upload more data" type="hidden"
									name="${_csrf.parameterName}" value="${_csrf.token}" />
							</table>
						</form>
						<div id="upload_error_div">.</div>
					</div>
				</div>
			</section>
		</div>

		<div class="row">
			<section class="col-lg-6 connectedSortable">
				<div class="box box-primary">
					<div class="box-header no-padding" style="cursor: move;">

						<div class="pull-right box-tools">
							<button class="btn btn-info btn-sm" data-widget="collapse"
								data-toggle="tooltip" title="" data-original-title="Collapse">
								<i class="fa fa-minus"> &#160;</i>
							</button>

						</div>
						<h3>Score</h3>

					</div>
					<div class="box-body no-padding">
						<table class="table table-striped">
							<thead>
								<tr>
									<th>-</th>
									<th>Group</th>
									<th>Type</th>
									<th>Property</th>
									<th>Value</th>
									<th>coefficient</th>
									<th>Norm. coefficient</th>
								</tr>
							</thead>
							<tbody id="parameters_tbody">

							</tbody>
							<tfoot>
								<tr>
									<td>-</td>
									<td><select id="category_select"></select></td>
									<td colspan="3"><select id="property_select"></select></td>


									<td>-</td>
									<td><button id="add_param_btn">Add parameter</button></td>
								</tr>
								<tr>
									<td colspan="6" align="center"><button id="run_score">Score
											Targets!</button></td>
								</tr>
							</tfoot>
						</table>
					</div>
				</div>
			</section>
			<section class="col-lg-6 connectedSortable">
				<div class="box box-primary">
					<div class="box-header no-padding" style="cursor: move;">

						<div class="pull-right box-tools">
							<button class="btn btn-info btn-sm" data-widget="collapse"
								data-toggle="tooltip" title="" data-original-title="Collapse">
								<i class="fa fa-minus"> &#160;</i>
							</button>

						</div>
						<h3>Filter</h3>

					</div>
					<div class="box-body no-padding">

						&#160; <b>Gene List:</b> &#160;
						<textarea id="gene_list" rows="3" cols="50">&#160;</textarea>

						<table class="table table-striped">
							<thead>
								<tr>
									<th>-</th>
									<th>Group</th>
									<th>Type</th>
									<th>Value</th>

									<th>-</th>

								</tr>
							</thead>
							<tbody id="filters_tbody">

							</tbody>
							<tfoot>
								<tr>
									<td>-</td>
									<td><select id="f_category_select"></select></td>
									<td><select id="f_property_select"></select></td>
									<td><select id="f_operation_select"></select></td>


									<td><button id="add_filter_btn">Add Filter</button></td>
								</tr>
							</tfoot>
						</table>
					</div>
				</div>
			</section>
			<section class="col-lg-12 connectedSortable">
				<div id="result_box" class="box box-primary">
					<div class="box-header no-padding" style="cursor: move;">

						<div class="pull-right box-tools">
							<button class="btn btn-info btn-sm" data-widget="collapse"
								data-toggle="tooltip" title="" data-original-title="Collapse">
								<i class="fa fa-minus"> &#160;</i>
							</button>

						</div>
						<h3>Results</h3>

					</div>
					<div class="box-body no-padding">

						<table id="result_table" class="table table-striped">
							<tbody>

							</tbody>
						</table>

					</div>
				</div>
			</section>
		</div>
	</div>
</body>
	</html>
</jsp:root>