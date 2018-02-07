<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page"
	xmlns:spring="http://www.springframework.org/tags"
	xmlns:c="http://java.sun.com/jsp/jstl/core"
	xmlns:form="http://www.springframework.org/tags/form"
	xmlns:fn="http://java.sun.com/jsp/jstl/functions"
	xmlns:sec="http://www.springframework.org/security/tags" version="2.0">
	<jsp:directive.page language="java" contentType="text/html" />

	<c:set var="reqUrl">${pageContext.request.requestURL}</c:set>
	<c:set var="baseURL"
		value="${fn:replace(reqUrl, pageContext.request.requestURI,pageContext.request.contextPath)}" />


	<html>
<head>

<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta name="header_title" content="Search" />
<meta name="header_title_desc" content="Gene Products" />


<style type="text/css">
.dataTables_processing {
	background-color: red
}

tr.selected {
	background-color: grey
}

tr.selected:hover {
	background-color: grey
}

.verticalTableHeader {
	text-align: center;
	white-space: nowrap;
	transform-origin: 50% 50%;
	-webkit-transform: rotate(90deg);
	-moz-transform: rotate(90deg);
	-ms-transform: rotate(90deg);
	-o-transform: rotate(90deg);
	transform: rotate(90deg);
}

.verticalTableHeader:before {
	content: '';
	padding-top: 11% 0;
	/* takes width as reference, + 10% for faking some extra padding */
	display: inline-block;
	vertical-align: middle;
}
</style>

<title>Search</title>


<link href="${baseURL}/public/widgets/datatables/datatables.min.css"
	rel="stylesheet" type="text/css" />



<link rel="stylesheet"
	href="${baseURL}/public/widgets/jquery-ui-1.11.1/themes/smoothness/jquery-ui.css" />

<link href="${baseURL}/public/widgets/select2/css/select2.min.css"
	rel="stylesheet" />



<link
	href="${baseURL}/public/theme/css/datatables/dataTables.bootstrap.css"
	rel="stylesheet" type="text/css" />





