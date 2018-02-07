$.ReactionsTree = function(divElement, organism,api) {
	this.divElement = divElement;
	this.organism = organism;	
	this.api = api;
	this.tree_data = []
	/**
	 * { pathway1:{ reaction1:{ substrates:
	 * {specie1:[gene1],specie2:[gene2,gene3]} products : {specie3:[gene4]} },
	 * reaction2:{ substrates: {specie2:[gene9,gene3]} products :
	 * {specie5:[gene5],specie4:[gene7,gene8]} },... }, pathway2:{ reaction1:{
	 * substrates: {specie1:[gene1],specie2:[gene2,gene3]} products :
	 * {specie3:[gene4]} }, reaction2:{ substrates:
	 * {specie1:[gene1],specie2:[gene2,gene3]} products : {specie3:[gene4]}
	 * },... },... }
	 */
	this.reactions = {}
	
	this._replace_ont_regexp = new RegExp("_", "g");
	this.render_links = true;

}

$.ReactionsTree.prototype = {
	_correct_name : function(term) {
		return term.replace(this._replace_ont_regexp, ".")
	},
	web_init:function (reactions,api){
		this.ontologies_term_url = "../rest/ontologies/terms";
		this.gene_prod_list_url = '../rest/redirect?type=filter&key=keywords&value={0}&organism={1}'; 	
		this.get_protein_with_gene_url = "../rest/redirect?type=product&key=gene&value={0}";
		this.ontologies_params = {
				"search" : []
			};
		this.ontologies_params[auth_key] = auth_value;
		
		var ontologies = [];
		$.each(this.pathways, function(i, pathway) {
			ontologies.push(this._correct_name(pathway));
		}.bind(this));
		$.each(reactions, function(i, reaction) {

			ontologies.push(this._correct_name(reaction.name));

		}.bind(this));
		this.ontologies_params["search"] = encodeURI(ontologies);
		$.post(this.ontologies_term_url, this.ontologies_params,
				"application/json").done(this._load_reactions.bind(this)).fail(
				function(jqxhr, textStatus, error) {
					var err = textStatus + ", " + error;
					console.log("Error fetching ontologies: " + err);
				});
		
	},	
	init : function(reactions,ontologies) {
		this.reactions = reactions
		this._load_reactions(ontologies);
		
	},
	_load_reactions : function(ontologies) {
		
		this.ontologies = ontologies;
		$.each(this.pathways, function(i, pathway) {
			var pathway_reactions = $.grep(this.reactions, function(reaction) {
				if(reaction.pathways.length > 0){
					return reaction.pathways.indexOf(pathway) != -1	
				} else {
					return pathway == null;
				}
				
			});
			this._render_pathway(pathway, pathway_reactions)
		}.bind(this));

		this.create_tree();
	},
	_render_pathway : function(pathway, reactions) {
		if(pathway){
			
		
		var ontology = $.grep(this.ontologies, function(e) {
			return e.term.toUpperCase() == pathway.toUpperCase();
		})[0];
		
		var gene_prod_list_url = this.api.url_search_genome_keyword(this.organism,pathway);

		var ontology_name = "unknown"
		if (pathway != "unknown") {
			if (ontology){
				ontology_name = ontology.name;	
			} else  {
				ontology_name = "";
			}
			
		}
		var text = ontology_name
				+ '<a href="'
				+ hrefOntologyLink("biocyc", pathway)
				+ '"><small title="go to Biocyc" class="badge bg-yellow">?</small></a>';

		if (this.render_links) {
			text = text
					+ ' <a href="'
					+ gene_prod_list_url
					+ '"><i title="list proteins in the pathway" class="fa fa-filter">&#160;</i><a/>'
		}

		var pathway_node = {
			'text' : text,
			'ontology_url' : hrefOntologyLink("biocyc", pathway),
			'list_url' : gene_prod_list_url,
			'type' : "pathway",
			"children" : this._reaction_nodes(reactions),
			'state' : {
				'opened' : true,
				'selected' : false
			}
		}} else {
			
			var pathway_node = {
					'text' : "unknown",
					'ontology_url' : "#",
					'list_url' : "#",
					'type' : "pathway",
					"children" : this._reaction_nodes(reactions),
					'state' : {
						'opened' : false,
						'selected' : false
					}
		}}
		this.tree_data.push(pathway_node)
	},
	_species_nodes : function(species) {
		var species_nodes_list = [];
		
		
			
		
		$.each(species, function(i, specie) {
			var ontology = $.grep(this.ontologies, function(e) {
				return e.term.toUpperCase() == specie.name.toUpperCase() ;
			})[0]
			var link_list = "";
			var genes = specie.producers.concat(specie.consumers);
			
			if(genes.length > 10){
				link_list = genes.length.toString() + " genes"
			} else {
				
			
			
			$.each(genes,
					function(i, gene) {

						if (this.render_links) {
							link_list = link_list
									+ " "
									+ '<a href="'
									+ this.api.url_gene(this.organism,gene) + '">' + gene
									+ '</a>'
						} else {
							link_list = link_list + " " + gene
						}

					}.bind(this));
			}
			var text = ((ontology != undefined) ? ontology.name : specie.name) + '<a href="'
			+ hrefOntologyLink("biocyc", specie.name)
			+ '"><small title="go to Biocyc" class="badge bg-yellow">?</small></a>';
			specie_node = {
				"text" : text + ": " + link_list,
				genes : genes,
				type : "species"
			}
			species_nodes_list.push(specie_node)
		}.bind(this));
		
		return species_nodes_list;
	},
	create_tree : function() {

		$(this.divElement)
				.jstree(
						{
							'core' : {
								'data' : this.tree_data
							},
							"types" : {
								"default" : {
									"icon" : "glyphicon glyphicon-flash"
								},
								"pathway" : {
									"icon" : "glyphicon  glyphicon-retweet"
								},
								"species" : {
									"icon" : "glyphicon glyphicon-asterisk"
								},
								"substrates" : {
									"icon" : "glyphicon glyphicon-log-in"
								},
								"products" : {
									"icon" : "glyphicon  glyphicon-log-out"
								},
								"reaction" : {
									"icon" : "glyphicon glyphicon-transfer"
								}
							},
							"contextmenu" : {
								"items" : function($node, data) {
									if ($node.type == "pathway") {
										pathway_context = {
											"Products" : {
												"separator_before" : false,
												"separator_after" : false,
												"label" : "Pathway in Biocyc",
												"action" : function(obj) {
													window
															.open($node.original.ontology_url);
												}
											}
										}
										if (this.render_links) {

											pathway_context["Products"] = {
												"separator_before" : false,
												"separator_after" : false,
												"label" : "Products of pathway",
												"action" : function(obj) {
													window.location = $node.original.list_url;
												}
											}
										}
										return pathway_context
									} else if ($node.type == "species") {
										if (this.render_links) {
											var genes = {}

											$
													.each(
															$node.original.genes,
															function(i, gene) {
																genes[i + gene] = {
																	"separator_before" : false,
																	"separator_after" : false,
																	"label" : "Go to "
																			+ gene,
																	"action" : function(
																			obj) {
																		window.location = this.get_protein_with_gene_url
																				.format(gene);
																	}
																			.bind(this)
																}

															}.bind(this));
											return genes;
										}
									}
								}.bind(this)
							},
							"plugins" : [ "types", "contextmenu" ]
						})
				.bind(
						"select_node.jstree",
						function(e, data) {
							if (data.event.target.tagName.toLowerCase() == "a") {
								window.location = $(data.event.target).attr(
										"href")
							}
							if (data.event.target.tagName.toLowerCase() == "small") {
								window.location = $(data.event.target).parent()
										.attr("href")
							}

						});
	},
	_reaction_nodes : function(reaction_list) {
		
		var reaction_nodes_list = []
		$.each(reaction_list, function(i, reaction) {
			
			var ontology = $.grep(this.ontologies, function(e) {
				return ((e.term.toUpperCase() == reaction.name.toUpperCase()) || (e.term.toUpperCase() == reaction.name.toUpperCase().replace("_",".")));
			})[0]
			
			var text = ((ontology != undefined) ?  ontology.name : reaction.name) + '<a href="'
			+ hrefOntologyLink("biocyc", reaction.name)
			+ '"><small title="go to Biocyc" class="badge bg-yellow">?</small></a>';
			var children = []
			if (reaction.substrates.length){
				children.push({
					text : "substrates",
					type : "substrates",
					state : {
						'opened' : true
					},
					children : this._species_nodes(reaction.substrates)
				});
			}
			if (reaction.products.length){
				children.push({
					text : "products",
					type : "products",
					state : {
						'opened' : true
					},
					children : this._species_nodes(reaction.products)
				});
			}
			reaction_node = {
				text : text,
				ontology_url : hrefOntologyLink("biocyc", reaction.name),
				type : "reaction",
				state : {
					'opened' : true
				},
				children : children
			}
			reaction_nodes_list.push(reaction_node);
		}.bind(this));
		return reaction_nodes_list
	}
}
