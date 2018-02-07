<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page"
	xmlns:spring="http://www.springframework.org/tags"
	xmlns:c="http://java.sun.com/jsp/jstl/core"
	xmlns:form="http://www.springframework.org/tags/form"
	xmlns:sec="http://www.springframework.org/security/tags" version="2.0">
	<jsp:directive.page language="java" contentType="text/html" />


	<html>
<head>

<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta name="header_title" content="Dashboard" />
<meta name="header_title_desc" content="information overview" />

<title>Genome List</title>

<style type="text/css">
.dropdown-menu img {
    height: 18px;
    width: 18px;
    margin-right: 5px;
}

.label {
    position: absolute;
    z-index: 10;
}

textarea, .contenteditable1 {
    display: block;
    width: 100%;
}

.contenteditable1 {
    height: 100px;
    border: 1px solid #ccc;
    color: #555;

    -webkit-transition: border linear 0.2s, box-shadow linear 0.2s;
       -moz-transition: border linear 0.2s, box-shadow linear 0.2s;
         -o-transition: border linear 0.2s, box-shadow linear 0.2s;
            transition: border linear 0.2s, box-shadow linear 0.2s;

    -webkit-box-shadow: inset 0 1px 1px rgba(0, 0, 0, 0.075);
       -moz-box-shadow: inset 0 1px 1px rgba(0, 0, 0, 0.075);
            box-shadow: inset 0 1px 1px rgba(0, 0, 0, 0.075);

    -webkit-border-radius: 4px;
       -moz-border-radius: 4px;
            border-radius: 4px;
}
</style>



</head>
<body>



	<script type="text/javascript">
		//<![CDATA[
		$(document)
				.ready(
						function() {
							$('#textarea1')
									.textcomplete(
											[ { // emoji strategy
												match : /\B:([\-+\w]*)$/,
												search : function(term,
														callback) {
													callback($
															.map(
																	emojies,
																	function(
																			emoji) {
																		return emoji
																				.indexOf(term) === 0 ? emoji
																				: null;
																	}));
												},
												template : function(value) {
													return '<img src="../public/text-complete/images/emoji/' + value + '.png"></img>'
															+ value;
												},
												replace : function(value) {
													return ':' + value + ': ';
												},
												index : 1,
												maxCount : 5
											} ]);
							//SyntaxHighlighter.all();

						});
	</script>
	<script type="text/javascript"
		src="../public/text-complete/jquery.textcomplete.js"></script>




	<script type="text/javascript" src="../public/text-complete/emoji.js"></script>


	<script type="text/javascript">
		// ]]>
	</script>

	<!-- Small boxes (Stat box) -->
	<div class="row">
		<div class="col-lg-3 col-xs-6">
			<!-- small box -->
			<div class="small-box bg-aqua">
				<div class="inner">
					<h3>O ${dashboard.genomeCount}</h3>
					<p>Genomes</p>
				</div>
				<div class="icon">
					<i class="glyphicon glyphicon-align-left">&#160;</i>
				</div>
				<a href="#" class="small-box-footer"> More info <i
					class="fa fa-arrow-circle-right">&#160;</i>
				</a>

			</div>
		</div>
		<div class="col-lg-3 col-xs-6">
			<!-- small box -->
			<div class="small-box bg-green">
				<div class="inner">
					<h3>O ${dashboard.genomeCount}</h3>
					<p>Transcriptomes</p>
				</div>
				<div class="icon">
					<i class="glyphicon glyphicon-stats">&#160;</i>
				</div>
				<a href="#" class="small-box-footer"> More info <i
					class="fa fa-arrow-circle-right">&#160;</i>
				</a>
			</div>
		</div>

		<div class="col-lg-3 col-xs-6">
			<!-- small box -->
			<div class="small-box bg-yellow">
				<div class="inner">
					<h3>O ${dashboard.genomeCount}</h3>
					<p>Pathways</p>
				</div>
				<div class="icon">
					<i class="glyphicon glyphicon-repeat">&#160;</i>
				</div>
				<a href="#" class="small-box-footer"> More info <i
					class="fa fa-arrow-circle-right">&#160;</i>
				</a>
			</div>
		</div>
		<div class="col-lg-3 col-xs-6">
			<!-- small box -->
			<div class="small-box bg-red">
				<div class="inner">
					<h3>O ${dashboard.genomeCount}</h3>
					<p>Organisms</p>
				</div>
				<div class="icon">
					<i class="glyphicon glyphicon-info-sign">&#160;</i>
				</div>
				<a href="#" class="small-box-footer"> More info <i
					class="fa fa-arrow-circle-right">&#160;</i>
				</a>
			</div>
		</div>


	</div>
	<!-- /.row -->

	<!-- Main row -->
	<div class="row">
		<!-- Left col -->
		<section class="col-lg-6 connectedSortable">
			<!-- Box (with bar chart) -->
			<div class="box box-danger" id="loading-example">
				<div class="box-header">
					<i class="fa fa-cloud">&#160;</i>

					<h3 class="box-title">Search</h3>
				</div>
				<!-- /.box-header -->
				<div class="box-body no-padding">


					<div class="textarea-wrapper">
<!-- <span class="label">#textarea1</span> -->
						<textarea id="textarea1" rows="6" >aaa </textarea>
					</div>

				</div>

			</div>
			<!-- /.box -->
		</section>

		<section class="col-lg-6 connectedSortable">
			<!-- Map box -->
			<div class="box box-primary">
				<div class="box-header">
					<!-- tools box -->
					<div class="pull-right box-tools">
						<button class="btn btn-primary btn-sm daterange pull-right"
							data-toggle="tooltip" title="Date range">
							<i class="fa fa-calendar">&#160;</i>
						</button>
					</div>
					<!-- /. tools -->

					<i class="fa fa-map-marker">&#160;</i>
					<h3 class="box-title">Links</h3>
				</div>
				<div class="box-body no-padding">
					<div class="table-responsive">
						<!-- .table - Uses sparkline charts-->
						<table class="table table-striped" id="links-table">
							<tr>
							<th></th>
								<th>Type</th>
								<th>Description</th>
								<th>Date</th>
							</tr>
							
						</table>
						<!-- /.table -->
					</div>
				</div>
				<!-- /.box-body-->
				<div class="box-footer">
					<button class="btn btn-info">
						<i class="fa fa-download">&#160;</i> Generate PDF
					</button>
					<button class="btn btn-warning">
						<i class="fa fa-bug">&#160;</i> Report Bug
					</button>
				</div>
			</div>
			<!-- /.box -->
		</section>
	</div>







</body>
	</html>

</jsp:root>