</head>
<body>

	<script type="text/javascript">
		//<![CDATA[
	</script>

	<script
		src="${baseURL}/public/theme/js/plugins/datatables/jquery.dataTables.min.js"
		type="text/javascript"></script>
	<!-- jquery.dataTables.js -->

	<script
		src="${baseURL}/public/theme/js/plugins/datatables/dataTables.tableTools.js"
		type="text/javascript"></script>



	<script
		src="${baseURL}/public/theme/js/plugins/datatables/dataTables.bootstrap.js"
		type="text/javascript"></script>


	<script type="text/javascript"
		src="${baseURL}/public/theme/js/plugins/datatables/dataTables.buttons.min.js"></script>

	<script type="text/javascript"
		src="${baseURL}/public/theme/js/plugins/datatables/buttons.html5.min.js"></script>


	<script src="${baseURL}/public/widgets/select2/js/select2.min.js"></script>



	<script src="${baseURL}/public/bia/ui/search/FilterTable.js"></script>
	<script src="${baseURL}/public/bia/ui/search/ScoreTable.js"></script>
	<script src="${baseURL}/public/bia/ui/search/TableFilters.js"></script>
	<script src="${baseURL}/public/bia/ui/search/SearchDialog.js"></script>




	<script type="text/javascript">
		//http://stackoverflow.com/questions/13649459/twitter-bootstrap-multiple-modal-error/15856139#15856139
		$.fn.modal.Constructor.prototype.enforceFocus = function() {
		};
	
		
		var currentOrganism = '${organism}';		
		var all_ontologies = ${ontologies};
		var searchProps = ${searchProps};		
		var strainsData = ${strains};
		var strains = strainsData.map(x => x.name);
		var project = ${project}
		
		window.strainComp = []
		
		if (currentOrganism != "") {

			$("<a/>", {
				"href" : "${baseURL}/genome"
			}).html("Genomes").appendTo($("#base_breadcrumb"));
			var li = $("<li/>").appendTo($(".breadcrumb"));
			$("<a/>", {
				"href" : "${baseURL}/genome/" + currentOrganism
			}).html("<i>" + currentOrganism + "</i>").appendTo(li);
			li = $("<li/>").addClass("active").appendTo($(".breadcrumb"));
			$("<a/>", {
				"href" : "#"
			}).html(project.name).appendTo(li);

			
		}
	
		function selectProp(elem) {

			if ($(elem).hasClass('selected')) {
				$(elem).removeClass('selected');
			} else {				
				$(elem).addClass('selected');
			}
		}

		
	function init(data){
		var i =  0 ;
		strainsData.forEach(strain => {
			i = i + 1;
			var tr = $("<tr />").appendTo($("#strains_table"));
			$("<td />").appendTo(tr).html("#" + i.toString());
			$("<td />").appendTo(tr).html(strain.name);
			$("<td />").appendTo(tr).html(strain.description);
		});
		$("#project_title").html("Strains from project <b>" +  project.name + "</b>")
		$("#project_desc").html( project.description )
		
		searchProps = $.map(searchProps, function(x) {
			var param = new $.ScoreParam(x.name);
			param.description = x.description;
			param.type = x.type;
			param.target = x.target;

			param.defaultGroupOperation = x.defaultGroupOperation;
			param.defaultOperation = x.defaultOperation;
			param.defaultValue = x.defaultValue;

			param.value = x.value;
			param.uploader = x.uploader;
			if (param.value == undefined) {
				param.value = null
			}
			param.value = param.defaultValue;
			param.options = x.options;
			if (param.defaultOperation) {
				param.operation = param.defaultOperation;
			} else {
				if (x.type == "value") {

					param.operation = "equal";
				} else {
					param.operation = ">";
				}
			}
			return param;
		});
		
		$.filterTable = new $.FilterTable($('#search_filter_table'),
				null);
		$.filterTable.duplicate_button = false; 
		$.filterTable.canDrawDist = false;
		$.filterTable.onClick = function(prop_name){
			var prop = searchProps.filter(function(x){return x.name == prop_name})[0];			
			$("#myModal2Label").html(prop.name);			
			$("#distModal").modal()	;			
		}
		
		new $.PathwaysFilter(
				$("#pathways_autocomplete"), $.filterTable);
		new $.DruggabilityFilter(
				$('#select_struct'), $.filterTable);
		
		var ontologyFilter = new $.OntologyFilter($("#search_select2"),
				$("#organism_select"), $.filterTable);
		
		ontologyFilter.searchTable = { search_gene_prods: () => {} }
		
			var searchDialog = new $.SearchDialog($("#searchDialog"),
					$("#search_select2"), $("#search_txt"),
					$("#properties_table"), searchProps, $("#modal_ok_btn"),
					$("#btn_cancel_modal"), null, ontologyFilter,
					$.filterTable, null, $.api.url_search_ontologies(),
					currentOrganism);
			searchDialog.ontologies = all_ontologies;
			searchDialog.pathwaySelect = true;
			searchDialog.init()
			
			
			
			strains.forEach(x => {
			$('<option/>',{selected:"selected"}).html(x).appendTo( $('#selectCepas'));
			$("<option/>").html(x).appendTo( $('#selectCepa1'))
			$("<option/>").html(x).appendTo( $('#selectCepa2'))
			});
			
			
			$("#refresh_btn").click(function(){
				$("#search-table-body").empty()				
				search(strains)
			});
			
			if (data != null) {
				
				$("#selectVartype").val(data.variant_type)
				
				data.strainComp.forEach(x => {
					_addRestriction(x.strain1,x.strain2,x.operation)
				});
				
				$.each(data.filters, function(i, x) {
					$.filterTable.addParam(x);
				});
			}
			
			search(strains)	
		}
		
		

		function save_state() {
			var data = JSON.stringify({
				filters : window.query.proteinFilters,
				strainComp : window.query.strainComp,
				strains: window.query.strains,
				variant_type: window.query.variant_type
				});

			var urlPath = window.location.href;
			window.history.replaceState({
				"data" : data
			}, '', urlPath);
		}
		
