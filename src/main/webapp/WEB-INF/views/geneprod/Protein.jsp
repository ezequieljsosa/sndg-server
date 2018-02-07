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
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>Protein Details</title>

<style type="text/css">
.box .box-header {
	padding-bottom: 10px;
}

input[type="search"] {
	width: 200px;
}
</style>


<!-- BIOJS feature viewer -->
<link rel="stylesheet"
	href="${baseURL}/public/biojs/resources/dependencies/jquery/images/ui-bg_flat_0_aaaaaa_40x100.png" />
<link rel="stylesheet"
	href="${baseURL}/public/biojs/resources/dependencies/jquery/images/ui-bg_flat_75_ffffff_40x100.png" />
<link rel="stylesheet"
	href="${baseURL}/public/biojs/resources/dependencies/jquery/images/ui-bg_glass_65_ffffff_1x400.png" />
<link rel="stylesheet"
	href="${baseURL}/public/biojs/resources/dependencies/jquery/images/ui-bg_glass_75_dadada_1x400.png" />
<link rel="stylesheet"
	href="${baseURL}/public/biojs/resources/dependencies/jquery/images/ui-bg_glass_75_e6e6e6_1x400.png" />
<link rel="stylesheet"
	href="${baseURL}/public/biojs/resources/dependencies/jquery/images/ui-bg_highlight-soft_75_cccccc_1x100.png" />
<link rel="stylesheet"
	href="${baseURL}/public/biojs/resources/dependencies/jquery/images/ui-icons_222222_256x240.png" />
<link rel="stylesheet"
	href="${baseURL}/public/biojs/resources/dependencies/jquery/images/ui-icons_454545_256x240.png" />





<link rel="stylesheet" type="text/css"
	href="${baseURL}/public/widgets/jstree/themes/default/style.css" />
<!-- DATA TABLES -->
<link
	href="${baseURL}/public/theme/css/datatables/dataTables.bootstrap.css"
	rel="stylesheet" type="text/css" />

<link href="${baseURL}/public/theme/css/bootstrap-slider/slider.css"
	rel="stylesheet" type="text/css" />

<link rel="stylesheet"
	href="${baseURL}/public/biojs/resources/dependencies/jquery/jquery-ui-1.8.2.css" />
<link rel="stylesheet"
	href="${baseURL}/public/biojs/resources/dependencies/jquery/jquery.tooltip.css" />

<link rel="stylesheet" type="text/css"
	href="${baseURL}/public/widgets/color-picker/colorPicker.css" />

