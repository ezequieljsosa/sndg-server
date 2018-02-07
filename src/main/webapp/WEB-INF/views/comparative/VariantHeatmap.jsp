<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page"
	xmlns:spring="http://www.springframework.org/tags"
	xmlns:c="http://java.sun.com/jsp/jstl/core"
	xmlns:form="http://www.springframework.org/tags/form" version="2.0">
	<jsp:directive.page language="java" contentType="text/html" />

	<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta name="header_title" content="Variants strain table" />
<meta name="header_title_desc" content="Genomes and Proteomes" />
<title>Expression analysis</title>

<style type="text/css">
.box .box-header {
	padding-bottom: 10px;
}
</style>

<link href="../public/jheatmap/css/jheatmap-1.0.0-min.css"
	rel="stylesheet" type="text/css" />

<link href="../public/select2/css/select2.min.css" rel="stylesheet" />

</head>


<body>

	
	<div class="row">
	

		<section class="col-lg-6 connectedSortable">


			<div id="box-heatmap" class="box box-primary">
				<div class="box-header">
					<input type="button" class="btn btn-info" id="goToProductBtn"
						value="Go to selected Gene Product" />
					<input type="button" id="goToGenomeBtn" class="btn btn-info"
						value="Go to selected Genome" />
						
					
				</div>
				<div class="box-body no-padding">
					<div width="100%" id="heatmap" style="min-height: 200px;">&#160;</div>
					
				</div>
				<div class="overlay">&#160;</div>
				<div class="loading-img">&#160;</div>
			</div>

		</section>
		
		<section class="col-lg-6 connectedSortable">
		

		<div id="params_box" class="box box-primary">
		<div class="box-body">
					 <select>
  <option value="volvo">Volvo</option>
  <option value="saab">Saab</option>
  <option value="mercedes">Mercedes</option>
  <option value="audi">Audi</option>
