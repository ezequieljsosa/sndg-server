<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page"
	xmlns:spring="http://www.springframework.org/tags"
	xmlns:c="http://java.sun.com/jsp/jstl/core"
	xmlns:form="http://www.springframework.org/tags/form" version="2.0">
	<jsp:directive.page language="java" contentType="text/html" />

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta name="header_title" content="Comparative Analysis" />
<meta name="header_title_desc" content="Orthologs" />
<title>Comparative analysis</title>

<style type="text/css">
	.box .box-header {
		padding-bottom: 10px;
	}
</style>
<link href="../public/jheatmap/css/jheatmap-1.0.0-min.css" rel="stylesheet" type="text/css" />
<link href="../public/select2/css/select2.min.css" rel="stylesheet" />
</head>


<body>

	
<div class="row">
		<section id="statistics_section" class="col-lg-3">
		
		<div class="box box-primary">
		
		<div class="box-header">
		<h3 class="box-title">Filters</h3>
		</div>
			<div class="box-body"  >
			<form id="form-gene-filter" action="#">
		
			<div class="form-group">
			
				<label>Gene name or locus tag</label>
				<select class="search_gene_name" multiple="multiple" style="width: 100%">&#160;</select>
			</div>
			<hr> </hr>
			&#160;
			</form>
			</div>
		</div>
		
			<div class="box">
			<div class="box-header">
			  <h3 class="box-title">Results</h3>
			</div>
			<div class="box-body no-padding">
				<table  class="table table-striped">
				<thead>
						<tr>
							<th>Strains</th>
							<th>#genes</th>
							<th>%ref</th>
						</tr>
				</thead>
				<tbody id="result-table-body"></tbody>
				</table>
			</div>

			</div>
		</section>

	<section class="col-lg-3 connectedSortable">
		<div id="box-heatmap" class="box box-primary">
			<div class="box-header">  <h3 class="box-title">Heatmap</h3> </div>
			<div class="box-body">
				<div width="100%" id="heatmap" style="min-height: 200px;">&#160;</div>
			</div>
			<div class="overlay">&#160;</div>
			<div class="loading-img">&#160;</div>
		</div>
	</section>
	
	<section class="col-lg-6">

			<div class="box box-primary">
			<div class="box-header">
			  <h3 class="box-title">Ortholog description</h3>
			</div>
			<div class="box-body no-padding">
				<table  class="table table-striped">
				<thead>
						<tr>
							<th>Strain</th>
							<th>Locus Tag</th>
							<th>Start</th>
							<th>End</th>
							<th>Strand</th>
							<th>Description</th>
						</tr>
				</thead>
				<tbody id="ortholog-table-body">
				<tr>
				<td colspan="6">Select a heatmap cell </td>
				</tr>
				
				</tbody>
				</table>
				</div>
			
	

			</div>
			
			
			<div id="box-msa" class="box" style="display: none">
				<div class="box-header"><h3 class="box-title">Multiple Sequence Alignment</h3></div>
				<div class="box-body no-padding">
					
					<ul class="nav nav-tabs">
  						<li role="presentation" id="msa-nuclotide-tab" class="active"><a id="msa-nuclotide-select" href="#">Nucleotides</a></li>
  						<li role="presentation" id="msa-protein-tab"><a id="msa-protein-select" href="#">Amino acid</a></li>
					</ul>

					<p><strong><span id="msa-protein-name">&#160;</span></strong></p>
					
					
					<div id="msa">&#160;</div>
				</div>
			</div>
			
		</section>
</div>



<script src="../public/jheatmap/js/jheatmap-1.0.0-min.js" type="text/javascript"></script>

<script type="text/javascript">
	//<![CDATA[
</script>

<script src="../public/biojs/msa.min.gz.js" type="text/javascript"></script>
<script src="../public/select2/js/select2.min.js"></script>

