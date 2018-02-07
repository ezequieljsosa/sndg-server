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
<meta name="header_title" content="Buscar" />



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
.tour-tour.fade.in{
    opacity: 1;
}
</style>

<title>Search</title>


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

	<script src="${baseURL}/public/widgets/chartjs/Chart.min.js"
		type="text/javascript"></script>



	<script src="${baseURL}/public/widgets/jquery-ui-1.11.1/jquery-ui.js"></script>


		
	</script>


<script
		src="${baseURL}/public/docs/tours.js?v=5"
		type="text/javascript"></script>



	<!-- DATA TABES SCRIPT -->

	<script
		src="${baseURL}/public/theme/js/plugins/datatables/jquery.dataTables.min.js"
		type="text/javascript"></script>
	<!-- jquery.dataTables.js -->

	<script
		src="${baseURL}/public/theme/js/plugins/datatables/dataTables.tableTools.js"
		type="text/javascript"></script>

	<script
		src="${baseURL}/public/theme/js/plugins/datatables/dataTables.colVis.js"
		type="text/javascript"></script>

	<script
		src="${baseURL}/public/theme/js/plugins/datatables/dataTables.bootstrap.js"
		type="text/javascript"></script>


	<script type="text/javascript"
		src="${baseURL}/public/theme/js/plugins/datatables/dataTables.buttons.min.js"></script>

	<script type="text/javascript"
		src="${baseURL}/public/theme/js/plugins/datatables/buttons.html5.min.js"></script>


	<script src="${baseURL}/public/widgets/select2/js/select2.min.js"></script>


	<!-- Bia JS -->
	<script src="${baseURL}/public/bia/ui/search/SearchTable.js"></script>
	<script src="${baseURL}/public/bia/ui/search/FilterTable.js"></script>
	<script src="${baseURL}/public/bia/ui/search/ScoreTable.js"></script>
	<script src="${baseURL}/public/bia/ui/search/TableFilters.js"></script>
	<script src="${baseURL}/public/bia/ui/search/SearchDialog.js"></script>



	<script type="text/javascript">
		//http://stackoverflow.com/questions/13649459/twitter-bootstrap-multiple-modal-error/15856139#15856139
		$.fn.modal.Constructor.prototype.enforceFocus = function() {
		};

		var currentOrganism = '${organism}';
		var currentKeywords = '${keywords}';
		var all_ontologies = ${ontologies};
		var currentGene = '${gene}';
		var organisms = '${organisms}'.split(",");
		var searchProps = ${searchProps};
		var statistics = ${statistics};

		function save_state() {
			var data = JSON.stringify({
				filters : $.filterTable.data(),
				scores : $.scoreTable.data()
			});

			var urlPath = $.api.url_search_genome_keyword(currentOrganism, '');
			window.history.replaceState({
				"data" : data
			}, '', urlPath);
		}

		function checkEmptyTables() {

			if ($.filterTable.data().length > 0) {

				$("#search_filter_table").show();

			} else {
				$("#search_filter_table").hide();

			}
			if ($.scoreTable.data().length > 0) {

				$("#search_params_table").show();
				$("#score_div_h3").show();

			} else {
				$("#score_div_h3").hide();
				$("#search_params_table").hide();

			}

		}
		
		function updateWarning(){
			
			
			if(($.scoreTable.data().length > 0) 
			&& ( (window.recordsFiltered == undefined )		
			|| (window.recordsFiltered > 4100)
					)
			){
				if($.filterTable.data().length > 0){
					$("#sizeAdvice").show()
					$("#sizeWarning").hide()
				} else {
					$("#sizeWarning").show()
					$("#sizeAdvice").hide()
				}
				
			} else {
				$("#sizeWarning").hide()
				$("#sizeAdvice").hide()
				
			}
		}
		
		
		function changeRefreshButton() {
			checkEmptyTables();
			downloadBtnEnblabled()
			updateWarning()
			$("<span/>").addClass("badge").addClass("bg-red").html("click!")
					.appendTo($("#refresh_btn"));
			save_state();
		}

		function changeRefreshButton2(data, evt) {
			if ((evt != undefined) && (evt.keyCode != 13)) {
				changeRefreshButton();
			} else {
				downloadBtnEnblabled()
				$.search_table.search_gene_prods();
				save_state();
			}

		}

		function setDisabledButtons(disabled) {
			$("#refresh_btn").attr("disabled", disabled);
			$(".open-modal").attr("disabled", disabled);

			$("#filter_help_button").attr("disabled", disabled);
			$("#search_txt").attr("disabled", disabled);
			$("#search_select2").attr("disabled", disabled);
			$("#filter_add_btn").attr("disabled", disabled);
			$("#score_add_btn").attr("disabled", disabled);
			$("#duplicateToScoreBtn").attr("disabled", disabled);

			$(".block_on_search").attr("disabled", disabled);
			if (disabled) {
				$(".block_on_search").addClass("disabled");
			} else {
				$(".block_on_search").removeClass("disabled");
			}

		}

		function selectProp(elem) {

			if ($(elem).hasClass('selected')) {
				$(elem).removeClass('selected');
			} else {
				//$("#properties_table").find('tr.selected').removeClass('selected');
				$(elem).addClass('selected');
			}
		}

		var init = function(data) {

			var currentUrl = window.location.href.split($.api.url)[1]
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

			$.each(organisms, function(index, organism) {
				$("#organism_select").append(
						'<option value="' + organism.split("|")[0] + '">'
								+ organism.split("|")[1] + '</option>');
			});

			$.search_table = new $.SearchTable($('#search-table'), $.api
					.url_search(), $.api);

			$.search_table.startSearch = function() {

				$("#refresh_btn").find(".badge").remove();
				setDisabledButtons(true);
				$(".loading-img").show();
				$(".overlay").show();

			}

			$.search_table.endSearch = function(data) {
				setDisabledButtons(false);
				$(".loading-img").hide();
				$(".overlay").hide();
				if(data != null){
					window.recordsFiltered = data.recordsFiltered	
				}				
				updateWarning()
				
			}

			$("#refresh_btn").click(function(evt) {

				$("#refresh_btn").find(".badge").remove();
				$.search_table.search_gene_prods();
			});

			$.scoreTable = new $.ScoreTable($('#search_params_table'));

			$.filterTable = new $.FilterTable($('#search_filter_table'),
					$.scoreTable);
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
			
			var organism_filter = new $.OrganismFilter($('#select_struct'),
					$.filterTable);
			$.search_table.addFilter(organism_filter);
			var keywordFilter = new $.FreeTextFilter($("#search_txt"),
					$.filterTable);
			$.search_table.addFilter(keywordFilter);
			/*$.search_table.addFilter(new $.DruggabilityFilter(
					$('#select_struct'), $.filterTable));
			$.search_table.addFilter(new $.PathwaysFilter(
					$("#pathways_autocomplete"), $.filterTable));*/
			var gene_filter = new $.GeneFilter($('#gene_filter'), $.filterTable)
			$.search_table.addFilter(gene_filter);
			$.search_table
					.addFilter(new $.DescriptionFilter($("#desc_filter")));
			var ontologyFilter = new $.OntologyFilter($("#search_select2"),
					$("#organism_select"), $.filterTable);
			$.search_table.addFilter(ontologyFilter);

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
				}).html("Search").appendTo(li);

				organism_filter.start_filtering_by(currentOrganism)
			}
			// On finish

			var searchDialog = new $.SearchDialog($("#searchDialog"),
					$("#search_select2"), $("#search_txt"),
					$("#properties_table"), searchProps, $("#modal_ok_btn"),
					$("#btn_cancel_modal"), keywordFilter, ontologyFilter,
					$.filterTable, $.scoreTable, $.api.url_search_ontologies(),
					currentOrganism);
			searchDialog.ontologies = all_ontologies;
			searchDialog.init()
			window.searchDialog = searchDialog;

			$("#search_filter_table").hide();
			$("#search_params_table").hide();
			$("#score_div_h3").hide();

			if (currentKeywords != "") {
				keywordFilter.start_filtering_by(currentKeywords);
				checkEmptyTables();
			}
			if (currentGene != "") {
				gene_filter.start_filtering(currentGene)
				checkEmptyTables();
			}

			$.search_table.init({}, function() {
			});

			if (data != null) {

				$.each(data.filters, function(i, x) {
					$.filterTable.addParam(x);
				});
				$.each(data.scores, function(i, x) {
					$.scoreTable.addParam(x);
				});

			}

			searchDialog.refreshTables(function() {
				checkEmptyTables();
				downloadBtnEnblabled()
				$.search_table.search_gene_prods();
			});

			$.scoreTable.onDelete = changeRefreshButton;
			$.scoreTable.onAdd = changeRefreshButton;
			$.scoreTable.onChange = changeRefreshButton;
			$.filterTable.onDelete = changeRefreshButton;
			$.filterTable.onAdd = changeRefreshButton;
			$.filterTable.onChange = changeRefreshButton2;

			$("#duplicateToScoreBtn").click(function(evt) {
				$.filterTable.duplicateAllToScore()
			});

			/* $("#search_select2").change(function(e) {
				$('#ontologyDialog').modal('hide');
			}); */
			updateWarning();			
			loadTutorial('${baseURL}');	
			
		}

		function downloadBtnEnblabled() {
			var enabled = $.scoreTable.data().length > 0
			$("#download_btn").attr("disabled", !enabled);
			/*$("#download_btn").attr("href", "${baseURL}/search/" + currentOrganism + "/download?search=" 
					+ encodeURIComponent(JSON.stringify({filters:$.filterTable.data(),scores:$.scoreTable.data()})) );			
			 */
			$("#download_btn").attr("href",
					"${baseURL}/search/" + currentOrganism + "/download");
		}

		$(document).ready(
				function() {
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
					var data = null;
					if ((window.history.state != null)
							&& (window.history.state.data != null)) {
						data = JSON.parse(window.history.state.data)

						data.filters = $.map(data.filters, objToParam);
						data.scores = $.map(data.scores, objToParam);
					}
					init(data)

				});
	</script>

	<script type="text/javascript">
		// ]]>
	</script>

	<div class="row">
		<section style="min-width: 300px" class="col-xs-6">


			<div  id="filterBox" class="box box-primary">
				<div class="box-header">
					<h3 class="box-title">Filtro</h3>

				</div>
				<div class="box-body">
					
					<a data-id="filter_keyword" data-target="#searchDialog"
						class="btn btn-app open-modal"> <i class="fa fa-plus-circle">&#160;</i>Keyword
					</a> <a data-id="filter_activity" class="btn btn-app open-modal"> <i
						class="fa  fa-cog">&#160;</i>Activity
					</a> <a data-id="filter_process" class="btn btn-app open-modal"> <i
						class="fa  fa-gears">&#160;</i>Biological Process
					</a> <a data-id="filter_localization" class="btn btn-app open-modal">
						<i class="fa  fa-circle-o">&#160;</i>Localization
					</a> 

