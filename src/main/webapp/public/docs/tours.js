/**
 * http://usejsdoc.org/
 */

var next = "<b>Click Next to continue. </b>";
var baseUrl = "";
function hideTourNext(tour) {
	setTimeout(function() {
		$('#tourNextButton').hide();
	}, 700);
}
function showTourNext(tour) {
	setTimeout(function() {
		$('#tourNextButton').show()
	}, 700);
}
function loadTutorial(url) {
	baseUrl = url;
	switch ($.QueryString["tour"]) {
	case "1":
		tourProteinPriorization();
		break;
	case "2":
		tourDataUpload();
		break;
	case "3":
		tourProteinSearchAndNavigation();
		break;
	case "4":
		tourProteinStrains();
		break;
	case "5":
		tourVariantSeach();
		break;
	case "6":
		tourPathwayPriorization();
		break;
	}
	
}

function tourProteinSearchAndNavigation() {
	var steps = [];
	if (window.location.href.indexOf("genome/H37Rv?") != -1) {

	}
	if (window.location.href.indexOf("search/H37Rv/product/?") != -1) {

	}
	if (window.location.href.indexOf("protein/") != -1) {

	}
	if (window.location.href.indexOf("structure/") != -1) {

	}

}

function tourPathwayPriorization(){
	var steps = [];
	if (window.location.href.indexOf("/genome") != -1) {
		steps = [
			{
				element : "#tourTable",
				title : "Target Priorization tutorial",
				content : "In this interactive tutorial we will show the avaliable tools to make pathway prioritization. "
						+ "Through this example, you'll be making a filter and a scoring function. "
						+ next,
				placement : "top"
			},	{
				element : "#tourTable",
				title : "Genome list",
				content : "This table has all the avaliable genomes. "
						+ next,
				placement : "top",

			},	{
				element : "#tutorial6Link",
				title : "Click here to start the target priorization",
				content : "In this tour we will use <i>Klebsiella pneumoniae Kp13</i>. <b>Click in the link to continue.</b>",
				reflex : true,
				onShow : hideTourNext,
				onNext : function(tour) {
					window.location = '${baseURL}/search/Kp13/product/?tour=6';
				},
			} ];
	}	
	if (window.location.href.indexOf("search/Kp13/pathways/?") != -1) {
		steps = [
			{
				element : "#filterBox",
				title : "Filter parameters",
				content : "Patways can be filtered by different properties. "
						+ next,
				placement : "top"
			},	{
				element : "#scoreBox",
				title : "Scoring parameters",
				content : "The Scoring Function can be built using pathways and/or proteins properties. A complete description of the properties can be found in the User Guide. "
						+ next,
				placement : "top"

			},	{
				placement : "top",
				element : "#filter_pathway",
				title : "Add a parameter",
				content : "Let's add a filter, <b>click on 'Pathways' </b>",
				reflex : true,
				onShow : hideTourNext,
				onNext : function(tour) {	
					setTimeout(function() {
						$(".class_druggable").focus();
						var n = steps.length
						tour.addSteps(secondSteps);
						tour.goTo(n-1)
					}, 1000);
				},
			}, {
				placement : "left",
				element : ".class_druggable",
				title : "Add parameter",
				reflex : true,
				content : "<b>Check 'druggable' </b> ",
				onShow : hideTourNext,
				onNext : function(tour) {	
					$("#modal_ok_btn").focus();
				}
			}

	];
	var secondSteps = [
			{
				reflex : true,
				placement : "top",
				element : "#modal_ok_btn",
				title : "Continue",
				content : "Proceed with the selected parameters by <b>clicking 'OK'</b>.",
				onShow : hideTourNext,
			
			},
			{
				placement : "top",
				element : "a:contains('^X$')",
				title : "Filter added",
				content : "The new parameter will filter all the pathways that have at least one druggable protein. "
						+ next,
				
			},	
			/*{
				placement : "top",
				element : "#search-table",
				title : "Wait until complete",
				content : "Wait until the pathways query is complete "
						+ next,
				onShow : function(tour) {	
					setTimeout(function() {
						
						
						var untilReady = function() {
						if($( "#search-table_info").is(":visible")){
							$('#tourNextButton').show()
						} else {
							untilReady();
						}}
						$('#tourNextButton').hide()
						untilReady()
					}, 700);
				}
						
				
			},		*/
			
			{
				placement : "top",
				backdrop : true,
				element : "#search-table_info",
				title : "Results",
				content : "Now the search space for targets has shrunk, only pathways fulfilling previously entered criteria remain. "
						+ next,
						
			} ,
			{
				placement : "top",
				element : "#score_pathway",
				title : "Score Function",
				content : "Let's add a pathway parameter to the scoring function. <b>Click 'Pathways' to continue. </b>",
				reflex : true,
				onShow : hideTourNext,
				onNext : function(tour) {				

					setTimeout(function() {						
						$(".class_norm_chokepoint").focus();
						var n = steps.length;
						tour.addSteps(fourthSteps);
						tour.goTo(n-1)
					}, 1000);
				},
			}, {
				placement : "left",
				element : ".class_norm_chokepoint",
				title : "Add parameter",
				reflex : true,
				content : "<b>Check 'norm chokepoint'. </b>",
				onShow : hideTourNext,
				onNext : function(tour) {
					$(".class_csa").focus();
				}
			} ];
	var fourthSteps = [			
			{
				reflex : true,
				placement : "top",
				element : "#modal_ok_btn",
				title : "Continue",
				onShow : hideTourNext,
				content : "Proceed with the selected parameters. <b>Click 'OK'.</b>",
			
			},
			{

				placement : "top",
				element : "#score_div",
				title : "Score",
				backdrop : true,
				content : "Here the scoring function is shown. Currently, we are using only one pathway parameter (norm_chokepoint), with the default coeficient ( 1 ). "
						+ next,		
			},
		
		{
			placement : "top",
			element : "#score_metadata",
			title : "Score Function",
			content : "Let's add some protein parameters to the scoring function. In this example we will use essentiality and expression metadata. <b>Click 'Metadata' </b>. ",
			reflex : true,
			onShow : hideTourNext,
			onNext : function(tour) {				

				setTimeout(function() {						
					$(".class_essential_in_mgh78578").focus();
					var n = steps.length;
					tour.addSteps(sixthSteps);
					tour.goTo(n-2)
				}, 1000);
			},
		}, {
			placement : "left",
			element : ".class_essential_in_mgh78578",
			title : "Add essentiality parameter",
			reflex : true,
			content : "<b>Check 'essential in mgh78578'.</b>",
			onShow : hideTourNext,
			onNext : function(tour) {
				$(".class_overexpressed_in_polymyxin").focus();
			}
		}, {
			placement : "left",
			element : ".class_overexpressed_in_polymyxin",
			title : "Add expression parameter",
			reflex : true,
			content : "<b>Check 'overexpressed in polymyxin'</b>",
			onShow : hideTourNext,
			onNext : function(tour) {	
				$("#modal_ok_btn").focus();
			}
			
		}
		
	];
	var sixthSteps = [
		{
			reflex : true,
			placement : "top",
			element : "#modal_ok_btn",
			title : "Continue",
			onShow : hideTourNext,
			content : "Proceed with the selected parameters. <b>Click 'OK'.</b>",
		
		},
		{

			placement : "top",
			element : "#groupHelp",
			title : "Group function",			
			content : "To rank pathways you may use protein properties. As there are many proteins related to each pathway, " + 
					"there are many ways to combine protein properties values. TargetPathogen offers the following operations: " + 
					" 'max' (Example: value of most druggable protein of the pathway),  'sum' (Example: protein count in the pathway with binding activity), " 
					+ "'min' (Example: protein with the lowest centrality) and  'avg' (Ex: hit in deg proteins / total proteins in the pathway) . "
					+ next,		
		},
		{

			placement : "top",
			element : "#search_params_table > tbody > tr:nth-child(2) > td:nth-child(5) > select",
			title : "Group function",	
			reflex : true,
			onShow : hideTourNext,
			content : "In this case 'avg' is set. This means that the value assigned to a pathway, is the count of essential proteins (related to that pathway) "
				+  "divided by the number of proteins in the pathway. "
				+ "Let's change the value to 'sum', which will give us the total number of essential proteins in the pathway. " 
				+ "<b>Change the selection from 'avg' to 'sum' to continue.</b>"		
		},	
		
		{

			placement : "top",
			element : "#score_div",
			title : "Score",
			backdrop : true,
			content : "Here the scoring function is shown. Now, it is defined by one pathway parameter and two protein parameters. "
					+ next,		
		},	{
				placement : "left",
				element : "th:contains('Score')",
				title : "Sorted results",
				content : "Now the table shows all pathways previously selected (with at least one druggable protein) sorted by the scoring function. " + next
			},
			{
				placement : "top",
				element : "#search-table > tbody > tr:nth-child(1) > td:nth-child(7)",
				title : "Properties row",
				backdrop : true,
				content : "You can check how the score was built by looking this row."
			},
			
			{
				element : "#filterBox",
				title : "End",
				content : "We have shown how filtering and ranking together allows to highlight relevant pathways acording to user biological criteria.",							
				placement : "top"
			} ];
	}
	var tour = new Tour(
			{
				debug : true,
				storage : false,
				template : "<div class='popover tour'><div class='arrow'></div><h3 class='popover-title'></h3><div class='popover-content'></div><nav class='popover-navigation'><div class='btn-group'><button class='btn btn-default' data-role='prev'>« Prev</button><button class='btn btn-default' id='tourNextButton' data-role='next'>Next »</button></div><button class='btn btn-default btn-end' data-role='end'>End tour</button></nav></div>",
				steps : steps
			});
	tour.init();
	tour.start();
}


