<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page"
	xmlns:spring="http://www.springframework.org/tags"
	xmlns:c="http://java.sun.com/jsp/jstl/core"
	xmlns:fn="http://java.sun.com/jsp/jstl/functions"
	xmlns:form="http://www.springframework.org/tags/form" version="2.0">
	<jsp:directive.page language="java" contentType="text/html" />
	<c:set var="reqUrl">${pageContext.request.requestURL}</c:set>
	<c:set var="baseURL"
		value="${fn:replace(reqUrl, pageContext.request.requestURI,pageContext.request.contextPath)}" />
	<html>
<head>
<title><spring:message code="genome.title"/></title>

<style type="text/css">
.loading-img2 {
	z-index: 1020;
	background: transparent
		url('${baseURL}/public/theme/img/ajax-loader1.gif') 50% 50% no-repeat;
}

.tour-tour.fade.in {
	opacity: 1;
}
</style>

<link href="${baseURL}/public/widgets/pivot/pivot.css" rel="stylesheet"
	type="text/css" />

<!-- DATA TABLES -->
<link
	href="${baseURL}/public/theme/css/datatables/dataTables.bootstrap.css"
	rel="stylesheet" type="text/css" />

</head>
<body>


<c:set var="genomes_breadcrum"><spring:message code="genome.genome_breadcrum"/></c:set>
			

	<script type="text/javascript">
		//<![CDATA[
	</script>

	<!-- DATA TABES SCRIPT -->
	<script
		src="${baseURL}/public/theme/js/plugins/datatables/jquery.dataTables.js"
		type="text/javascript"></script>

	<script
		src="${baseURL}/public/theme/js/plugins/datatables/dataTables.tableTools.js"
		type="text/javascript"></script>

	<script
		src="${baseURL}/public/theme/js/plugins/datatables/dataTables.colVis.js"
		type="text/javascript"></script>

	<script
		src="${baseURL}/public/theme/js/plugins/datatables/dataTables.bootstrap.js"
		type="text/javascript"></script>



	<script src="${baseURL}/public/docs/tours.js?v=5"
		type="text/javascript"></script>


	<!-- 
	<script src="${baseURL}/public/widgets/chartjs/Chart.min.js"
		type="text/javascript"></script>
	<script src="${baseURL}/public/widgets/pivot/pivot.js"
		type="text/javascript"></script>  -->


	<script src="${baseURL}/public/widgets/krona/krona-2.0.js"></script>

	<!-- Bia libs -->
	<script src="${baseURL}/public/bia/ui/GenomeOverview.js"
		type="text/javascript"></script>
	<script src="${baseURL}/public/bia/ui/JBrowseWrapper.js"
		type="text/javascript"></script>
	<script src="${baseURL}/public/bia/ui/KronaWrapper.js"
		type="text/javascript"></script>


	<script type="text/javascript">
		var genome_id = '${genome_id}'
		var druggability_data = ${druggability_distribution}; //${druggability_distribution}
		var druggability_table = ${druggability_table}; // ${druggability_table}
		var user = '${user.name}';
		var logged_user = ${logged_user};
		var jbrowse_enabled = ${jbrowse_enabled};

		function render_uploads(uploads) {
			$.each(uploads, function(i, upload) {
				var tr = $("<tr/>").appendTo($("#uploads_table")).data(upload);

				$("<td/>").appendTo(tr).html(upload.formatedTimestamp);
				$("<td/>").appendTo(tr).html(upload.properties.join(","));
				var td = $("<td/>").appendTo(tr);

				if (upload.errors.length) {
					td.append($('<button/>', {
						"data-toggle" : "modal",
						"data-target" : "#errorsUpload",
					}).html("Errors").addClass("btn").addClass("btn-info")
							.click(
									function(evt) {
										$("#missing_proteins").html(
												upload.errors.join(","))
									}));
				}
			});
		}

		function load_properties(properties) {

			if (properties.length > 0) {
				$
						.each(
								properties,
								function(i, prop) {

									prop.org_name = prop.name
									var tr = $("<tr/>").appendTo(
											$("#properties_table")).data(prop);

									$("<td/>").appendTo(tr).html(prop.name);
									if (prop.uploader != "demo") {
										$("<td/>")
												.appendTo(tr)
												.append(
														$(
																"<textarea/>",
																{
																	style : "width:100%;"
																})
																.html(
																		prop.description)
																.change(
																		function(
																				evt) {
																			var newProp = $(
																					tr)
																					.data();
																			newProp.description = $(
																					this)
																					.val();
																			$(
																					tr)
																					.data(
																							newProp)
																		}));
									} else {
										$("<td/>").appendTo(tr).html(
												prop.description);
									}

									$("<td/>").appendTo(tr).html(prop.type);
									if (prop.uploader != "demo") {
										$("<td/>")
												.appendTo(tr)
												.append(
														$(
																"<form/>",
																{
																	method : "POST",
																	action : genome_id
																			+ "/properties/delete/"
																})
																.append(
																		$(
																				"<input/>",
																				{
																					type : "hidden",
																					name : "name",
																					value : prop.name
																				}))
																.append(
																		$(
																				"<input/>",
																				{
																					type : "hidden",
																					name : "uploader",
																					value : prop.uploader
																				}))
																.append(
																		$(
																				"<button/>",
																				{
																					type : "button"
																				})
																				.addClass(
																						"iflogged")
																				.addClass(
																						"btn")
																				.addClass(
																						"btn-danger")
																				.html(
																						"delete")
																				.click(
																						function(
																								evt) {
																							var response = confirm("All metadata associated with this property will be deleted. Are you sure?")
																							if (response) {
																								$(
																										this)
																										.parent()
																										.submit();
																							}
																						})));
									}

								});
			} else {
				$("#upload_save_btn").remove();
			}

		}

		function upload_properties() {
			var size = document.getElementById('input_file').files[0].size
			if (size > 0) {
				var tour = "";
				if ($.QueryString["tour"] == "2") {
					tour = "?tour=2"
				}
				$('#form_upload').attr(
						'action',
						'${baseURL}/genome/' + genome_id + "/properties/upload"
								+ tour);
				$('#form_upload').submit()
			} else {
				alert("file cannot be empty")
			}

		}

		var load_genome = function(genome) {

			if ($.QueryString.isUpload) {

				$("#overview_tab_link").removeClass("active");
				$("#overview_tab").removeClass("active");
				$("#data_tab_link").addClass("active");
				$("#data_tab").addClass("active");

				if ($.QueryString.uploadErrors != null) {
					alert("UPLOAD ERROR: " + $.QueryString.uploadErrors)
				}

			}
			loadTutorial('${baseURL}');

			$("#btn_upload_csv").click(upload_properties);

			$("#upload_save_btn").click(
					function(evt) {
						var btn = $(this);
						var data = $.map($("#properties_table").find("tr"),
								function(x, i) {

									return $(x).data()
								});
						btn.attr("disabled", true)

						$.ajax(
								{
									url : '${baseURL}/genome/' + genome_id
											+ "/properties",
									type : 'post',
									data : JSON.stringify(data),
									headers : {
										'Accept' : 'application/json',
										'Content-Type' : 'application/json',
									//'${_csrf.headerName}' : '${_csrf.token}'
									},
									dataType : 'json',

								}).success(function(data) {

							alert("data saved")

						}).fail(function(data) {

							alert("error saving data")

						}).always(function() {
							btn.attr("disabled", false)
						});

					});

			$("<a/>", {
				"href" : "${baseURL}/genome"
			}).html('${genomes_breadcrum}').appendTo($("#base_breadcrumb"));
			var li = $("<li/>").addClass("active").appendTo($(".breadcrumb"));
			$("<a/>", {
				"href" : "${baseURL}/genome/" + genome.name
			}).html("<i>" + genome.organism + "</i>").appendTo(li);

			$("#keyword_search").attr("href",
					$.api.url_search_genome_keyword(genome.name, ""));

			load_properties($.grep(genome.druggabilityParams, function(x) {
				return x.uploader == user
			}));
			if (user != "demo") {
				render_uploads($.grep(genome.uploads, function(x) {
					return x.uploader == user
				}));
			} else {
				$("#uploads_table2").hide();
			}

			if (genome.description == null) {
				genome.description = "";

			}
			$("#keyword_search_icon").click(
					function(evt) {
						window.location = $.api.url_search_genome_keyword(
								genome.name, $('#keyword_search_text').val());
					});
			$('#keyword_search_text').change(
					function(evt) {
						$("#keyword_search").attr(
								"href",
								$.api.url_search_genome_keyword(genome.name, $(
										this).val()));
					}).keyup(
					function(e) {
						if (e.keyCode == 13) {
							window.location = $.api.url_search_genome_keyword(
									genome.name, $(this).val());
						}
					});

			$("#gene_search_icon").click(
					function(evt) {
						window.location = $.api.url_search_genome_gene(
								genome.name, $('#gene_search_text').val());
					});

			$('#gene_search_text').change(
					function(evt) {
						$("#gene_search").attr(
								"href",
								$.api.url_search_genome_gene(genome.name, $(
										this).val()));

					}).keyup(
					function(e) {
						if (e.keyCode == 13) {
							window.location = $.api.url_search_genome_gene(
									genome.name, $(this).val());
						}
					});

			if ($.isDefAndNotNull(genome.goIndex) && genome.goIndex) {
				$("#go_search").attr("href",
						$.api.url_search_genome_go(genome.name));

			} else {
				$("#go_search").remove();
			}

			if (genome.has_expression) {
				$("#heatmap").attr(
						"href",
						$.api.url_search_genome_keyword(genome.name,
								'overexpressed'));

			} else {
				$("#expressionbox").remove();
			}
			
			if(genome.strainProjects  && genome.strainProjects.length){
				genome.strainProjects.forEach( proj => {
					var tr = $("<tr />").appendTo( $("#projects_table"));
					$("<td />").appendTo(tr).append($("<a />",{ href:'${baseURL}/variant/'  + proj.id  }).html(proj.name));
				});				
			} else {
				$("#projectsbox").hide();
			}
			
			

			if (genome.strains != null) {
				$("#heatmap_variants").attr("href",
						$.api.url_genome_variants(genome.id));

				var_genomes = [ genome.id ] + genome.strains;
				$("#variants_query").attr(
						"href",
						$.api.url_search_genome_keyword(genome.name,
								'strain_variants'));

				$("#variants_query").remove()
			} else if (genome.refGenomeId != null) {

				$("#heatmap_variants").attr("href",
						"VariantHeatmap.jsp?genome=" + genome.refGenomeId);
				$("#variants_query").attr("href",
						$.api.url_genome(genome.refGenomeId)).text(
						"Reference Genome").val("Reference Genome");

			} else {
				$("#variantbox").remove();
			}

			if (genome.pathways.length > 0) {
				$("#pathways_search")
						.attr("href", $.api.url_genome_pathways(genome.name))
						.text(genome.name + " Pathways ")
						.append(
								'<i  id="pathways_search_icon" class="fa fa-search">&#160;</i>');

			} else {
				$("#pathways_tr").remove();

			}

			/* if (genome.druggabilityParams.length > 0) {
				$("#druggability_search").attr("href",
						$.api.url_genome_druggability(genome.name)).text(
						genome.name + "  Druggability " ).append('<i  id="pathways_search_icon" class="fa fa-search">&#160;</i>');
			

			} else {
				$("#druggability_tr").remove();

			} */
			$("#druggability_tr").remove();

			var genome_overview = new $.GenomeOverview($('#overview_section'),
					genome);
			genome_overview.init();

			var krona = new $.KronaWrapper($('#krona_row'),
					"${baseURL}/krona/", genome.ecIndex, genome.goIndex,
					genome.name);

			if (genome.ecIndex) {
				krona.init("ec", "root");
			} else {
				if (genome.goIndex) {
					krona.init("go", "go:0008150");
				} else {
					$("#krona_row").remove();
				}
			}

			if (jbrowse_enabled) {
				var jbrowse = new $.JBrowseWrapper($("#jbrowse"),
						"${baseURL}/public/jbrowse/?data=data/");

				jbrowse.init(genome.name, true);

			} else {
				$("#jbrowse_row").remove();
			}

			if (genome.publications != null) {
				$("#publications_row").show();
				$.each(genome.publications, function(key, value) {
					$("#publication_list").append('<li>' + value + '</li>');
				});
			}

			$(".loading-img").remove();
			$(".overlay").remove();

			if (!logged_user) {
				$(".iflogged").attr("disabled", true);
				$("#btn_upload_window").parent().append(
						$(
								"<a/>",
								{
									href : encodeURI($.api.url_login()
											+ "?_return=/genome/" + genome.name
											+ '?isUpload=true')
								}).html("Log in to upload!").addClass("btn")
								.addClass("btn-danger").addClass("btn-lg"))
				$("#btn_upload_window").remove()

			}
		};

		var init = function() {

			$.api.genome(genome_id, load_genome)

			var data = {
				labels : [ "0-0.1", "0.1-0.2", "0.2-0.3", "0.3-0.4", "0.4-0.5",
						"0.5-0.6", "0.6-0.7", "0.7-0.8", "0.8-0.9", "0.9-1" ],
				datasets : [ {
					label : "Druggability",
					fillColor : "rgba(220,220,220,0.2)",
					strokeColor : "rgba(220,220,220,1)",
					pointColor : "rgba(220,220,220,1)",
					pointStrokeColor : "#fff",
					pointHighlightFill : "#fff",
					pointHighlightStroke : "rgba(220,220,220,1)",
					data : druggability_data
				} ]
			};
			$("#pathways_button").click(function() {
				window.location = $.api.url_score_pathways(genome_id)
			})

			$("#druggability_button").click(
					function() {
						window.location = $.api.url_search_genome_keyword(
								genome_id, "")

					});
			/* setTimeout(function(){
			if (druggability_data.length > 0) {
				$("#druggability_chart").css("width",
						$("#druggability_chart").parent().width())
				
				var ctx = document.getElementById("druggability_chart")
						.getContext("2d");
				var myNewChart = new Chart(ctx).Bar(data);
				
				$("#drugability_div").pivot(druggability_table, {
					rows : [ "druggability_group" ],
					cols : [ "experimental","drug" ]
				});
			} else {
				$("#druggability_distribution_section").remove();
				
				
			}
			$("#druggability_index_section").remove();
			 }, 500); */

		};
		$(document).ready(init);
	</script>

	<script type="text/javascript">
		// ]]>
	</script>


	<div class="nav-tabs-custom" style="cursor: move;">
		<!-- Tabs within a box -->
		<ul class="nav nav-tabs ">
			<li class="pull-left active" id="overview_tab_link"><a
				href="#overview_tab" data-toggle="tab" aria-expanded="false"><spring:message
						code="genome.overview" /> </a></li>
			<li class="pull-left" id="data_tab_link"><a href="#data_tab"
				data-toggle="tab" aria-expanded="true"><spring:message
						code="genome.datatab" /></a></li>
			<li class="pull-left  "><a id="druggability_button"
				aria-expanded="true"><spring:message code="genome.search_button" />
			</a></li>



		</ul>
		<div class="tab-content no-padding">

			<div class="tab-pane active" id="overview_tab">

				<div id="jbrowse_row" class="row">
					<div class="col-xs-12">
						<div class="box box-primary">
							<div class="box-body">
								<![CDATA[
		<iframe 
			id="jbrowse"
			height="400px" width="100%"> </iframe>
		]]>
								<br />
							</div>
						</div>
					</div>
				</div>

				<div class="row">

					<section id="overview_section" class="col-lg-4 ">

						<div class="box">

							<div class="box-body no-padding">

								<table class="table table-striped">

									<tbody id="overview_table">


									</tbody>
								</table>
							</div>
							<div class="overlay">&#160;</div>
							<div class="loading-img">&#160;</div>
						</div>
					</section>



					<section id="search_section" class="col-lg-6">
						<!-- small box -->
						<div class="box">
							<div class="box-body no-padding">
								<table class="table table-striped">
									<thead>
										<tr>
											<th colspan="2"><spring:message code="genome.search_by" /></th>
										</tr>
									</thead>
									<tbody>
										<tr>
											<td><a id="keyword_search" href="#"> <spring:message code="genome.keyword_search" /> </a>
												<div class="input-group">
													<input id="keyword_search_text" class="form-control"
														type="text" /> <span id="keyword_search_icon"
														class="input-group-addon"><i class="fa fa-search"></i></span>
												</div></td>




										</tr>
										<tr>


											<td><a id="gene_search" href="#"> <spring:message code="genome.gene_search" /> </a>
												<div class="input-group">
													<input id="gene_search_text" class="form-control"
														type="text" /> <span id="gene_search_icon"
														class="input-group-addon"><i class="fa fa-search"></i></span>
												</div></td>
										</tr>
										<tr style="display: none;" id="gosearch_tr">
											<td><a id="go_search" href="GOSearch.jsp"> GO Term
													&#160;&#160; <i id="go_search_icon" class="fa fa-search">&#160;</i>
											</a></td>
										</tr>
										<tr id="pathways_tr">
											<td><a id="pathways_search" href="Pathways.jsp">
													Pathways &#160;&#160; </a></td>
										</tr>
										<tr id="druggability_tr">
											<td><a id="druggability_search" href="#">
													Druggability Score &#160;&#160; </a></td>
										</tr>


									</tbody>
								</table>
							</div>
							<div class="overlay">&#160;</div>
							<div class="loading-img">&#160;</div>
						</div>

						<div id="expressionbox" class="box">
							<div class="box-body no-padding">
								<table class="table table-striped">
									<thead>
										<tr>
											<th colspan="2">Expression Data</th>
										</tr>
									</thead>
									<tbody>
										<tr>
											<td><a id="heatmap" href="Expression.jsp"> Heatmap</a></td>
										</tr>
										<tr>
											<td><a id="overexpressed" href="Search.jsp">
													Overexpressed List </a></td>
										</tr>
									</tbody>
								</table>
							</div>
						</div>

						<div id="variantbox" class="box">
							<div class="box-body no-padding">
								<table class="table table-striped">
									<thead>
										<tr>
											<th colspan="2">Variant Data</th>
										</tr>
									</thead>
									<tbody>
										<tr>
											<td><a id="heatmap_variants" href="Expression.jsp">
													Heatmap</a></td>
										</tr>
										<tr>
											<td><a id="variants_query" href="Search.jsp">
													Variant proteins</a></td>
										</tr>
									</tbody>
								</table>
							</div>
						</div>
						<div id="projectsbox" class="box">
							<div class="box-header no-padding">
								<h3>Projects</h3>
							</div>
							<div class="box-body no-padding">
								<table id="projects_table" class="table table-striped">
									<tbody>

									</tbody>
								</table>
							</div>

						</div>
					</section>



					<section id="statistics_section" class="col-lg-6 ">
						<!-- small box -->
						<div class="box">
							<div class="box-header no-padding">
								<h3><spring:message code="genome.statistics" /></h3>
							</div>
							<div class="box-body no-padding">
								<table id="statistics_table" class="table table-striped">
									<thead style="display: none;"></thead>
									<tbody></tbody>
								</table>
							</div>
							
						</div>
					</section>




				</div>

				<div id="krona_row" class="row">
					<div class="col-xs-12">
						<div class="box">
							<div class="box-header">

								<div class="col-lg-offset-4 box-tools">
									<select id="krona_select">
										<option id="ec_option" value="ec">EC Number</option>
										<option id="bp_option" value="bp">GO Biological
											Process</option>
										<option id="cc_option" value="cc">GO Cellular
											Component</option>
										<option id="mf_option" value="mf">GO Molecular
											Function</option>

									</select>
								</div>

							</div>
							<div id="krona-box" class="box-body">

								<img id="hiddenImage"
									src="${baseURL}/public/widgets/krona/img/hidden.png"
									style="display: none" /> <img id="loadingImage"
									src="${baseURL}/public/widgets/krona/img/loading.gif"
									style="display: none" />
								<div id="krona2" style="display: none;">-</div>
								<div id="krona1" height="500px" width="90%">-</div>

							</div>
						</div>
					</div>
				</div>



				<div id="publications_row" style="display: none;" class="row">
					<div class="col-xs-12">
						<div class="box">

							<div class="box-body">
								<h3>Publications</h3>

								<ul id="publication_list">

								</ul>
							</div>
							<div class="overlay">&#160;</div>
							<div class="loading-img">&#160;</div>
						</div>
					</div>
				</div>


			</div>
			<div class="tab-pane" id="data_tab"
				style="position: relative; height: 300px;">
				<section id="data_section" class="col-lg-12">

					<div class="box">
						<div class="box-header">
							<h2>Downloads</h2>
						</div>
						<div class="box-body no-padding">

							<ul id="data_table">

							</ul>

						</div>
						<div class="overlay">&#160;</div>
						<div class="loading-img">&#160;</div>
					</div>


					<div id="properties_table_box" class="box">
						<div class="box-header">
							<h2>Properties</h2>
							<button id="btn_upload_window"
								class="btn btn-primary btn-lg iflogged" data-toggle="modal"
								data-target="#myModal">Upload Metadata &#13;</button>
						</div>
						<div class="box-body">
							<div class="col-md-12">
								<table style="width: 100%" class="table">
									<thead>
										<tr>
											<td width="100px">Name</td>
											<td>Description</td>
											<td width="50px">Type</td>
											<td></td>

										</tr>
									</thead>
									<tbody id="properties_table">

									</tbody>
								</table>
							</div>


							<button id="upload_save_btn"
								class="iflogged btn btn-success btn-lg">Save</button>
						</div>
					</div>

					<div class="box">
						<div class="box-header">
							<h2>Uploads</h2>



						</div>
						<div class="box-body">

							<table id="uploads_table2" style="width: 100%" class="table">
								<thead>
									<tr>
										<td>Date</td>
										<td>Properties</td>
										<td>Errors</td>

									</tr>
								</thead>
								<tbody id="uploads_table">

								</tbody>
							</table>
						</div>
					</div>

				</section>
				<div class="modal fade" id="myModal" tabindex="-1" role="dialog"
					aria-labelledby="myModalLabel">
					<div class="modal-dialog" role="document">
						<div class="modal-content">
							<div class="modal-header">
								<button type="button" class="close" data-dismiss="modal"
									aria-label="Close">
									<span aria-hidden="true">&#215;</span>
								</button>
								<h4 class="modal-title" id="myModalLabel">Upload Metadata</h4>
							</div>
							<div class="modal-body">
								<form id="form_upload" action="/upload" method="POST"
									enctype="multipart/form-data">

									<div class="form-group">
										<p>
											The file must be plain text, with columns separated by tab
											and UTF-8 encoding. There is one mandatory field called "<b>id</b>"",
											with the locus tag or Uniprot code. There are 2 types of
											columns, "tags" or "numeric". Value columns must have no more
											than 20 different values. Numeric columns must have "," as
											decimal separator and no other symbol. To represent the
											absence of value in a numeric column, the string "NaN" must
											be used. Here we provide to simple examples, to download
											click on the links: <a id="linkExDownload1"
												href="${baseURL}/public/docs/data_example.tsv">Example 1</a>
											and <a href="${baseURL}/public/docs/data_example2.tsv">Example
												2</a> The uploaded properties will be avaliable for filtering
											and scoring in the <a class="btn btn-app open-modal"
												data-id="filter_metadata"><i class="fa fa-tags">-</i>Metadata
											</a> button.
										</p>
										<label for="in_new_trascript">Example</label>
										<table class="table">
											<tr>
												<td>id</td>
												<td>essential</td>
												<td>chokepoint</td>
												<td>stress fold change</td>
											</tr>
											<tr>
												<td>Rv0064</td>
												<td>yes</td>
												<td>product</td>
												<td>0.3</td>
											</tr>
											<tr>
												<td>Rv1363</td>
												<td>no</td>
												<td>no</td>
												<td>6</td>
											</tr>
											<tr>
												<td>Rv1364</td>
												<td>no</td>
												<td>unknown</td>
												<td>NaN</td>
											</tr>

										</table>
									</div>



									<div class="form-group">
										<input type="file" id="input_file" name="input_file" />
										<p class="help-block">Select TSV File</p>
									</div>



								</form>

							</div>
							<div class="modal-footer">
								<button type="button" class="btn btn-default"
									data-dismiss="modal">Cancel</button>
								<button type="submit" id="btn_upload_csv"
									class="iflogged btn btn-primary">Upload file</button>
							</div>
						</div>
					</div>
				</div>


				<div class="modal fade" id="errorsUpload" tabindex="-1"
					role="dialog" aria-labelledby="myModalLabel">
					<div class="modal-dialog" role="document">
						<div class="modal-content">
							<div class="modal-header">
								<button type="button" class="close" data-dismiss="modal"
									aria-label="Close">
									<span aria-hidden="true">&#215;</span>
								</button>
								<h4 class="modal-title" id="myModal2Label">Error List</h4>
							</div>
							<div class="modal-body">

								<div id="missing_proteins" class="form-group">-</div>


							</div>

						</div>
					</div>
				</div>

			</div>


			<div class="tab-pane" id="druggability_tab"
				style="position: relative; height: 300px;">

				<section id="druggability_distribution_section" class="col-lg-6 ">
					<!-- small box -->
					<div class="box">
						<div class="box-header no-padding">
							<h3>Druggability Distribution</h3>
						</div>
						<div class="box-body" style="height: 500px;">
							<canvas id="druggability_chart" style="height: 500px;"></canvas>

						</div>
						<div class="box-footer">Frequecy (Y) vs Fpocket Druggability
							Score (X)</div>
						<div class="overlay">&#160;</div>
						<div class="loading-img">&#160;</div>
					</div>
				</section>
				<section id="druggability_index_section" class="col-lg-6 ">
					<!-- small box -->
					<div class="box">
						<div class="box-header no-padding">
							<h3>Druggability</h3>
						</div>
						<div class="box-body">
							<div id="drugability_div"></div>

						</div>
						<div class="overlay">&#160;</div>
						<div class="loading-img">&#160;</div>
					</div>
				</section>


			</div>


		</div>
	</div>









</body>
	</html>
</jsp:root>