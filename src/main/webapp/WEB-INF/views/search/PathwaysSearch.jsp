<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page"
	xmlns:spring="http://www.springframework.org/tags"
	xmlns:c="http://java.sun.com/jsp/jstl/core"
	xmlns:form="http://www.springframework.org/tags/form"
	xmlns:fn="http://java.sun.com/jsp/jstl/functions"
	xmlns:sec="http://www.springframework.org/security/tags" version="2.0">
		
	
	<jsp:directive.page language="java" contentType="text/html" />

	
	
	<c:set var="reqUrl">${pageContext.request.requestURL}</c:set>
	<c:set var="baseURL" value="${fn:replace(reqUrl, pageContext.request.requestURI,pageContext.request.contextPath)}" />



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
</style>

<title>Search</title>

<!-- DATA TABLES -->





<link href="${baseURL}/public/widgets/datatables/datatables.min.css"
	rel="stylesheet" type="text/css" />


<link rel="stylesheet" type="text/css"
	href="${baseURL}/public/theme/css/datatables/dataTables.colVis.css" />

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


	<script src="${baseURL}/public/widgets/jquery-ui-1.11.1/jquery-ui.js"></script>
	<script src="${baseURL}/public/widgets/jstree/jstree.js"></script>

	<!-- DATA TABES SCRIPT -->

	<script
		src="${baseURL}/public/theme/js/plugins/datatables/jquery.dataTables.min.js"
		type="text/javascript"></script>
<script type="text/javascript"
		src="${baseURL}/public/theme/js/plugins/datatables/dataTables.buttons.min.js"></script>

	<script type="text/javascript"
		src="${baseURL}/public/theme/js/plugins/datatables/buttons.html5.min.js"></script>
	<script
		src="${baseURL}/public/theme/js/plugins/datatables/dataTables.tableTools.js"
		type="text/javascript"></script>

	<script
		src="${baseURL}/public/theme/js/plugins/datatables/dataTables.colVis.js"
		type="text/javascript"></script>




	<script
		src="${baseURL}/public/theme/js/plugins/datatables/dataTables.bootstrap.js"
		type="text/javascript"></script>


	<script src="${baseURL}/public/widgets/select2/js/select2.min.js"></script>
<script src="${baseURL}/public/widgets/chartjs/Chart.min.js"
		type="text/javascript"></script>


	<!-- Bia JS -->

	<script src="${baseURL}/public/bia/ui/search/FilterTable.js"></script>
	<script src="${baseURL}/public/bia/ui/search/ScoreTable.js"></script>
	<script src="${baseURL}/public/bia/ui/search/SearchPWDialog.js"></script>