function search(selectedStrains){
	
	window.query = {
			reference : currentOrganism ,
			strains: strains,
			variant_type:$("#selectVartype").val(),
			strainComp: window.strainComp,
			proteinFilters:$.filterTable.data(), 
								
			offset:0,
			pageNumber:0,
			pageSize:0,
	}		
	
	save_state()
	
			$.post("${baseURL}/variant/" + currentOrganism  + '/',JSON.stringify( window.query)).done( function( data ) {
				showHeader(selectedStrains)
				$("#pageResults").html('results ' + data.recordsFiltered.toString() + ' of ' + data.recordsTotal)
				  data.data.forEach(variant => {
					  var tr = $("<tr/>").appendTo($("#search-table-body"));
					  $("<td/>").html(variant.contig + ":" + variant.pos).appendTo(tr)					  
					  $("<a/>",{href: $.api.url_protein(variant.prot_ref) }).html(variant.gene).appendTo($("<td/>").appendTo(tr));
					  
					  $("<td/>").html( [].concat.apply([],variant.sample_alleles.map(x => x.variant_type )).join(", ").replace(new RegExp("_", 'g')," ") ).appendTo(tr)
					  var sa1 = null;
					  selectedStrains.forEach(x => {
						 
						 var strain_allele = [];
						  
						  selectedStrains.forEach(x => {
							  strain_allele = variant.sample_alleles.filter(sa => sa.samples.filter(s => s.sample == x).length > 0 );
								  if(strain_allele.length > 0){								  
									  sa1 = strain_allele[0];
									  
								  } 
						  });
					  })
					  if (sa1 != null){					  
						  if($("#selectVisualization").val() != "Protein" ){ 
							  var ref = variant.ref
							  if (ref.length > 1){
								  ref = '<b><span style="cursor:pointer" title="deletion: ' + ref + '" >Del</span><b>'
							  }
							  $("<td/>").html(ref).appendTo(tr);
							  
								  var strain_allele = [];						  
								  selectedStrains.forEach(x => {
									  strain_allele = variant.sample_alleles.filter(sa => sa.samples.filter(s => s.sample == x).length > 0 );	
									  
									  if(strain_allele.length > 0){
										  var td = $("<td/>").appendTo(tr)
										  var alt_txt = strain_allele[0].alt;
										  if (strain_allele[0].alt.length > 1){
											  alt_txt = '<b><span style="cursor:pointer" title="' + alt_txt+ '" >Ins</span></b>' 
										  }
										  $("<a/>",{href:"${baseURL}/genome/" + currentOrganism + "/strain/" + x + "?variantId=" + variant.id}).html(alt_txt).appendTo(td)
										  if (strain_allele[0].feature != null){
											  td.css("background-color","red").attr("title","reported: " + strain_allele[0].feature.identifier).css("cursor","pointer")
										  }
									  } else {
										  
										  $("<td/>").html(ref).appendTo(tr)
									  }
									  
									  
								  });
						  }
						  
						  if($("#selectVisualization").val() != "Nucleotide" ){ 
						  
							  if (sa1 != null){
								  $("<td/>").html(sa1.aa_pos).appendTo(tr)
								  $("<td/>").html(sa1.aa_ref).appendTo(tr)  
							  } else {
								  $("<td/>").html("").appendTo(tr)
								  $("<td/>").html("").appendTo(tr)
							  }					   
							  
							  
							  selectedStrains.forEach(x => {
								  strain_allele = variant.sample_alleles.filter(sa => sa.samples.filter(s => s.sample == x).length > 0 );	
								  
								  if(strain_allele.length > 0){
									  
									  var alt_txt = strain_allele[0].aa_alt;
									  if (strain_allele[0].aa_alt.length > 1){
										  alt_txt = '<b><span style="cursor:pointer" title="' + alt_txt+ '" >X</span></b>' 
									  }
									  
									  $("<td/>").html(alt_txt).appendTo(tr)
									  sa1 = strain_allele[0];
								  } else {
									  $("<td/>").html(sa1.aa_ref).appendTo(tr)
								  }
								  
								  
							  });
						  }
						  
						  if($("#selectVisualization").val() != "Nucleotide" ){ 
							  
							  
							  selectedStrains.forEach(x => {
								  allele = variant.sample_alleles.filter(sa => sa.samples.filter(s => s.sample == x).length > 0 );	
								  if(allele.length > 0){
									  var sampleAllele =  allele[0].samples.filter(s => s.sample == x)
									  if(sampleAllele.length > 0){
										  $("<td/>").html(sampleAllele[0].annotations["AD"]).appendTo(tr)									  
									  } else {
										  $("<td/>").appendTo(tr)
									  }
								  } else {
										  $("<td/>").appendTo(tr)
								  }
								  
							  });
						  }
						  $("<td/>").appendTo(tr);
						  
					  }	  
				  })
			}).fail(function(qXHR,  textStatus,  errorThrown){
				console.log(textStatus);
				console.log(errorThrown);
				alert("error al consultar las variantes")
			});
		
		}
		
		
		
		function showHeader(selectedStrains){
			
			var header=$("#search-header").css("cursor","pointer");
			var header2=$("#search-header2");
			header.empty()
			header2.empty()
			
			$('<th/>',{height:70,width:180}).html('Pos').appendTo(header)
			$('<th/>').html('').appendTo(header2)
			
			$('<th/>',{height:70,width:90}).html('Gene').appendTo(header)
			$('<th/>').html('').appendTo(header2)
			
			//$("<th/>",{title:"Reported in database"}).html("Reported").appendTo(header);
			//$('<th/>').html('').appendTo(header2)
			
			$('<th/>',{height:70,width:140}).html('Type').appendTo(header)
			$('<th/>').html('').appendTo(header2)
			
			if($("#selectVisualization").val() != "Protein" ){
				$('<th/>',{width:30,title:"Nucleotide Reference"}).html('Ref').appendTo(header)
				$('<th/>').html('').appendTo(header2)
				
				
				
				$('<th/>',{width:20 * selectedStrains.length,colspan:selectedStrains.length,title:"Nucleotide Alternative" }).html('Alt').appendTo(header).css("background-color","grey").css("text-align","center")
				var i = 0;
				selectedStrains.forEach(x => {
					i = i + 1
					$('<th/>',{width:20,title:x}).html( "#" + i.toString() ).appendTo(header2);
				});
			}
			
			if($("#selectVisualization").val() != "Nucleotide" ){
			$('<th/>',{title:"Aminoacid Position"}).html('PPos').appendTo(header).css("width","40px")
			$('<th/>').html('').appendTo(header2)
			
			$('<th/>',{width:30,title:"Amnoacid Reference"}).html('PRef').css("width","40px").appendTo(header)//.addClass("verticalTableHeader").css("padding-right",35).css("padding-bottom",10)
			$('<th/>').html('').appendTo(header2)			
			
			$('<th/>',{colspan:selectedStrains.length,width: 25 * selectedStrains.length,title:"Strain Aminoacid Alternative" }).html('PAlt').appendTo(header).css("background-color","grey").css("text-align","center")
			
			
			var i = 0;
			selectedStrains.forEach(x => {
				i = i + 1
				$('<th/>',{width:20,title:x}).html( "#" + i.toString() ).appendTo(header2);
			});
			}
			
			
			if($("#selectVisualization").val() != "Protein" ){
			
				
				$('<th/>',{colspan:selectedStrains.length,width: 50 * selectedStrains.length,title:"Allele Depth" }).html('Allele Depth')
				.appendTo(header).css("background-color","black").css("color","white").css("text-align","center")
				
				var i = 0;
				selectedStrains.forEach(x => {
					i = i + 1
					$('<th/>',{width:20,title:x}).html( "#" + i.toString() ).appendTo(header2);
				});
				}
			
			$('<th/>').html('').appendTo(header2)
			$('<th/>').html('').appendTo(header)
				
		}
		
		function addRestriction() {
			_addRestriction($('#selectCepa1').val(),$('#selectCepa2').val(),$('#operacionCepa').val())
			
		}
	function _addRestriction(strain1,strain2,operation) {
			
			var tr = $("<tr/>").appendTo($('#restrictionTable'));
			var strainComp = {"strain1":strain1,"strain2":strain2,"operation":operation}
			window.strainComp.push( strainComp )
			tr.data(strainComp)
			$("<td>").html( $('#selectCepa1').val()).appendTo(tr)
			$("<td>").html( $('#operacionCepa').val()).appendTo(tr)
			$("<td>").html( $('#selectCepa2').val()).appendTo(tr)
			$("<td>").html(  "Delete"  ).appendTo(tr).css("background-color","red").click(function() {
				window.strainComp = window.strainComp.filter( x => {
					return !((x.strain1 == strainComp.strain1) && (x.strain2 == strainComp.strain2) && (x.operation == strainComp.operation) ); 
				});
				tr.remove();
			});
		}
		
		
		
		$(document).ready(
				function() {					
					var data = null;
					
					if ((window.history.state != null)
							&& (window.history.state.data != null)) {
						

						var objToParam = function(x) {
							var sp = new $.ScoreParam(x.name)
							sp.name = x.name;
							sp.description = x.description;
							sp.coefficient = x.coefficient;
							sp.value = x.value;
							sp.operation = x.operation;
							sp.type = x.type;
							sp.options = x.options;
							sp.uploader = x.uploader;
	
							return sp;
						}
					
						data = JSON.parse(window.history.state.data)

						data.filters = $.map(data.filters, objToParam);
												
					}							
			
					init(data)

				});
	</script>

	<script type="text/javascript">
		// ]]>
	</script>

	<div class="row">



		<section style="min-width: 300px" class="col-xs-6">


			<div class="box box-primary">
				<div class="box-header">
					<h3 class="box-title">Filter</h3>

				</div>
				<div class="box-body">
					<a data-id="filter_activity" class="btn btn-app open-modal"> <i
						class="fa  fa-cog">&#160;</i>Activity
					</a> <a data-id="filter_process" class="btn btn-app open-modal"> <i
						class="fa  fa-gears">&#160;</i>Biological Process
					</a> <a data-id="filter_localization" class="btn btn-app open-modal">
						<i class="fa  fa-circle-o">&#160;</i>Localization
					</a> <a data-id="filter_pathways" class="btn btn-app open-modal"> <i
						class="fa fa-random">&#160;</i>Pathways
					</a> <a data-id="filter_variant-db" class="btn btn-app open-modal">
						<i class="fa fa-barcode">&#160;</i>Variant Position
					</a> <a data-id="filter_metadata" class="btn btn-app open-modal"> <i
						class="fa fa-tags">&#160;</i>Metadata
					</a> <a id="download_btn" class="btn btn-app"> <i
						class="fa fa-download">&#160;</i>Download list
					</a>


					<table id="search_filter_table" class="table">
						<thead>
							<tr>
								<td width="10px"></td>
								<td width="30px">Name</td>
								<td width="80px">Description</td>
								<td width="100px">Operation</td>
								<td width="100px">Value</td>
								<td></td>

							</tr>
						</thead>
						<tbody></tbody>
					</table>
				</div>
			</div>
			
			<div class="modal fade" id="searchDialog" tabindex="-1" role="dialog"
			aria-labelledby="#searchDialogLabel">
			<div class="modal-dialog" role="document">
				<div class="modal-content">
					<div class="modal-header">
						<button type="button" class="close" data-dismiss="modal"
							aria-label="Close">
							<span aria-hidden="true"> &amp;times </span>
						</button>
						<h4 class="modal-title"></h4>
					</div>
					<div class="modal-body">

						<div class="input-group" style="width: 100%">

							<span class="input-group-addon" id="keyword_search_icon"><i
								class="fa fa-search">&#160;</i></span> <input type="text"
								placeholder="free text search" class="form-control"
								id="search_txt" />
						</div>

						<div id="pw_title">&#160;</div>
						<div class="input-group" style="width: 100%">

							<span class="input-group-addon" id="keyword_search_icon"><i
								class="fa fa-search">&#160;</i></span> <select type="search"
								class="form-control" id="search_select2" style="width: 99%"><option></option>
							</select>
						</div>
						<br /> <br />
						<div class="input-group" style="width: 100%">
							<table id="properties_table" class="table">
								<thead>
									<tr>
										<td></td>
										<td>Name</td>
										<td>Description</td>
										<td>Type</td>
									</tr>
								</thead>
								<tbody></tbody>
							</table>
						</div>
					</div>
					<div class="modal-footer">
						<button id="modal_ok_btn" type="button" class="btn btn-success">OK</button>
						<button type="button" data-dismiss="modal" id="btn_cancel_modal"
							class="btn btn-danger">Cancel</button>

					</div>
				</div>
			</div>
		</div>
			
			
		</section>



		
		<section class="col-lg-6">
			<div class="box">
			<div class="box-header">
					<h3 id="project_title" class="box-title"></h3>