function tourDataUpload() {
	var steps = [];
	var secondSteps = [
			{
				element : "#myModalLabel",
				title : "Download the data sample",
				content : "In this dialog we explain the data format. Read it carefully and examine the examples before preparing your data. "
						+ next,
				placement : "top"

			},
			{
				element : "#linkExDownload1",
				title : "Download the data sample",
				content : "In this tutorial, we well use sample data. <b>Click 'Example 1' to download</b>."
						+ "Click on the 'Data' tab. ",
				reflex : true,
				onShow : hideTourNext,
				placement : "top"

			},
			{
				element : "#input_file",
				title : "Select the file",
				content : "<b>Click this button and select the previously downloaded file</b> (data_example.tsv).",
				reflex : true,
				onShow : hideTourNext,
				placement : "top"

			},
			{
				element : "#btn_upload_csv",
				title : "Upload the file",
				content : "<b>Click 'Upload File'.</b>",
				reflex : true,

				onShow : hideTourNext,
			// placement : "top"

			},
			{
				element : "#properties_table_box",
				title : "Wait...",
				content : "Wait until the operation is complete. Depending on the file size and internet connection the operation may take several minutes.  ",
				onShow : hideTourNext,
				placement : "top"

			}, ]
	if (window.location.href.indexOf("/genome/H37Rv?") != -1) {
		if (!("skipFirst" in $.QueryString)) {
			steps
					.push({
						element : "#overview_tab_link",
						title : "Target Data Upload Tutorial",
						content : "In this interactive tutorial we will show how to upload user data. "
								+ "Through this example, you'll be associating sample metadata to <i>M. tuberculosis H37Rv</i> genome. "
								+ next,
						placement : "top"
					})
		}

		if ($("#username").text() == "demo") {
			var loginStep = {
				element : "#user_name",
				title : "Login or register first",
				content : "To be able to upload data, you have to be logged in. "
						+ "<b>Click 'Next'</b> to go to the login/register page. ",
				onNext : function(tour) {
					window.location = baseUrl
							+ '/login?_return=/genome/H37Rv?tour=2&skipFirst=1';
				},
				placement : "bottom"
			}
			steps.push(loginStep)
			steps.push({})
		} else {

			if ("uploadComplete" in $.QueryString) {
				steps
						.push({
							element : "#properties_table",
							title : "Properties list",
							content : "Now, in this table, the uploaded properties are shown: AMI, PAS, EMB, etc.. "
									+ next,
							placement : "top"

						});
				steps
						.push({
							element : "#properties_table > tr:nth-child(1) > td:nth-child(2) > textarea",
							title : "Describe Property",
							content : "In the description column, you can add details of your properties (it is not mandatory)." 
								+ "The uploaded data annotated all the proteins that have at least one aminoglycoside " 
								+ "resistance conferring mutation in TBDream (https://tbdreamdb.ki.se/Info/). "
									+ "We will put this description automatically for this example. "
									+ next,
							onNext : function(tour) {
								$(
										'#properties_table > tr:nth-child(1) > td:nth-child(2) > textarea')
										.val(
												'Proteins that have at least one aminoglycoside resistance conferring mutation in TBDream (https://tbdreamdb.ki.se/Info/)');
								$('#properties_table > tr:nth-child(1) > td:nth-child(2) > textarea').change()
							},
							placement : "top"

						});
				steps
						.push({
							element : "#properties_table > tr:nth-child(1) > td:nth-child(3)",
							title : "Property Type",
							content : "In this column we see the type of the property. Can be either 'value' (tags) or numeric. "
									+ next,
							placement : "top"

						});
				steps
						.push({
							element : "#upload_save_btn",
							title : "Save description",
							content : "Using this button we can save the changed descriptions. <b>Click 'Save' </b> and await for the change confirmation. ",
							reflex : true,
							onShow : hideTourNext,
							placement : "top"

						});
				steps
						.push({
							element : "#uploads_table2",
							title : "Uploads list",
							content : "And to finish with this screen, this table shows the list of uploaded files, the properties and eventual errors of each upload process. "
									+ next,
							placement : "top",
							onNext : function(tour) {
								window.location = baseUrl
										+ '/search/H37Rv/product/?tour=2';
							},

						});
				steps.push({});
			} else {

				steps
						.push({
							element : "#data_tab_link",
							title : "Data tab",
							content : "Let's go to the data section, where all metadata properties are listed. "
									+ "<b>Click  'Data' tab. </b>",
							reflex : true,
							onShow : hideTourNext,
							placement : "bottom"
						});
				steps
						.push({
							element : "#data_tab_link",
							title : "Data tab description",
							content : "In this tab we have 3 different sections: Downloads, Properties and Uploads. If you have not uploaded any data, the last 2 sections will be empty. "
									+ next,
							placement : "left"
						});
				var startSecondSteps = function(tour) {
					setTimeout(function() {

						if ($("#myModalLabel").is(":visible")) {
							$("#myModalLabel").focus();
							var n = steps.length;
							tour.addSteps(secondSteps);
							tour.goTo(n)

						} else {
							startSecondSteps();
						}

					}, 1000);
				};
				steps
						.push(
								{
									element : "#btn_upload_window",
									title : "Open Data upload",
									reflex : true,
									onShow : hideTourNext,
									content : "To load our data, let's <b> click 'Upload Metadata' </b> .",
									placement : "right",
									onNext : startSecondSteps,
								}, {})

			}
		}
		var tour = new Tour(
				{
					debug : true,
					storage : false,
					template : "<div class='popover tour'><div class='arrow'></div><h3 class='popover-title'></h3><div class='popover-content'></div><nav class='popover-navigation'><div class='btn-group'><button class='btn btn-default' data-role='prev'>« Prev</button><button class='btn btn-default' id='tourNextButton' data-role='next'>Next »</button></div><button class='btn btn-default btn-end' data-role='end'>End tour</button></nav></div>",
					steps : steps
				});
		tour.init();
		tour.start();

	}

	if (window.location.href.indexOf("search/H37Rv/product/?") != -1) {
		var steps = [];
		var secondSteps = [];

		steps
				.push({
					element : "#filter_metadata",
					title : "Metadata",
					content : "The previouly uploaded properties, are now avaliable for filtering and scoring in Metadata category. "
							+ "<b>Click 'Metadata'</b>",
					reflex : true,
					onShow : hideTourNext,
					onNext : function(tour) {
						setTimeout(function() {
							$(".class_AMI").focus();
							tour.addSteps(secondSteps);
							tour.goTo(1)
						}, 1000);
					},

				});
		steps.push({});
		secondSteps.push({
			element : ".class_AMI",
			title : "Property",
			content : "<b>Check 'AMI'</b>",
			reflex : true,
			onShow : hideTourNext,
			onNext : function(tour) {
				$("#modal_ok_btn").focus();
			},
			placement : "left",

		});
		secondSteps.push({
			element : "#modal_ok_btn",
			title : "Property",
			content : "Click the ok button",
			reflex : true,
			onShow : hideTourNext,

		});
		secondSteps
				.push({
					element : "#search_filter_table > tbody > tr > td:nth-child(5) > select",
					title : "Filter value",
					content : "<b>Change the value to 'Yes'</b>",
					reflex : true,
					onShow : hideTourNext,

				});
		secondSteps.push({
			element : "#search-table",
			title : "Results",
			backdrop : true,
			content : "Now we see the only protein that matches the criteria. "
					+ next,
			placement : "top",
		});
		secondSteps
				.push({
					element : "#filterBox",
					title : "Wrapping up",
					content : "By now, you should be able to integrate your data in TargetPathogen. "
							+ "You can make filters and scoring functions applying your own research or 3rd party data. "
							+ "Remember to check the format and see the examples before uploading. "					
				});

		var tour = new Tour(
				{
					debug : true,
					storage : false,
					template : "<div class='popover tour'><div class='arrow'></div><h3 class='popover-title'></h3><div class='popover-content'></div><nav class='popover-navigation'><div class='btn-group'><button class='btn btn-default' data-role='prev'>« Prev</button><button class='btn btn-default' id='tourNextButton' data-role='next'>Next »</button></div><button class='btn btn-default btn-end' data-role='end'>End tour</button></nav></div>",
					steps : steps
				});
		tour.init();
		tour.start();

	}

}

