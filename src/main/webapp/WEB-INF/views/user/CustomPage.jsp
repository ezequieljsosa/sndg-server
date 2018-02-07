<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page"
	xmlns:spring="http://www.springframework.org/tags"
	xmlns:c="http://java.sun.com/jsp/jstl/core"
	xmlns:form="http://www.springframework.org/tags/form" version="2.0">
	<jsp:directive.page language="java" contentType="text/html" />

	<html>
<head>

<title>Custom page</title>

<!-- DATA TABLES -->
<link href="../public/theme/css/datatables/dataTables.bootstrap.css"
	rel="stylesheet" type="text/css" />

</head>
<body>

	<script type="text/javascript">
		//<![CDATA[
	</script>

	<!-- DATA TABES SCRIPT -->
	<script
		src="../public/theme/js/plugins/datatables/jquery.dataTables.js"
		type="text/javascript"></script>
	<script
		src="../public/theme/js/plugins/datatables/dataTables.bootstrap.js"
		type="text/javascript"></script>


	<script type="text/javascript">
		function rendersection(box_element, section) {
			if (section.type == "table") {

				var columns = "";
				var datacolumns = [];
				$.each(section.columns, function(index, value) {
					columns = columns + '<td>' + value.capitalize() + '</td>';
					datacolumns.push({"data":value,"defaultContent":"-"});
				});
			
				var table = $('<table class="table table-striped"><thead><tr>' + columns
						+ '</tr></thead></table>');

				box_element.append(table);
				
				
				section.table_options["ajax"] = section.table_options["ajax"] + "?columns=" + encodeURIComponent(section.columns);
				section.table_options["columns"]=datacolumns;
				$(table).dataTable(section.table_options); 

			} else if (section.type == "static") {
				//TODO -> no deberia ser un frame publico
				box_element
						.append('<iframe height="100%" width="100%" src="../public/custompages/'
								+ section.file + '" ></iframe>');
			} else {
				alert("unknown section type:" + section.type);
			}
						
		}

		var loadpage = function(page) {
			window.title = page.title;
			$("#algo").css("max-height", screen.height * 2 / 3);
			$("#header_title").html(
					page.title + '<small id="header_title_desc">'
							+ page.description + '</small>');

			for (var i = 0; i < page.layout[0]; i++) {
				var row_id = 'row' + i;
				$("#algo")
						.append('<div id="' + row_id+  '" class="row"></div>');
				for (var j = 0; j < page.layout[1]; j++) {
					var section_id = 'section' + i + "_" + j;
					$("#" + row_id)
							.append(
									'<section id="' + section_id +  '" class="row"></div>');
					var size = 12 / page.layout[1];
					$("#" + section_id).addClass("col-lg-" + size);
					var box_id = section_id + "box";
					$("#" + section_id).append(
							'<div style="height: 100%;" id="' + box_id
									+ '" class="box"></div>');

					var section = page.sections.pop();
					if (section.title != null) {
						$("#" + box_id).append(
								'<div class="box-header"><h3 class="box-title">'
										+ section.title + '</h3></div>');
					}
					rendersection($("#" + box_id), section);

				}
			}

		}
		$(document).ready(
				function() {
					$.getJSON("../rest/page/" + $.QueryString["id"]).done(
							loadpage).fail(function(jqxhr, textStatus, error) {
						var err = textStatus + ", " + error;
						console.log("Request Failed: " + err);
					}).fail(function(req, err) {
						alert(err);
					});
				});
	</script>

	<script type="text/javascript">
		// ]]>
	</script>


	<div id="algo" style="height: 100%; width: 100%;"></div>



</body>
	</html>
</jsp:root>