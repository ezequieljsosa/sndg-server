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

<title>Gene Details</title>
</head>
<body>

	<script type="text/javascript">
		//<![CDATA[
	</script>

	<script src="${baseURL}/public/jslibs/jquery/jquery-migrate-1.2.1.js"></script>

	<script src="${baseURL}/public/bia/ui/SequencePanel.js"></script>
	<script src="${baseURL}/public/bia/model/Localization.js"></script>

	<script src="${baseURL}/public/bia/ui/JBrowseWrapper.js"
		type="text/javascript"></script>


	<!-- BIOJS SCRIPT -->

	<script language="JavaScript" type="text/javascript"
		src="${baseURL}/public/biojs/Biojs.js"></script>

	<script language="JavaScript" type="text/javascript"
		src="${baseURL}/public/biojs/Biojs.Tooltip.js"></script>

	<script language="JavaScript" type="text/javascript"
		src="${baseURL}/public/biojs/Biojs.Sequence.js"></script>

<script type="text/javascript">

var strain = '${strain}';
var genome_id = '${genome_id}';
var variant = ${variant};




</script>

	<script type="text/javascript">
		function setJbrowseURL(genome_name, variant) {
			var loc = new $.Localization(variant.contig, variant.pos , variant.pos +  variant.ref.length - 1 );
			
			var jbrowse = new $.JBrowseWrapper($("#jbrowse"),
					"${baseURL}/public/jbrowse/?data=data/");
			jbrowse.tracks = jbrowse.tracks.concat(  [strain + "_vcf" ,strain + "_bam"])
			jbrowse.init(genome_name, true, loc, loc);
		
		}

		function load_variant(organism, variant) {

			$("<a/>",{"href":"${baseURL}/genome"}).html("Genomes").appendTo( $("#base_breadcrumb") );
			var li = $("<li/>").appendTo( $(".breadcrumb") );
			$("<a/>",{"href":"${baseURL}/genome/" + organism}).html("<i>" + organism + "</i>").appendTo(li);
			
			li = $("<li/>").appendTo( $(".breadcrumb") );
			$("<a/>").html(strain).appendTo(li);
			
			
		}

		

		$(document).ready(function() {
			
			setJbrowseURL(genome_id, variant)
			load_variant(genome_id,variant);
		});
	</script>

	<script type="text/javascript">
		// ]]>
	</script>

	<div id="jbrowse_row" class="row">
		<div class="col-xs-12">
			<div class="box box-primary">
				<div class="box-body">
					<![CDATA[
		<iframe id="jbrowse" style="border: 1px solid black"
			
			height="800px" width="100%"> </iframe>
	]]>

				</div>
			
			</div>
		</div>
	</div>

	<div class="row">
		<section class="col-lg-12 connectedSortable">
			<!-- small box -->
			<div class="box">
				<div class="box-body no-padding">
					<table class="table table-striped">
						<thead>
							<tr>
								<th colspan="2">Overview</th>
							</tr>
						</thead>
						<tbody>


							<tr>
								<td>Genome</td>
								<td id="genome_link"></td>
							</tr>
							<tr>
								<td>Locus</td>
								<td id="strLocus">?</td>
							</tr>
							<tr>
								<td>Locus Tag</td>
								<td id="locus_tag">?</td>
							</tr>
							<tr>
								<td>Type</td>
								<td id="type">?</td>
							</tr>
							<!-- <tr>
								<td>Status</td>
								<td id="status">?</td>
							</tr> -->
							<tr>
								<td>Product</td>
								<td id="product_link"></td>
							</tr>
							<tr>
								<td>Length</td>
								<td id="strSize">?</td>
							</tr>
						</tbody>
					</table>
				</div>
			
			</div>
		</section>
	</div>



	


</body>
	</html>
</jsp:root>