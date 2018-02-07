/**
 * column filter keywords
 * 
 */
$.FreeTextFilter = function(textInput, scoreTable) {

	this.searchTable = null;
	this.scoreTable = scoreTable;
	this.textInput = textInput;
	
}
$.FreeTextFilter.prototype = {
	start_filtering_by : function(keywords) {
		var param = this.score_param(keywords);
		this.searchTable._options["search"] = {
			"search" : JSON.stringify( {filters: [param], scores:[]} )
		}
		this.scoreTable.addParam(param);
	},
	score_param : function(keyword) {
		var param = new $.ScoreParam("keyword");
		param.description = "related word";
		param.type = "value";
		
		param.value = keyword
		param.operation = "equal";
		return param
	},
	add_keyword : function(value){
		
		if (!value.replace(" ", "").isEmpty()) {
			var param = this.score_param(value);
			
			this.scoreTable.addParam(param);
			this.searchTable.search_gene_prods();	
		}			
	}, 
	init : function() {
		$("body").on('keyup change', "#" + this.textInput.attr('id'), function(e) {
			var value = $(e.target).val();
			e.preventDefault();
			if (e.keyCode == 13) {
				this.add_keyword(value)			
							
			}
		}.bind(this));
	},
}
$.DescriptionFilter = function(textInput) {
	
	this.textInput = textInput;

}
$.DescriptionFilter.prototype = {
	column : function(){ return  {
		"regex" : true,
		"name" : "description",
		"title" : "Description",
		"data" : "description",
		"defaultContent" : "?"
	}},
	init : function() {
		this.textInput.on(
				'keyup change',
				function(e) {
					
					var me = this;
					var value = $(e.target).val()
					
						e.preventDefault();
					if(this.proc != null){
						clearTimeout(this.proc);
					}
					this.proc = setTimeout(function(){
							me.proc = null;
							me.searchTable.searchBy("description:name",  value,true);
							
						}, 500);										
					
//					if (e.keyCode == 13) {
//						e.preventDefault();
//						this.searchTable.searchBy("description:name",$(e.target).val(),true)
//						
//					}
				}.bind(this));
	},
}
$.PathwaysFilter = function(textInput,  scoreTable) {
	this.scoreTable = scoreTable;
	this.textInput = textInput;
}
$.PathwaysFilter.prototype = {
	column : function(){ return  {
		"name" : "pathways",
		"title" : "Pathways",
		"data" : "reactions",
		"render" : function(data, type, row) {
			var me = this;
			if (data.length == 0) {
				return "No data";
			} else {
				/*
				var reaction_names = $.unique(
						$.map(data, function(reaction) {

							return this.render_pathway_ontologies_cell(reaction,
									row.organism)
						}.bind(this))).join(",");
				
				if( reaction_names.length < 100 ){
					return '<p style="width:100px;word-wrap: break-word;">' +  reaction_names + '</p>' ;
				}  else {
					
					return '<p style="width:100px;word-wrap: break-word;"' + reaction_names.split("-.-")[0] + " " + '<a onclick="$(this).parent().html(\'' + 
						reaction_names.replace(new RegExp('"', 'g'),"\\'")  + '\')"> More...</a></p>';
				}*/
				var pathways = [];
				data.forEach( x => {x.pathways.forEach(y => { if(pathways.indexOf(y) == -1  ){pathways.push(y)} })} )
				if (pathways.length > 0){
					return '<a href="'
					+ me.searchTable.api.url_genome_pathway(row.organism,
							pathways.join(","))
					+ '" title="' + pathways.length.toString() +  ' pathways associated"><i  class="fa  fa-gears">&#160;</i> ' + pathways.length.toString() +  'PW </a>'
				} else {
					return '<span title="No pathways associated, ' + data.length.toString() + 'reactions detected " >0 PW ' + data.length.toString() + " R</span>" 
				}
				
			}
		}.bind(this)
	}},
	init : function() {
		var me = this;
		this.textInput.autocomplete(
				{
					source : function(request, response) {

						$.get(this.searchTable.api.url_pw_ontology_search(), {
							q : request.term,
							ontologies : "biocyc_pw"
						}, function(data) {
							if (data.length == 0) {

								$("#pw-empty-message").text(
										"No results found with: "
												+ request.term);
							} else {
								$("#pw-empty-message").empty()
								response( $.map( data,function(item){
									
									return {label: item.term   + "-" + item.name, id: item.term  ,value: item.term + "-" + item.name }
								} ) );
							}

						});
					}.bind(this),

					minLength : 3,
					select : function(event, ui) {
						var param = new $.ScoreParam("pathway");
						param.description = "the protein is involved in a reaction that belongs to the specifided pathway";
						param.type = "value";
						param.value =  ui.item.id
						param.operation = "equal";
						
						this.scoreTable.addParam(param);
						this.searchTable.search_gene_prods();
						//		me.searchTable.tableFilter(ui.item.term)
					}.bind(this)
				});
//			.autocomplete("instance")._renderItem = function(ul, item) {
//			return $("<li>").append(
//					"<a>" + item.term + "<br />" + item.name + "</a>")
//					.appendTo(ul);
//		};
	},
	render_pathway_ontologies_cell : function(reaction, organism) {

		var reaction_str = "";
		var me = this;
		if(reaction.pathways.length != 0){
			$
			.each(
					reaction.pathways,
					function(i, pathway) {
						reaction_str = reaction_str
								+ '<a href="'
								+ me.searchTable.api.url_genome_pathway(organism,
										pathway)
								+ '" title="Go to Pathway"><i  class="fa  fa-gears">&#160;</i></a>'
//								+ '<a href="#" title="Filter gene products" onclick="$(\'#'
//								+ me.searchTable.divElement.attr('id')
//								+ '\').data().tableFilter(\'' + pathway
//								+ '\')" ><i  class="fa fa-filter"></i></a>'
								+ " - " +  pathway + " -.- ";
					});
		} else {
			reaction_str = reaction_str + " Reaction " +  reaction.name + " (no pw assigned)  -.-"
		}
		
		
		return reaction_str;

	},
}
$.GeneFilter = function(textInput,  scoreTable) {
	this.scoreTable = scoreTable;
	this.textInput = textInput;
}
$.GeneFilter.prototype = {
	column : function(){ return  {
		"name" : "gene",
		"title" : "Gene",
		"data" : "gene",
		"regex" : true,
		"defaultContent" : "?",

		"render" : function(data, type, row) {

			var render_genes_cell = function(gene, index) {
				return '<a href="' + hrefOrganismLink(row["organism"], gene)
						+ '">' + gene + '</a>';
			};

			var genes = $.parseJSON(data);

			if ($.isArray(genes)) {
				var locus_tag = genes[0];
				return $.map(
						genes,
						function(gene, index) {
							
							//url_gene(row["organism"],locus_tag)
							return '<a href="'
									+ this.searchTable.api.url_protein(row.id) + '">' + gene + '</a>';
						}.bind(this)).join(" ");
			} else {
				return data;
			}

		}.bind(this)
	}},
	start_filtering : function(gene) {
		this.textInput.val(gene)
		this.searchTable._options["searchCols"][6] = {
			"sSearch" : gene,
			"bRegex" : true
		};
	},
	init : function() {
		//$('#search-table').DataTable().column('gene:name').filter_setted = true;
		
		$("body").on('keyup', "#" + this.textInput.attr('id'),
				this.search_gene.bind(this));

	},
	search_gene : function(e) {
		var me = this;
		var value = $(e.target).val()
		//if (e.keyCode == 13) {
			e.preventDefault();
		if(this.proc != null){
			clearTimeout(this.proc);
		}
		this.proc = setTimeout(function(){
				me.proc = null;
				me.searchTable.searchBy("gene:name",  value,true);
				
			}, 500)
						

		//}
	},
}
$.OntologyFilter = function(textInput,organism_select,  scoreTable) {
	this.scoreTable = scoreTable;
	this.textInput = textInput;
	this.organism_select = organism_select;
}
$.OntologyFilter.prototype = {
	init : function() {
		this.textInput.select2({
			placeholder : "search for an ontology",
			// allowClear : true,
			// tags : true,
			createSearchChoice : function() {
				return null;
			},
			ajax : {
				url : this.searchTable.api.url_search_ontologies(),// "../rest/ontologies/search",
				dataType : 'json',
				delay : 250,
				data : function(params) {
					return {

						q : params.term,
						organism : $('#organism_select').val()
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
			minimumInputLength : 2,

		}).focus(function() {
			$(this).select2('focus');
		});

//		$("#search_select2").change(function(e) {
//			var description = $(e.target).text().split(" - ")[1] // "the protein is annotated with the controlled vocabulary";
//			var ontology = $(e.target).text().split(" - ")[0]
//			this.add_ontoloty(ontology,description)
//			//me.searchTable.tableFilter( $(e.target).val() )		
//		}.bind(this));

	},
	add_ontoloty : function(ontology_str,description){
		var param = new $.ScoreParam("ontology");
		
		param.description = description.trim()
		param.type = "value";
		param.value =  ontology_str.trim()
		param.operation = "equal";
		
		this.scoreTable.addParam(param);
		this.searchTable.search_gene_prods();
	},
	render_ontologies_cell : function(ontology, index) {

		return '<a href="#" onclick="tableFilter(\'' + ontology + '\')">'
				+ ontology + '</a>';
	}
}
$.DruggabilityFilter = function(selectElement, scoreTable) {
	this.scoreTable = scoreTable;
	
	this.selectElement = selectElement;
}
$.DruggabilityFilter.prototype = {
	column : function(){ return  {
		"name" : "structure",
		"title" : "Druggability",
		"data" : "search.druggability",
		// "orderable" : false,
		"render" : function(data, type, row) {
			
			if (data == null) {
				return "-";
			} else {
				return data;
			}
		}
	}},
	init : function() {
		var me = this;
		
		
		
		this.selectElement.change(function(e) {
			var values_to_remove = [];
			var current = me.selectElement.val();
			
			
			me.selectElement.children().each(function(i, x) {
				if ($(this).val() != current) {
					values_to_remove.push($(this).val());
				}
			});
			
			var param = new $.ScoreParam("druggability_cat");
			param.description = "the protein is 3 categories acording with their druggability score";
			param.type = "value";
			param.operation = "equal";
			param.value =  $(this).val();
			
			
			me.scoreTable.addParam(param);
			me.searchTable.search_gene_prods();
			//me.searchTable.tableFilter( $(e.target).val() )	
//
//			var str_final = [];
//
//			$.each($("#search_txt").val().split(" "), function(j, y) {
//				if (values_to_remove.indexOf(y) == -1) {
//					str_final.push(y);
//				}
//			})
//
//			$("#search_txt").val(str_final.join(" "))
//			me.tableFilter(current);
//			me._save_state()

		});
	}

}

$.OrganismFilter = function(textInput,  scoreTable) {
	this.scoreTable = scoreTable;
	this.textInput = textInput;
}
$.OrganismFilter.prototype = {
	column : function(){ return  {
		"name" : "organism",
		"title" : "Organism",
		"data" : "organism",
		"visible": false,
		"render" : function(data, type, row) {
			return '<a href="' + this.searchTable.api.url_genome(row.organism)
					+ '">' + data.trim() + '</a>';
		}.bind(this)
	}},
	init : function() {
		var me = this;
		$("#organism_select").on(
				'change',
				function() {
					me.searchTable.searchBy(me.searchTable.searchBy("organism:name",'^' + $(this).val() + '$',true, false))	
				});
	},
	start_filtering_by : function(organism) {

		this.searchTable._options["searchCols"][3] = {
			"sSearch" : organism
		};

	},
}