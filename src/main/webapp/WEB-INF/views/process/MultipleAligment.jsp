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

<meta name="header_title" content="MSA" />
<meta name="header_title_desc" content="Multiple Sequence Alignment" />

<meta name="_csrf" content="${_csrf.token}" />
<!-- default header name is X-CSRF-TOKEN -->
<meta name="_csrf_header" content="${_csrf.headerName}" />

<title>Blast</title>

<style type="text/css">
.box .box-header {
	padding-bottom: 10px;
}
</style>

<!-- DATA TABLES -->
<link href="${baseURL}/public/theme/css/datatables/dataTables.bootstrap.css"
	rel="stylesheet" type="text/css" />

</head>
<body>

	<script type="text/javascript">
		//<![CDATA[
	</script>

	<!-- DATA TABES SCRIPT -->
	<script
		src="${baseURL}/public/theme/js/plugins/datatables/jquery.dataTables.js"
		type="text/javascript"></script>
	<script
		src="${baseURL}/public/theme/js/plugins/datatables/dataTables.bootstrap.js"
		type="text/javascript"></script>


	<link rel="stylesheet"
		href="${baseURL}/public/biojs/resources/dependencies/jquery/jquery-ui-1.8.2.css" />
	<link rel="stylesheet"
		href="${baseURL}/public/biojs/resources/dependencies/jquery/jquery.tooltip.css" />


	<script src="${baseURL}/public/jslibs/jquery/jquery-migrate-1.2.1.js"></script>

	<script src="${baseURL}/public/biojs/msa.min.js" type="text/javascript"></script>


	<!-- 	<script language="JavaScript" type="text/javascript"
		src="${baseURL}/public/biojs/Biojs.js"></script>

	<script language="JavaScript" type="text/javascript"
		src="${baseURL}/public/biojs/Biojs.Tooltip.js"></script>

	<script language="JavaScript" type="text/javascript"
		src="${baseURL}/public/biojs/Biojs.Sequence.js"></script> -->




	<script type="text/javascript">
		function presentDatabseString(str) {
			return str.charAt(0).toUpperCase() + str.slice(1).replace("_", " ");
		};

		var query_job_status = function(job_id) {

			$.getJSON("${baseURL}/msa/" + job_id).done(waiting_result).fail(
					function(jqxhr, textStatus, error) {
						var err = textStatus + ", " + error;
						console.log("Request Failed: " + err);
					});
		};

		var waiting_result = function(job) {

			if (job.status != "finished" && job.status != "error") {
				$("#msa-div").html(job.status)
				window.setTimeout(query_job_status, 6000, job.id);
			} else {
				$("#download_btn").removeAttr("disabled").removeClass(
						".disabled");

				$(".loading-img").remove();
				$(".overlay").remove();
				if (job.status == "finished") {
					load_aln(job.result)
				} else {
					$("#msa-div").html(job.result[0].hit);
				}

			}

		};

		function load_fasta(fasta_txt) {
			$("#fasta_txt").html(fasta_txt);
			$("#fasta_txt").width($("#fasta_txt").parent().width() - 40)

		}

		function until_proteins_loaded() {
			$("#msa_protein_list_table").html("");
			if (typeof window.proteins == "undefined") {
				window.setTimeout(until_proteins_loaded, 1000);
			} else {
				var id_list = [];
				$
						.each(
								window.proteins.values,
								function(name, id) {
									id_list.push(id);
									$("#msa_protein_list_table")
											.append(
													'<tr><td><a href="'
															+ $.api.url_protein( id)
															+ '">'
															+ name
															+ '</a></td><td><input checked="checked"  type="checkbox" onchange="addProt(\''
															+ name
															+ '\',\''
															+ id
															+ '\',$(this).is(\':checked\'))" />'
															+ "</td></tr>");
								})
				if (id_list.length == 0) {
					return;
				}
				$.post("${baseURL}/protein/fasta_from_ids", {
					"id_list" : id_list,
					'${_csrf.parameterName}' : '${_csrf.token}'
				}).done(load_fasta)

			}

		}

		var document_ready_fn = function() {

			if (typeof $.QueryString["job"] != "undefined") {
				query_job_status($.QueryString["job"])
				$("#prot_list_row").remove();
				return

			}

			$("#download_btn").attr("disabled", "disabled").addClass(
					".disabled");

			var proteins = [];

			until_proteins_loaded();
			$("#update_fasta_btn").click(until_proteins_loaded);
			$("#malign_btn")
					.click(
							function(evt) {
								$(this).attr("disabled", "disabled").addClass(
										".disabled");
								$
										.post(
												"${baseURL}/msa/",
												{
													fasta : $("#fasta_txt")
															.html(),
													'${_csrf.parameterName}' : '${_csrf.token}'
												})
										.done(
												function(job) {
													waiting_result(job);
													$("#box-msa")
															.append(
																	'<div class="overlay">&#160;</div><div class="loading-img">&#160;</div>');

												});
							});

		};
		$(document).ready(document_ready_fn);

		function load_aln(data) {

			$("#box-msa > .loading-img").remove();
			$("#box-msa > .overlay").remove();

			var msa = require("msa");
			var opts = {
				el : document.getElementById('msa-div')
			};
			opts.vis = {
				conserv : true,
				overviewbox : false,
				seqlogo : false,
				labelId : false,
				labelName : true
			};
			opts.seqs = data;
			opts.colorscheme = {
				"scheme" : "hydro"
			};
			opts.zoomer = {
				labelNameLength : 220
			};
			var m = msa(opts);

			/* var menuOpts = {};
			menuOpts.msa = m;
			var defMenu = new msa.menu.defaultmenu(menuOpts); */
			//m.addView("menu", defMenu); 
			m.render();

			$("#download_btn").click(function(evt) {
				msa.utils.export.saveAsFile(m, "aln.fasta")
			});

			//$("#msa-protein-name").html(data.name);

			//borra loading

		}
	</script>

	<script type="text/javascript">
		// ]]>
	</script>

	<div id="prot_list_row" class="row">
		<section class="col-lg-12 connectedSortable">
			<div class="box box-primary">
				<div class="box-header">


					<i class="fa fa-ruble">&#160;</i>
					<h3 class="box-title">Protein List</h3>

					<div class="pull-left box-tools">


						<button class="btn btn-info" id="update_fasta_btn"
							title="Updates the fasta to align with the current selection">Delete
							Unselected</button>
						<button class="btn btn-info" id="malign_btn"
							title="Runs the clustal algorithm">Run Aligment</button>



					</div>

				</div>
				<div class="box-body no-padding">

					<table>

						<tbody id="msa_protein_list_table">

						</tbody>
					</table>
					<textarea rows="10" id="fasta_txt">...</textarea>

				</div>
			</div>
		</section>
	</div>

	<div class="row">
		<section class="col-lg-12 connectedSortable">
			<div id="box-msa" class="box box-primary">


				<div class="box-header">
					<i class="fa fa-align-center">&#160;</i>
					<h3 class="box-title">Results</h3>
					<div class="pull-left box-tools">


						<button id="download_btn" class="btn btn-info"
							title="Download Result in fasta format">Download Aln</button>

					</div>
				</div>

				<div class="box-body no-padding">
					<div id="msa-div">...</div>
				</div>
			</div>
		</section>
	</div>


</body>
	</html>
</jsp:root>