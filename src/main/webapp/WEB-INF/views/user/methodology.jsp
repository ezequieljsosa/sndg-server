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


<meta name="header_title" content="Pathway" />
<meta name="header_title_desc" content="reactions in pathway" />

<meta name="_csrf" content="${_csrf.token}" />
<meta name="_csrf_header" content="${_csrf.headerName}" />

<title>Methodology</title>


</head>
<body>



	<script src="${baseURL}/public/jslibs/pdf.js" type="text/javascript">
		-
	</script>
	<script src="${baseURL}/public/jslibs/pdf.worker.js"
		type="text/javascript">
		-
	</script>


	<script type="text/javascript">
		<![CDATA[
		var url = '${baseURL}/public/docs/TargetMethodology.pdf';
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


        ]]>
	</script>


	<script type="text/javascript">
		$("body").addClass("container")
		$(".content-header").remove()
	</script>

	<nav class="col-sm-2 d-none d-md-block bg-light sidebar">
		<div class="sidebar-sticky">

			<ul id="rss-feeds">


			</ul>

		</div>
	</nav>

	<a href="${baseURL}/public/docs/TargetMethodology.pdf">Download
		methodology</a>

	<div id="the-canvas">-</div>



</body>
	</html>
</jsp:root>