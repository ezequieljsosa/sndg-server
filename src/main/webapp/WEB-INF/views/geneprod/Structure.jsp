<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page"
	xmlns:spring="http://www.springframework.org/tags"
	xmlns:c="http://java.sun.com/jsp/jstl/core"
	xmlns:fn="http://java.sun.com/jsp/jstl/functions"
	xmlns:form="http://www.springframework.org/tags/form" version="2.0">
	<c:set var="reqUrl">${pageContext.request.requestURL}</c:set>
	<c:set var="baseURL"
		value="${fn:replace(reqUrl, pageContext.request.requestURI,pageContext.request.contextPath)}" />
	<jsp:directive.page language="java" contentType="text/html" />

	<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>Structure</title>



<link
	href="${baseURL}/public/theme/css/datatables/dataTables.bootstrap.css"
	rel="stylesheet" type="text/css" />

<link rel="stylesheet" type="text/css"
	href="${baseURL}/public/widgets/glmol/glmolPanel.css" />

<link rel="stylesheet" type="text/css"
	href="${baseURL}/public/widgets/color-picker/colorPicker.css" />


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


	


<link href="${baseURL}/public/theme/css/ionicons.min.css"
	rel="stylesheet" type="text/css" />

<link href="${baseURL}/public/theme/css/bootstrap-slider/slider.css"
	rel="stylesheet" type="text/css" />

<link rel="stylesheet"
	href="${baseURL}/public/biojs/resources/dependencies/jquery/jquery-ui-1.8.2.css" />
<link rel="stylesheet"
	href="${baseURL}/public/biojs/resources/dependencies/jquery/jquery.tooltip.css" />

<!--  -->


<style type="text/css">
div.colorPicker-palette {
	width: 130px;
}

div.colorPicker-specialSwatch {
	height: 25px;
	width: 110px;
}
</style>