<p id="project_desc"></p>
				</div>
				<div class="box-body">
				<table class="table table-striped" id="strains_table">
				<tbody></tbody>
				</table>
				</div>
			</div>
		</section>


		<section class="col-lg-6">
			<div class="box">
				<div class="box-body">




					<table id="restrictionTable">
						<tr>
							<td><select width="100%" id="selectCepa1">-
							</select></td>
							<td><select width="100%" id="operacionCepa">
									<option>equal</option>
									<option>different</option>
							</select></td>
							<td><select width="100%" id="selectCepa2">-
							</select></td>
							<td><button onclick="addRestriction()">Add
									Restriction</button></td>
						</tr>

					</table>



					<div>
						Type <select multiple="multiple" id="selectVartype">

							<option selected="selected">missense_variant</option>
							<option>synonymous_variant</option>
							<option>All</option>

						</select>
					</div>
					<div>
						Show <select id="selectVisualization">selectVisualization
							<option>All</option>
							<option>Protein</option>
							<option>Nucleotide</option>



						</select>
					</div>
					<a id="refresh_btn" class="btn btn-app"> <i
						class="fa fa-repeat">&#160;</i>Refresh
					</a>
				</div>
			</div>
		</section>

	</div>
	<div class="row">

		<section class="col-lg-12 ">
			<div class="box">
				<div class="box-body">
					<p id="pageResults"></p>
					<table id="search-table" class="table table-striped"
						style="table-layout: fixed;">
						<thead>

							<tr id="search-header">

							</tr>
							<tr id="search-header2">

							</tr>
						</thead>
						<tbody id="search-table-body">

						</tbody>
						<tfoot></tfoot>
					</table>
				</div>

			</div>

		</section>
	</div>



	<div class="modal fade" id="searchDialog" tabindex="-1" role="dialog"
		aria-labelledby="#searchDialogLabel">
		<div class="modal-dialog" role="document">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						aria-label="Close">
						<span aria-hidden="true"> &amp;times </span>
					</button>
					<h4 class="modal-title"></h4>
				</div>
				<div class="modal-body">

					<div class="input-group" style="width: 100%">

						<span class="input-group-addon" id="keyword_search_icon"><i
							class="fa fa-search">&#160;</i></span> <input type="text"
							placeholder="free text search" class="form-control"
							id="search_txt" />
					</div>

					<div id="pw_title">&#160;</div>
					<div class="input-group" style="width: 100%">

						<span class="input-group-addon" id="keyword_search_icon"><i
							class="fa fa-search">&#160;</i></span> <select type="search"
							class="form-control" id="search_select2" style="width: 99%"><option></option>
						</select>
					</div>
					<br /> <br />
					<div class="input-group" style="width: 100%">
						<table id="properties_table" class="table">
							<thead>
								<tr>
									<td></td>
									<td>Name</td>
									<td>Description</td>
									<td>Type</td>
								</tr>
							</thead>
							<tbody></tbody>
						</table>
					</div>
				</div>
				<div class="modal-footer">
					<button id="modal_ok_btn" type="button" class="btn btn-success">OK</button>
					<button type="button" data-dismiss="modal" id="btn_cancel_modal"
						class="btn btn-danger">Cancel</button>

				</div>
			</div>
		</div>
	</div>


</body>
	</html>

</jsp:root>