</select> 
			<div class="form-group">
				<label>Search Gene Products:</label>
				<select class="search_select" multiple="multiple" style="width: 100%">&#160;</select>
			</div>
		</div>
	   </div>

		
			<div id="box-msa" class="box box-primary" style="display: none">
				<div class="box-header"><h3 class="box-title">Multiple Sequence Alignment</h3></div>
				<div class="box-body no-padding">
					<p><strong><span id="msa-protein-name">&#160;</span></strong></p>
					<div id="msa">&#160;</div>
				</div>
			</div>
	   </section>
	</div>



	<script src="../public/jheatmap/js/jheatmap-1.0.0-min.js"
		type="text/javascript"></script>
	<script type="text/javascript">
		//<![CDATA[
	</script>
	
	<script src="../public/biojs/msa.min.js" type="text/javascript"></script>
	<script src="../public/select2/js/select2.min.js"></script>

	<script>
		jheatmap.actions.DimentionSelected = function(heatmap) {
			this.heatmap = heatmap;
			this.shortCut = "";
			this.keyCodes = [];
			this.title = "";
			this.icon = "fa-eye-slash";

		};

		/**
		 * Execute the action. *
		 * @private
		 */
		jheatmap.actions.DimentionSelected.prototype.run = function(dimension) {
			
			if (dimension.selected.length > 0) {
				
				
				var value = dimension.values[dimension.selected]
				return value[0]
				

			} else {
				alert("Nothing is selected");
				return null;
			}
		};

		jheatmap.actions.DimentionSelected.prototype.rows = function() {
			var name = this.run(this.heatmap.rows);
			if (name != null)
				window.location = '../rest/redirect?type=product&key=name&value=' +  name;
		};

		jheatmap.actions.DimentionSelected.prototype.columns = function() {
			var name = this.run(this.heatmap.cols);
			if (name != null)
				window.location = '../rest/redirect?type=collection&key=organism&value=' +  name;
		};
		
		function reload_heatmap() {
		
			url_data = "../rest/variants/" + $.QueryString["genome"]

			url_genes = "../rest/variants/" + $.QueryString["genome"]
					+ "/genes";
			url_samples = "../rest/variants/" + $.QueryString["genome"]
					+ "/strains";
			
			if ($(".search_select").val() != null){
				var search_value = encodeURI($(".search_select").val().join(" "));
				url_data += "?keywords=" + search_value;
				url_genes += "?keywords=" + search_value;;
				
			}

			$('#heatmap')
					.heatmap(
							{
								data : {
									rows : new jheatmap.readers.AnnotationReader(
											{
												url : url_genes
											}),
									cols : new jheatmap.readers.AnnotationReader(
											{
												url : url_samples
											}),
									values : new jheatmap.readers.TableHeatmapReader(
											{
												url : url_data
											})
								},

								init : function(heatmap) {
									
									// borra loading
									$("#box-heatmap > .loading-img").remove();
									$("#box-heatmap > .overlay").remove();
									
									heatmap.controls.cellSelector = false; 
									heatmap.controls.poweredByJHeatmap = true;
									heatmap.controls.filters = true;
									heatmap.controls.rowSelector = true;
									heatmap.controls.columnSelector = true;
									heatmap.controls.shortcuts = false

									heatmap.paintCellDetails = paintCellDetails;
									
									// Column annotations
									heatmap.cols.decorators["strain"] = new jheatmap.decorators.CategoricalRandom();
									heatmap.cols.annotations = [ "strain" ];

									// Rows annotations
									heatmap.rows.decorators["gene"] = new jheatmap.decorators.PValue(
											{
												cutoff : 0.05
											});
									heatmap.rows.annotations = [ "gene" ];

									heatmap.cells.decorators["has_variant"] = new jheatmap.decorators.Categorical(
											{
												values : [ "true", "false" ],
												colors : [ "green", "white" ]
											});

									$('#goToProductBtn')
											.click(
													function(e) {														
														action = new jheatmap.actions.DimentionSelected(
																heatmap);
														action.rows();
													});
									$('#goToGenomeBtn')
											.click(
													function(e) {
														action = new jheatmap.actions.DimentionSelected(
																heatmap);
														action.columns();
													});

								}
							});

		}
		
		 
	    
	    /**
	    * Funcion llamada en celeccion de celda de heatmap.
	    * Carga div con msa de proteinas
	    */
		function paintCellDetails(row, col, heatmap, boxTop, boxLeft, details) {
			
			var protein_id = heatmap.rows.getValue(row, 2);
			
			// agrega animacion loading
			if ( !$("#box-msa > .loading-img" ).length ) {
				$("#box-msa").append('<div class="overlay">&#160;</div><div class="loading-img">&#160;</div>');
			}
			
			$("#box-msa").show();
			heatmap.rows.selected = [row];
		 	heatmap.focus.col = undefined;
		 	heatmap.focus.row = undefined;
		 	heatmap.drawer.paint();
		 	details.css('display', 'none');
			
			
			var url = "../rest/variants/" + $.QueryString["genome"] +"/" + protein_id + "/msa";
			
				$.getJSON(url, function( data ) {
					
					var opts = {
							el: document.getElementById('msa')
							};
					opts.vis = {
							conserv: false,
							overviewbox: false,
							seqlogo: true,
							labelId: false,
							labelName: true
							};
					opts.seqs = data.msa;   
					opts.colorscheme = {"scheme": "hydro"};
					opts.zoomer  = {labelNameLength: 220};
					var a = msa(opts);
					a.render();
					$("#msa-protein-name").html(data.name);
					
					//borra loading
					$("#box-msa > .loading-img").remove();
					$("#box-msa > .overlay").remove();
				});


			}
		

		$(document).ready(function() {

			
			if (typeof $.QueryString["genome"] == "undefined") {
				$("#heatmap").html("No genome selected");
				return;
			}

			reload_heatmap();

		    $(".search_select").select2({
		        ajax: {
			        url: "../rest/ontologies/search",
			        dataType: 'json',
			        delay: 250,
			        data: function (params) { return { q: params.term, organism:$.QueryString["organism"] };},
			        processResults: function (data, page) {
		                 return {
		                     results: $.map(data, function (item) {
		                         return {
		                             text: item.term + " - " + item.name,
		                             id: item.term
		                         }
		                     })
			         	 };   
			       	},
			        cache: true
		        },
		        minimumInputLength: 3,
		        });
		    
		    $(".search_select").change(function() {
		    	console.log($(".search_select").val());
		    	if ( !$("#box-heatmap > .loading-img" ).length ){
					$("#box-heatmap").append('<div class="overlay">&#160;</div><div class="loading-img">&#160;</div>');
					console.log("loading");
		    	}
		    	$("#box-msa").hide();
				reload_heatmap();
		    });


			
		});
	</script>



	<script type="text/javascript">
		// ]]>
	</script>
</body>
	</html>
</jsp:root>