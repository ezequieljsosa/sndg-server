<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page"
	xmlns:spring="http://www.springframework.org/tags"
	xmlns:c="http://java.sun.com/jsp/jstl/core"
	xmlns:form="http://www.springframework.org/tags/form" version="2.0">
	<jsp:directive.page language="java" contentType="text/html" />

	<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />

<meta name="header_title" content="Blast" />
<meta name="header_title_desc" content="Blast Protein sequence" />

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
<link href="/xomeq/public/theme/css/datatables/dataTables.bootstrap.css"
	rel="stylesheet" type="text/css" />

</head>
<body>

	<script type="text/javascript">
		//<![CDATA[
	</script>

	<!-- DATA TABES SCRIPT -->
	<script
		src="/xomeq/public/theme/js/plugins/datatables/jquery.dataTables.js"
		type="text/javascript"></script>
	<script
		src="/xomeq/public/theme/js/plugins/datatables/dataTables.bootstrap.js"
		type="text/javascript"></script>


	<link rel="stylesheet"
		href="/xomeq/public/biojs/resources/dependencies/jquery/jquery-ui-1.8.2.css" />
	<link rel="stylesheet"
		href="/xomeq/public/biojs/resources/dependencies/jquery/jquery.tooltip.css" />


	<script src="/xomeq/public/jslibs/jquery/jquery-migrate-1.2.1.js"></script>

	<script language="JavaScript" type="text/javascript"
		src="/xomeq/public/biojs/Biojs.js"></script>

	<script language="JavaScript" type="text/javascript"
		src="/xomeq/public/biojs/Biojs.Tooltip.js"></script>

	<script language="JavaScript" type="text/javascript"
		src="/xomeq/public/biojs/Biojs.Sequence.js"></script>

	<script src="/xomeq/public/bia/ui/ProteinOverview.js"></script>


	<script type="text/javascript">
		//var organisms = JSON.parse('${organisms}') ;
		var jobObj = ${job};
		var seq = ${seq};
		var seq_name = (seq != null) ? seq.name : "Custom sequence";
		var start = ${start};
		var end = ${end};

		function presentDatabseString(str) {
			return str.charAt(0).toUpperCase() + str.slice(1).replace("_", " ");
		};

		var load_genomes = function(genomes_from_user) {
			$("#seq_overlay").remove();
			$("#seq_loading").remove();

			$.each(genomes_from_user, function(index, organism) {
				if (genome.type == "peptide_collection") {
					$("#genomes_select").append(
							'<option selected="selected" value="' + organism+ '">'
									+ organism + '</option>');
				}

			});
			$("#option_to_delete").remove();
		};

		var query_job_status = function(job) {
			$.api.blastJobStatus(job, waiting_result)

		};

		var waiting_result = function(job) {

			if (job.status != "finished" && job.status != "error") {
				$("#wait_messages").append(
						"Job " + job.id + " not finished, retrying...");
				window.setTimeout(query_job_status, 6000, job.id);
			} else {
				$(".btn").removeAttr("disabled").removeClass(".disabled");
				$("#wait_messages").remove();
				$(".loading-img").remove();
				$(".overlay").remove();
				if (job.status == "finished") {
					$("#results_body")
							.html(
									'<table width="100%" class="table table-striped"  id="results_table"><thead><th>Hit</th><th>Length</th><th>Score</th><th>Identity</th><th>Evalue</th></thead><tbody></tbody></table>');
					$("#results_table")
							.show()
							.dataTable(
									{
										"data" : job.result,
										"columns" : [
												
												{
													"title" : "Hit",
													"data" : "hit",
													"render" : function(data,
															type, full, meta) {

														var id = data
																.split("_")[0];
														var name = data
																.replace(id
																		+ "_",
																		"");
														return '<a href="'
																+ $.api
																		.url_protein_gene(id)
																+ '">' + name
																+ '</a>';
													}
												}, {
													"title" : "Length",
													"data" : "length"
												}, {
													"title" : "Score",
													"data" : "score"
												}, {
													"title" : "Identity",
													"data" : "identity"
												}, {
													"title" : "Evalue",
													"data" : "evalue"
												} ]
									});
				} else {
					$("#results_body").html(job.result[0].hit);
				}

			}

		};

		var document_ready_fn = function() {

			if (jobObj == null){
				$("#overview_row").remove();
			} else  {
				waiting_result(jobObj)
				if ((seq != null) && (seq != "?")) {
					var prot_overview = new $.ProteinOverview($('#overview_table'),
							seq, $.api);
					prot_overview.init();
				} else {
					$("#overview_row").remove();
				}
				$("#options_row").remove();
				return;
			}

			
			
			if (seq != null) {
				$("#copy_area").val(seq.seq);
			} else {
				$("#sequence_box").remove();
				$("#blast_button").remove();
				$("#end_seq_input").remove();
				$("#start_seq_input").remove();
			}

			var mySequence = new Biojs.Sequence({
				sequence : ".",
				target : "sequence_box",
				format : 'FASTA',
				id : seq_name
			});

			var load_sequence = function(seq_name, seq) {
				mySequence.setSequence(seq, seq_name);

			};
			var load_dna_fn = function(dna) {

				

				
				$("#start_seq_input").val(0);
				$("#end_seq_input").val(dna.length);

				if (end != -1) {

					mySequence.setSelection(start, end);
					$("#start_seq_input").val(start);
					$("#end_seq_input").val(end);
				}
			};

			mySequence.onSelectionChanged(function(objEvent) {
				$("#start_seq_input").val(objEvent.start);
				$("#end_seq_input").val(objEvent.end);
				$("#copy_area").val(
						mySequence.opt.sequence.substring(objEvent.start - 1,
								objEvent.end));
			});

			var run_blast = function() {

				$("#results_table").remove();
				$("#result_box")
						.append(
								'<div class="overlay">&#160;</div><div class="loading-img">&#160;</div>');
				$(".btn").attr("disabled", "disabled").addClass(".disabled");

				//Collapse params 
				var box = $("#params_box");
				var bf = box.find(".box-body, .box-footer");
				if (!box.hasClass("collapsed-box")) {
					box.addClass("collapsed-box");
					bf.slideUp();
				}

				var params = {

					'${_csrf.parameterName}' : '${_csrf.token}',
					"seq" : $("#copy_area").val(),
					"genomes" : [ $("#genomes_select").val() ],
			
					"gap_open" : parseInt($("#gap_open_input").val()),
					"gap_extend" : parseInt($("#gap_extend_input").val()),
					"max_evalue" : parseFloat($("#evalue_select").val()),
					"low_complexity" : $("#low_complexity_select").val() == "Y",
					"max_results" : parseInt($("#results_select").val())

				};

				$.api.blastnJob(params, waiting_result);

			}
		

			$("#blast_button").click(function(evt) {
				$("#copy_area").val(mySequence.opt.sequence)
				if ($("#genomes_select").val() == null) {
					alert("No genome was selected");
					return;
				}
				run_blast();

			});

			$("#blast_selected_button").click(function(evt) {
				if ($("#genomes_select").val() == null) {
					alert("No genome was selected");
					return;
				}
				if ($("#copy_area").val().trim() == "") {
					alert("Nothing is selected from the sequence");
					return;
				}
				run_blast();

			});

			if ((seq != null) && (seq != "?")) {

				load_dna_fn(seq)

			} else {
				$("#overview_row").remove();
			}

		};
		$(document).ready(document_ready_fn);
	</script>

	<script type="text/javascript">
		// ]]>
	</script>

	<div id="overview_row" class="row">
		<section class="col-lg-12 connectedSortable">
			<div class="box box-primary">
				<div class="box-header">

					<i class="fa  fa-info-circle">&#160;</i>
					<h3 class="box-title">Overview</h3>
				</div>
				<div class="box-body no-padding">
					<div class="table-responsive">
						<!-- .table - Uses sparkline charts-->
						<table id="overview_table" class="table table-striped">
							<tr>
								<td>Organism/Strain</td>
								<td id="organism">-</td>
							</tr>						
							
							<tr id="gene_row">
								<td>Gene</td>
								<td id="gene_link">-</td>
							</tr>

							<tr>
								<td>Status</td>
								<td id="status">-</td>
							</tr>

							<tr>
								<td>Length</td>
								<td id="strSize">??</td>
							</tr>
							<tr>
								<td>Description</td>
								<td id="prot_desc"></td>
							</tr>
							
						</table>
						<!-- /.table -->

					</div>
				</div>

			</div>
		</section>
	</div>

	<div id="options_row" class="row">
		<section class="col-lg-6 connectedSortable">
			<div class="box box-primary">
				<div class="box-header">

					<div class="pull-right box-tools">
						<div class="input-group">
							<button class="btn btn-info" id="blast_button"
								title="Runs the blast algorithm">Blast whole sequence</button>
							<input style="width: 40px" id="start_seq_input" type="number"
								readonly="readonly" /> <input style="width: 40px"
								id="end_seq_input" type="number" readonly="readonly" />
							<button class="btn btn-info" id="blast_selected_button"
								title="Runs the blast algorithm">Blast On selected
								Sequence</button>



						</div>
					</div>
					<i class="fa fa-map-marker">&#160;</i>
					<h3 class="box-title">Sequence</h3>
				</div>
				<div class="box-body no-padding">
					<div id="sequence_box"></div>
				</div>

				<div id="seq_overlay" class="overlay">&#160;</div>
				<div id="seq_loading" class="loading-img">&#160;</div>

			</div>
			<!-- /.box -->
			<div class="box box-primary">


				<div class="box-body no-padding">
					<div class="form-group">
						<label>Selected Sequence</label>
						<textarea id="copy_area" class="form-control" rows="5"
							placeholder="Enter ...">--</textarea>
					</div>

				</div>
				<div class="body-footer">
					<div style="color: red;" id="wait_messages"></div>
				</div>
			</div>
			<!-- /.box -->
		</section>
		<section class="col-lg-6 connectedSortable">
			<div class="box box-primary">
				<div class="box-body">
					<div class="form-group">
						<label>Databases</label>
						<!-- <select  multiple=""
							class="form-control">
							<option id="option_to_delete"></option>
						</select> -->

						<select id="genomes_select" class="form-control">
							<option value="My Genomes">My Genomes</option>
							<option value="Public Genomes">Public Genomes</option>	
						</select>
						
						<label>Search in </label>
						

						<select id="search_in_select" class="form-control">
							<option value="genes">Genes</option>
							<option value="contigs">Entire genome</option>								
						</select>

					</div>
				</div>
			</div>
			<div id="params_box" class="box box-primary">
				<div class="box-header" style="cursor: move;">
					<!-- tools box -->
					<div class="pull-right box-tools">

						<button class="btn btn-info btn-sm" data-widget="collapse"
							data-toggle="tooltip" title="" data-original-title="Collapse">
							<i class="fa fa-minus"> &#160;</i>
						</button>

					</div>
					<!-- /. tools -->
					<h3 class="box-title">Parameters</h3>


				</div>



				<div class="box-body">

		
					<div class="form-group">
						<label>Gap open</label> <input id="gap_open_input" value="11"
							class="form-control" type="number" />
					</div>
					<div class="form-group">
						<label>Gap extend</label> <input id="gap_extend_input" value="1"
							class="form-control" type="number" />
					</div>
					<div class="form-group">
						<label>Max evalue</label> <select id="evalue_select"
							class="form-control">
							<option>1.0E-100</option>
							<option>1.0E-10</option>
							<option>1.0E-5</option>
							<option>0.01</option>
							<option>0.1</option>
							<option>1</option>
							<option selected="selected">10</option>
							<option>100</option>
						</select>
					</div>
					<div class="form-group">
						<label>Low complexity filter</label> <select
							id="low_complexity_select" class="form-control">
							<option>Yes</option>
							<option>No</option>

						</select>
					</div>
					<div class="form-group">
						<label>Results</label> <select id="results_select"
							class="form-control">
							<option>10</option>
							<option>50</option>
							<option>100</option>
							<option>All</option>
						</select>
					</div>
				</div>
			</div>
		</section>
	</div>
	<div>
		<div class="row">
			<section class="col-lg-12 connectedSortable"></section>
			<div id="result_box" class="box box-primary">
				<div class="box-header" style="cursor: move;">
					<!-- tools box -->
					<div class="pull-right box-tools">

						<button class="btn btn-info btn-sm" data-widget="collapse"
							data-toggle="tooltip" title="" data-original-title="Collapse">
							<i class="fa fa-minus"> &#160;</i>
						</button>

					</div>
					<!-- /. tools -->
					<h3 class="box-title">Results</h3>


				</div>

				<div id="results_body" class="box-body table-responsive no-padding">


				</div>
			</div>
		</div>
	</div>


</body>
	</html>
</jsp:root>