</head>
<body>

	<script type="text/javascript">
		//<![CDATA[
	</script>




	<script src="${baseURL}/public/jslibs/jquery/jquery-migrate-1.2.1.js"></script>

	<!-- DATA TABES SCRIPT -->
	<script
		src="${baseURL}/public/theme/js/plugins/datatables/jquery.dataTables.js"
		type="text/javascript"></script>
	<script
		src="${baseURL}/public/theme/js/plugins/datatables/dataTables.bootstrap.js"
		type="text/javascript"></script>

	<!-- GLMOL SCRIPT -->
	<script type="text/javascript"
		src="${baseURL}/public/widgets/glmol/Three49custom.js"></script>
	<script type="text/javascript"
		src="${baseURL}/public/widgets/glmol/GLmol.js"></script>

	<script type="text/javascript"
		src="${baseURL}/public/widgets/sbgglmol/model/colorer/AtomColorer.js"></script>
	<script type="text/javascript"
		src="${baseURL}/public/widgets/sbgglmol/model/colorer/ByResidueColorer.js"></script>
	<script type="text/javascript"
		src="${baseURL}/public/widgets/sbgglmol/model/colorer/ByAtomColorer.js"></script>
	<script type="text/javascript"
		src="${baseURL}/public/widgets/sbgglmol/model/colorer/ByAtomNameColorer.js"></script>

	<script type="text/javascript"
		src="${baseURL}/public/widgets/sbgglmol/model/colorer/ChainColorer.js"></script>
	<script type="text/javascript"
		src="${baseURL}/public/widgets/sbgglmol/model/colorer/ChainbowColorer.js"></script>
	<script type="text/javascript"
		src="${baseURL}/public/widgets/sbgglmol/model/colorer/BFactorColorer.js"></script>

	<script type="text/javascript"
		src="${baseURL}/public/widgets/sbgglmol/model/style/CartoonStyle.js"></script>
	<script type="text/javascript"
		src="${baseURL}/public/widgets/sbgglmol/model/style/PocketStyle.js"></script>
	<script type="text/javascript"
		src="${baseURL}/public/widgets/sbgglmol/model/style/SphereStyle.js"></script>
	<script type="text/javascript"
		src="${baseURL}/public/widgets/sbgglmol/model/style/StickStyle.js"></script>

	<script type="text/javascript"
		src="${baseURL}/public/widgets/sbgglmol/model/SBGGLMol.js"></script>
	<script type="text/javascript"
		src="${baseURL}/public/widgets/sbgglmol/model/Layer.js"></script>
	<script type="text/javascript"
		src="${baseURL}/public/widgets/sbgglmol/model/Pocket.js"></script>


	<script type="text/javascript"
		src="${baseURL}/public/widgets/sbgglmol/ui/style/UISphereStyle.js"></script>
	<script type="text/javascript"
		src="${baseURL}/public/widgets/sbgglmol/ui/style/UICartoonStyle.js"></script>
	<script type="text/javascript"
		src="${baseURL}/public/widgets/sbgglmol/ui/style/UIPocketStyle.js"></script>
	<script type="text/javascript"
		src="${baseURL}/public/widgets/sbgglmol/ui/style/UIStickStyle.js"></script>

	<script type="text/javascript"
		src="${baseURL}/public/widgets/sbgglmol/ui/UIAtomColorer.js"></script>


	<script type="text/javascript"
		src="${baseURL}/public/widgets/sbgglmol/ui/UILayer.js"></script>
	<script type="text/javascript"
		src="${baseURL}/public/widgets/sbgglmol/ui/UILayerAtomTable.js"></script>
	<script type="text/javascript"
		src="${baseURL}/public/widgets/sbgglmol/ui/UILayerList.js"></script>
	<script type="text/javascript"
		src="${baseURL}/public/widgets/sbgglmol/ui/UIPocket.js"></script>

	<script type="text/javascript"
		src="${baseURL}/public/widgets/sbgglmol/ui/UIByPropertyColorer.js"></script>

	<script type="text/javascript"
		src="${baseURL}/public/widgets/sbgglmol/utils/SBGParser.js"></script>
	<script type="text/javascript"
		src="${baseURL}/public/widgets/sbgglmol/utils/SBGBuilder.js"></script>



	<!-- BIOJS SCRIPT -->
	<script language="JavaScript" type="text/javascript"
		src="${baseURL}/public/biojs/Biojs.js"></script>
	<script language="JavaScript" type="text/javascript"
		src="${baseURL}/public/biojs/Biojs.FeatureViewer.js"></script>
	<script language="JavaScript" type="text/javascript"
		src="${baseURL}/public/biojs/Biojs.SimpleFeatureViewer.js"></script>



	<script src="${baseURL}/public/bia/model/features/FeatureTrack.js"></script>
	<script
		src="${baseURL}/public/bia/model/features/FeatureTrackCategory.js"></script>
	<script src="${baseURL}/public/bia/model/features/FeatureTrackStyle.js"></script>
	<script src="${baseURL}/public/bia/ui/FeatureTrackRenderer.js"></script>


	<script language="JavaScript" type="text/javascript"
		src="${baseURL}/public/bia/aux_structure_jsp.js?v=2"></script>



	<script src="${baseURL}/public/biojs/msa.min.js" type="text/javascript"></script>
	<script src="${baseURL}/public/biojs/biojs-io-gff.js"
		type="text/javascript"></script>

	<script language="JavaScript" type="text/javascript"
		src="${baseURL}/public/bia/msa.js"></script>






	<!-- 	<script
		src="${baseURL}/public/jquery-ui-1.11.1/jquery-ui.js"
		type="text/javascript"></script> -->
	<script language="JavaScript" type="text/javascript"
		src="${baseURL}/public/biojs/resources/dependencies/jquery/jquery.tooltip.js"></script>
	<script language="JavaScript" type="text/javascript"
		src="${baseURL}/public/biojs/resources/dependencies/graphics/raphael-2.1.2.js"></script>
	<script language="JavaScript" type="text/javascript"
		src="${baseURL}/public/biojs/resources/dependencies/graphics/canvg.js"></script>
	<script language="JavaScript" type="text/javascript"
		src="${baseURL}/public/biojs/resources/dependencies/graphics/rgbcolor.js"></script>

	<script
		src="${baseURL}/public/theme/js/plugins/bootstrap-slider/bootstrap-slider.js"
		type="text/javascript"></script>

	<!-- -->


	<script type="text/javascript"
		src="${baseURL}/public/widgets/color-picker/jquery.colorPicker.js"></script>




	<script type="text/javascript">
		// ]]>
	</script>



	<!-- <div class="row">
		<section class="col-lg-12 connectedSortable">
			<div class="box box-primary">
				<div class="box-header">

					<i class="fa fa-map-marker">&#160;</i>
					<h3 class="box-title">Summary Structure</h3>

					

				</div>
				<div class="box-body no-padding">
					<div class="table-responsive">
						<table class="table table-bordered">
							<tbody>
								<tr>
									<td
										title="Structure has predicted pKa value deviated more than 1 point from reference. pKa has been determined with propKa heuristics.">Structure
										has pKa &gt;= 1 from reference</td>
									<td>true</td>
									<td title="Value of the most druggable pocket.">Max Pocket
										Druggability</td>
									<td>0.919</td>
									<td
										title="Reports if the structure has been obtained in complex with a drug like molecule.">Structure
										has drug</td>
									<td>false</td>
								</tr>
								<tr>
									<td title="">Pocket Matches PFAM relevant residue</td>
									<td>false</td>
									<td title="Structure is in CSA.">Structure is in CSA</td>
									<td>false</td>
									<td title="Structure has Metals.">Structure has Metals</td>
									<td>false</td>
								</tr>
							</tbody>
						</table>
					</div>
				</div>
			</div>
		</section>
	</div> -->
	<div class="row">
		<section class="col-lg-12 connectedSortable">
			<div class="box">
				<div class="box-body no-padding">
					<div class="table-responsive">
						<table class="table table-bordered">
							<tbody>

								<tr id="cristal_row">
									<td id="pdb_code"></td>
									<td><b>Resolution</b></td>
									<td id="pdb_resolution">?</td>
									<td><b>Quaternary</b></td>
									<td id="pdb_quaternary">-</td>
								</tr>

								<tr class="model_row">

									<td><b>Assesments</b></td>
									<td id="pdb_assesments">-</td>
								</tr>
								<tr class="model_row">

									<td><b>Template</b></td>
									<td id="templateDiv">-</td>
								</tr>
								<tr class="model_row">
									<td title="PDB Chains cluster with a 95% identity"><b>PDB
											Cluster</b></td>
									<td id="templatesDiv">-</td>
								</tr>
								<tr class="model_row">
									<td><b>Template Quaternaty Structure</b></td>
									<td id="quaternaryTempleteDiv">-</td>
								</tr>
								<tr>


									<td colspan="2"><a class="btn" id="download_button"
										title="Download a zip file with the structure pdb and pockets">Download</a></td>
								</tr>
							</tbody>
						</table>
					</div>

				</div>

			</div>
		</section>
	</div>
	<div id="screen_row" class="row">

		<section id="pdb_section" class="col-lg-8">
		<span style="display:none" id="alnAdvice"> 
			<h1>
									<large class="badge bg-yellow">!</large> 
									We are showing the monomer structure of the identified gene. To see the whole PDB  <a href="${baseURL}/structure/${pdbName}">click here</a>.  
									</h1>
								</span>
			<div id="msa_reference" class="box">

				<table>
					<tr>
						<td>Reference:</td>
						<td><span style="background-color: yellow">D: Drug
								Binding</span></td>
						<td><span style="background-color: green">A: IN CSA</span></td>
						<td><span style="background-color: grey">I: Important
								PFAM Residue</span></td>
						<td><span style="background-color: red">#: Pocket
								Number</span></td>
					</tr>
				</table>
				<div id="table-box-body" class="box-body no-padding">
					<div id="msa-box-body">-</div>
				</div>
				<div id="div2">-</div>


			</div>
			
			
			

			<div id="table-box" class="box" style="display: none">
				<div class="box-header">
					<!-- tools box -->
					<div class="pull-right box-tools">
						<button class="btn btn-danger btn-sm" data-widget="remove"
							data-toggle="tooltip" title="" data-original-title="Remove">
							<i class="fa fa-times">&#160;</i>
						</button>
					</div>
					<!-- /. tools -->


					<h3 id="box-table-title" class="box-title">?</h3>
				</div>

				<div id="table-box-body" class="box-body no-padding">


					<table id="dialog-table">
						<thead>
							<tr></tr>
						</thead>
						<tbody></tbody>
					</table>

				</div>

			</div>


			<div class="box" id="glmol_pdb_box">

				<div id="glmol_pdb" class="box-body">
					<h3 id="glmol_pdb_msg">Loading molecules... if the PDB file is
						big (more than 8 chains) it may take a few minutes</h3>

				</div>

			</div>
			
			<div id="YourOwnDivId"></div>
			

		</section>

		<section id="vis-section" class="col-lg-4 connectedSortable">

			<div class="box box-primary">
				<div class="box-header">

					<i class="fa  fa-gear">&#160;</i>
					<h3 class="box-title">Chain/s List</h3>
					<div class="pull-right box-tools">
						<button
							title="paints in white the atoms resulting from the intersection beetween the selected layers"
							id="btn_intersect" class="btn btn-info btn-sm">Select
							intersection</button>
						&#160;
						<button id="btn_clear_intersect" class="btn btn-info btn-sm">
							Clear intersection</button>
						&#160;
						<button id="btn_reset_camera" class="btn btn-info btn-sm">
							Reset Zoom <i class="fa fa-fw fa-search-plus">&#160; </i>
						</button>
						&#160;&#160;
						<button class="btn btn-info btn-sm" data-widget="collapse"
							data-toggle="tooltip" title="" data-original-title="Collapse">
							<i class="fa fa-minus"> &#160;</i>
						</button>
					</div>
				</div>
				<div class="box-body">
					<table id="layer_list_div">
						<thead>
							<tr>
								<td>Visible</td>
								<td>Name</td>
								<td title="Zooms in and fixes the camera in the selected layer">Center
									In</td>
								<td>Style</td>
							</tr>
						</thead>
						<tbody></tbody>
					</table>
				</div>
			</div>
			<div class="box box-primary">
				<div class="box-header">
					<!-- tools box -->
					<div class="pull-right box-tools">
						<i class="fa fa-question"
							title="We show only the pockets with druggability > 0.2. The pockets with a druggability prediction bellow 0.2 show very bad results and are not, normally, worth the effort to check them. You can access the results of FPocket for all the pockets at the bottom of the 3D visualization.">&#160;</i>

						<button class="btn btn-info btn-sm" data-widget="collapse"
							data-toggle="tooltip" title="" data-original-title="Collapse">
							<i class="fa fa-minus"> &#160;</i>
						</button>

					</div>
					<!-- /. tools -->
					<i class="fa  fa-gear">&#160;</i>
					<h3 class="box-title">Pocket List</h3>
				</div>
				<div id="pocket_list_div" class="box-body">
					<table>
						<thead>
							<tr>
								<td>Visible</td>
								<td>Name</td>
								<td title="Zooms in and fixes the camera in the selected layer">Center
									In</td>
								<td>Style</td>
								<td>Druggability</td>
							</tr>
						</thead>
						<tbody></tbody>
					</table>
				</div>
			</div>
			<div id="features_list_box" class="box box-primary">
				<div class="box-header">
					<!-- tools box -->
					<div class="pull-right box-tools">

						<button class="btn btn-info btn-sm" data-widget="collapse"
							data-toggle="tooltip" title="" data-original-title="Collapse">
							<i class="fa fa-minus"> &#160;</i>
						</button>

					</div>
					<!-- /. tools -->
					<i class="fa  fa-gear">&#160;</i>
					<h3 class="box-title">Features List</h3>
				</div>
				<div id="features_list_div" class="box-body">
					<table>
						<thead>
							<tr>
								<td>Visible</td>
								<td>Name</td>
								<td title="Zooms in and fixes the camera in the selected layer">Center
									In</td>
								<td>Style</td>
							</tr>
						</thead>
						<tbody></tbody>
					</table>
				</div>
			</div>

		</section>

	</div>



	<script type="text/javascript">
		//<![CDATA[
	</script>

	<script type="text/javascript">
		var structure = ${structure};
		var stemplate = ${template};
		$.proteins = ${proteins};
		var protein = $.proteins[0];
		var ontologies = ${ontologies};
		var pocket_pdbs = ${pocket_pdbs};
		var pdb = ${pdb};
		var pdb = pdb.join("\n");
		var features = ${features};
		
		var valid_pockets = $.map(structure.pockets,function(pocket,i){
			if(pocket.druggability_score > 0.2){
				return parseInt(pocket.name.split("_")[1] ) 
			}
		})
		
		var init2 = function(){
			
			$('#glmol_pdb').css("max-height", $(window).height());
			$('#glmol_pdb').css("min-width", 500);
			$('#glmol_pdb').css("min-height", 500);
			$('#glmol_pdb_box').css("min-width", 500);
			$('#glmol_pdb_box').css("min-height", 500);

			if($('#glmol_pdb').css("height") <  (2 * $(window).height() / 3)){
				$('#glmol_pdb').css("height",2 * $(window).height() / 3);
			} else {
				$('#glmol_pdb').css("height", $('#glmol_pdb').css("height"));	
			}		
			
			
			
			var builder = new $.SBGBuilder().div('glmol_pdb');
			builder.data(pdb)
			builder.pockets_data = []
			
			$.each(pocket_pdbs,function(i,pocket_data){
				if(valid_pockets.indexOf(pocket_data.number) != -1){					
					builder.pockets_data.push([pocket_data.number,pocket_data.atoms,pocket_data.as_lines.join("") ]);	
				}
				
			} );
			
			builder._data_loaded_fn = function(glmol) {		
				$("#glmol_pdb_msg").remove()
				$.glmol = glmol		
				$.glmol.atom_labels = []
				
				var backbone_prop = glmol.atoms.filter(x => !x.hetflag ).length / glmol.atoms.filter(x => x.atom == "CA" ).length  
				if (backbone_prop < 5){
					
					$('#screen_row').empty();
					$('#screen_row').html("<h1>PDB file has not enough information to show this structure with GLMol. You can download the model or go to the PDB record by clicking in the upper left of the previous table.</h1>")
					return
				}
				
				
				
				var chain_layers = glmol.chain_layers();
				
				
				
				if((chain_layers.length == 1) && ($.trim(chain_layers[0].name) == "")){
					chain_layers[0].name = "A";
				} else {
					if(window.used != null){
						$('#alnAdvice').show()
						chain_layers = chain_layers.filter( x => {
							if (x.name == window.used.chain ){
								
								return true
							} else {
								
								x.visible = false;
							}
							return false;
							
						})	
						var newAtoms = glmol.atoms.filter(x =>  (x.resi >= window.used.start &&  x.resi <= window.used.end && x.chain == window.used.chain ) ).map(x => x.serial);
						chain_layers[0].atoms = newAtoms;
						var hetChain =  glmol.chain_layers().filter( x => x.name == 'heatoms');
						if(hetChain.length > 0 ) {
							hetChain = hetChain[0]
							var newAtoms2 = []
							hetChain.atoms.forEach(h => {
								h = glmol.atoms[h]
								newAtoms.forEach( atm=> {
									atm = glmol.atoms[atm]
									var dist = Math.sqrt(
					                        ( atm.x - h.x ) * ( atm.x - h.x ) +
					                        ( atm.y - h.y ) * ( atm.y - h.y ) +
					                        ( atm.z - h.z ) * ( atm.z - h.z )
					                    );
									if ( dist < 6){
										newAtoms2.push(h.serial);
								}
							})
							});
							if(newAtoms2.length > 0){
								hetChain.atoms = newAtoms2;
								chain_layers.push(hetChain)
								hetChain.visible = true;
							} 
						}
					}
				}
				
				var ll1 = new $.UILayerList ($("#layer_list_div"),chain_layers);
				
				ll1.style_list = ["cartoon","spectrum","bfactor","atom"];
				ll1.init();								
				
				var pockets = glmol.pocket_layers().filter(x =>  window.used == null || window.used.pockets.indexOf(x.name) != -1 )
				
				$.each(pockets,function(i,pocket_layer){		
					
					var pocket_data = $.grep(structure.pockets,function(x){return  parseInt(x.name.split("_")[1]) ==pocket_layer.name})
					pocket_layer.data = pocket_data[0];
				});
				
				var ll2 = new $.UILayerList ($("#pocket_list_div"),pockets);
				
				ll2.data_renderers = [{render:function(elem,layer){
					elem.html(layer.data.druggability_score);
				}}]
				
				ll2.init();
				
				var map_aln_pos_from_query_res_id = {} 
				$.each(structure.chains[0].residues,function(i,residue){
					map_aln_pos_from_query_res_id[residue.resid] = i;
				});
				
				function allowResidue(res){
					if (window.used == null){
						return true
					} else {						
						var resid = parseInt(res.split("_")[1])
						var chain = res.split("_")[0]
						return  chain == window.used.chain &&  window.used.start <= resid &&  window.used.end >= resid;    
					}
				} 
				
				
				var feature_layers = []
				$.each(structure.residueSets,function(i,residueSet){
					
					var style = new $.StickStyle(new $.ByAtomColorer( $.ByAtomColorer.aa_atoms_map ), 1)
					var residues = $.map(residueSet.residues.filter(allowResidue),function(x){return x.replace("_",".")});
					if (residues.length > 0){
					var atoms = $.map(glmol.residue_atoms(residues),function(x){
						return x.serial;
					});
					var layer = new $.Layer(residueSet.name,atoms ,
							style, false);
					layer.visible = false;
					glmol.add_layer(layer)
					feature_layers.push(layer)
					}
				});
				if (feature_layers.length > 0){
					var ll3 = new $.UILayerList ($("#features_list_div"),feature_layers);
					ll3.style_list = ["atom"];
					ll3.init();	
				} else {
					$("#features_list_box").remove()
				}
				if(window.used != null){
					glmol.zoomInto(newAtoms)
				}
			}
			
			builder.build();
			
			
			
		}
		
		
		var init = function() {
			window.used = null;
			//$("<a/>",{"href":"${baseURL}/genome"}).html("Genomes").appendTo( $("#base_breadcrumb") );
			
			
			if (protein != undefined){
				var li = $("<li/>").appendTo( $(".breadcrumb") ); 
				$("<a/>",{"href":"${baseURL}/genome/" + protein.organism}).html("<i>" + protein.organism + "</i>").appendTo(li);			
				li = $("<li/>").appendTo( $(".breadcrumb") );
				$("<a/>",{"href":"${baseURL}/protein/"+ protein.id}).html(protein.name).appendTo(li);
					
			} else {
				li = $("<li/>").appendTo( $(".breadcrumb") );
				$("<a/>").html("PDB").appendTo(li);
			}
			
			li = $("<li/>").addClass("active").appendTo( $(".breadcrumb") );
			$("<a/>",{"href":"#"}).html(structure.name).appendTo(li);
			
			
			init_buttons(structure.name);
			
			load_structure_tables(structure,stemplate)
			
			if ($.proteins.length > 0){
				render_chain_tracks(structure,$.proteins);
				
				
				
			}
			
			
			
			if ((stemplate != null) && (stemplate.name != undefined) && (structure.templates[0]["aln_query"]["seq"] != "?")){
				$("#download_button").html("Download Model")
				var msa =	load_msa_model(structure,stemplate,protein);	
				msa.msa.g.on('column:click', data =>{ 
					
					var selectedRes = msa.msa.seqs.features[structure.templates[0]["aln_hit"].name].filter(x => x.start == data.rowPos);
					if (selectedRes.length > 0) {
						var resid = selectedRes[0].res_id;
						if (resid){
							$.glmol.atoms.filter(x => x.resi == resid && x.atom == "CA" ).forEach(x => {
								$.glmol._draw_labels([x])	
								$.glmol.show();
							})	
						}
						
							
					}
					
				});
				
			} else {
				$("#download_button").html("Download PDB")
				if (protein != undefined){
					//chain:aln_chain,{start:aln_start,end:aln_end,pockets:usedPockets,important:usedImportant,csa:usedCSA,drugBinding:usedDBinding}
					window.used = load_msa_cristal(structure,protein);
					
					
				} else {
					$("#msa_reference").remove();
				}
			}
			
			
			
			$("#download_button").attr("href", $.api.url_download_structure(structure.name));
	
			
			$('#btn_reset_camera').click(function(evt){
				$.glmol.zoomInto($.glmol.getAllAtoms());
				$.glmol.refreshAll()
			});
			$('#btn_clear_intersect').click(function(evt){
				$.glmol.clear_selection();
			});
			$("#btn_intersect").click(function(evt){	
				
				if( $.grep($.glmol.layers,function(layer){ return layer.visible }).length > 1   ){
				var iner_l = [];
				
				
				$.glmol.clear_selection();
				
				var inter = $.glmol.getAllAtoms()
				$.each($.glmol.layers,function(i,layer){
					if(layer.visible){
						iner_l.push(layer.name)
						inter = $.grep(inter, function(atom){
							return layer.atoms.indexOf(atom) != -1;
						})
					}
				})
				
				if (inter.length > 0){
					$.glmol.zoomInto(inter)
					$.glmol.add_to_selected(inter)	
				} else {
					alert("no intersection beetween: " + iner_l)
				}				
				
				
				} else {
					alert("more than one layer must be selected")
				}
			})
			
			
			setTimeout(init2,100)
			
							
			
			
		};
		
		function swap(json){
			  var ret = {};
			  for(var key in json){
			    ret[json[key].toUpperCase()] = key;
			  }
			  return ret;
			}
		
		protein_letters_1to3  = {
			    'A': 'Ala', 'C': 'Cys', 'D': 'Asp',
			    'E': 'Glu', 'F': 'Phe', 'G': 'Gly', 'H': 'His',
			    'I': 'Ile', 'K': 'Lys', 'L': 'Leu', 'M': 'Met',
			    'N': 'Asn', 'P': 'Pro', 'Q': 'Gln', 'R': 'Arg',
			    'S': 'Ser', 'T': 'Thr', 'V': 'Val', 'W': 'Trp',
			    'Y': 'Tyr',
			};
		
		protein_letters_3to1 = swap(protein_letters_1to3);
		
		function render_chain_tracks(structure,proteins) {
			
			//var renderer = new $.fn.SequenceRender(structure.name);
			var protein = proteins[0];
			
			var aln = (structure.templates) ?  structure.templates[0].aln_query : protein.features.filter(x => x.identifier.startsWith(structure.name))[0].location
			var ft2 = new FeatureViewer(
					protein.sequence,"#div2", {
			    showAxis: true,
			    showSequence: false,
			    brushActive: false,
			    toolbar: false,
			    bubbleHelp:true,
			    zoomMax:10
			            });
			ft2.addFeature({
			    data: [{x:aln.start,y:aln.end,description:  structure.name     ,id:"a1"}],
			    name: (structure.templates) ? "Modelled Seq" : "Aligned with PDB",
			    className: "test6",
			    color: "#81BEAA",
			    type: "rect",
			    filter: "type2"
			});
			
		
			
		
			
			//renderer.render_seq(proteins); 
			
			
			
			
		};

		$(document).ready(init);
		
	</script>


	 <script type="text/javascript"
		src="${baseURL}/public/widgets/nextprot/feature-viewer.nextprot.js">

	</script> 


	<script type="text/javascript">
		// ]]>
	</script>





</body>
	</html>
</jsp:root>