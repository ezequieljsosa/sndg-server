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

<title>Target Pathogen</title>

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
$("body").addClass("container")
$(".content-header").remove()
</script>
<div class="jumbotron text-justify text-center" style="padding-top:20,padding-bottom:20">
        <img src="${baseURL}/public/html/Logo PathogenTARGET.jpg" />
        
      </div>
<div class="page-header">
								
				Target-Pathogen database is a bioinformatic approach to prioritize drug targets in pathogens. Available genomic data for pathogens has created new opportunities for drug discovery and development, including new species, resistant and multiresistant ones. However, this data must be cohesively integrated to be fully exploited and be easy to interrogate. Target-Pathogen has been designed and developed as an online resource to allow genome wide based data consolidation from diverse sources focusing on structural druggability, essentiality and metabolic role of proteins. By allowing the integration and weighting of this information, this bioinformatic tool aims to facilitate the identification and prioritization of candidate drug targets for pathogens. With the structurome and drugome information Target-Pathogen is a unique resource to analyze whole genomes of relevants pathogens.
</div>

<section class="col-lg-6" >
<a href="${baseURL}/genome/" class="btn btn-primary btn-lg btn-block">Select Your Genome</a>
</section>
	

<section class="col-lg-6" >
<a href="${baseURL}/login" class="btn btn-success btn-lg btn-block">LogIn</a>
</section>

<br />
<br />
<br />
<br />
 
<section class="col-lg-12" >
			<p>
			<a href="https://academic.oup.com/nar/article/doi/10.1093/nar/gkx1015/4584621">
			<i>Target-Pathogen: a structural bioinformatic approach to prioritize drug targets in pathogens</i></a>
			: Ezequiel J. Sosa,  Germ&#225;n Burguener,  Esteban Lanzarotti,  Lucas Defelipe, Leandro Radusky,  Agust&#237;n M. Pardo,  Marcelo Marti,  Adri&#225;n G. Turjanski, Dar&#237;o Fern&#225;ndez Do Porto <br />
			</p>
			<b>Nucleic Acids Research</b> (2017)  Database Issue 

</section>

<br />
<hr />
<br />


</body>
	</html>
</jsp:root>