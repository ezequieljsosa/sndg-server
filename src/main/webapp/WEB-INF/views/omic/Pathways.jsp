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

<meta name="header_title" content="Pathway" />
<meta name="header_title_desc" content="reactions in pathway" />

<meta name="_csrf" content="${_csrf.token}" />
<!-- default header name is X-CSRF-TOKEN -->
<meta name="_csrf_header" content="${_csrf.headerName}" />

<title>Genome Pathways</title>

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

	<script src="${baseURL}/public/bia/ui/BioCycGraph.js"></script>

		<script src="${baseURL}/public/jslibs/cytoscape/cytoscape.js"></script>
		<script src="${baseURL}/public/jslibs/dagre.min.js"></script>
		
		
	<script type="text/javascript">
		 
		 
		var ontologies = ${ontologies} ;
		var genome = ${genome} ;
		var selected_pathways = '${pathways}' ;
		
	</script>


	<script type="text/javascript">
		function add_pathway_to_graph(pathway2render, proteins, ontologies,
				reactions) {
			$
					.each(
							proteins,
							function(i, protein) {
								if ($.isDefAndNotNull(protein.reactions)) {
									var prot_reactions = [];
									$.each(protein.reactions,function(i,reaction){										
										if( reaction.pathways.indexOf(pathway2render) != -1){
											prot_reactions.push(reaction)
										}
									});									
									$
											.each(
													prot_reactions,
													function(i, reaction) {
														var ontology = $
																.grep(
																		ontologies,
																		function(
																				e) {
																			return e.term
																					.toLowerCase() == reaction.name
																					.toLowerCase();
																		});

														var desc = reaction.name;
														if (ontology.length > 0) {
															desc = ontology[0].name;
														}
														reactions
																.push({
																	"prot_id" : protein.id,
																	"protein" : protein.name,
																	"reaction" : reaction.name,
																	"reaction_desc" : desc,
																	"products" : $
																			.map(
																					reaction.products,
																					function(
																							specie) {
																						return specie.name;
																					}),
																	"substrates" : $
																			.map(
																					reaction.substrates,
																					function(
																							specie) {
																						return specie.name;
																					})
																});
													});
								}

							});
		}
		function render_graph(proteins, ontologies) {
			var reactions = []
			$.each(window.pathways, function(i, pathway) {
				var pathway2render = pathway.toUpperCase();
				if (pathway == "unknown") {
					pathway2render = pathway;
				}

				add_pathway_to_graph(pathway2render, proteins, ontologies,
						reactions)
			});

			$("#reactions-table")
					.dataTable(
							{
								"data" : reactions,
								columns : [
										{
											"name" : "msa",
											"title" : '<i class="fa fa-align-center">&#160;</i>',
											"orderable" : false,
											"data" : null,
											"render" : function(data, type, row) {
												var checked = "";
												if ((typeof window.proteins != "undefined")
														&& (typeof window.proteins.values[data.name] != "undefined")
														&& window.proteins.values[data.name] == data.id) {
													checked = "checked=checked";
												}
												return ''
														+ '<input '
														+ checked
														+ ' onclick="addProt(\''
														+ data.protein
														+ "','"
														+ data.prot_id
														+ '\',$(this).is(\':checked\'));" '
														+ 'type="checkbox" />';
											}
										},
										{
											"title" : "Protein",
											"data" : null,
											"render" : function(data, type, row) {
												return '<a href="' + $.api.url_protein(data.prot_id)
														+ '"><i class="fa fa-link">&#160;</i> '
														+ data.protein
														+ '  </a>';
											}
										},
										{
											"title" : "Reaction",
											"data" : null,
											"render" : function(data, type, row) {
												return '<a href="'
														+ hrefOntologyLink(
																"biocyc",
																data.reaction)
														+ '"><small title="click to go to Biocyc" class="badge bg-yellow">?</small></a> '
														+ '<a href="' + $.api.url_search_genome_keyword(genome.name,data.reaction)															
														+ '"><i class="fa fa-filter">&#160;</i> '
														+ data.reaction_desc
														+ '  </a>';
											}
										} ]
							});

			$("#cy").height(window.innerHeight - 300).css("min-height",500);
			$("#cy").html("");
			$("#box_cy").height(window.innerHeight - 300).css("min-height",500);

			var g = new $.BioCycGraph($("#cy"), reactions);
			g.init();

		}
		function load_pathways(prot_onts) {
			var proteins = prot_onts.proteins;
			var pw_ontologies = prot_onts.ontologies;			
			var ontology_names = window.pathways
/* 			$.each(proteins, function(i, protein) {
				$.each(window.pathways,
						function(i, pathway) {
							if ($.isDefAndNotNull( protein.reactions )) {
								
								$.each( protein.reactions ,function(i,reaction){
									if( reaction.pathways.indexOf(pathway) != -1){
										ontology_names.push(reaction.name)
									}
									
								});								
							}

						});

			}); */

			
				render_graph(proteins, ontologies);
			
		}
		function pathwaychange(pathway, status) {
			if (status) {
				window.pathways.push(pathway);
			} else {
				window.pathways.splice(window.pathways.indexOf(pathway), 1);
			}
		}

		function load_pathways_table(genome) {			
			
			$("#pathways_list_title").html('Pathway list of <a style="color:blue;" href="' + $.api.url_genome( genome.name ) + '">' + genome.name + '</a>' );
			$("#pathways_table")
					.dataTable(
							{
								"data" : genome.pathways,
								"order" : [ [ 0, 'desc' ] ],

								"columns" : [
										{
											"visible" : false,
											"title" : ".",
											"name" : "order2",
											"data" : "term",
											"render" : function(data, type, row) {
												
												if( window.pathways
														.indexOf(data) == -1){
													return 0;
												}    else {
													return 1
												}
											}

										},
										{
											"name" : "chk",
											"orderable" : false,
											"title" : '<i tile="Pathway visibility" class="fa fa-eye"> &#160;</i>',
											"data" : null,
											"render" : function(data, type, row) {
												var checked = "";
												if (window.pathways
														.indexOf(data.term) != -1) {
													checked = 'checked="checked"';
												}
												return '<input '
														+ checked
														+ ' type="checkbox" onclick="pathwaychange(\''
														+ data.term
														+ '\',this.checked)" />'
											}

										},
										{
											"name" : "name",
											"title" : "Name",
											"data" : null,
											"render" : function(data, type, row) {

												return '<a href="'
														+ hrefOntologyLink(
																"biocyc",
																data.term)
														+ '"><small title="click to go to Biocyc" class="badge bg-yellow">?</small></a> '
														+ '<a href="' + $.api.url_search_genome_keyword(genome.name,data.term)
														+ '">'
														+ '<i tile="get protein list" class="fa fa-filter">&#160;</i>'
														+ data.name + '</a>'
											}

										}, {
											"name" : "count",
											"title" : "Count",
											"data" : "count"

										} ]
							});
		}
		
		function request_pathways_data(genome_name) {

			$("#result_box")
					.append(
							'<div id="overlay" class="overlay">&#160;</div>	<div id="loading-img" class="loading-img">&#160;</div>')
			$.api.genome_pathways(genome_name,window.pathways,load_pathways).always(function() {
				$("#loading-img").remove();
				$("#overlay").remove();}).error(function(x) {
					console.log(x)
					debugger
				});
			
	
		}
		$(document).ready(function() {
			
			$("#reload").click(function(evt) {
				if (typeof window.cy.destroy != "undefined") {
					window.cy.destroy()
					$('#reactions-table').DataTable().destroy();
					$('#box_cy').append('<div id="cy">.</div>')
				}

				request_pathways_data(genome.name)
			});
			$("#redraw").click(function(evt) {
				window.cy.fit();
			});
			window.pathways = []
			
			$("<a/>", {
				"href" : "${baseURL}/genome"
			}).html("Genomes").appendTo($("#base_breadcrumb"));
			var li = $("<li/>").appendTo($(".breadcrumb"));
			$("<a/>", {
				"href" : "${baseURL}/genome/" + genome.name
			}).html("<i>" + genome.name + "</i>").appendTo(li);
			
			
			
			if (selected_pathways != "") {
				selected_pathways.split(",").forEach(x =>{
					pathwaychange(x.toUpperCase(), true)	
				} )
				
				request_pathways_data(genome.name)
				
				li = $("<li/>").addClass("active").appendTo($(".breadcrumb"));
			$("<a/>", {
				"href" : "#"
			}).html(selected_pathways).appendTo(li);
				
				/* var box = $("#params_box");
				var bf = box.find(".box-body, .box-footer");
				if (!box.hasClass("collapsed-box")) {
					box.addClass("collapsed-box");
					bf.slideUp();
				} */
				
			}
			load_pathways_table(genome)
			

		});

	</script>

	<script type="text/javascript">
		// ]]>
	</script>


	<div>

		<div class="row">
			<section class="col-lg-12 connectedSortable">
				<div id="params_box" class="box box-primary">
					<div class="box-header" style="cursor: move;">
						<!-- tools box -->
						<div class="pull-right box-tools">

							<button class="btn btn-info btn-sm" data-widget="collapse"
								data-toggle="tooltip" title="" data-original-title="Collapse">
								<i class="fa fa-minus"> &#160;</i>
							</button>

						</div>
						<!-- /. tools -->
						<h3 id="pathways_list_title" class="box-title">Pathways list</h3>


					</div>
					<div class="box-body">
						<table class="table table-striped" id="pathways_table">
							<thead>
								<tr>
									<td width="30px">orden...</td>
									<th width="30px">Term</th>
									<th>Name</th>
									<th>Protein Count</th>
								</tr>
							</thead>
						</table>
					</div>
				</div>
			</section>
		</div>


		<div class="row">
			<section class="col-lg-12 connectedSortable">
				<div id="result_box" class="box box-primary">
					<div class="box-header" style="cursor: move;">
						<!-- tools box -->
						<div class="pull-right box-tools">

							<button class="btn btn-info" id="reload">Reload selected
								Pathways</button>
							<button class="btn btn-info" id="redraw">Redraw</button>

						</div>



					</div>

					<div id="box_cy" class="box-body">
						<div id="cy">Select the desired pathway/s</div>

					</div>

				</div>
			</section>
			<section class="col-lg-12 connectedSortable">
				<div id="reactions-table-box" class="box box-primary">
					<table class="table table-striped" id="reactions-table">
						<thead>
							<tr>

								<td width="30px">.</td>
								<td>.</td>
								<td>.</td>
							</tr>
						</thead>
					</table>
				</div>
			</section>
		</div>
	</div>


</body>
	</html>
</jsp:root>