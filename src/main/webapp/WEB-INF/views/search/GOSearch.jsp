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

<title>GO Search</title>


<link rel="stylesheet" type="text/css"
	href="${baseURL}/public/widgets/jstree/themes/default/style.css" />

</head>
<body>

	<script type="text/javascript">
		//<![CDATA[
	</script>

	<script src="${baseURL}/public/widgets/jstree/jstree.js">
		
	</script>


	<script type="text/javascript">
	var genome = ${genome};
	var search = '${search}';
		function init() {
			$('#search_txt').val(search);
			$("#header_title")
					.html(
							genome.name
									+ '<small id="header_title_desc">GO search</small>');

		

			$("#clear_btn").click(function(e) {
				$("#treeViewDiv").jstree(true).clear_state();						
				window.location = $.api.url_search_genome_go(genome.name,'' );
			});
			
			$("#search_btn").click(function(e) {
				$("#treeViewDiv").jstree(true).clear_state();
				window.location = $.api.url_search_genome_go(genome.name,encodeURI( $('#search_txt').val())) ;
			})
			 $('#search_txt').keyup(
					function(e) {
						if (e.keyCode == 13) {
							window.location = $.api.url_search_genome_go(genome.name,encodeURI( $('#search_txt').val())) ;
						}
					});
			
			
			$("#refresh_btn").click(
					function(e) {				
						window.location = $.api.url_search_genome_go(genome.name,encodeURI( $('#search_txt').val())) ;	
					});
			$('#treeViewDiv')
					.jstree(
							{
								'core' : {
									'data' : {
										'url' : $.api.url_search_genome_go(genome.name ,search),												
										'data' : function(node) {
											return {
												'id' : node.id
											};
										}
									}
								},
								'types' : {

									"annotation" : {
										"icon" : "${baseURL}/public/widgets/jstree/themes/default/geneprod.ico"
									}
								// 													/* "term" : {
								// 														"icon" : "jstree/themes/default/geneprod.ico"
								// 													} */

								},
								// 												'search' : {													
								// 													"ajax":{
								// 														"url":'../rest/ontologies/go/'
								// 															+ $.QueryString["genome"] + "/search"
								// 														}
								// 												},
								"contextmenu" : {
									"items" : function($node, data) {

										if ($node.type == "annotation") {

											return {
												"Product" : {
													"separator_before" : false,
													"separator_after" : false,
													"label" : "Go to Product",
													"action" : function(obj) {
														window.location = $.api.url_protein( $node.id );
													}
												}

											};
										} else {

											return {
												"Seachterm:" : {
													"separator_before" : false,
													"separator_after" : false,
													"label" : "Seach Term: "
															+ $node.id,
													"action" : function(obj) {
														window.location = $.api.url_search_genome_keyword(genome.name,$node.id);
													}
												}
											}
										}
									}
								},
								"plugins" : [ "types", "wholerow",
										"contextmenu"
										, "state" 
										]
							});
			/* .on(
			 'changed.jstree',
			 function(e, data) {
			 if (data.node == null) return;
			 if (data.node.type == "annotation") {
			 window.location = '../rest/redirect?type=gene&key=_id&value='
			 + data.node.id;
			 } else {
			 window.location = '../rest/redirect?type=filter&key=keywords&value='
			 + data.node.id
			 + "&genome="
			 + $.QueryString["genome"];
			 }
			 }); */
			// 									.on('open_node.jstree', function(e, data) {
			// 											tree = $('#treeViewDiv').jstree(true);
			// 											if($.isArray(tree.pending_search) && tree.pending_search.length>0 ){												
			// 												if(tree.current_search.toLowerCase() == data.node.id.toLowerCase()){
			// 													node_to_open = tree.pending_search.shift();
			// 													tree.current_search = node_to_open
			// 													tree.open_node(node_to_open,function(e){alert(e)},100);
			// 												}
			// 											}
			// 										})
			// 							$('#search_button').click(function() {	
			// 								$('#search_button').attr('disabled','disabled')
			// 									var v = $('#search_input').val();
			// 									tree = $('#treeViewDiv').jstree(true);									
			// 									$.getJSON('../rest/ontologies/go/'	+ 
			// 											$.QueryString["genome"] + "/search?str=" +  
			// 											encodeURIComponent(v))
			// 										.done(function(data){												
			// 												tree.pending_search = data;
			// 												node_to_open = tree.pending_search.shift();		
			// 												tree.current_search = node_to_open;
			// 												funcPosterior = null;
			// 												func = null;
			// 												for (i=data.length-1;i>=0;i-- ){
			// 													if(funcPosterior == null){
			// 														funcPosterior = function(){
			// 															tree.open_node(node_to_open);
			// 														}
			// 													} else {
			// 														func = function(){
			// 															tree.open_node(node_to_open,funcPosterior,100);
			// 														}
			// 														funcPosterior = func
			// 													}
			// 												}
			// 												if(func != null) func();
			// 											})
			// 										.fail(function(error){
			// 											alert("Error:" + error);
			// 									});									
			// 									$('#search_button').removeAttr('disabled')								
			//							});
		}
		$(document).ready(init); //End Document Ready
	</script>

	<script type="text/javascript">
		// ]]>
	</script>
	<div class="row">

		<section class="col-lg-12 connectedSortable">
			<div class="box">
				<div class="box-header no padding">
					<div class="box-tools pull-left">

						<div class="input-group">
							<span class="input-group-addon">
								<input id="search_txt" width="100%" />
								<button id="search_btn" class="btn btn-info btn-sm">Search</button>
								<button id="clear_btn" class="btn btn-danger btn-sm">Clear</button>
								<button id="refresh_btn" class="btn btn-primary btn-sm">Refresh</button>
							</span>

						</div>




					</div>
				</div>

				<div id="treeViewDiv" class="box-body"></div>
			</div>
		</section>
	</div>

</body>
	</html>
</jsp:root>