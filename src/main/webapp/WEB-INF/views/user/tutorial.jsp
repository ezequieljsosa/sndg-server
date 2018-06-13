<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page"
	xmlns:spring="http://www.springframework.org/tags"
	xmlns:c="http://java.sun.com/jsp/jstl/core"
	xmlns:form="http://www.springframework.org/tags/form"
	xmlns:fn="http://java.sun.com/jsp/jstl/functions"
	xmlns:sec="http://www.springframework.org/security/tags" version="2.0">
	<jsp:directive.page language="java" contentType="text/html" />

		<c:set var="reqUrl">${pageContext.request.requestURL}</c:set>
	<c:set var="baseURL" value="${fn:replace(reqUrl, pageContext.request.requestURI,pageContext.request.contextPath)}" />

	<html xmlns="http://www.w3.org/1999/xhtml" lang="en" xml:lang="en">
<head>


<meta name="header_title" content="Pathway" />
<meta name="header_title_desc" content="reactions in pathway" />

<meta name="_csrf" content="${_csrf.token}" />
<meta name="_csrf_header" content="${_csrf.headerName}" />

<title>Tutorial</title>


</head>

<script
		src="${baseURL}/public/jslibs/pdf.js"
		type="text/javascript"> - </script>
<script
		src="${baseURL}/public/jslibs/pdf.worker.js"
		type="text/javascript"> - </script>

		
		<script type="text/javascript">
		<![CDATA[
var url = '${baseURL}/public/docs/Targettutorial.pdf';
var loadingTask = PDFJS.getDocument(url);
loadingTask.promise.then(function(pdf) {
	console.log('PDF loaded');

	// Fetch the first page

	function renderPage(page) {
		console.log('Page loaded');

		var scale = 1.5;
		var viewport = page.getViewport(scale);

		// Prepare canvas using PDF page dimensions
		var canvas = document.createElement("CANVAS")
		pepe.appendChild(canvas)
		var context = canvas.getContext('2d');
		canvas.height = viewport.height;
		canvas.width = viewport.width;

		// Render PDF page into canvas context
		var renderContext = {
			canvasContext : context,
			viewport : viewport
		};
		var renderTask = page.render(renderContext);
		renderTask.then(function() {
			if (pageNumber < pdf.numPages) {
				pageNumber = pageNumber + 1;
				pdf.getPage(pageNumber).then(renderPage);
			}
			console.log('Page rendered');
		});
	}

	var pepe = document.getElementById('the-canvas');
	//for(var pageNumber = 1; pageNumber    <=  pdf.numPages ; pageNumber++){
	var pageNumber = 1;
	pdf.getPage(pageNumber).then(renderPage);
	//}

}, function(reason) {
	// PDF loading error
	console.error(reason);
});
]]>
		</script>

		
<script type="text/javascript">
$("body").addClass("container")
$(".content-header").remove()
</script>
<h1>Interactive tutorials</h1>
<ul>
<li><a href="${baseURL}/genome?tour=1">Target priorization </a></li>
<li><a href="${baseURL}/genome?tour=6">Pathway priorization</a></li>
<li><a href="${baseURL}/genome/H37Rv?tour=2">Data upload</a> (Register First)</li>
<li style="display: none;"><a href="${baseURL}/genome/H37Rv?tour=3">Protein search and navigation</a></li>
<li style="display: none;" ><a href="${baseURL}/genome/H37Rv?tour=4">Strain proteins filter</a></li>
<li style="display: none;" ><a href="${baseURL}/genome/H37Rv?tour=5">Variant search</a></li>
</ul>
<h1>Complete example tutorial</h1>
<a href="${baseURL}/public/docs/Targettutorial.pdf">Download tutorial</a>

<div id="the-canvas">-</div>

	</html>
</jsp:root>
