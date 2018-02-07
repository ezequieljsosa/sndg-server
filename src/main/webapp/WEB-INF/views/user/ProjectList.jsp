<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page"
	xmlns:spring="http://www.springframework.org/tags"
	xmlns:c="http://java.sun.com/jsp/jstl/core"
	xmlns:form="http://www.springframework.org/tags/form"
	xmlns:sec="http://www.springframework.org/security/tags" version="2.0">
	<jsp:directive.page language="java" contentType="text/html" />



	<html>
<head>

<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta name="header_title" content="Projects" />
<meta name="header_title_desc" content="list" />

<title>Genome List</title>

<!-- DATA TABLES -->
<link href="/xomeq/public/theme/css/datatables/dataTables.bootstrap.css"
	rel="stylesheet" type="text/css" />



</head>
<body>

	<script type="text/javascript">
		//<![CDATA[
	</script>

	<!-- DATA TABES SCRIPT -->
	<script
		src="/xomeq/public/theme/js/plugins/datatables/jquery.dataTables.js"
		type="text/javascript"></script>
	<script
		src="/xomeq/public/theme/js/plugins/datatables/dataTables.bootstrap.js"
		type="text/javascript"></script>


	<sec:authentication property="principal.username" />

	<script type="text/javascript">
		$(document).ready(function() {
			$.getJSON("/xomeq/user/${pageContext['request'].userPrincipal.name}/project/").done(function(projects) {
				$.each(projects, function(index,project){
					var strToAppend = '<div class="row"><section class="col-xs-12"><div class="box box-primary"><div class="box-header"><i class="fa fa-briefcase">&#160;</i><h3 class="box-title">';
					strToAppend +=  project.name;
					strToAppend += '</h3></div><div class="box-body no-padding">';
					strToAppend += project.description;
					strToAppend += '<div class="table-responsive"><table class="table table-striped">';
					$.each(project.links, function(index,link){
							strToAppend += '<tr><td><a href="' +link.url + '">';
							strToAppend += link.description;
							strToAppend += '</a></td></tr>';					
					});
					strToAppend += '</table></div></div></div></section></div>';
					$("#content").append(strToAppend);
				}); //Project iteration
			}); // Ajax call
		});
	</script>



	<script type="text/javascript">
		// ]]>
	</script>




<div id="content"></div>








</body>
	</html>

</jsp:root>