<script
		src="${baseURL}/public/docs/tours.js?v=5"
		type="text/javascript"></script>


	<script type="text/javascript">
		//http://stackoverflow.com/questions/13649459/twitter-bootstrap-multiple-modal-error/15856139#15856139
		$.fn.modal.Constructor.prototype.enforceFocus = function() {
		};

		var genome = ${genome};
		var ontologies = ${ontologies};
		var searchProps = ${searchProps};
		var all_ontologies = ${ontologies};
		var statistics = ${statistics};

		function save_state() {
			var data = JSON.stringify({
				scores : $.scoreTable.data(),
				filter : $.filterTable.data()
			});
			//var urlPath = $.api.url_search_genome_keyword(currentOrganism, '');
			window.history.replaceState({
				"data" : data
			}, '', window.location.href);
		}

		function selectProp(elem) {

			if ($(elem).hasClass('selected')) {
				$(elem).removeClass('selected');
			} else {				
				$(elem).addClass('selected');
			}
		}

			function renderPwPropLine(pwProp) {
				if (pwProp.result == undefined){					
					return
				} 
				var line = '';
				if (pwProp.result != 0){
					line += '<span style="color:green">'
				} else {
					line += '<span  style="color:red">'
				}
				line += pwProp.name + " = " + 
				//pwProp.filtered + "/" + pwProp.total + 
				" " +   pwProp.result.toFixed(2).toString() +  " " + ((pwProp.operation != "") ? "(" + pwProp.operation + ")" : "")
				
				return line + '</span>'
				
			}
		var init = function(data) {
			
			var currentUrl = window.location.href.split($.api.url)[1]
			searchProps = $.map(searchProps, function(x) {
				var param = new $.ScoreParam(x.name);
				param.description = x.description;
				param.type = x.type;
				param.target = x.target;
				param.value = x.value
				param.uploader = x.uploader
				
				param.groupoperation = "";
				
				
				
				param.defaultGroupOperation = x.defaultGroupOperation;
				if(param.target != "pathway"){
					if(x.defaultGroupOperation){
						param.groupoperation = x.defaultGroupOperation
					} else {
						param.groupoperation = "max";	
					}
						
				}
				
				if (param.value == undefined) {
					param.value = null
				}
				param.options = x.options;
				if (x.type == "value") {
					param.operation = "equal";
				} else {
					param.operation = ">";
				}
				
				
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

			var search_table_columns = [
			         {
			        	 data:null,
			        	 render : function(d,t,r){
			        		 
			        		 return ""
			        	 } 
			         },
					{
						data : "term",
						"render" : function(data, type, row) {

							return '<a href="'
									+ hrefOntologyLink("biocyc", data)
									+ '"><small title="click to go to Biocyc" class="badge bg-yellow">?</small></a> '
									+ '<a href="'
									+ $.api.url_genome_pathway(
											genome.name, data)
									+ '">'
									+ '<i tile="get protein list" class="fa fa-filter">&#160;</i>'
									+ data + '</a>'
						}
					},

					{
						data : "name"
					},
					{
						data : "properties2",
						"render" : function(pathwayData, type, row) {	
							return '<p align="center">'  +  pathwayData["reactions"] + '</p>';
						}
					},
					{
						data : "properties2",
							"render" : function(pathwayData, type, row) {	
								return '<p align="center">'  + pathwayData["reactions_with_gene"] + '</p>';
							}
					},
					{
						data : "count",
							"render" : function(pathwayData, type, row) {	
								return '<p align="center">'  + pathwayData + '</p>';
							}
					},
					{
						"name" : "properties",
						"title" : "Properties",
						"data" : "properties",
						"defaultContent" : "-",
						"orderable" : false,

						"render" : function(pathwayData, type, row) {			
							if( pathwayData == undefined) return "";
							return $.map(pathwayData,
									renderPwPropLine  ).join(", ");
						}
					},{
						data : "score",
						"render":function(value){
							return '<p align="center">' + value.toFixed(2)  + '</p>';
						}
					} ]
			$.pathwaysTable = $('#search-table').DataTable({
				//data: genome.pathways,
				"ajax" :  {"url":"${baseURL}/search/" + genome.name + "/pathways/", "type": "POST"   }, 
				"lengthMenu" : [ [ -1 ], [ "All" ] ],
				"processing" : true,
				"serverSide" : true,
				"ordering" : false,				
				dom : 'Birt',
				buttons: [
				          {
				               extend: 'csvHtml5',
				               text: 'Export to CSV'}
				      ],
				"deferLoading" : 0, //Para que no haga la carga inicial
				columns : search_table_columns,
				fnCreatedRow: function (row, data, index) {
		            $('td', row).eq(0).html(index + 1);
		        }/*
				 rowCallback: function( row, data, iDisplayIndex ) {
				     var info = this.api.page.info();
				     var page = info.page;
				     var length = info.length;
				     var index = (page * length + (iDisplayIndex +1));
				     $('td:eq(0)', row).html(index);
				  }*/
			});
		
			
			$.pathwaysTable.on('xhr.dt', function ( e, settings, data ) {		
			      console.log("a")
			    } )
			    $.pathwaysTable.on('preXhr.dt', function ( e, settings, data ) {
			    	console.log("b")
			    } )

			
			$.filterTable = new $.FilterTable($('#search_filter_table'),
					$.scoreTable);
			$.filterTable.duplicate_button = false;
			$.scoreTable = new $.ScoreTable($('#search_params_table'));
			$.scoreTable.group = true;

			
			/***/
			
			
			window.myNewChart = null;
			$('#distModal').on('shown.bs.modal', function (event) {
				if (window.myNewChart != null){
					myNewChart.destroy();
				}
								
				var stats = statistics.filter(function(x){return x.name == ("dp_" + $("#myModal2Label").html() ) })[0];
				var prop_name = $("#myModal2Label").html().replace("_", " ");
				prop_name = prop_name[0].toUpperCase() + prop_name.slice(1);
				
				var values = stats.values.slice(1);
				var labels = stats.labels.slice(1);
				var ylabel = '# Proteins';
				
				var prop = searchProps.filter(function(x){return x.name == $("#myModal2Label").html()})[0]
				if(prop.target == "pathway"){
					 
					 ylabel = '# Pathways';
				
				} else {
					$("#myModal2Label").html(prop_name + " (" +  stats.values[0].toString()  + " unannotated)" )
				}
				
				
				
				var data = {
						labels : labels ,
						datasets : [ {
							//label : "# Proteins",
							fillColor : "rgba(220,220,220,0.2)",
							strokeColor : "rgba(220,220,220,1)",
							pointColor : "rgba(220,220,220,1)",
							pointStrokeColor : "#fff",
							pointHighlightFill : "#fff",
							pointHighlightStroke : "rgba(220,220,220,1)",
							data : values
						} ]
						};
						
						$("#canvas_chart").css("width",
								$("#canvas_chart").parent().width())
						
						var ctx = document.getElementById("canvas_chart")
								.getContext("2d");
						config = {
								type:'bar',data:data, 
								options:{
									
									legend: {
								        display: false
								    },
									scales: {
								
									 
		                    xAxes: [{
		                        display: true,
		                        scaleLabel: {
		                            display: true,
		                            labelString: prop_name
		                        }
		                    }],
		                    yAxes: [{
		                        display: true,
		                        scaleLabel: {
		                            display: true,
		                            labelString: ylabel
		                        }
		                    }]}}}
						window.myNewChart = new Chart(ctx,config);
				
			});
			
			$.filterTable.onClick = function(prop_name){
				var prop = searchProps.filter(function(x){return x.name == prop_name})[0]
				
				$("#myModal2Label").html(prop.name)
				
				$("#distModal").modal()
				
				
			}
			$.scoreTable.onClick = function(prop_name){
				var prop = searchProps.filter(function(x){return x.name == prop_name})[0]
				
				$("#myModal2Label").html(prop.name)
				
				$("#distModal").modal()
				
				
			}
			
			
			/***/
			
			$("<a/>", {
				"href" : "${baseURL}/genome"
			}).html("Genomes").appendTo($("#base_breadcrumb"));
			var li = $("<li/>").appendTo($(".breadcrumb"));
			$("<a/>", {
				"href" : "${baseURL}/genome/" + genome.name
			}).html("<i>" + genome.name + "</i>").appendTo(li);
			li = $("<li/>").addClass("active").appendTo($(".breadcrumb"));
			$("<a/>", {
				"href" : "#"
			}).html("Search Pathways").appendTo(li);

			var searchDialog = new $.SearchPWDialog($("#searchDialog"),
					$("#search_select2"), $("#properties_table"), searchProps,
					$("#modal_ok_btn"), $("#btn_cancel_modal"),
					$.filterTable, $.scoreTable,
					$.api.url_search_ontologies(), genome.name);
			searchDialog.ontologies = all_ontologies;
			searchDialog.init()

			/* $("#search_params_table").hide();
			$("#score_div_h3").hide();
			 */

			if (data != null) {

				$.each(data.scores, function(i, x) {
					$.scoreTable.addParam(x);
				});
				$.each(data.filter, function(i, x) {
					$.filterTable.addParam(x);
				});

			}

			searchDialog.refreshTables(function() {
				//checkEmptyTables();
				//$.search_table.search_gene_prods();

				score_pathways()

			});

			$.scoreTable.onDelete = score_pathways_wait;
			$.scoreTable.onAdd = score_pathways_wait;
			$.scoreTable.onChange = score_pathways_wait;
			$.filterTable.onDelete = score_pathways_wait;
			$.filterTable.onAdd = score_pathways_wait;
			$.filterTable.onChange = score_pathways_wait;

			loadTutorial('${baseURL}');
		}
		
		var score_wait = null;
		function score_pathways_wait(){
			if(score_wait != null){
				clearTimeout(score_wait);
			} 
			score_wait = setTimeout(score_pathways, 2000);;
		}
		
		function score_pathways() {

			var data = JSON.stringify({
				filters : $.filterTable.data(),
				scores : $.scoreTable.data()
			});
			//var table = this.divElement.DataTable();
			$.pathwaysTable.search(data).draw();
			save_state()

		}

		$(document).ready(
				function() {
					var objToParam = function(x) {
						var sp = new $.ScoreParam(x.name)
						
						sp.name = x.name;
						sp.description = x.description;
						sp.coefficient = x.coefficient;
						sp.value = x.value;
						sp.target = x.target;
						sp.operation = x.operation;
						sp.type = x.type;
						sp.options = x.options;
						sp.uploader = x.uploader;
						sp.groupoperation = x.groupoperation;
						
						sp.defaultGroupOperation = x.defaultGroupOperation;
						sp.defaultOperation = x.defaultOperation;
						sp.defaultValue = x.defaultValue;
						
						

						return sp;
					}
					var data = null;
					if ((window.history.state != null)
							&& (window.history.state.data != null)) {
						data = JSON.parse(window.history.state.data)

						data.filter = $.map(data.filter, objToParam);						
						data.scores = $.map(data.scores, objToParam);
					}
					init(data)

				});
	</script>

	<script type="text/javascript">
		// ]]>
	</script>

	<div class="row">
<section class="col-xs-6">

			<div id="filterBox" class="box box-primary">

				
			<div class="box-header">
					<h3 class="box-title">Filter</h3>
					
				</div>
				<div class="box-body">
<p>Removes the pathways that do not fulfill ALL the conditions</p>

					<a id="filter_pathway"  data-id="filter_pathway" class="btn btn-app open-modal"> <i
						class="fa fa-random">&#160;</i>Pathways
					</a> 
					<table id="search_filter_table" class="table">
						<thead>
							<tr>
								<td width="10px"></td>
								<td>Name</td>
								<td>Description</td>
								<td>Operation</td>
								<td>Value</td>
								

							</tr>
						</thead>
						<tbody></tbody>
					</table>

					

				</div>
			</div>
		</section>



		<section class="col-xs-6">

			<div  id="scoreBox"  class="box box-primary">

				
				<div class="box-header">
					<h3 class="box-title">Score</h3>					
				</div>
				<div class="box-body"> 
				<p>Sorts all / the filtered proteins by calculating a numeric value o score. Score formula is a weighted linear sum of the protein properties.</p>

					<a data-id="score_activity" class="btn btn-app open-modal"> <i
						class="fa  fa-cog">&#160;</i>Activity
					</a> <a data-id="score_process" class="btn btn-app open-modal"> <i
						class="fa  fa-gears">&#160;</i>Biological Process
					</a> <a data-id="score_localization" class="btn btn-app open-modal">
						<i class="fa  fa-circle-o">&#160;</i>Localization
					</a> <a id="score_pathway" data-id="score_pathway" class="btn btn-app open-modal"> <i
						class="fa fa-random">&#160;</i>Pathways
					</a> <a data-id="score_structure" class="btn btn-app open-modal"> <i
						class="fa fa-sitemap">&#160;</i>Structure
					</a> <a data-id="score_pocket" class="btn btn-app open-modal"> <i
						class="fa  fa-puzzle-piece">&#160;</i>Pocket
					</a> <a id="score_metadata" data-id="score_metadata" class="btn btn-app open-modal"> <i
						class="fa fa-tags">&#160;</i>Metadata
					</a> 
					<!-- <a data-id="score_uploaded" class="btn btn-app open-modal"> <i
						class="fa fa-book">&#160;</i>Uploaded Properties
					 </a>--> 
					<a class="btn btn-app open-modal"
						href="${baseURL}/genome/${organism}?isUpload=true"> <i
						class="fa fa-plus-circle">&#160;</i>Add new Properties
					</a>

					<table id="search_params_table" class="table">
						<thead>
							<tr>
								<td width="10px"></td>
								<td>Name</td>
								<td>Description</td>
								<td>Coefficient</td>
								<td><small id="groupHelp" class="badge bg-green">?</small>Group</td>
								<td></td>
								<td>Norm.</td>
							</tr>
						</thead>
						<tbody></tbody>
					</table>

					<h3 id="score_div_h3">
						<div id="score_div"></div>
					</h3>

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



	<div class="row">

		<section class="col-lg-12 ">
			<div class="box">
				<div class="box-body">

					<table id="search-table" class="table table-striped">
						<thead>

							<tr>
								<th width="1px">-</th>
								<th  width="100px">Term</th>
								<th style="width:150px;padding-left: 5px; padding-right: 5px;">Name</th>
								<th  style="width:40px;padding-left: 5px; padding-right: 5px;">Reactions</th>
								<th style="width: 40px; word-wrap: break-word;;padding-left: 5px; padding-right: 5px;">Reactions with gene</th>
								<th style="width:30px;padding-left: 5px; padding-right: 5px;">Genes</th>
								<th>Properties</th>
								<th width="40px">Score</th>
							</tr>
						</thead>
						<tbody>

						</tbody>
						<tfoot></tfoot>
					</table>
				</div>
				<div style="display: none;" class="overlay">&#160;</div>
				<div style="display: none;" class="loading-img">&#160;</div>
			</div>

		</section>
	</div>
	
		<div class="modal fade" id="distModal" tabindex="-1" role="dialog"
		aria-labelledby="myModalLabel">
		<div class="modal-dialog" role="document">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						aria-label="Close">
						<span aria-hidden="true">&#215;</span>
					</button>
					<h4 class="modal-title" id="myModal2Label">-</h4>
				</div>
				<div style="height: 500px;" class="modal-body">

					<canvas style="height: 400px;" id="canvas_chart"></canvas>


				</div>

			</div>
		</div>
	</div>
	

<script type="text/javascript">
$('#groupHelp').attr("title",'<spring:message code="pathwayssearch.grouphelp" />');
</script>


</body>
	</html>

</jsp:root>