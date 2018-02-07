<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page"
	xmlns:spring="http://www.springframework.org/tags"
	xmlns:c="http://java.sun.com/jsp/jstl/core"
	xmlns:form="http://www.springframework.org/tags/form" version="2.0">
	<jsp:directive.page language="java" contentType="text/html" />	
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>Reactions</title>

<link rel="stylesheet" type="text/css"
	href="../jstree/themes/default/style.css" />
			<!-- Bootstrap -->
			<link href="../theme/css/bootstrap.min.css" rel="stylesheet" type="text/css"></link>

<!-- Theme style -->
<link href="../theme/css/AdminLTE.css" rel="stylesheet"
	type="text/css" />
			

</head>
<body>

	<script type="text/javascript">
		//<![CDATA[
	</script>
<!-- jQuery 2.1.1 -->
			<script src="../theme/js/jquery.min.js"></script>
	<script src="../jstree/jstree.js"></script>
	<script src="../bia/bia.js"></script>
	<script src="../bia/metabolism.js"></script>
	<script src="../theme/js/bootstrap.min.js"
				type="text/javascript"></script>


	<script type="text/javascript">
		
		$.getJSON("../../rest/protein/search?gene=" + $.QueryString["rv"]).done(
				function(protein) {
					rt = new $.ReactionsTree($("#pathwaystree"),
							protein.organism, '${_csrf.parameterName}',
							'${_csrf.token}')
					rt.ontologies_term_url = "../../rest/ontologies/terms";
					rt.gene_prod_list_url = '../../rest/redirect?type=filter&key=keywords&value={0}&organism={1}';
					rt.get_protein_with_gene_url = "../../rest/redirect?type=product&key=gene&value={0}";
					rt.render_links = false;
					rt.load_reactions(protein.pathways)
				}).fail(function(jqxhr, textStatus, error) {
			var err = textStatus + ", " + error;
			console.log("Request Failed: " + err);
		}).always(function(){
			$(".overlay").remove();
			$(".loading-img").remove();
		});
		// ]]>
	</script>

</body>
<div  style="min-height: 500px" class="box"> 
				
				<div  class="box-body no-padding">
					<div id="pathwaystree" width="100%">&#160;</div>
				</div>
			
				<div  class="overlay">&#160;</div>
				<div class="loading-img">&#160;</div>

			</div>

</html>
</jsp:root>