<script>

	var msa_gene_name = "";
	var msa_type = "nucleotide"
	

	jheatmap.actions.DimentionSelected = function(heatmap) {
		this.heatmap = heatmap;
		this.shortCut = "";
		this.keyCodes = [];
		this.title = "";
		this.icon = "fa-eye-slash";

	};
	

    
	function reload_heatmap() {
		
		var filterGenes = [];
		var genesList = [];
		$( ".filter-genes-strain" ).each(function( index ) {filterGenes.push($( this ).val()); });
		
		url_data = "../rest/comparative/" + $.QueryString["analysis"] + "/orthologs/heatmap?filter-genes-strain[]=" +filterGenes;
		url_genes = "../rest/comparative/" + $.QueryString["analysis"] + "/orthologs/heatmap/genes?filter-genes-strain[]=" +filterGenes;
		url_samples = "../rest/comparative/" + $.QueryString["analysis"] + "/orthologs/heatmap/organisms";
		
		
		if ($(".search_gene_name").val() != null){
			$(".search_gene_name").each(function( index ) {genesList.push($( this ).val()); });
			url_data += "&genelist[]=" + genesList
			url_genes += "&genelist[]=" + genesList;
		}

		$('#heatmap').heatmap({
			data : {
				rows : new jheatmap.readers.AnnotationReader({url : url_genes}),
				cols : new jheatmap.readers.AnnotationReader({url : url_samples}),
				values : new jheatmap.readers.TableHeatmapReader({url : url_data})
			},
			init : function(heatmap) {
				 
				// borra loading
				$("#box-heatmap > .loading-img").remove();
				$("#box-heatmap > .overlay").remove();
				
				heatmap.controls.cellSelector = false; 
				heatmap.controls.poweredByJHeatmap = true;
				heatmap.controls.filters = true;
				heatmap.controls.rowSelector = false;
				heatmap.controls.columnSelector = false;
				heatmap.controls.shortcuts = false

				heatmap.paintCellDetails = paintCellDetails;
				
				// Column annotations
				heatmap.cols.decorators["strain"] = new jheatmap.decorators.CategoricalRandom();
				heatmap.cols.annotations = [ "strain" ];

				// Rows annotations
				heatmap.rows.decorators["gene"] = new jheatmap.decorators.PValue({cutoff : 0.05});
				heatmap.rows.annotations = [ "gene" ];

				heatmap.cells.decorators["identity"] = new jheatmap.decorators.Heat({
					minValue: -100,
					midValue: 0,
					maxValue: 100,
					nullColor: [0,0,0],
					minColor: [125,125,125],
					midColor: [255,255,255],
					maxColor : [0,125,0]
				});
			}
		});

	}

	function load_resul_table(){
		
		var filterGenes = [];
		$( ".filter-genes-strain" ).each(function( index ) {filterGenes.push($( this ).val()); });
		
		$.getJSON("../rest/comparative/" + $.QueryString["analysis"] + "/orthologs/resume?filter-genes-strain[]=" +filterGenes)
		 	.done(function( data ) {
		 		$("#result-table-body").empty();
				for (var i=0; i<data.length;i++)
					$("#result-table-body").append("<tr><td><a href='../rest/redirect?type=collection&key=organism&value=" +   data[i]['strain'] + "'>" + data[i]['strain'] + "</a></td>" +
						  						   "<td>" + data[i]['total_genes'] + "</td>" +
						  						   "<td>" + data[i]['percent_in_ref'] + "</td>" +
					  							   "</tr>"); 
			})
			.fail(function(jqxhr, textStatus, error) {
				var err = textStatus + ", " + error;
				console.log("Request Failed: " + err);
			});

	}
	

	function load_filter_genes_by_strain(){

		$.getJSON("../rest/comparative/" + $.QueryString["analysis"] + "/organisms")
		 	.done(function( data ) {
				for (var i=0; i<data.length;i++){
					
					var select_html = "<div class='form-group'>"+
					"<label> Genes: " + data[i]["name"] + "</label>" +
					"<select name='filter-genes-strain["+ i +"]' class='filter-genes-strain form-control'>" +
					"<option value=''></option>" +
					"<option value='present'>Present</option>" +
					"<option value='absent'>Absent</option>"+
					"</select>" +
					"</div>";
					
					$("#form-gene-filter").append(select_html);
				}
				
				$( ".filter-genes-strain" ).change(function() {
				 	load_resul_table();
				 	reload_heatmap();
				});
				
			})
			.fail(function(jqxhr, textStatus, error) {
				var err = textStatus + ", " + error;
				console.log("Request Failed: " + err);
			});
	}
	
    /**
    * Funcion llamada en seleccion de celda de heatmap.
    * Carga div con msa de proteinas
    */
	function paintCellDetails(row, col, heatmap, boxTop, boxLeft, details) {
    	
		gene_name = heatmap.rows.getValue(row, 0);
		

		
		$("#box-msa").show();
		heatmap.rows.selected = [row];
	 	heatmap.focus.col = undefined;
	 	heatmap.focus.row = undefined;
	 	heatmap.drawer.paint();
	 	details.css('display', 'none');
		
		
		var url = "../rest/comparative/" + $.QueryString["analysis"] + "/orthologs/" + gene_name;
			$.getJSON(url, function( data ) {
				
		 		$("#ortholog-table-body").empty();
				for (var i=0; i<data.length;i++)
					$("#ortholog-table-body").append("<tr><td><a href='../rest/redirect?type=collection&key=organism&value=" +   data[i]['strain'] + "'>" + data[i]['strain'] + "</a></td>" +
						  						     "<td>" + data[i]['locus_tag'] + "</td>" +
						  						     "<td>" + data[i]['start'] + "</td>" +
						  						     "<td>" + data[i]['end'] + "</td>" +
						  						     "<td>" + data[i]['strand'] + "</td>" +
						  						     "<td>" + data[i]['description'] + "</td>" +
					  							     "</tr>"); 
				showMSA();
			
			});
	}
    
    
    function showMSA(){
    	
    	
    	if(gene_name == "")
    		return;
		
		// agrega animacion loading
		if ( !$("#box-msa > .loading-img" ).length ) {
			$("#box-msa").append('<div class="overlay">&#160;</div><div class="loading-img">&#160;</div>');
		}
		
		$("#box-msa").show();
		
		
		var urlMsa = "../rest/comparative/" + $.QueryString["analysis"] + "/orthologs/" + gene_name + "/msa";
		
		if (msa_type == "proteins")
			urlMsa += "/proteins"; 
			
		$.getJSON(urlMsa, function( data ) {
			var opts = {
					el: document.getElementById('msa')
					};
			opts.vis = {
					conserv: false,
					overviewbox: false,
					seqlogo: false,
					labelId: false,
					labelName: true,
					 metacell: true,
					metaIdentity: false,
					metaGaps: false,
					metaLinks: false,
					gapHeader: true
					};
			opts.seqs = data.msa;   
			opts.colorscheme = {"scheme": "hydro"};
			
			
			var fun = {}

			// the init function is only called once
			fun.init = function(){
			  // you have here access to the conservation or the sequence object
			  this.cons = this.opt.conservation();
			}

			fun.run = function(letter,opts){
			  return this.cons[opts.pos] < 1 ? "red" : "#fff" 
			};

			//opts.zoomer  = {labelNameLength: 220};
			var a = msa(opts);
			a.g.colorscheme.addDynScheme("dyn", fun);
			a.g.colorscheme.set("scheme", "dyn");
			
			

			a.render();
			$("#msa-protein-name").html(data.name);
			
			//borra loading
			$("#box-msa > .loading-img").remove();
			$("#box-msa > .overlay").remove();
		});
    }

	$("#msa-nuclotide-select").click(function(){
		msa_type = "nucleotide";
		$("#msa-nuclotide-tab").addClass("active");
		$("#msa-protein-tab").removeClass("active");
		showMSA()
		
	});
	
	$("#msa-protein-select").click(function(){
		msa_type = "proteins";
		$("#msa-protein-tab").addClass("active");
		$("#msa-nuclotide-tab").removeClass("active");
		showMSA()
		
	});

	$(document).ready(function() {
		
		if (typeof $.QueryString["analysis"] == "undefined") {
			$("#heatmap").html("No analysis selected");
			return;
		}
		
		load_filter_genes_by_strain();
		load_resul_table();
		reload_heatmap();

	    $(".search_gene_name").select2({
	        ajax: {
		        url: "../rest/comparative/" + $.QueryString["analysis"] + "/gene/searchList",
		        dataType: 'json',
		        delay: 250,
		        data: function (params) { return { q: params.term };},
		        processResults: function (data, page) {
	                 return {
	                     results: $.map(data, function (item) {
	                         return {
	                             text: item.name,
	                             id: item.id
	                         }
	                     })
		         	 };   
		       	},
		        cache: true
	        },
	        minimumInputLength: 3,
	        });
	   
	    
	    $(".search_gene_name").change(function() {
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