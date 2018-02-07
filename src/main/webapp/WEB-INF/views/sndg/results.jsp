<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page"
	xmlns:spring="http://www.springframework.org/tags"
	xmlns:c="http://java.sun.com/jsp/jstl/core"
	xmlns:form="http://www.springframework.org/tags/form"
	xmlns:fn="http://java.sun.com/jsp/jstl/functions"
	xmlns:sec="http://www.springframework.org/security/tags" version="2.0">
	<jsp:directive.page language="java" contentType="text/html" />

	<c:set var="reqUrl">${pageContext.request.requestURL}</c:set>
	<c:set var="baseURL"
		value="${fn:replace(reqUrl, pageContext.request.requestURI,pageContext.request.contextPath)}" />


	<html>
<head>

<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta name="header_title" content="Resultados" />

</head>
<body>

	<script type="text/javascript">
		//<![CDATA[
	</script>

	<script type="text/javascript">
	
	var urlMap = {
			"seq": x => "${baseUrl}/sndg/protein/" + x._id,
			"genome":x => "${baseUrl}/sndg/genome/" + x.name,
			"prot":x => "${baseUrl}/sndg/protein/" + x._id,
			"struct":x => "${baseUrl}/sndg/structure/" + x.name,
			"barcode":x => "${baseUrl}/sndg/barcode/" + x.processid,
			
			"tool": x => x.url
	}
	
	$.maxVisiblePages = 5
	$.pageSize = 10;
	$.page = ${page};
	
	function searchUrl(page,pageSize){
		
		if(pageSize == undefined){
			pageSize = $.pageSize
		}
		
		return ('${baseURL}/search/results?type=${datatype}&query=' + $("#searchInput").val() + 
				   "&pageSize=" + pageSize.toString() + "&start=" +  (page*pageSize).toString() );
	}
	
	
		function init() {

			$('#searchBtn').click(function(evt) {
				evt.preventDefault()
				window.location.href = searchUrl(0)
			})

			$("#searchInput").keyup(function(evt) {
				if (evt.keyCode == 13) {
					evt.preventDefault()
					window.location.href = searchUrl(0)

				}
			});

			var request = new Request('${baseUrl}/sndg/search/data_result?' + window.location.href.split("?")[1], {
				headers : new Headers({
					'Content-Type' : 'application/json'
				})
			});
			fetch(request).then(function(response) {
				return response.json();
			}).then(function(res) {
				var resultTableBody = $("#resultTableBody");
				resultTableBody.empty();
				
				res.data.forEach((record,i) => {
					var tr = $("<tr />").appendTo(resultTableBody);
					$("<td />").appendTo(tr).html(i + 1 + ($.page*$.pageSize))
					var name = record.name;
					if (record.ncbi_assembly){
						name = record.ncbi_assembly; 
					}
					if (record.processid){
						name = record.processid; 
					}
						
					$("<td />").appendTo(tr).append( $("<a />",{href: urlMap['${datatype}']( record) }).html(  name))
					$("<td />").appendTo(tr).html(record.description)
					$("<td />").appendTo(tr).html(record.organism)
				});
				
				
			     var prev = $("<li />").appendTo(".pagination").append(
					$("<a />",{"aria-label":"Previous", "href": searchUrl($.page -1)})	
							.append($("<span />" ).html("&#171;")   ) 
					
				)
				
				if($.page == 0){
					prev.addClass("disabled");
				} 
				
			     var idx = 0;
			     if (($.page ) == $.maxVisiblePages ){
			    	 idx = $.page 
			    	 $("<li />").appendTo(".pagination").append(
								$("<a />")	
										.append($("<span />" ).html( "...")   ) ) ;
			    	 
			     }
			     
			     while((idx < res.recordsFiltered / $.pageSize) &&  ( (idx - $.page) < $.maxVisiblePages) ){			    	 
			    	 //<li class="active"><a href="#">1 <span class="sr-only">(current)</span></a></li>		
			    	 var li = $("<li />").appendTo(".pagination").append(
								$("<a />",{ "href": searchUrl(idx) })	
										.append($("<span />" ).html(idx  + 1)   )
							);
			    	 
			    	 if(idx == $.page){
			    		 li.addClass("active");
			    	 }
			    	 
			    	 idx++;		
			     } 
			     if(idx < res.recordsFiltered / $.pageSize){
			    	 $("<li />").appendTo(".pagination").append(
								$("<a />")	
										.append($("<span />" ).html( "...")   ) ) ;
			     }
			     
			     var next = $("<li />").appendTo(".pagination").append(
							$("<a />",{"aria-label":"Next", "href": searchUrl(idx)})	
									.append($("<span />" ).html("&#187;")   )
						);
			     
				if($.page ==  Math.floor(res.recordsFiltered / $.pageSize) ){
					next.addClass("disabled")
						}				
				
				$("<li />").appendTo(".pagination").html('&#160; &#160; &#160; Total: <b> ' +  res.recordsFiltered.toString() + ' </b>' );								
				
			});

		}

		$(document).ready(init);
	</script>

	<script type="text/javascript">
		// ]]>
	</script>

	<div class="row">

		<section class="col-lg-12">

			<table width="100%">
				<tr>

					<td width="100%"><input id="searchInput" width="100%"
						type="text" value="${query}" class="form-control" /></td>
					<td><button id="searchBtn" class="btn btn-info">
							<i class="fa fa-search">&#160;</i>
						</button></td>
				</tr>

			</table>
			<br />
		</section>
	</div>
	<div class="row">

		<section class="col-lg-12">

			<div class="panel panel-default">
				<div class="panel-heading">Resultados</div>
				<table class="table">
					<thead>
						<tr>
							<th>#</th>
							<th>Nombre</th>
							<th>Descripcion</th>
							<th>Detalles</th>
						</tr>
					</thead>
					<tbody id="resultTableBody">

					</tbody>
				</table>
				<ul class="pagination">


				</ul>

			</div>

		</section>
	</div>

</body>
	</html>
</jsp:root>