</head>
<body>



	<script
		src="${baseURL}/public/theme/js/plugins/bootstrap-slider/bootstrap-slider.js"
		type="text/javascript"></script>



	<script type="text/javascript">
		//<![CDATA[
	</script>


	<script src="${baseURL}/public/bia/model/features/FeatureTrack.js"></script>
	<script
		src="${baseURL}/public/bia/model/features/FeatureTrackCategory.js"></script>
	<script src="${baseURL}/public/bia/model/features/FeatureTrackStyle.js"></script>

	<script src="${baseURL}/public/widgets/jstree/jstree.js"></script>
	<script src="${baseURL}/public/bia/ui/BioCycGraph.js"></script>
	<script src="${baseURL}/public/bia/ui/ProteinOverview.js"></script>
	<script src="${baseURL}/public/bia/ui/GOGraph.js"></script>
	<script src="${baseURL}/public/bia/ui/SequencePanel.js"></script>
	<script src="${baseURL}/public/bia/ui/StructureList.js?v=2"></script>
	<script src="${baseURL}/public/bia/ui/FeatureList.js"></script>
	<script src="${baseURL}/public/bia/ui/FeatureTrackRenderer.js"></script>
	<script src="${baseURL}/public/bia/ui/OntologiesList.js"></script>
	<script src="${baseURL}/public/bia/ui/ReactionsTree.js"></script>



	<script src="${baseURL}/public/jslibs/cytoscape/cytoscape.min.2.4.9.js"></script>
	<script src="${baseURL}/public/jslibs/dagre.min.js"></script>


	<!-- DATA TABES SCRIPT -->
	<script
		src="${baseURL}/public/theme/js/plugins/datatables/jquery.dataTables.js"
		type="text/javascript"></script>



	<script
		src="${baseURL}/public/theme/js/plugins/datatables/dataTables.bootstrap.js"
		type="text/javascript"></script>





	<script type="text/javascript"
		src="${baseURL}/public/widgets/color-picker/jquery.colorPicker.js"></script>

	<script src="${baseURL}/public/jslibs/jquery/jquery-migrate-1.2.1.js"></script>

	<!-- BIOJS SCRIPT -->

	<script language="JavaScript" type="text/javascript"
		src="${baseURL}/public/biojs/Biojs.js"></script>

	<script language="JavaScript" type="text/javascript"
		src="${baseURL}/public/biojs/Biojs.Tooltip.js"></script>

	<script language="JavaScript" type="text/javascript"
		src="${baseURL}/public/biojs/Biojs.Sequence.js"></script>

	<script language="JavaScript" type="text/javascript"
		src="${baseURL}/public/biojs/Biojs.FeatureViewer.js"></script>

	<script language="JavaScript" type="text/javascript"
		src="${baseURL}/public/biojs/Biojs.SimpleFeatureViewer.js"></script>

	<script src="${baseURL}/public/biojs/msa.min.js" type="text/javascript"></script>


	<script language="JavaScript" type="text/javascript"
		src="${baseURL}/public/biojs/resources/dependencies/graphics/raphael-2.1.2.js"></script>
	<script language="JavaScript" type="text/javascript"
		src="${baseURL}/public/biojs/resources/dependencies/graphics/canvg.js"></script>
	<script language="JavaScript" type="text/javascript"
		src="${baseURL}/public/biojs/resources/dependencies/graphics/rgbcolor.js"></script>

	<script language="JavaScript" type="text/javascript"
		src="${baseURL}/public/biojs/resources/dependencies/jquery/jquery.tooltip.js"></script>


	<script
		src="${baseURL}/public/theme/js/plugins/bootstrap-slider/bootstrap-slider.js"
		type="text/javascript"></script>

	<script type="text/javascript">
		 
		var protein = ${protein} ;
		var ontologies = ${ontologies} ;
		var structures = ${structures} ; 
		var user = "${user.name}";
		
	</script>

	<script type="text/javascript">	
		function load_experimental_structures(protein) {
			
			if ((typeof protein.structures != "undefined")
					&& (protein.structures != null)) {
				var structs = []
				$.each(protein.structures, function(i, pdb) {
					var struct = '<a href="' + hrefOntologyLink('pdb', pdb)
							+ '" >' + pdb + '</a>';
					structs.push(struct);
				});

				$("#tr_structure").html("Structures: " + structs.join(" - "));
			}

		}
		function load_expression(protein) {
			if (protein.expression) {
				$.each(protein.expression, function(condition, data) {
					fold_change = data.fold_change
					significant = data.significant

					if (fold_change == null) {
						fold_change = "NA"
					}

					if (significant == null) {
						significant = "NA"
					} else {
						significant = (significant == "true") ? "Yes" : "No";

					}

					row = "<tr><td>" + condition + "</td> <td>" + fold_change
							+ "</td><td>" + significant + "</td></tr>";
					$("#expression_table").append(row)
				})

				$("#heatmap").attr("href",
						"Expression.jsp?genome=" + protein.strCollectionId);
			}

			else {
				$("#expressionbox").remove();
			}

		}

		function load_variants(features,seq) {
			if (features == null) {
				$('#dbvariants-box').remove()
				$('#strainvariants-box').remove()
				return;
			}
			var dbfeatures = $.grep(features, function(e) {
				return   (["tbdream"].indexOf( e.type)  != -1) ||  (["Aanensen2016"].indexOf( e.type)  != -1) ;
			});
			var strainfeatures = $.grep(features, function(e) {
				return   ["strain_variant"].indexOf( e.type)  != -1 ;
			});
			
			if (dbfeatures.length == 0) {
				$('#dbvariants-box').remove()				
			}
			if (strainfeatures.length == 0) {
				$('#strainvariants-box').remove()				
			}
			
			
			if (dbfeatures.length > 0) {
				var in_detail = false; 
				if (dbfeatures.length > 10) {
					in_detail = true;
				}

				$('#dbvariants-table').dataTable({
					"data" : dbfeatures,
					"order" : [ 1, 'asc' ],
					"paging" : in_detail,
					"info" : false,
					"searching" : in_detail,
					"columns" : [ 
						{ 
							data: 'type',
							render (x,y,row){
									if(x == "tbdream"){
										return '<a href="' + BIAlinks.tbdream(row.qualifiers.drug,row.qualifiers.gene) + '">' + row.identifier + '</a>';
									} else {										
										return row.identifier;
									}
							}
						},{
							data: 'location.start'
						},{
							data: 'type',
							render (x,y,row){
								if([ "tbdream","Aanensen2016"].indexOf(x) != -1){
									return row.qualifiers.change + " -> " + row.qualifiers.drug
								} else {									
									return  row.qualifiers.type  + " -> " +  row.qualifiers.change 
								}
						}},{
							data: 'type',
							render (x,y,row){
								
								if(x == "tbdream"){
									return row.qualifiers.pattern;
								} else {									
									return  "";
								}
						}}
			
					],
					"dom" : '<"top"f<"clear">>rt<"bottom"lp<"clear">>'

				});

				
			} else {

				$('#dbvariants-box').remove()

			}
			
			if (strainfeatures.length > 0) {
				var in_detail = false; 
				if (strainfeatures.length > 10) {
					in_detail = true;
				}

				$('#strainvariants-table').dataTable({
					"data" : strainfeatures,
					"order" : [ 1, 'asc' ],
					"paging" : in_detail,
					"info" : false,
					"searching" : in_detail,
					"columns" : [ 
						{ 
							data: 'type',
							render (x,y,row){																			
										return row.identifier;
									
							}
						},{
							data: 'location.start'
						},{
							data: 'type',
							render (x,y,row){
																	
									return  row.qualifiers.type  + " -> " +  row.qualifiers.change 
								
						}},{
							data: 'type',
							render (x,y,row){
								if (row.qualifiers.ref != undefined){									
									return dbfeatures.filter(x => x.id == row.qualifiers.ref)[0].identifier;
								}	else {
									return "";
								} 
								
						}}
			
					],
					"dom" : '<"top"f<"clear">>rt<"bottom"lp<"clear">>'

				});

				
			} else {

				$('#strainfeatures-box').remove()

			}
			
			
			/* $.each(features, function(type, feature) {
				
				if(feature.name == "missense_variant"){
					
					
					
					"position" : {
			            "start" : int(row["AA_POS"]),
			            "end" : int(row["AA_POS"]) + 1
			        },
			        "_id" : ObjectId(),
			        "type" : row["MUTATION_TYPE"],
			        "qualifiers" : {
			            "ref":row["AA_ORIG"],
			            "change":row["AA_SUBST"],
			            "clnsig" : row["CLNSIG"],
			            "clndbn" : row["CLNDBN"],
			            "caf" : row["CAF"],
			            "af" : row["AF"],
			            "snpeff" : row["IMPACT(SNPEFF)"],
			            "rsid" : row["RS_ID"]
			        }
				}
				
			}); */
		}

		function load_ont_net(ontologies) {
			var go_ont = $.grep(ontologies, function(ont) {
				return ont.ontology == "go";
			});
			
			var go_graph = new $.GOGraph($('#cy'), go_ont)

			$('#cy').css("min-height",500);
			$('#cy').height($("#ontologies_section").height());
			
			$('#cy').css("max-height",2 * $(window).height() / 3);
			
			$("#enableWheel").change(function(evt){
				
				go_graph.cy.userZoomingEnabled($(this).prop("checked"))
			});
			
			go_graph.init();
			$("#redraw").click(function(evt) {
				go_graph.fit();
			});
			$("#grap_mode_select").change(function(evt) {
				go_graph.change_mode($(this).val());
				go_graph.fit();
			})
		}

		function load_pathways(protein) {
			
			if ($.isDefAndNotNull(protein.reactions) && (protein.reactions.length !=0 ) ) {
				rt = new $.ReactionsTree($("#pathwaystree"), protein.organism,$.api)
				var pathways = [];
				$.each(protein.reactions,function(i,reaction){
					if(reaction.pathways.length > 0){
						$.each(reaction.pathways,function(j,pathway){
							if(pathways.indexOf(pathway) == -1){
								pathways.push(pathway);
							}
						});	
					} else {
						if(pathways.indexOf(null) == -1){
							pathways.push(null);	
						}
						
					}
					
				});
				rt.pathways = pathways;
				rt.init(protein.reactions,ontologies)
				$("#pathways_loading-img").remove();
				$("#pathways_overlay").remove();

			} else {
				//$("#pathways_box").remove();
				$("#pathways-section").html("<h2>No Pathways/Reactions information avaliable</h2>");
			}
		}

		function presentDatabseString(str) {
			return str.charAt(0).toUpperCase() + str.slice(1).replace("_", " ");
		}
		
		var loadStructures = function(structures) {

			$
					.each(
							structures,
							function(index, structure) {

								$("#" + structure.seqId + " .structure_ref")
										.html(
												'<a href="Structure.jsp?seqId='
														+ structure.seqId
														+ "&structure="
														+ structure.strId
														+ '"><i class="fa fa-sitemap">&#160;</i>  </a>');
							});
			var in_detail = false;
			if (features.length > 10) {
				in_detail = true;
			}

			$("#features_table").dataTable({
				"order" : [ 2, 'desc' ],
				"paging" : in_detail,
				"info" : false,
				"searching" : in_detail
			//"dom" : '<"top"<"clear">>rt<"bottom"lp<"clear">>'

			});

		};
		var loadPDBStructures = function(structures) {
			
			$
					.each(
							structures,
							function(index, structure) {
								$("#" + structure.id + " .structure_ref")
										.html(
												'<a href="Structure.jsp?seqId='
														+ structure.seqId
														+ "&structure="
														+ structure.strId
														+ '"><i class="fa fa-sitemap">&#160;</i>Go to Structure</a>');
							});

		};
		

		var load_protein_fn = function(protein) {
			$("#overview_overlay").remove();
			$("#overview_loading-img").remove();
			
			
			
			if ((typeof window.proteins != "undefined")
					&& (typeof window.proteins.values[protein.name] != "undefined")
					&& window.proteins.values[protein.name] == protein.id) {
				//$("#msa-check").prop("checked", true);
				$('#msa-check').iCheck('check')
			}

			$('#msa-check').on('ifToggled', function(event) {
				addProt(protein.name, protein.id, this.checked);
			})

			/* 	$("#msa-check").change(function(evt) {
					addProt(protein.name, protein.id, $(this).is(':checked'));
				}); */

			
			

			var sp = new $.SequencePanel('sequence_box', protein.sequence,
					protein.name, protein.id,$.api)
			sp.init()
			
			var prot_overview = new $.ProteinOverview($('#overview_table'),
					protein,$.api);
			prot_overview.ontologies = ontologies;
			prot_overview.propTypes.push(user)
			prot_overview.init();

			/* $.each(protein, function(key, value) {
				$("#" + key).html(value);
			}); */

			if (protein.search.druggability > 0){
				var text = '<tr><td>Druggability</td><td>' +  	protein.search.druggability + '</td></tr>';
				$("#overview_table")
						.append(text);
			}
			
			load_pathways(protein);
			//load_protein_structure(protein.name);
			
			load_expression(protein);
			load_experimental_structures(protein);
			load_variants(protein.features,protein.sequence)

				
			if (protein.publications) {
				$.each(protein.publications, function(key, value) {
					$("#publication_list").append('<li>' + value + '</li>');
				});
			} else {
				$("#publications_row").remove();
			}
			
			var go_ontologies =  (ontologies != null) ? ontologies.filter(x => x.ontology == "go")  : []
			if ( go_ontologies.length > 0) {
				
				var ontologies_list = new $.OntologiesList(	$("#ontologies_section"),$.api );
				//ontologies_list.organism = protein.organism
				ontologies_list.init(ontologies);
			} else {
				$("#ontologies_section").remove();
				$("#ontologies_graph_section").remove();

			}

			/*if (protein.dbxrefs != null) {
				$.each(protein.dbxrefs, function(type, dbxref) {

					row = '<tr><td></td><td>' + dbxref + '</td><td></td></tr>';
					$("#cross_references").append(row);
				});
				$("#cross_references_overlay").remove();
				$("#cross_references_loading-img").remove();
			} else {
*/
				$("#cross_refereces_section").remove();
	//		}
			$('#structures_section').hide();
			
			
			load_ont_net(ontologies)
			
			if (protein.features) {
				
				var features =  protein.features;
				
				structures.forEach(x => {
					if(x.templates && x.templates.length > 0){
						
						var aln = x.templates[0]
						var fmodel = {
								id: x.name,						
								identifier: x.name ,
								location:{start: aln.aln_query.start,end:aln.aln_query.end},
								type:"SO:0001079",
								aln: aln,
								strLocus: aln.aln_query.start.toString() + ":" +aln.aln_query.end.toString() 
						}
						features.push(fmodel);
					}
				})
				
				var fl = new $.FeaturesList($("#features"), features,
						protein.organism);
				
				fl.init(ontologies);
				
				
				
					$('#structures_section').show();					
					var sl = new $.StructureList($('#structures_section'),$.api);
					sl.column_renderers.push(function(row,feature,struct){
						var cell = $('<td/>').appendTo(row);
						cell.html('')
						
					});
					sl.on_end = function(structs){
						if (structs.length == 0) {
							$('#structures_section').html("<h2>No Structure information avaliable</h2>")
							//$('#structures_section').remove();
						} else {							
							
							$('input[type="search"]').css("width","200px")
						}
					}
					sl.init(protein,structures);
				
			

				
					$("#features_overlay").remove();
					$("#features_loading-img").remove();

					var tracks_with_category = $.grep(fl.feature_tracks,
							function(e) {
								e.track = e.category;
								return typeof e.category != "undefined";
							});
					var ftr = new $.FeatureTrackRenderer("protein_tracks",
							protein.name, tracks_with_category,
							protein.sequence.length);
					ftr.onSelect = function(feature_track) {
						
						if ((typeof feature_track["aln"] != "undefined")
								&& (typeof feature_track["aln"]["aln_query"] != "undefined")) {
							$("#aln_section").show()

							load_aln([{"seq":feature_track["aln"]["aln_query"]["seq"],"name":protein.name,"id":protein.id},
							          {"seq":feature_track["aln"]["aln_query"]["seq"],"name":protein.name,"id":protein.id},
											 {"seq":feature_track["aln"]["aln_hit"],"name":feature_track.name,"id":feature_track.id}
							])	 


							var hit_name = feature_track.name;
							if ($.isDefAndNotNull(feature_track.source)) {
								hit_name = feature_track.source;
							}
							
							load_aln([ {
								"seq" : feature_track["aln"]["aln_query"]["seq"],
								"name" : protein.name,
								"id" : protein.id
							}, {
								"seq" : feature_track["aln"]["aln_query"]["seq"],
								"name" : protein.name,
								"id" : protein.id
							}, {
								"seq" : feature_track["aln"]["aln_hit"]["seq"],
								"name" : hit_name,
								"id" : feature_track.id
							} ])

						}	;

					}
					
					ftr.onUnSelect = function(feature_track) {
						$("#aln_section").hide();
					}
					$
							.each(
									protein.features,
									function(i, feature) {

										if ((feature.identifier == null)
												|| feature.identifier
														.startsWith("SO:")) {

											var ident_ontology = $
													.grep(
															fl.ontologies,
															function(e) {
																return e.term
																		.toUpperCase() == feature.type
																		.toUpperCase();
															});

											feature.identifier = ident_ontology[0].name;
										}

										//ftr.id_map[ontology.term.toUpperCase()] = function(x){ return  ontology.name;}
									});

					ftr.init();
					fl.listeners.push(sp);
				

			} else {
				$("#sequence_row").remove();
			}

		};

		function load_aln(data) {

			var msa = require("msa");
			var opts = {
				el : document.getElementById('msa-div')
			};
			opts.vis = {
				conserv : true,
				overviewbox : false,
				seqlogo : false,
				labelId : false,
				labelName : true
			};
			opts.seqs = data;
			opts.colorscheme = {
				"scheme" : "hydro"
			};
			opts.zoomer = {
				labelNameLength : 220
			};
			var m = msa(opts);

			/* var menuOpts = {};
			menuOpts.msa = m;
			var defMenu = new msa.menu.defaultmenu(menuOpts); */
			//m.addView("menu", defMenu); 
			m.render();
		}

		var document_ready_fn = function() {
			$("#aln_section").hide();
			
			$("<a/>",{"href":"${baseURL}/genome"}).html("Genomes").appendTo( $("#base_breadcrumb") );
			var li = $("<li/>").appendTo( $(".breadcrumb") );
			$("<a/>",{"href":"${baseURL}/genome/" + protein.organism}).html("<i>" + protein.organism + "</i>").appendTo(li);
			
			li = $("<li/>").appendTo( $(".breadcrumb") );
			$("<a/>",{"href":"${baseURL}/search/" + protein.organism +  "/product/" }).html("Search").click(function(evt){
				//window.history.go(-1);
				}).appendTo(li);
			
			li = $("<li/>").addClass("active").appendTo( $(".breadcrumb") );
			$("<a/>",{"href":"#"}).html(protein.name).appendTo(li);
			
			load_protein_fn(protein);
		};

		$(document).ready(document_ready_fn);
	</script>

	<script type="text/javascript">
		// ]]>
	</script>


	<div class="row">
		<section class="col-lg-12 connectedSortable">
			<div class="box box-primary">
				<div class="box-header">

					<i class="fa  fa-info-circle">&#160;</i>
					<h3 class="box-title">Overview</h3>
				</div>
				<div class="box-body no-padding">
					<div class="table-responsive">
						<!-- .table - Uses sparkline charts-->
						<table id="overview_table" class="table table-striped">
							<tr>
								<td>Organism/Strain</td>
								<td id="organism">-</td>
								<td></td>
							</tr>
							<tr id="taxon-row">
								<td>Taxonomic Information</td>
								<td id="taxon">-</td>
								<td></td>
							</tr>
							<tr>
								<td>Protein</td>
								<td id="prot_name">-</td>
								<td></td>
							</tr>
							<tr id="gene_row">
								<td>Gene</td>
								<td id="gene_link">-</td>
								<td></td>
							</tr>

							<tr>
								<td>Status</td>
								<td id="status">-</td>
								<td></td>
							</tr>

							<tr>
								<td>Length</td>
								<td id="strSize">??</td>
								<td></td>
							</tr>
							<tr id="prot_desc_tr">
								<td>Description</td>
								<td id="prot_desc"></td>
							</tr>
							<tr id="tr_structure">
							</tr>
						</table>
						<!-- /.table -->

					</div>
				</div>

			</div>

		</section>
		<section class="col-lg-6 connectedSortable">
			<div class="box box-primary" id="dbvariants-box">
				<div class="box-header">

					<i class="fa fa-map-marker">&#160;</i>
					<h3 class="box-title">Database Variants</h3>
				</div>
				<div class="box-body no-padding">
					<div class="table-responsive">
						<table id="dbvariants-table" class="table table-striped">
							<thead>
								<tr>
									<th>Id</th>
									<th width="50px">AA pos</th>									
									<th>Description</th>
									<th>Additional info</th>
								</tr>
							</thead>
							<tbody id="variants"></tbody>
						</table>
					</div>
				</div>

			</div>
