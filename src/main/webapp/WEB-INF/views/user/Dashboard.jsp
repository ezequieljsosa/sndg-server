<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page"
	xmlns:spring="http://www.springframework.org/tags"
	xmlns:c="http://java.sun.com/jsp/jstl/core"
	xmlns:form="http://www.springframework.org/tags/form"
	xmlns:sec="http://www.springframework.org/security/tags" version="2.0">
	<jsp:directive.page language="java" contentType="text/html" />


	<html>
<head>

<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta name="header_title" content="Dashboard" />
<meta name="header_title_desc" content="information overview" />

<title>Genome List</title>

<style type="text/css">
.dropdown-menu img {
	height: 18px;
	width: 18px;
	margin-right: 5px;
}

.label {
	position: absolute;
	z-index: 10;
}
</style>



</head>
<body>

	<script type="text/javascript">
		//<![CDATA[
		var projectCount = ${projectCount};
		var genomeCount = ${genomeCount};
		var init = function() {
			
			$("#project_count")
			.html(	projectCount);
			$("#genome_count")
			.html(	genomeCount);
			
			$('#search_input').change(
					function(evt) {
						$("#gene_search").attr("href",
								$.api.url_search_keyword($(this).val()));

					}).keyup(function(e) {
				if (e.keyCode == 13) {
					window.location = $.api.url_search_keyword($(this).val());
				}
			});
		};
		$(document).ready(init);
	</script>



	<script type="text/javascript">
		// ]]>
	</script>


	<!-- Small boxes (Stat box) -->
	<div class="row">
		<div class="col-lg-4 col-xs-6">
			<!-- small box -->
			<div class="small-box bg-aqua">
				<div class="inner">
					<h3 id="project_count">?</h3>
					<p>Projects</p>
				</div>
				<div class="icon">
					<i class="glyphicon glyphicon-briefcase">&#160;</i>
				</div>
				<a href="/xomeq/user/project/" class="small-box-footer"> Project
					List <i class="fa fa-arrow-circle-right">&#160;</i>
				</a>

			</div>
		</div>
		<div class="col-lg-4 col-xs-6">
			<!-- small box -->
			<div class="small-box bg-aqua">
				<div class="inner">
					<h3>Search</h3>
					<p><input width="99%" id="search_input" value="" /></p>

				</div>
				


			</div>
		</div>
		<div class="col-lg-4 col-xs-6">
			<!-- small box -->
			<div class="small-box bg-aqua">
				<div class="inner">
					<h3 id="genome_count">?</h3>
					<p>Genomes</p>
				</div>
				<div class="icon">
					<i class="glyphicon glyphicon-briefcase">&#160;</i>
				</div>
				<a href="/xomeq/genome/" class="small-box-footer"> Genomes
					List <i class="fa fa-arrow-circle-right">&#160;</i>
				</a>

			</div>
		</div>





	</div>
	<!-- /.row -->

	<!-- Main row -->
	<div class="row">


		<section class="col-lg-12 connectedSortable">
			<!-- Map box -->
			<div class="box box-primary">
				<div class="box-header">
					<!-- tools box -->
					<div class="pull-right box-tools">
						<button class="btn btn-primary btn-sm daterange pull-right"
							data-toggle="tooltip" title="Date range">
							<i class="fa fa-calendar">&#160;</i>
						</button>
					</div>
					<!-- /. tools -->

					<i class="fa fa-map-marker">&#160;</i>
					<h3 class="box-title">Links</h3>
				</div>
				<div class="box-body no-padding">
					<div class="table-responsive">
						<!-- .table - Uses sparkline charts-->
						<table class="table table-striped" id="links-table">
							<tr>
								<th></th>
								<th>Type</th>
								<th>Description</th>
								<!-- 								<th>Date</th> -->
							</tr>

						</table>
						<!-- /.table -->
					</div>
				</div>
				<!-- /.box-body-->
				<div class="box-footer"></div>
			</div>
			<!-- /.box -->
		</section>
	</div>






</body>
	</html>

</jsp:root>