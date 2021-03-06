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

<meta name="header_title" content="Blast" />
<meta name="header_title_desc" content="Blast Protein sequence" />

<meta name="_csrf" content="${_csrf.token}" />

<meta name="_csrf_header" content="${_csrf.headerName}" />

<title>Blast</title>

<style type="text/css">
.box .box-header {
	padding-bottom: 10px;
}
</style>


<link
	href="${baseURL}/public/theme/css/datatables/dataTables.bootstrap.css"
	rel="stylesheet" type="text/css" />

</head>
<body>

	<script type="text/javascript">
		//<![CDATA[
	</script>


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


	<script src="http://code.jquery.com/jquery-migrate-1.2.1.js"></script>

	<script language="JavaScript" type="text/javascript"
		src="${baseURL}/public/biojs/Biojs.js"></script>

	<script language="JavaScript" type="text/javascript"
		src="${baseURL}/public/biojs/Biojs.Tooltip.js"></script>

	<script language="JavaScript" type="text/javascript"
		src="${baseURL}/public/biojs/Biojs.Sequence.js"></script>

	<script src="${baseURL}/public/bia/ui/ProteinOverview.js"></script>


	<script type="text/javascript">
		//var organisms = JSON.parse('${organisms}') ;
		var jobObj = ${job};
		var seq = ${seq};
		var seq_name = (seq != null) ? seq.name : "Custom sequence";
		var start = ${start};
		var end = ${end};
		
		$.inputType = 'aminoacids';
		$.blastType = 'blastp';
		
		function chageInput(inputType){
			if(inputType == 'nucleotides'){
				$("#nucleotides-btn").removeClass("btn-default")	
				$("#nucleotides-btn").addClass("btn-primary")
				$("#aminoacids-btn").removeClass("btn-primary")
				$("#aminoacids-btn").addClass("btn-default")		
			} else  {
				$("#aminoacids-btn").removeClass("btn-default")	
				$("#aminoacids-btn").addClass("btn-primary")
				$("#nucleotides-btn").removeClass("btn-primary")	
				$("#nucleotides-btn").addClass("btn-default")
			}
			$.inputType = inputType
			updateBlastType();
		}
		
		
		
		function blastType(blastT){
			["blastp","blastn","tblastn","blastx"].forEach(x => {
				$("#" + x).removeClass("btn-primary")
				$("#" + x).addClass("btn-default")			
				
			})
			if(blastT == "blastn"){
				$("#matrix_input").hide();
				$("#gap_open_input").val(1)
				$("#gap_extend_input").val(2)
			} else {
				$("#gap_open_input").val(11)
				$("#gap_extend_input").val(1)
				$("#matrix_input").show();
			}
			$("#" + blastT).addClass("btn-primary")
			$.blastType = blastT
		}
		
		function updateBlastType(){
			if ($.inputType  ==  'aminoacids'){
				if( ["SNDGNr","PDB"].indexOf(  $("#database_select").val()) != -1 ){
					blastType("blastp")
				} else {
					blastType("tblastn")
				}
			} else {
				if( ["SNDGNr","PDB"].indexOf(  $("#database_select").val()) != -1 ){
					blastType("blastx")
				} else {
					blastType("blastn")
				}
			}
		}
		
		var columns = [
				{
					"name" : "msa",
					"title" : '<i title="add to msa list" class="fa fa-align-center">&#160;</i>',
					"orderable" : false,
					"data" : "hit",
					"render" : function(data, type, row) {
						/*
						var id = data.split("_")[0];
						var name = data.replace(id + "_", "");

						var checked = "";
						if ((typeof window.proteins != "undefined")
								&& (typeof window.proteins.values[name] != "undefined")
								&& window.proteins.values[name] == id) {
							checked = "checked=checked";
						}

						return '<input ' + checked + ' onclick="addProt(\''
								+ name + "','" + id
								+ '\',$(this).is(\':checked\'));" '
								+ 'type="checkbox" />';
						*/
						return "";
					}
				},
				{
					"title" : "Hit",
					"data" : "hit",
					"render" : function(data, type, full, meta) {
						
						var args = data.split("||")
						switch(jobObj.patameters.database){
						case "SNDGNr":
							//5903bf99be737e3aad46c322||5903ba91be737e3aad46a316||Staphylococcus_aureus_subsp._aureus_N315||protname
							return '<a href="' + $.api.url_protein(args[0]) + '">'
								+ args[3] + '</a>';
							break;
						case "SNDGNt":
							// NC_002745.2||SaureusN315|| <unknown description>
							return '<a href="' + $.api.url_genome_part(args[0],args[1],full.sstart,full.send) + '">'
								+ args[0] + '</a>';
							break;
						case "PDB":
							//56748ffabe737e4a18524cbf||pdbx_C||bos_taurus <unknown description>
							return '<a href="' + $.api.url_structure(args[1].split("_")[0]) + '">'
								+ args[1] + '</a>';
							break;
						case "Barcodes":
							//FARG409-08||Rajiformes
							return '<a href="' + $.api.url_barcode(args[0]) + '">'
								+ args[0] + '</a>';
							break;							
						}
						
						
						var id = data.split("_")[0];
						var name = data.replace(id + "_", "");
						return '<a href="' + $.api.url_protein(id) + '">'
								+ name + '</a>';wa
					}
				},{
					"title" : "Collection",
					"data" : "hit",
					"render" : function(data, type, full, meta) {
						
						var args = data.split("||")
						switch(jobObj.patameters.database){
						case "SNDGNr":
							//5903bf99be737e3aad46c322||5903ba91be737e3aad46a316||Staphylococcus_aureus_subsp._aureus_N315||protname
							return '<a href="' + $.api.url_genome(data.split("||")[1]) + '">'
								+ data.split("||")[2] + '</a>';
							break;
						case "SNDGNt":
							// NC_002745.2||SaureusN315|| <unknown description>
							return '<a href="' + $.api.url_genome(args[1]) + '">'
								+ args[1] + '</a>';							
							break;
						case "PDB":
							//56748ffabe737e4a18524cbf||C||bos_taurus <unknown description>
							return args[2]
							break;
						case "Barcodes":
							//FARG409-08 || algo
							return args[1]
							break;							
						}
						
						
						
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
				}, {
					"title" : "Q.Start",
					"data" : "qstart"
				}, {
					"title" : "Q.End",
					"data" : "qend"
				}, {
					"title" : "S.Start",
					"data" : "sstart"
				}, {
					"title" : "S.End",
					"data" : "send"
				} ];

		function presentDatabseString(str) {
			return str.charAt(0).toUpperCase() + str.slice(1).replace("_", " ");
		};

		var load_genomes = function(genomes_from_user) {
			$("#seq_overlay").remove();
			$("#seq_loading").remove();

			$.each(genomes_from_user, function(index, organism) {
				if (genome.type == "peptide_collection") {
					$("#database_select").append(
							'<option selected="selected" value="' + organism+ '">'
									+ organism + '</option>');
				}

			});
			$("#option_to_delete").remove();
		};
		var waitTime = 1000;
		var query_job_status = function(job) {
			$.api.blastJobStatus(job, waiting_result)

		};

		var waiting_result = function(job,x,y) {

			if (job.status != "finished" && job.status != "error") {
				var date = new Date();
				var minutes = date.getMinutes();
			    minutes = minutes < 10 ? '0'+minutes : minutes;

				$("#wait_messages").html(
					date.getHours()  + ":" + minutes +	" - Job " + job.id + " not finished, retrying in "  +  (waitTime/3000).toString()  + " seconds" );
				
				window.setTimeout(query_job_status, waitTime, job.id);
				waitTime = 10000;
			} else {
				$(".btn").removeAttr("disabled").removeClass(".disabled");
				$("#wait_messages").remove();
				$(".loading-img").remove();
				$(".overlay").remove();
				if (job.status == "finished") {
					jobObj = job;
					$("#results_body")
							.html(
									'<table width="100%" class="table table-striped"  id="results_table"><thead><th width="30px">MSA</th><th>Hit</th><th>HitGroup</th><th>Length</th><th>Score</th><th>Identity</th><th>Evalue</th><th>Q.Start</th><th>Q.End</th><th>S.Start</th><th>S.end</th></thead><tbody></tbody></table>');
					$("#results_table").show().dataTable({
						"data" : job.result,
						"columns" : columns
					});
				} else {
					$("#results_body").html(job.result[0].hit);
				}

			}

		};

		var document_ready_fn = function() {

			$("#database_select").change(x => {
				
				updateBlastType()
			});
			
			var mySequence = new Biojs.Sequence({
				sequence : ".",
				target : "sequence_box",
				format : 'FASTA',
				id : "."
			});

			if (jobObj == null) {
				$("#overview_row").remove();
			} else {
				waiting_result(jobObj)
				if ((seq != null) && (seq != "?")) {
					var prot_overview = new $.ProteinOverview(
							$('#overview_table'), seq, $.api);
					prot_overview.init();
				} else {
					$("#overview_row").remove();
				}
				$("#options_row").remove();
				return;
			}

			if (seq == null) {
				$("#sequence_box").remove();
				$("#blast_button").remove();
				$("#end_seq_input").remove();
				$("#start_seq_input").remove();

			} else {
				$("#copy_area").val(seq.seq);

				var load_sequence = function(seq_name, seq) {
					mySequence.setSequence(seq, seq_name);

				};
				var load_protein_fn = function(protein) {

					var prot_overview = new $.ProteinOverview(
							$('#overview_table'), protein, $.api);
					prot_overview.init();

					mySequence._container.ready(function() {

						load_sequence(protein.name, protein.sequence);
						$("#start_seq_input").val(0);
						$("#end_seq_input").val(protein.size);

						if (end != -1) {

							mySequence.setSelection(start, end);
							$("#start_seq_input").val(start);
							$("#end_seq_input").val(end);
						}
					});
				};

				mySequence.onSelectionChanged(function(objEvent) {
					$("#start_seq_input").val(objEvent.start);
					$("#end_seq_input").val(objEvent.end);
					$("#copy_area").val(
							mySequence.opt.sequence.substring(
									objEvent.start - 1, objEvent.end));
				});

			}
			var run_blast = function() {

				/* if($("#genomes_select").val() == "uniprotkb"){
					$.post("http://www.ebi.ac.uk/Tools/services/rest/ncbiblast/run/",{
						email:	"ezejajaja@hotmail.com",		//User e-mail address. See Why do you need my e-mail address?
						
						program:"blastp"	 ,		//BLAST program to use to perform the search.
						matrix:	$("#matrix_select").val(),		//Scoring matrix to be used in the search.
						
						gapopen:	parseInt($("#gap_open_input").val()),		//Penalty for the initiation of a gap.
						gapext:		 parseInt($("#gap_extend_input").val()),	//Penalty for each base/residue in a gap.
						filter:		$("#low_complexity_select").val() == "Y",	//Low complexity sequence filter to process the query sequence before performing the search.
						
						stype:	"protein",		//Query sequence type. One of: dna, rna or protein.
						sequence:	 $("#copy_area").val(),	//Query sequence. The use of fasta formatted sequence is recommended.
						database:"uniprotkb"
						
							//seqrange:		//Region of the query sequence to use for the search. Default: whole sequence.
							//gapalign:		//Perform gapped alignments.
							//compstats:		//Compositional adjustment or compositional statistics mode to use.
							//align:			//Alignment format to use in output.
							//alignments:		//Maximum number of alignments displayed in the output.
						//scores:			//Maximum number of scores displayed in the output.
						//exp	:			//E-value threshold.
						//dropoff:			//Amount score must drop before extension of hits is halted.
						//match_scores:	//Match/miss-match scores to generate a scoring matrix for for nucleotide searches.
						//title:			//an optional title for the job.
					})
					return
				} */

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
					"blastType ":$.blastType ,
					"seq" : $("#copy_area").val(),
					"database" :  $("#database_select").val() ,
					"matrix" : $("#matrix_select").val(),
					"gap_open" : parseInt($("#gap_open_input").val()),
					"gap_extend" : parseInt($("#gap_extend_input").val()),
					"max_evalue" : parseFloat($("#evalue_select").val()),
					"low_complexity" : $("#low_complexity_select").val() == "Y",
					"max_results" : parseInt($("#results_select").val())

				};
				function error_handler(err,x,y){
					$(".btn").removeAttr("disabled").removeClass(".disabled");
					$("#wait_messages").remove();
					$(".loading-img").remove();
					$(".overlay").remove();			
					
					$("#results_body").html( ((err.statusText == "error") ? "Sever Error..." :  err.statusText) ).css("color","red");
					
				}
				$.api.blastJob(params, waiting_result,error_handler);

			}
			$("#blast_unip_button").click(
					function(evt) {

						window.open('http://www.uniprot.org/blast/?blastQuery='
								+ mySequence.opt.sequence, '_blank');

					});

			$("#blast_button").click(function(evt) {
				$("#copy_area").val(mySequence.opt.sequence)
				
				run_blast();

			});

			$("#blast_selected_button").click(function(evt) {
				if ($("#database_select").val() == null) {
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

				load_protein_fn(seq)

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
						<table id="overview_table" class="table table-striped">
							<tr>
								<td>Organism/Strain</td>
								<td id="organism">-</td>
							</tr>
							<tr id="taxon-row">
								<td>Taxonomic Information</td>
								<td id="taxon">-</td>
							</tr>
							<tr>
								<td>Seq</td>
								<td id="prot_name">-</td>
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
							<tr id="tr_structure">
							</tr>
						</table>


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

							<button class="btn btn-warning" id="blast_unip_button"
								title="Opens a new tab">Blast in Uniprot</button>


						</div>
					</div>
					<i class="fa fa-map-marker">&#160;</i>
					<h3 class="box-title">
						<spring:message code="blast.sequence" />
					</h3>
				</div>
				<div class="box-body no-padding">
					<div id="sequence_box"></div>
				</div>

				<div id="seq_overlay" class="overlay">&#160;</div>
				<div id="seq_loading" class="loading-img">&#160;</div>

			</div>

			<div class="box box-primary">


				<div class="box-body no-padding">
					<div class="form-group">
						<label><spring:message code="blast.selected_sequence" /></label>
						<textarea id="copy_area" class="form-control" rows="5"
							placeholder="Enter ...">&#160;</textarea>
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
					<label><spring:message code="blast.input" /></label>
					<br />
						<div class="btn-group" role="group" aria-label="...">
							<button  id="aminoacids-btn" onclick="chageInput('aminoacids')" type="button" class="btn btn-primary">
								<spring:message code="blast.aminoacid" />
							</button>
							<button id="nucleotides-btn" onclick="chageInput('nucleotides')" type="button" class="btn btn-default">
								<spring:message code="blast.nucleotides" />
							</button>

						</div>
						</div>
						
					</div>

					<div class="form-group">
						<label><spring:message code="blast.databases" /></label> <select
							id="database_select" class="form-control">

							<option value="SNDGNr">Proteinas SNDG</option>
							<option value="SNDGNt">Secuencias Ensambladas SNDG</option>
							<option value="PDB">Proteinas con estructura SNDG</option>
							<option value="Barcodes">Barcodes</option>

						</select>

					</div>
					<div class="form-group">
											<label><spring:message code="blast.type" /></label>
											<br />
						<div class="btn-group" role="group" aria-label="...">
							<button  id="blastp" type="button"
								class="btn btn-primary">blastp</button>
							<button  id="blastn" type="button"
								class="btn btn-default">blastn</button>
							<button  id="blastx" type="button"
								class="btn btn-default">blastx</button>
							<button  id="tblastn" type="button"
								class="btn btn-default">tblastn</button>
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

					<h3 class="box-title">
						<spring:message code="blast.parameters" />
					</h3>


				</div>



				<div class="box-body">

					<div id="matrix_input" class="form-group">
						<label>Matrix</label> <select id="matrix_select"
							class="form-control">

							<option value="BLOSUM45">Blowsum 45</option>
							<option value="BLOSUM50">Blowsum 50</option>
							<option selected="selected" value="BLOSUM62">Blowsum 62</option>
							<option value="BLOSUM80">Blowsum 80</option>
							<option value="BLOSUM90">Blowsum 90</option>
							<option value="PAM30">Pam 30</option>
							<option value="PAM70">Pam 70</option>
							<option value="PAM250">Pam 250</option>
						</select>
					</div>
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

					<div class="pull-right box-tools">

						<button class="btn btn-info btn-sm" data-widget="collapse"
							data-toggle="tooltip" title="" data-original-title="Collapse">
							<i class="fa fa-minus"> &#160;</i>
						</button>

					</div>
					<h3 class="box-title">
						<spring:message code="blast.results" />
					</h3>
				</div>
				<div id="results_body" class="box-body table-responsive no-padding">
				</div>

			</div>
		</div>
	</div>


</body>
	</html>
</jsp:root>