</section><section class="col-lg-6 connectedSortable">
<div class="box box-primary" id="strainvariants-box">
				<div class="box-header">

					<i class="fa fa-map-marker">&#160;</i>
					<h3 class="box-title">Strain Variants</h3>
				</div>
				<div class="box-body no-padding">
					<div class="table-responsive">
						<table id="strainvariants-table" class="table table-striped">
							<thead>
								<tr>
									<th>Id</th>
									<th width="50px">AA pos</th>										
									<th>Description</th>
									<th>Reported</th>
								</tr>
							</thead>
							<tbody></tbody>
						</table>
					</div>
				</div>

			</div>
</section>

		
		<section class="col-lg-6 connectedSortable">
			

			<!-- /.box -->

			<div class="box box-primary" id="expressionbox">
				<div class="box-header">

					<i class="fa fa-map-marker">&#160;</i>
					<h3 class="box-title">Expression</h3>
				</div>
				<div class="box-body no-padding">
					<div class="table-responsive">
						<!-- .table - Uses sparkline charts-->
						<table id="expression_table" class="table table-striped">
							<tr>
								<th>Condition</th>
								<th>Fold Change</th>
								<th>Significant</th>
							</tr>
						</table>

						<table id="expression_table" class="table table-striped">
							<tr>
								<th><a id="heatmap" href="Expression.jsp"> Heatmap</a></th>
							</tr>
						</table>
						<!-- /.table -->
					</div>
				</div>

			</div>

		</section>
		

	</div>

	<div class="row">
		<section id="ontologies_section" class="col-lg-6 connectedSortable">
			<!-- Map box -->
			<div class="box box-primary">
				<div class="box-header">

					<i class="fa  fa-tag">&#160;</i>
					<h3 class="box-title">Function/s</h3>
				</div>
				<div class="box-body no-padding">
					<div class="table-responsive">
						<!-- .table - Uses sparkline charts-->
						<table id="ontologies_list" class="table table-striped">

							<tbody>
							</tbody>
						</table>
						<!-- /.table -->
					</div>
				</div>

			</div>
			<!-- /.box -->
		</section>
		<section id="structures_section" class="col-lg-6 connectedSortable">
			<!-- Map box -->
			<div class="box box-primary">
				<div class="box-header">

					<i class="fa  fa-tag">&#160;</i>
					<h3 class="box-title">Structure/s</h3>
				</div>
				<div class="box-body no-padding">
					<div class="table-responsive">
						<!-- .table - Uses sparkline charts-->
						<table id="structures_list" class="table table-striped">
							<thead>
								<tr>
									<th>Type</th>
									<th>Structure</th>
									<th>Template</th>
									<th>Location in chain/s</th>
									<th>Domain/s</th>
									<th>Druggability</th>
								</tr>
							</thead>
							<tbody id="structures_list_body">
							</tbody>
						</table>
						<!-- /.table -->
					</div>
				</div>

			</div>
			<!-- /.box -->
		</section>
		<section id="ontologies_graph_section"
			class="col-lg-12 connectedSortable">
			<!-- Map box -->
			<div class="box box-primary">
				<div class="box-header">

					<i class="fa  fa-tag">&#160;</i>
					<h3 class="box-title">Gene Ontology Graph</h3>
					<div class="pull-right box-tools">

						<div class="input-group input-group-sm">
							<button class="btn btn-info" id="redraw">Redraw</button>


							<input id="enableWheel" type="checkbox" /><label
								for="enableWheel">Enable Wheel Zoom</label> <select
								id="grap_mode_select">
								<option value="all">ALL</option>
								<option value="bp">Biological Process</option>
								<option value="cl">Celular Localization</option>
								<option value="mf">Molecular Function</option>
							</select>
						</div>
					</div>

				</div>
				<div class="box-body no-padding">
					<div id="cy">&#160;</div>
				</div>
			</div>
			<!-- /.box -->
		</section>
	</div>

	<div class="row">
		<section id="pathways-section" class="col-lg-12 connectedSortable">
			<div id="pathways_box" class="box box-primary">
				<div class="box-header">

					<i class="fa  fa-gears">&#160;</i>
					<h3 class="box-title">Reactions (grouped by pathway)</h3>
				</div>
				<div class="box-body no-padding">
					<div id="pathwaystree"></div>
				</div>


			</div>
		</section>

	</div>


	<div class="row">
		<section id="sequence_row" class="col-lg-12 connectedSortable">
			<!-- Map box -->
			<div class="box box-primary">
				<div class="box-header">

					<i class="fa fa-align-center">&#160;</i>
					<h3 class="box-title">Features</h3>
				</div>
				<div class="box-body no-padding">
					<div class="table-responsive">
						<!-- .table - Uses sparkline charts-->

						<table id="features_table" class="table table-striped">

							<tbody id="sequence_list">

							</tbody>
						</table>
						<table id="other_features" style="display: None">&#160;
						</table>
						<!-- /.table -->
					</div>
				</div>


				<!-- /.box-body-->

			</div>
			<!-- /.box -->
		</section>
	</div>

	<div class="row">
		<section class="col-lg-6 connectedSortable">
			<!-- Map box -->
			<div class="box box-primary">

				<div class="box-header">

					<div class="pull-right box-tools">
						<div class="row">
							<div class="box-header col-xs-3">
								<i class="fa  fa-align-justify">&#160;</i>
								<h3 class="box-title">Sequence</h3>
							</div>
							<div class="col-xs-3">
								<div style="display: none;" class="input-group input-group-sm">
									<span class="input-group-addon"><i
										class="fa fa-align-center">&#160; </i> </span><input
										class="form-control " readonly="readonly" value="Add to MSA" />
									<span class="input-group-addon"><input id="msa-check"
										type="checkbox" /></span>
								</div>
							</div>
							<div class="col-xs-3">
								<div class="input-group input-group-sm">
									<input id="bps_per_line" type="number" class="form-control " />
									<span class="input-group-btn">
										<button id="bps_per_line_btn" class="btn btn-info">
											Change aa/line</button>
									</span>
								</div>
							</div>
							<div class="col-xs-3">
								<div class="input-group">
									<div class="input-group input-group-sm">
										<input id="start_end_seq_input" type="text"
											class="form-control" readonly="readonly" /> <span
											class="input-group-btn"> <!-- <button class="btn btn-info" id="blast_button"
												title="Runs the blast algorithm">Blast</button> -->
										</span>
									</div>
								</div>
							</div>
						</div>
					</div>
				</div>

				<div class="box-body no-padding">

					<div id="sequence_box"></div>
				</div>
				<div class="box-footer">
					<textarea id="copy_area" rows="0" cols="50">-</textarea>
				</div>
			</div>
			<!-- /.box -->


		</section>
		<section id="aln_section" class="col-lg-6 connectedSortable">
			<!-- Map box -->
			<div class="box box-primary">

				<div class="box-header">
					<i class="fa  fa-align-justify">&#160;</i>
					<h3 class="box-title">Aligment</h3>
				</div>
				<div class="box-body no-padding">
					<div id="msa-div">...</div>
				</div>
			</div>
		</section>
	</div>
	<div class="row">

		<section class="col-lg-12 connectedSortable">

			<div class="box box-primary">
				<div class="box-body no-padding">
					<div id="protein_tracks"></div>
				</div>
			</div>
		</section>


		<section id="cross_refereces_section"
			class="col-lg-6 connectedSortable">
			Map box
			<div class="box box-primary">
				<div class="box-header">
					<i class="fa fa-map-marker">&#160;</i>
					<h3 class="box-title">Cross-references</h3>
				</div>
				<div class="box-body no-padding">

					<table class="table">
						<thead>
							<tr>
								<th>Base</th>
								<th>Cod</th>
								<th>Description</th>
							</tr>
						</thead>
						<tbody id="cross_references"></tbody>
					</table>

				</div>

				<div class="box-footer"></div>
			</div>

		</section>

	</div>



	<div id="publications_row" class="row">
		<div class="col-md-12">
			<div class="box box-solid">
				<div class="box-header">
					<i class="fa fa-text-width">&#160;</i>
					<h3 class="box-title">Publications</h3>
				</div>
				<!-- /.box-header -->
				<div class="box-body">
					<dl id="publication_list" class="dl-horizontal">
						<dt>Description lists</dt>
						<dd>A description list is perfect for defining terms.</dd>
						<dt>Euismod</dt>
						<dd>Vestibulum id ligula porta felis euismod semper eget
							lacinia odio sem nec elit.</dd>
						<dd>Donec id elit non mi porta gravida at eget metus.</dd>
						<dt>Malesuada porta</dt>
						<dd>Etiam porta sem malesuada magna mollis euismod.</dd>
						<dt>Felis euismod semper eget lacinia</dt>
						<dd>Fusce dapibus, tellus ac cursus commodo, tortor mauris
							condimentum nibh, ut fermentum massa justo sit amet risus.</dd>
					</dl>
				</div>
				<!-- /.box-body -->

			</div>
			<!-- /.box -->
		</div>



	</div>





</body>
	</html>
</jsp:root>