function tourProteinPriorization() {
	if (window.location.href.indexOf("/genome?") != -1) {
		var tour = new Tour(
				{
					storage : false,
					template : "<div class='popover tour'><div class='arrow'></div><h3 class='popover-title'></h3><div class='popover-content'></div><nav class='popover-navigation'><div class='btn-group'><button class='btn btn-default' data-role='prev'>« Prev</button><button class='btn btn-default' id='tourNextButton' data-role='next'>Next »</button></div><button class='btn btn-default btn-end' data-role='end'>End tour</button></nav></div>",
					steps : [
							{
								element : "#tourTable",
								title : "Target Priorization tutorial",
								content : "In this interactive tutorial we will show the avaliable tools for target prioritization. "
										+ "Through this example, you'll be making a filter and a scoring function. "
										+ next,
								placement : "top"
							},
							{
								element : "#tourTable",
								title : "Genome list",
								content : "This table has all available genomes. "
										+ next,
								placement : "top",

							},
							{
								element : "#tutorialExampleGenome",
								title : "Click here to start the target priorization",
								content : "We will now select <i>M. tuberculosis</i> H37Rv genome. <b>Click in the link to continue.</b>",
								reflex : true,
								onShow : hideTourNext,
								onNext : function(tour) {
									window.location = '${baseURL}/search/H37Rv/product/?tour=1';
								},
							} ]
				});
		tour.init();
		tour.start();
	}
	if (window.location.href.indexOf("search/H37Rv/product/?") != -1) {
		var firstSteps = [
				{
					element : "#filterBox",
					title : "Filter parameters",
					content : "Proteins can be filtered using different properties. "
							+ next,
					placement : "top"
				},
				{
					element : "#scoreBox",
					title : "Scoring parameters",
					content : "Also, a Scoring Function can be built using proteins properties. "
							+ next,
					placement : "top"

				},
				{
					placement : "left",
					element : "#activityScoreButton",
					title : "Scoring parameters",
					content : "Both panels have the same set of properties, grouped by different categories. A complete description of the properties can be found in the User Uuide. "
							+ next,

				},
				{
					placement : "top",
					element : "#structureFilterButton",
					title : "Add a parameter",
					content : "Let's add a filter by a property in the Structure category. <b>Click on the 'Structure' Button</b>",
					reflex : true,
					onShow : hideTourNext,
					onNext : function(tour) {
						// window.searchDialog.modal_options(
						// "Structure parameters", "filter", "structure",
						// false, false, true)
						// window.searchDialog.modal.modal('show');

						setTimeout(function() {

							$(".class_has_structure").focus();
							tour.addSteps(secondSteps);
							tour.goTo(4)
						}, 1000);
					},
				}, {
					placement : "left",
					element : ".class_has_structure",
					title : "Add parameter",
					reflex : true,
					content : "To select only proteins with an avaliable 3D structure, <b>Check 'has structure' </b>",
					onShow : hideTourNext,
					onNext : function(tour) {
						$("#modal_ok_btn").focus();

					}
				}

		];
		var secondSteps = [
				{
					reflex : true,
					placement : "top",
					element : "#modal_ok_btn",
					title : "Continue",
					content : "Once you checked all the properties of interest in this category. <b>Press the 'OK' button.</b>",
					onShow : hideTourNext,
				// onNext : function(tour) {
				// window.searchDialog.ok();
				// }
				},
				{
					placement : "bottom",
					element : "a:contains('X')",
					title : "Filter added",
					content : "Selected properties to filter data will be added in this table. Now we have only 'has structure'. "
							+ next
				},
				{
					element : "#refresh_btn",
					title : "Apply the filter",
					content : "Each time you change the filters or the scoring function, the table must be updated. <b>Click on the refresh button. </b>",
					reflex : true,
					onShow : hideTourNext,
					onNext : function(tour) {
						// $("#refresh_btn").find(".badge").remove();
						// $.search_table.search_gene_prods();
						setTimeout(function() {
							tour.addSteps(thirdSteps);
							tour.goTo(8)
							// $("#refresh_btn").focus();
						}, 1000);
					}

				},
				{
					placement : "top",
					backdrop : true,
					element : "#search-table_info",
					title : "Results",
					content : "Now the search space for targets has shrunk, only proteins fulfilling previously entered criteria remain. "
							+ next
				} ];

		var thirdSteps = [
				{
					placement : "top",
					element : "#structureScoreButton",
					title : "Score Function",
					content : "Let's add some parameters to the scoring function. <b>Click on the Structure Button to continue.</b> ",
					reflex : true,
					onShow : hideTourNext,
					onNext : function(tour) {
						// window.searchDialog.modal_options(
						// "Structure parameters", "score", "structure",
						// false, false, true)
						// window.searchDialog.modal.modal('show');

						setTimeout(function() {
							$(".class_druggability").focus();
							tour.addSteps(fourthSteps);
							tour.goTo(10)
						}, 1000);
					},
				}, {
					placement : "left",
					element : ".class_druggability",
					title : "Add parameter",
					reflex : true,
					content : "<b>Check 'druggability'.</b>",
					onShow : hideTourNext,
					onNext : function(tour) {
						$(".class_csa").focus();
					}
				} ]
		var fourthSteps = [
				{
					placement : "left",
					element : ".class_csa",
					title : "Add parameter",
					reflex : true,
					onShow : hideTourNext,
					content : "<b>Check 'csa'</b>. ",
					onNext : function(tour) {
						$("#modal_ok_btn").focus();

					}
				},
				{
					reflex : true,
					placement : "top",
					element : "#modal_ok_btn",
					title : "Continue",
					onShow : hideTourNext,
					content : "Proceed with the selected parameters. <b>Click 'OK'.</b>",
				// onNext : function(tour) {
				//
				// window.searchDialog.ok();
				// }
				},
				{

					placement : "top",
					element : "#score_div",
					title : "Score",
					backdrop : true,
					content : "Here, the scoring function is presented. In this case, 'druggability' and 'csa' parameters are used with a default coeficient (1). "  
							+ "You can change this value to adjust parameters weights. "
							+ next,
				// onNext : function(tour) {
				// window.searchDialog.ok();
				// }
				},
				{
					element : "#refresh_btn",
					title : "Apply the SF",
					content : "Lets refresh again the results. <b>Click 'Refresh'.<b/>",
					reflex : true,
					onShow : hideTourNext,
					onNext : function(tour) {
						// $("#refresh_btn").find(".badge").remove();
						// $.search_table.search_gene_prods();
						$("#search-table_info").focus();
					}

				},
				{
					placement : "left",
					element : "th:contains('Score')",
					title : "Sorted results",
					content : "Now the table shows all proteins previously selected (with available structure) sorted by the scoring function. "
						+ next
				},
				{
					element : "#filterBox",
					title : "End",
					content : "We have shown how filtering and ranking together allows to highlight relevant targets acording to user biological criteria.",
					placement : "top"
				} ];

		var tour = new Tour(
				{
					debug : true,
					template : "<div class='popover tour'><div class='arrow'></div><h3 class='popover-title'></h3><div class='popover-content'></div><nav class='popover-navigation'><div class='btn-group'><button class='btn btn-default' data-role='prev'>« Prev</button><button class='btn btn-default' id='tourNextButton' data-role='next'>Next »</button></div><button class='btn btn-default btn-end' data-role='end'>End tour</button></nav></div>",
					storage : false,
					steps : firstSteps
				});
		tour.init();
		tour.start();
	}
}