<a id="structureFilterButton" data-id="filter_structure" class="btn btn-app open-modal">
						<i class="fa fa-sitemap">&#160;</i>Structure
					</a> 
					
					<a class="btn btn-app open-modal" data-id="filter_metadata" id="filter_metadata"><i class="fa fa-tags">&#160;</i>Metadata
					</a>


					<table id="search_filter_table" class="table">
						<thead>
							<tr>
								<td width="10px"></td>
								<td>Name</td>
								<td width="80px">Description</td>
								<td width="100px">Operation</td>
								<td width="100px">Value</td>
								<td width="100px">
									
								</td>

							</tr>
						</thead>
						<tbody></tbody>
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


	<div class="row">

		<section class="col-lg-12 ">
			<div class="box">
				<div class="box-body">

					<table id="search-table" class="table table-striped"
						style="table-layout: fixed;">
						<thead>
						
							<tr id="filters">
								<th></th>
								<th width="80px"><a id="refresh_btn" class="btn btn-app">
										<i class="fa fa-repeat">&#160;</i>Refresh
								</a>
								
								</th>
								
								<th width="20px"></th>
								<!-- <th width="60px"></th>
								<th style="width: 50px"></th> -->




								<th width="80px" id="gene_filter_th"><input
									id="gene_filter" type="text" style="width: 50px;"
									placeholder="gene..." /></th>

								



								<th width="200px"><input id="desc_filter" type="text"
									style="width: 100%" placeholder="description..." /></th>

								<!-- <th></th>
								<th width="25px"></th> -->

							</tr>
							<tr>
								<th>0</th>
								<th>1</th>
								<th>2</th>
								<!-- <th>3</th>
								<th>4</th> -->
								<th>5</th>
								<th>6</th>
								<th>7</th>
								<!-- <th>8</th>
								<th>9</th> -->

								<!-- <th>11</th> -->
							</tr>
						</thead>
						<tbody>

						</tbody>
						<tfoot></tfoot>
					</table>
				</div>
				<div class="overlay">&#160;</div>
				<div class="loading-img">&#160;</div>
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


</body>
	</html>

</jsp:root>