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


<meta name="_csrf" content="${_csrf.token}" />
<!-- default header name is X-CSRF-TOKEN -->
<meta name="_csrf_header" content="${_csrf.headerName}" />

<title>SNDG</title>

</head>

<body>


<script type="text/javascript">
		//<![CDATA[
	</script>
	
		<script src="${baseURL}/public/widgets/chartjs/Chart.min.js"
		type="text/javascript"></script>

	<script type="text/javascript">
		$("body").addClass("container")
		$(".content-header").remove()
		
		function searchUrl(){
			window.location.href='${baseURL}/search/results?type=' +  $('#search_select').val() + "&query=" + $("#searchInput").val(); 
		}
		
		function init(){			
			
			
			var ctx = document.getElementById('myChart').getContext('2d');
			var chart = new Chart(ctx, {
			    
			    type: 'bar',

			    data: {
			        labels: ${tooltypes},
			        datasets: [{
			            label: "Tipos de Herramientas",
			            //backgroundColor: 'rgb(255, 99, 132)',
			            //borderColor: 'rgb(255, 99, 132)',
			            data: ${toolvalues},
			        }]
			    },		    
			    options: {
			    	legend: {
			           // display: false
			        },
			        tooltips: {
			            callbacks: {
			               label: function(tooltipItem) {
			                      return tooltipItem.yLabel;
			               }
			            }
			        }
			    }
			});
			
			
			
			$('#searchBtn').click(function(evt){
				evt.preventDefault()
				searchUrl()
			})
			
			$("#searchInput").keyup(
					function(evt) {
						if (evt.keyCode == 13) {
							evt.preventDefault()
							searchUrl();
							
						}
					});
			
		}
		
		$(document).ready(init);		
	</script>
	

	
	
	<script type="text/javascript">
		// ]]>
	</script>
	
	
	
	
	

	<div class="jumbotron text-justify text-center"
		style="height: 600px; background-color: white">
		<div class="row">
			<section class="col-lg-6">
				<img alt=""
					src="http://datos.sndg.mincyt.gob.ar/assets/logo_sistemas_nacionales-48dd30357d8c0e7dc192ba175f34427a.png" />

				<form class="form-inline">
					<div class="form-group">
						<table width="100%">
							<tr>
								<td width="100px"><select id="search_select" class="form-control">
										<option value="all">Todo</option>										
										<option value="prot">Proteinas</option>
										<option value="genome">Genomas</option>
										<option value="struct">Estructuras</option>
										<option value="barcode">Barcodes</option>
										<option value="tool">Herramientas</option>
									<option value="bioproject">Proyecto</option>

								</select></td>
								<td width="100%"><input width="100%" type="text" id="searchInput"
									class="form-control" /></td>
								<td><button class="btn btn-info" id="searchBtn">
										<i class="fa fa-search">&#160;</i>
									</button></td>
							</tr>

						</table>
						<small class="form-text text-muted">Ejemplo de
							b&#250;squedas: <a href="search/results?type=all&#38;query=gyra">gyrA</a> &#160;
							<a href="search/results?type=all&#38;query=tuberculosis">tuberculosis</a> &#160;
							<a href="search/results?type=barcode&#38;query=COI-5P">COI-5P</a>
							
							
							</small> <br />

						<button style="display: none" class="btn info">
							<i class="fa fa-cloud-upload">&#160;</i>Depositar Datos
						</button>

						&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;<br />
						&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;<br />
						&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;<br />
						&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;<br />
						&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;<br />
						&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;<br />
						&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;<br />






					</div>
				</form>
			</section>
			<section class="col-lg-3">

				<div class="small-box bg-aqua">
					<div align="left" class="inner">
						<h3> ${generalStats.genome}</h3>
						<p>Genomas</p>
					</div>
					<div class="icon">
						<i class="fa fa-circle-o">&#160;</i>
					</div>
					<a href="${baseURL}/search/results?type=genome" class="small-box-footer"> &#160; <i
						class="fa fa-arrow-circle-right">&#160;</i>
					</a>

				</div>
			</section>
			<section  class="col-lg-3">
				<div class="small-box bg-aqua">
					<div align="left" class="inner">
						<h3>${generalStats.prot}</h3>
						<p>Proteinas</p>
					</div>
					<div class="icon">
						<i class="fa fa-file-text">&#160;</i>
					</div>
					<a href="${baseURL}/search/results?type=prot" class="small-box-footer"> &#160; <i
						class="fa fa-arrow-circle-right">&#160;</i>
					</a>

				</div>
			</section>
			<section class="col-lg-3">
				<div class="small-box bg-aqua">
					<div align="left" class="inner">
						<h3>${generalStats.tool}</h3>
						<p>Herramientas</p>
					</div>
					<div class="icon">
						<i class="fa fa-wrench">&#160;</i>
					</div>
					<a href="${baseURL}/search/results?type=tool" class="small-box-footer"> &#160; <i
						class="fa fa-arrow-circle-right">&#160;</i>
					</a>

				</div>
			</section>
			<section class="col-lg-3">
				<div class="small-box bg-aqua">
					<div align="left" class="inner">
						<h3 id="struct_count">${generalStats.struct}</h3>
						<p>Estructuras</p>
					</div>
					<div class="icon">
						<i class="fa fa-sitemap">&#160;</i>
					</div>
					<a href="${baseURL}/search/results?type=struct" class="small-box-footer"> &#160; <i
						class="fa fa-arrow-circle-right">&#160;</i>
					</a>

				</div>

			</section>
			<section style="float: right !important;" class="col-lg-3">
				<div class="small-box bg-aqua">
					<div align="left" class="inner">
						<h3>${generalStats.seq}</h3>
						<p>Secuencias <br /> Ensambladas</p>
					</div>
					<div class="icon">
						<i class="fa fa-file-text">&#160;</i>
					</div>
					<a href="${baseURL}/search/results?type=seq" class="small-box-footer"> &#160; <i
						class="fa fa-arrow-circle-right">&#160;</i>
					</a>

				</div>

			</section>
			<section style="float: right !important;" class="col-lg-3">
				<div class="small-box bg-aqua">
					<div align="left" class="inner">
						<h3>${generalStats.barcode}</h3>
						<p>Barcodes</p>
					</div>
					<div class="icon">
						<i class="fa fa-barcode">&#160;</i>
					</div>
					<a href="${baseURL}/search/results?type=barcode" class="small-box-footer"> &#160; <i
						class="fa fa-arrow-circle-right">&#160;</i>
					</a>

				</div>

			</section>

			<section style="float: right !important;" class="col-lg-3">
				<div class="small-box bg-aqua">
					<div align="left" class="inner">
						<h3>${generalStats.bioproject}</h3>
						<p>Proyectos</p>
					</div>
					<div class="icon">
						<i class="fa fa-calendar">&#160;</i>
					</div>
					<a href="${baseURL}/search/results?type=bioproject" class="small-box-footer"> &#160; <i
							class="fa fa-arrow-circle-right">&#160;</i>
					</a>

				</div>

			</section>

		</div>

	</div>

	<br />
	<section class="col-lg-6" >
	<div width="100%" id="stats_genome">
	<![CDATA[
		<iframe id="stats_prot_iframe" height="400px" width="100%"
			src="${baseURL}/krona/stats/tax_genome.html?depth=4">.</iframe>
		]]>
	</div>
	</section>
	<section class="col-lg-6">
	<div width="100%" id="stats_prot">
	<![CDATA[
		<iframe id="stats_prot_iframe" height="400px" width="100%"
			src="${baseURL}/krona/stats/tax_prot.html?depth=4">.</iframe>
		]]>
	</div>
	</section>
	
	<section class="col-lg-6">
	<div width="100%" id="stats_struct">
	<![CDATA[
		<iframe id="stats_struct_iframe"  height="400px" width="100%"
			src="${baseURL}/krona/stats/tax_struct.html?depth=4">.</iframe>
		]]>
	</div>
	</section>
	<section class="col-lg-6">
	<div width="100%" id="stats_barcode">
	<![CDATA[
		<iframe id="stats_barcode_iframe"  height="400px" width="100%"
			src="${baseURL}/krona/stats/tax_barcode.html?depth=4">.</iframe>
		]]>
	</div>
	</section>
	<section class="col-lg-6">
	<div width="100%" id="stats_seq">
	<![CDATA[
		<iframe id="stats_seq_iframe"  height="400px" width="100%"
			src="${baseURL}/krona/stats/tax_seq.html?depth=4">.</iframe>
		]]>
	</div>
	</section>
	
	<section class="col-lg-6">
	<div width="100%" id="stats_tool">
	<![CDATA[
		<canvas id="myChart"></canvas>
		]]>
	</div>



	</section>



	<br />
	<hr />
	<br />
	
	
	


</body>
	</html>
</jsp:root>