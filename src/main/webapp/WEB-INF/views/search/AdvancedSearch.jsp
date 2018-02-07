<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page"
	xmlns:spring="http://www.springframework.org/tags"
	xmlns:c="http://java.sun.com/jsp/jstl/core"
	xmlns:form="http://www.springframework.org/tags/form"
	xmlns:sec="http://www.springframework.org/security/tags" version="2.0">
	<jsp:directive.page language="java" contentType="text/html" />


	<html>
<head>

<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta name="header_title" content="Search" />
<meta name="header_title_desc" content="Gene Products" />

<title>Seach</title>

<!-- DATA TABLES -->
<link href="../public/theme/css/datatables/dataTables.bootstrap.css"
	rel="stylesheet" type="text/css" />

<link rel="stylesheet" type="text/css"
	href="../public/jstree/themes/default/style.css" />

</head>
<body>

	<script type="text/javascript">
		//<![CDATA[
	</script>


	<script src="../public/jstree/jstree.js">
		
	</script>

	<!-- DATA TABES SCRIPT -->
	<script
		src="../public/theme/js/plugins/datatables/jquery.dataTables.js"
		type="text/javascript"></script>
	<script
		src="../public/theme/js/plugins/datatables/dataTables.bootstrap.js"
		type="text/javascript"></script>

	<script type="text/javascript">
		var tableInitComplete = function(settings, json) {

		};


		var init_seach_func = function() {
			
			$('#searchTree').jstree();
			
			$
					.getJSON(
							"../rest/user/${pageContext['request'].userPrincipal.name}/organism/")
					.done(function(organisms) {
					}).fail(function(req, error) {
						alert(error);
					});
			$("organism_li").append('<table><tr><td>asdfasd</td>asdfasdfasdfasdf<td></td></tr><tr><td>sadfasdfadsfasdfs<br />sadfasdfadsfasdfs<br />sadfasdfadsfasdfs<br />sadfasdfadsfasdfs<br /></td></tr></table>');
		};
		$(document).ready(init_seach_func);
	</script>

	<script type="text/javascript">
		// ]]>
	</script>

	<div class="row">

		<section class="col-lg-4 connectedSortable">
			<div class="box box-primary">
				<div class="box-header">

					<i class="fa fa-map-marker">&#160;</i>
					<h3 class="box-title">Build Search</h3>
				</div>
				<div class="box-body no-padding">

					<div id="searchTree">

						<ul>
							<li>General
								<ul>
									<li id="organism_li">Organism</li>
									<li>Genome</li>
									<li>Keywords</li>
								</ul>
							</li>
							<li>Sequence
								<ul>
									<li>Features</li>
									<li></li>
								</ul>
							</li>
							<li>Expression
								<ul>
									<li><a href="#">Condition/s</a></li>
								</ul>
							</li>
							<li>Pathways
								<ul>
									<li>Reaction</li>
									<li>Pawthway</li>
								</ul>
							</li>
							<li>Variants
								<ul>
									<li>Model</li>
									<li>Ligand</li>
									<li>Catalitic</li>
									<li>Pocket</li>
								</ul>
							</li>
							<li>Structure
								<ul>
									<li>Model</li>
									<li>Ligand</li>
									<li>Catalitic</li>
									<li>Pocket</li>
								</ul>
							</li>
						</ul>

					</div>
				</div>
			</div>
		</section>
	</div>




</body>
	</html>

</jsp:root>