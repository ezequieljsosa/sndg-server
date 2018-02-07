/**
 * column filter keywords
 * 
 */
$.SearchDialog = function(modal, select, textInput, paramsTable, drugParams,
		okBtn, cancelBtn, keywordFilter, ontologyFilter, filterTable,
		scoreTable, ontologies_url, organism) {

	this.actions = [];

	this.drugParams = drugParams;

	this.keywordFilter = keywordFilter;
	this.ontologyFilter = ontologyFilter;
	this.drugParamsTable = filterTable;

	this.filterTable = filterTable;
	this.scoreTable = scoreTable;

	this.organism = organism;
	this.ontologies_url = ontologies_url;

	this.pathwaySelect = false;
	
	// HTML
	this.modal = modal;
	this.textInput = textInput;
	this.select = select;
	this.paramsTable = paramsTable;
	this.change = true;
	this.okBtn = okBtn;
	this.cancelBtn = cancelBtn;

	//
	this.ontologiesMap = {
		activity : [ "go:molecular_function", "ec", "pfam", "cog" ],
		process : [ "go:biological_process" ],
		localization : [ "go:cellular_component" ],
		pathways : [ "biocyc_pw" ] // , "biocyc_reac"
	};

};

$.SearchDialog.prototype = {
	init : function() {
		var me = this;

		this.cancelBtn.click(this.cancel.bind(this))
		this.okBtn.click(this.ok.bind(this))

		this.modal.on('shown.bs.modal', function() {
			me.paramsTable.find("tr").removeClass('selected');
			
			
			$.each(me.paramsTable.find(".propBoxes"), function(i,input){
				if ($.grep(me.drugParamsTable.data(),
						function(filterData) {
							return filterData.name == $(input).attr("class").split(' ')[1].split("class_")[1]
						}).length > 0) {
					
					$(input).parent().parent().addClass("selected")
					$(input).prop("checked",true)
				}
			})
			
			
			
			$.each(me.actions, function(i, x) {
				x()
			})
			me.actions = [];
		});
		
		this.modal.on('hide.bs.modal', function() {
			me.paramsTable.find("input").prop( "checked", false );
			me.paramsTable.find("tr").removeClass('selected');
			me.select.val("");
			me.select.text("");
			me.textInput.val("");
		});

		this.initPropsTable();

		$("body").on('keyup change', "#" + this.textInput.attr("id"),
				function(e) {
					if (e.keyCode == 13) {
						// e.preventDefault();
						// me.modal.modal('hide');
						me.ok();
					}
				});

		$(".open-modal").click(function(evt) {
			me.open_modal(this, evt);
		});	

	},

	modal_options : function(title, action, category, showTxt, showSelect,
			showProps, formatInputTooShort ) {
		this.modal.find(".modal-title").html(title)
		
		$("#pw_title").html('');
		
		if (action == "score") {
			this.drugParamsTable = this.scoreTable;
		} else {
			this.drugParamsTable = this.filterTable;
		}

		if (showTxt) {
			this.textInput.parent().show();
			this.actions.push(function() {
				this.textInput.focus();
			}.bind(this))

		} else {
			this.textInput.parent().hide()
		}
		if (showSelect) {
			if (category == "pathways") {
				
				$("#pw_title").html('Search by pathway');
					
			} else {
				this.actions.push(function() {
					this.select.select2('open');
				}.bind(this))
			}

			this.select.parent().show();
			this.updateSelect(this.ontologiesMap[category],formatInputTooShort);

		} else {
			this.select.parent().hide();
		}
		if (showProps) {
			this.paramsTable.parents('div.dataTables_wrapper').first().show();
			var table = $("#properties_table").DataTable();
			table.column("target:name").search(category ).draw();
			table.rows().invalidate().draw()
		} else {
			this.paramsTable.parents('div.dataTables_wrapper').first().hide();
		}

	},

	open_modal : function(element, evt) {
		// score - filter
		var action = $(element).data('id').split("_")[0];
		// structure uploaded keyword activity process localization pathways
		// pocket metadata
		var category = $(element).data('id').split("_")[1];

		switch (category) {
		
		case "structure":
			this.modal_options("Structure parameters", action, category, false,
					false, true)
			break;
		case "pocket":
			this.modal_options("Pocket parameters", action, category, false,
					false, true)
			break;
		case "keyword":
			this.modal_options("Filter by keyword", action, category, true,
					false, false)
			break;
		case "activity":
			this.modal_options("Select protein activity", action, category,
					false, true, false, "Enter a word to search for EC or GO:MolecularFunction term")
			break;
		case "process":
			this.modal_options("Select biological process", action, category,
					false, true, false, "Enter a word to search for  GO:BiologicalProcess term")
			break;
		case "localization":
			this.modal_options("Select molecular localization", action,
					category, false, true, false)
			break;
		case "pathways":
			this.modal_options("Pathway Parameters", action, category, false,
					this.pathwaySelect, true)
			break;
		case "variant-strain":
			this.modal_options("Variant Strains", action, category, false,
					false, true)
			break;
		case "variant-db":
			this.modal_options("Variant Databases", action, category, false,
					false, true)
			break;
		case "uploaded":
			this.modal_options("Your properties", action, category, false,
					false, true)
			break;
		case "metadata":
			this.modal_options("Select metadata", action, "protein", false,
					false, true)
			break;

		}
		this.modal.modal('show');

	},
	
	selectedOntologies : function(ontologies){
		var onts = []
		ontologies.forEach(x => {
			var arr = x.split(":");
			if(arr.length > 1){
				onts.push( [arr[0],arr[1]] )
			} else {
				onts.push( [arr[0],null] )
			}
		})
		return this.ontologies.filter(
				x =>{
					var result = false;
					onts.forEach( o => {
						if((x.ontology ==  o[0]) &&  (x.database ==  o[1]) ) {
						result = true;					 
					}})
					return result;
				}
				
		).sort((x,y) =>  x.order > y.order);
// return this.ontologies.filter( x =>
// ((ontologies.indexOf(x.ontology.split(":")[]) != -1) &&
// ( (x.ontology.split(":").length > 1) ? true : (x.database ==
// x.ontology.split(":")[1] ) ))
	},
	updateSelect : function(ontologies,formatInputTooShort) {
		var me = this;
		var selected_data = me.selectedOntologies(ontologies).map(x => { return {id:x.term,text: x.term + " - " + x.name} } ) ;
		this.select.select2({
			data: selected_data,	
			templateResult: function (d) { return $("<div>" + d.text + "</div>" ); },
		    templateSelection: function (d) { return $("<div>" + d.text + "</div>" ); },
			/*
			 * ajax : { url : me.ontologies_url,// "../rest/ontologies/search",
			 * dataType : 'json', delay : 250, data : function(params) { return {
			 * ontologies : ontologies.join(","), q : params.term, organism :
			 * me.organism }; }, processResults : function(data, page) { return {
			 * results : $.map(data, function(item) { return { text : item.term + " - " +
			 * item.name, id : item.term } }) }; }, cache : true },
			 */
			language: { inputTooShort: function (x) { return (formatInputTooShort != undefined) ? formatInputTooShort :  'Please enter ' + x.minimum + ' or more characters' } },
			value: ""
			//minimumInputLength : 2
		});
	},

	setDrugParamsTable : function(drugParamsTable) {
		this.drugParamsTable = drugParamsTable;
	},

	open : function(dpType, mode) {
		this.selectMode(mode);
		var dps = $.grep(this.drugParams, function(x) {
			return x.target == dpType;
		});
	},
	selectMode : function(mode) {
		if ("score" == mode) {
			$("#btn_add_filter").hide();
			$("#btn_add_score").show();
		} else {
			$("#btn_add_filter").show();
			$("#btn_add_score").hide();
		}
	},
	ok : function() {
		// evt.preventDefault();
		var keywordsData = this.textInput.val();
		if (keywordsData.length > 0) {
			this.keywordFilter.add_keyword(keywordsData);
			this.textInput.val("")
		}

		var ontologyData = this.select.val();
		if ((ontologyData != null) && (ontologyData.length > 0)) {
			var description = this.select.select2("data")[0].text.split(" - ")[1];
			var ontology = ontologyData.trim();
			if(this.drugParamsTable == this.filterTable){
				this.ontologyFilter.add_ontoloty(ontology, description);	
			} else {
				
				
				var param = new $.ScoreParam("ontology");
				
				param.description = description.trim()
				param.type = "value";
				param.value =  ontology;
				param.operation = "equal";
				
				
					param.coefficient = 1;
					this.drugParamsTable.addParam(param);
				
			}			
		}

		var rows = this.props_table.$('tr.selected');
		$.each(rows, function(i, row) {
			var data = this.props_table.row(row).data();
			if (!this.has_param(data.name)) {
				data.coefficient = 1;
				this.drugParamsTable.addParam(data);
			}
		}.bind(this));
		
		this.change = true;
		this.modal.modal('hide');

	},

	has_param : function(param_name) {
		return $.grep(this.drugParamsTable.data(), function(filterData) {
			return filterData.name == param_name;
		}).length > 0;
	},
	cancel : function() {
		this.change = false;
		this.modal.modal('hide');
	},

	refreshTables : function(callback_on_change) {
		if (this.change) {
			callback_on_change()
		}
		this.change = false;

	},
	initPropsTable : function() {
		var paramsTable = this.paramsTable;
		var me = this;
		
		this.props_table = paramsTable
				.DataTable({
					"data" : searchProps,
					"language": {						
					    "sInfoFiltered":   ""
					},
					"drawCallback" : function(row, data, index) {

						
						me.paramsTable.find("tr").removeClass('selected');
						
						
						$.each(me.paramsTable.find(".propBoxes"), function(i,input){
							if ($.grep(me.drugParamsTable.data(),
									function(filterData) {
										return filterData.name == $(input).attr("class").split(' ')[1].split("class_")[1]
									}).length > 0) {
								
								$(input).parent().parent().addClass("selected")
								$(input).prop("checked",true)
							}
						})
						
						
					},
					"columns" : [
							{
								"name" : "check",
								"title" : 'check',
								"orderable" : false,
								"data" : "name",
								render : function(data, type, full, meta) {
									var selected = "";

// if ($.grep(me.drugParamsTable.data(),
// function(filterData) {
// return filterData.name == data
// }).length > 0) {
// selected = ' class="propBoxes"';
// }

									return '<input class="propBoxes class_' + data + '"'
											+ selected
											+ ' type="checkbox" onchange="selectProp($(this).parent().parent())" ' + data + '" />';
								}
							}, {
								"name" : "name",
								"title" : 'Name',
								"orderable" : false,
								"data" : "name",
									"render" : (d) => '<p style="width:150px;word-wrap: break-word;">' + d.replace(new RegExp("_","g")," ") + "</p>"

							}, {
								"name" : "description",
								"title" : 'Description',
								"orderable" : false,
								"data" : "description",
								"render" : (d) => '<p style="width:300px;word-wrap: break-word;">' + d + "</p>"

							}, {
								"name" : "type",
								"title" : 'Type',
								"orderable" : false,
								"data" : "type"

							}, {
								"name" : "target",
								"title" : 'target',
								"orderable" : false,
								"visible" : false,

								"data" : "target"

							} ]
				});
	}

}
