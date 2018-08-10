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
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />

<meta name="header_title" content="Pathway" />
<meta name="header_title_desc" content="reactions in pathway" />

<meta name="_csrf" content="${_csrf.token}" />
<!-- default header name is X-CSRF-TOKEN -->
<meta name="_csrf_header" content="${_csrf.headerName}" />

<title>TP About</title>

<style type="text/css">
.box .box-header {
	padding-bottom: 10px;
}



</style>

<!-- DATA TABLES -->
<link
	href="${baseURL}/public/theme/css/datatables/dataTables.bootstrap.css"
	rel="stylesheet" type="text/css" />

</head>
<body>
<script type="text/javascript">
$("body").addClass("container");
$(".content-header").remove();

$.get("http://target.sbg.qb.fcen.uba.ar/targetwp/feed", function (data) {
    const ul = $("#rss-feeds");
    $(data.getElementsByTagName("item")).each((i, x) => {
        const title = $(x.getElementsByTagName("title")).text();
        const link = $(x.getElementsByTagName("link")).text();
        let pubDate = $(x.getElementsByTagName("pubDate")).text();
        pubDate = pubDate.split(" ").slice(0, 4).join(" ");
        const description = $(x.getElementsByTagName("description")).text();

        const li = $("<li />");
        li.appendTo(ul);
        li.append($("<span />").html(pubDate));
        li.append($("<br />"));
        li.append($("<a />", {href: link}).html("<b>" + title + "</b>").css("color","blue"));
        li.append($("<br />"));
        li.append($("<span />").html(description));


    });

});

</script>

<nav class="col-sm-2 d-none d-md-block bg-light sidebar">
	<div class="sidebar-sticky">

		<ul id="rss-feeds">


		</ul>

	</div>
</nav>


	<div class="row">


		<div class="col-md-12">
			<h2>Citing Target-Pathogen</h2>
			
			If you find Target-Pathogen useful, please consider citing the reference that describes this work:<br /><br />
			<p>
			<a href="https://academic.oup.com/nar/article/doi/10.1093/nar/gkx1015/4584621">
			<i>Target-Pathogen: a structural bioinformatic approach to prioritize drug targets in pathogens</i></a>
			: Ezequiel J. Sosa,  Germ&#225;n Burguener,  Esteban Lanzarotti,  Lucas Defelipe, Leandro Radusky,  Agust&#237;n M. Pardo,  Marcelo Marti,  Adri&#225;n G. Turjanski, Dar&#237;o Fern&#225;ndez Do Porto <br />
			</p>
			<b>Nucleic Acids Research</b> (2018)  Database Issue
		
			
		</div>

	</div>



	<div class="row">


		<div class="col-md-12">
			<h2>Related Publications</h2>
			<ul>
				<li>Leandro Radusky, Lucas A. Defelipe, Esteban Lanzarotti, Javier Luque, Xavier Barril, Marcelo A. Marti and Adri&#225;n G. Turjanski (2014) <b>TuberQ: a Mycobacterium tuberculosis protein druggability database</b></li>
				<li> Lucas A. Defelipe, Dario Fern&#225;ndez Do Porto, Pablo Ivan Pereira Ramos, d, Marisa Fabiana Nicol&#225;s, 
					Ezequiel Sosa, Leandro Radusky, Esteban Lanzarotti, Adri&#225;n G. Turjanski, 
					Marcelo A. Marti, (2015) <b>A whole genome bioinformatic approach to determine potential latent phase specific targets in Mycobacterium tuberculosis</b></li>

				<li>
					Pablo Ivan Pereira Ramos, Darío Fernandez Do Porto, ... , Adrian G. Turjanski - Marisa F. Nicolas  <a href="https://www.nature.com/articles/s41598-018-28916-7"> <b> An integrative, multi-omics approach towards the prioritization of Klebsiella pneumoniae drug targets</b></a>

				</li>


<li style="display:None">Karp, P. D., Latendresse, M., Paley, S. M., Krummenacker, M., Ong, Q. D., Billington, R., . . . Caspi, R. (2015). Pathway Tools version 19.0 update: software for pathway/genome informatics and systems biology. Brief Bioinform. doi: 10.1093/bib/bbv079</li>
<li style="display:None">Le Guilloux, V., Schmidtke, P., &#38; Tuffery, P. (2009). Fpocket: an open source platform for ligand pocket detection. BMC Bioinformatics, 10, 168. doi: 10.1186/1471-2105-10-168</li>
<li style="display:None">Sonnhammer, E. L., Eddy, S. R., &#38; Durbin, R. (1997). Pfam: a comprehensive database of protein domain families based on seed alignments. Proteins, 28(3), 405-420. </li>


			</ul>
		</div>

	</div>

	<div class="row">
		<div class="col-md-12">
			<h1 id="software">Used Software</h1>

			<ul>
				<li id="jbrowse"><a href="http://jbrowse.org/">JBrowse</a></li>
				<li id="glmol"><a href="http://webglmol.osdn.jp/index-en.html">GLMol</a></li>
				<li id="krona"><a href="https://github.com/marbl/Krona/wiki">Krona</a></li>
				<li id="biojs"><a href="http://biojs.net/">BioJs</a></li>
				<li id="msa"><a href="http://msa.biojs.net/">MSAViewer</a></li>


			</ul>
			<h2>Search, processes and annotations</h2>
			<ul>
				<li><a href="http://hmmer.org/">Hmmer</a></li>
				<li><a href="http://blast.ncbi.nlm.nih.gov/Blast.cgi">Blast</a></li>
				<li><a href="http://www.ebi.ac.uk/Tools/msa/clustalo/">Clustal
						Omega</a></li>
				<li><a href="http://brg.ai.sri.com/ptools/">Pathway Tools</a></li>
				<li><a href="http://fpocket.sourceforge.net">FPocket</a></li>

			</ul>

			<h1 id="databases">Databases</h1>
			<ul>
				<li><a href="http://pfam.xfam.org/">PFam</a></li>
				<li><a href="www.rcsb.org/">PDB</a></li>
				<li><a href="geneontology.org/">GO</a></li>
				<li><a href="www.sequenceontology.org/">SO</a></li>
				<li><a href="www.ncbi.nlm.nih.gov/COG/">COG</a></li>
				<li><a href="http://enzyme.expasy.org/">EC</a></li>
				<li><a href="https://www.ebi.ac.uk/thornton-srv/databases/CSA/">CSA</a></li>
				<li><a href="http://modbase.compbio.ucsf.edu/modbase-cgi/index.cgi">ModBase</a></li>
				
				<li><a href="http://tritrypdb.org/tritrypdb/">TriTrypDB</a></li>
				<li><a href="http://plasmodb.org/plasmo/">PlasmoDB</a></li>
				<li><a href="toxodb.org">ToxoDB</a></li>
				
				<li><a href="http://biocyc.org/">BioCyc</a></li>
				
				<li><a href="http://www.uniprot.org/">UNIPROT</a></li>
				<li><a href="www.ncbi.nlm.nih.gov/">NCBI</a></li>
				

			</ul>

		</div>
	</div>

</body>
	</html>
</jsp:root>