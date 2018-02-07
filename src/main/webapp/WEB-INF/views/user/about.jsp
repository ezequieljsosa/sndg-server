<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page"
	xmlns:spring="http://www.springframework.org/tags"
	xmlns:c="http://java.sun.com/jsp/jstl/core"
	xmlns:fn="http://java.sun.com/jsp/jstl/functions"
	xmlns:form="http://www.springframework.org/tags/form" version="2.0">
	<jsp:directive.page language="java" contentType="text/html" />
	<c:set var="reqUrl">${pageContext.request.requestURL}</c:set>
	<c:set var="baseURL"
		value="${fn:replace(reqUrl, pageContext.request.requestURI,pageContext.request.contextPath)}" />
	<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />

<meta name="header_title" content="Pathway" />
<meta name="header_title_desc" content="reactions in pathway" />

<meta name="_csrf" content="${_csrf.token}" />
<!-- default header name is X-CSRF-TOKEN -->
<meta name="_csrf_header" content="${_csrf.headerName}" />

<title>SNDG</title>

<style type="text/css">
.box .box-header {
	padding-bottom: 10px;
}
</style>

</head>
<body>
	<script type="text/javascript">
		$("body").addClass("container")
		$(".content-header").remove()
	</script>


	<div class="row">

		<h2>Visualizador de genomas</h2>
		<div class="col-md-12">
			El visualizador de genomas del SNDG tiene los siguientes objetivos:
			<ul>
				<li>Buscar informaci&#243;n biol&#243;gica, en especial la asociada a secuencias</li>
				<li>Presentar datos de manera clara y precisa</li>
				<li>Integrar resultados de otras herramientas y cruzar informacil&#243;n con otras bases de datos</li>
				
			</ul>
			Tambi&#233;n cruza y actualiza sus datos peridodicamente con el  <a href="http://datos.sndg.mincyt.gob.ar/">Portal de datos</a>			
		</div>
	</div>

	<div class="row">
		<div class="col-md-12">
			<h1 id="software">Visualizaciones de 3ros</h1>

			<ul>
				<li id="jbrowse"><a href="http://jbrowse.org/">JBrowse</a></li>
				<li id="glmol"><a href="http://webglmol.osdn.jp/index-en.html">GLMol</a></li>
				<li id="krona"><a href="https://github.com/marbl/Krona/wiki">Krona</a></li>
				<li id="biojs"><a href="http://biojs.net/">BioJs</a></li>
				<li id="msa"><a href="http://msa.biojs.net/">MSAViewer</a></li>


			</ul>
			<h2 style="display: None">Search, processes and annotations</h2>
			<ul style="display: None">
				<li><a href="http://hmmer.org/">Hmmer</a></li>
				<li><a href="http://blast.ncbi.nlm.nih.gov/Blast.cgi">Blast</a></li>
				<li><a href="http://www.ebi.ac.uk/Tools/msa/clustalo/">Clustal
						Omega</a></li>
				<li><a href="http://brg.ai.sri.com/ptools/">Pathway Tools</a></li>
				<li><a href="http://fpocket.sourceforge.net">FPocket</a></li>

			</ul>

			<h1 id="databases">Bases de datos</h1>
			<ul>
				<li><a href="http://pfam.xfam.org/">PFam</a></li>
				<li><a href="www.rcsb.org/">PDB</a></li>
				<li><a href="geneontology.org/">GO</a></li>
				<li><a href="www.sequenceontology.org/">SO</a></li>
				<li><a href="www.ncbi.nlm.nih.gov/COG/">COG</a></li>
				<li><a href="http://enzyme.expasy.org/">EC</a></li>
				<li style="display: None"><a
					href="https://www.ebi.ac.uk/thornton-srv/databases/CSA/">CSA</a></li>
				<li style="display: None"><a
					href="http://modbase.compbio.ucsf.edu/modbase-cgi/index.cgi">ModBase</a></li>

				<li style="display: None"><a
					href="http://tritrypdb.org/tritrypdb/">TriTrypDB</a></li>
				<li style="display: None"><a href="http://plasmodb.org/plasmo/">PlasmoDB</a></li>
				<li style="display: None"><a href="toxodb.org">ToxoDB</a></li>

				<li style="display: None"><a href="http://biocyc.org/">BioCyc</a></li>

				<li><a href="http://www.uniprot.org/">UNIPROT</a></li>
				<li><a href="www.ncbi.nlm.nih.gov/">NCBI</a></li>


			</ul>

		</div>
	</div>

</body>
	</html>
</jsp:root>