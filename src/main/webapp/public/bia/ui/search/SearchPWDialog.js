$.SearchPWDialog = function(modal, select,  paramsTable, drugParams,
		okBtn, cancelBtn, filterTable,scoreTable, ontologies_url, organism) {

	this.actions = [];

	this.drugParams = drugParams;
	this.drugParamsTable = scoreTable;
	this.filterTable = filterTable;
	this.scoreTable = scoreTable;
	
	this.ontologies_url = ontologies_url;
	
	// HTML
	this.modal = modal;
	this.select = select;
	this.paramsTable = paramsTable;
	this.change = true;
	this.okBtn = okBtn;
	this.cancelBtn = cancelBtn;
	
	this.ontologiesMap = {
			activity : [ "go:molecular_function", "ec", "pfam", "cog" ],
			process : [ "go:biological_process" ],
			localization : [ "go:cellular_component" ],
			pathways : [ "biocyc_pw" ] //, "biocyc_reac"
		};

}
$.SearchPWDialog.prototype = {
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
				
			});

			this.initPropsTable();			

			$(".open-modal").click(function(evt) {
				me.open_modal(this, evt);
			});	

		},

		modal_options : function(title, action, category,  showSelect,
				showProps) {
			this.modal.find(".modal-title").html(title)
			
			$("#pw_title").html('');
			
			if (action == "score") {
				this.drugParamsTable = this.scoreTable;
			} else {
				this.drugParamsTable = this.filterTable;
			}
			
			if (showSelect) {
				
					this.actions.push(function() {
						this.select.select2('open');
					}.bind(this))
				

				this.select.parent().show();
				this.updateSelect(this.ontologiesMap[category])

			} else {
				this.select.parent().hide();
			}
			if (showProps) {
				
				this.paramsTable.parents('div.dataTables_wrapper').first().show();
				var table = $("#properties_table").DataTable();
				table.column("target:name").search("^" +  category + "$",true).draw();
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
				this.modal_options("Structure parameters", action, category, 
						false, true)
				break;
			case "pocket":
				this.modal_options("Pocket parameters", action, category, 
						false, true)
				break;			
			case "activity":
				this.modal_options("Select protein activity", action, category,
						 true, false)
				break;
			case "process":
				this.modal_options("Select biological process", action, category,
						 true, false)
				break;
			case "localization":
				this.modal_options("Select molecular localization", action,
						category,  true, false)
				break;
			case "pathway":
				this.modal_options("Pathway Parameters", action, category, 
						false, true)
			case "pathways":
				this.modal_options("Pathway Parameters", action, category, 
						false, true)
				break;
			case "uploaded":
				this.modal_options("Your properties", action, category, 
						false, true)
				break;
			case "metadata":
				this.modal_options("Select metadata", action, "protein", 
						false, true)
				break;

			}
			this.modal.modal('show');

		},
		updateSelect : function(ontologies) {
			
			var me = this;
			this.select.select2({
				ajax : {
					url : me.ontologies_url,// "../rest/ontologies/search",
					dataType : 'json',
					delay : 250,
					data : function(params) {
						
						var ontologies_txt = ontologies;
						if (ontologies != undefined){
							ontologies_txt = ontologies.join(",")
						}
						return {
							ontologies : ontologies_txt,
							q : params.term,
							organism : me.organism
						};
					},
					processResults : function(data, page) {
						return {
							results : $.map(data, function(item) {
								return {
									text : item.term + " - " + item.name,
									id : item.term
								}
							})
						};
					},
					cache : true

				},
				minimumInputLength : 2
			});
		},

		

		open : function(dpType, mode) {
			
			var dps = $.grep(this.drugParams, function(x) {
				return x.target == dpType;
			});
		},
		
		ok : function() {
			
			var ontologyData = this.select.val();
			if ((ontologyData != null) && (ontologyData.length > 0)) {
				var description = this.select.select2("data")[0].text.split(" - ")[1];
				var ontology = ontologyData.trim();
				
					
					var param = new $.ScoreParam("ontology");
					
					param.description = description.trim()
					param.type = "value";
					param.value =  ontology;
					param.operation = "equal";
					param.groupoperation = "max";
					
						param.coefficient = 1;
						this.drugParamsTable.addParam(param);
					
							
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

//										if ($.grep(me.drugParamsTable.data(),
//												function(filterData) {
//													return filterData.name == data
//												}).length > 0) {
//											selected = ' class="propBoxes"';
//										}

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
