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

    <script type="application/javascript" src="${baseURL}/public/sequence-viewer.min.js">
    </script>

<script type="text/javascript">

var gene_obj = ${gene};
var product_obj = ${protein};


</script>

	<script type="text/javascript">
		function setJbrowseURL(genome_name, gene) {
			var loc = new $.Localization(gene.location.reference, gene.location.start, gene.location.end);
			var ext_loc = loc.expand();
			var jbrowse = new $.JBrowseWrapper($("#jbrowse"),
					"${baseURL}/public/jbrowse/?data=data/");

			jbrowse.init(genome_name, true, ext_loc, loc);
		
		}

		function load_gene(organism, gene) {

			//$("<a/>",{"href":"${baseURL}/genome"}).html("Genomes").appendTo( $("#base_breadcrumb") );
			var li = $("<li/>").appendTo( $(".breadcrumb") );
			$("<a/>",{"href":"${baseURL}/genome/" + organism}).html("<i>" + organism + "</i>").appendTo(li);
			
			li = $("<li/>").appendTo( $(".breadcrumb") );
			$("<a/>").html("Search").appendTo(li);
			
			li = $("<li/>").appendTo( $(".breadcrumb") );
			$("<a/>",{"href":"${baseURL}/protein/"+ product_obj.id}).html("Protein:  " + product_obj.name).appendTo(li);
			
			li = $("<li/>").addClass("active").appendTo( $(".breadcrumb") );
			$("<a/>",{"href":"#"}).html(gene.name).appendTo(li);
			
			
			if (gene.description == null){
				gene.description = "";
			}
			
			$("#header_title").html(
					gene.name + '<small id="header_title_desc">'
							+ gene.description + '</small>');

			if (gene.strCollectionId) {
				$("#genome_link").html(
						'<a href="' + $.api.url_genome(organism)
								+ '">' + organism + '</a>');
			} else {
				$("#genome_link").html(organism);
			}

			var sp = new $.SequencePanel("sequence_box", gene.seq, gene.name,
					gene.id);
			sp.init();

			$.each(gene, function(key, value) {
				$("#" + key).html(value);
			});

			$("#locus_tag").html(gene.name);
			if (gene.publications) {
				$.each(gene.publications, function(key, value) {
					$("#publication_list").append('<li>' + value + '</li>');
				});
			} else {
				$("#publications_row").remove();
			}
			;

            var seq1 = new Sequence(gene_obj.seq);

            seq1.render('#protein-sequence-viewer', {
                'showLineNumbers': true,
                'wrapAminoAcids': true,
                'charsPerLine': 100,
                'toolbar': false,
                'search': false,
                'title' : gene.name,
                'sequenceMaxHeight': "300px",
                'badge': true
            });

		}

		function load_product(gene, product) {

			if (product == null) {

				if (gene.product) {
					$("#product_link").html(gene.product);
				} else {
					$("#product_link").remove();
				}
			} else {
				product_name = "";
				if (product.name != null) {
					product_name = product.name;
				}
				$("#product_link").html(
						'<a href="' + $.api.url_protein( product.id ) + '">'
								+ product.name + '</a>');
			}

		}

		$(document).ready(function() {

			load_gene(product_obj.organism,gene_obj);



			setJbrowseURL(product_obj.organism, gene_obj)
			load_product(gene_obj, product_obj)
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
			
			height="400px" width="100%"> </iframe>
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

	<div class="row">
		<section class="col-lg-8 connectedSortable">
			<div id="protein-sequence-viewer"></div>


		</section>
	</div>


</body>
	</html>
</jsp:root>