<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page"
	xmlns:spring="http://www.springframework.org/tags"
	xmlns:c="http://java.sun.com/jsp/jstl/core"
	xmlns:form="http://www.springframework.org/tags/form" version="2.0">
	<jsp:directive.page language="java" contentType="text/html" />

	<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta name="header_title" content="Differential expression table" />
<meta name="header_title_desc" content="Genomes and Proteomes" />
<title>Expression analysis</title>

<style type="text/css">
.box .box-header {
	padding-bottom: 10px;
}
</style>

<link href="../public/jheatmap/css/jheatmap-1.0.0-min.css"
	rel="stylesheet" type="text/css" />

</head>


<body>
	<div class="row">
		<section class="col-lg-6 connectedSortable">
			<div class="box box-primary">
				<div class="box-header">
					<input type="button" class="btn btn-info" id="goToProductBtn"
						value="Go to selected Gene Product" />
				</div>
				<div class="box-body no-padding">
					<select id="replicas">
						<option value="false">Show conditions (group replicas)</option>
						<option value="true">Show all replicas</option>
					</select>
					<div id="heatmap"></div>
				</div>
			</div>
		</section>
	</div>


	<script src="../public/jheatmap/js/jheatmap-1.0.0-min.js"
		type="text/javascript"></script>
	<script type="text/javascript">
		//<![CDATA[
	</script>

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
				return value[0];

			} else {
				alert("Nothing is selected");
				return null;
			}
		};

		jheatmap.actions.DimentionSelected.prototype.rows = function() {
			var name = this.run(this.heatmap.rows);
			if (name != null)
				window.location = '../rest/redirect?type=product&key=name&value='
						+ name;
		};

		jheatmap.actions.DimentionSelected.prototype.columns = function() {
			var name = this.run(this.heatmap.cols);
			if (name != null)
				window.location = '../rest/redirect?type=filter&key=keywords&value=overexpressed_' +name+  "&genome="
				+ $.QueryString["genome"];
						
		};

		function reload_heatmap() {
			url_data = "../rest/expression/" + $.QueryString["genome"]
					+ "?replicas=" + $("#replicas").val();
			url_genes = "../rest/expression/" + $.QueryString["genome"]
					+ "/genes"; //564687234
			url_samples = "../rest/expression/" + $.QueryString["genome"]
					+ "/samples" + "?replicas=" + $("#replicas").val();
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
									// Column annotations
									heatmap.cols.decorators["subtype"] = new jheatmap.decorators.CategoricalRandom();
									heatmap.cols.annotations = [ "subtype" ];

									// Rows annotations
									heatmap.rows.decorators["gene"] = new jheatmap.decorators.PValue(
											{
												cutoff : 0.05
											});
									heatmap.rows.annotations = [ "gene" ];

									// Aggregators

									//heatmap.cells.aggregators["Expression"] = new jheatmap.aggregators.Median();

									// Decorators

									heatmap.cells.decorators["expression"] = new jheatmap.decorators.Heat(
											{
												minValue : -2000,
												midValue : 0,
												maxValue : 2000,
												minColor : [ 85, 0, 136 ],
												nullColor : [ 255, 255, 255 ],
												maxColor : [ 255, 204, 0 ],
												midColor : [ 240, 240, 240 ]

											});

									heatmap.cells.decorators["significant"] = new jheatmap.decorators.Categorical(
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

								}

							});

		}

		$(document).ready(function() {

			if (typeof $.QueryString["genome"] == "undefined") {
				$("#heatmap").html("No genome selected");
				return;
			}
			$("#replicas").change(function(evt) {
				reload_heatmap();
			});
			reload_heatmap();
		});
	</script>



	<script type="text/javascript">
		// ]]>
	</script>
</body>
	</html>
</jsp:root>