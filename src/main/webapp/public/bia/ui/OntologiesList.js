$.OntologiesList = function(divElement, api) {
	this.divElement = divElement;
	this.show_infered = false;
	this.children = {}
	this.leaf_nodes = [];
	this.organism = null;
	this.api = api;
	this.ontologies_loaded_handler = function(ontologies) {
	}
}

$.OntologiesList.prototype = {
	web_init : function(ontology_names,organism) {
		this.api.ontology_terms(organism,ontology_names,this.init.bind(this))
		.always(
						function() {
							this.divElement
									.find("#ontologies_list_loading-img")
									.remove();
							this.divElement.find("#ontologies_list_overlay")
									.remove();
						}.bind(this));
		
				

	},
	init : function(ontologies) {

		var grouped_onts = this._group_by_ontology(ontologies);
		// grouped_onts_and_db = $.map(grouped_onts, this._group_by_database
		// .bind(this));
		var grouped_onts_and_db = {}

		$.each(grouped_onts, function(ont_name, ontologies2) {
			grouped = {};
			$.each(ontologies2, function(i, ontology) {
				
				var database = ontology.database
				if (typeof (ontology.children) != 'undefined') {
					if (typeof (this.children[ont_name]) == 'undefined') {
						this.children[ont_name] = [];
					}
					this.children[ont_name].push(ontology.term);
				}
				if (typeof (database) == 'undefined' || database == null) {
					database = '';
				}
				if (typeof (grouped[database]) == 'undefined') {
					grouped[database] = []
				}
				grouped[database].push(ontology);
			}.bind(this));
			grouped_onts_and_db[ont_name] = grouped;

		}.bind(this));

		$.each(grouped_onts_and_db, this._render_ont_group.bind(this));

		$("#ontologies_section .overlay").remove();
		this.ontologies_loaded_handler(ontologies)

	},
	_render_ont_group : function(ont_name, grouped_by_db_ontologies) {
		$.each(grouped_by_db_ontologies, this._render_db_group.bind(this));
	},
	_render_db_group : function(db_name, ontologies) {
		
		var ont_to_render = ontologies;
		if (!this.show_infered) {
			ont_to_render = this._filter_infered(ont_to_render);
		}
		if (ont_to_render.length == 0){
			return;
		}
		
		if (!db_name.isEmpty()) {
			this.divElement.find("#ontologies_list").append(
					'<tr><td colspan="2"><b>' + db_name + " ("
							+ ontologies[0].ontology.toUpperCase()
							+ ")</b></td><tr>");
		} else {
			this.divElement.find("#ontologies_list").append(
					'<tr><td colspan="2"><b>'
							+ ontologies[0].ontology.toUpperCase()
							+ "</b></td><tr>");
		}

		$.each(ontologies, this._render_ontology_term.bind(this));
	},
	_group_by_ontology : function(ontologies) {
		grouped = {};
		
		$.each(ontologies, function(i, ontology) {
			ont_name = ontology.ontology;
			if (ont_name.toLowerCase() != "BIOCYC_REAC".toLowerCase()){
				
			
			if (typeof (grouped[ont_name]) == 'undefined') {
				grouped[ont_name] = []
				
			}
			if ( $.grep(grouped[ont_name],function(x){
				return ontology.term == x.term; 
			}).length   == 0){
				grouped[ont_name].push(ontology);	
			}
			
			}

		});
		
		
		return grouped;
	},
	_filter_infered : function(ontologies){
		return $.grep(ontologies, function(ontology){
			
			if (typeof (ontology.children) != 'undefined') {
				var show = true;
				$.each(ontology.children,
						function(i, child_ont) {
							if (this.children[ontology.ontology]
									.indexOf(child_ont) != -1) {
								show = false;
							}
						}.bind(this));
				if (!show) {
					return false;
				}
			}			
			return true;
			
			
		}.bind(this));
	},

	_render_ontology_term : function(subgroup, ontology) {

		if (!this.show_infered) {
			if (typeof (ontology.children) != 'undefined') {
				var show = true;
				$.each(ontology.children,
						function(i, child_ont) {
							if (this.children[ontology.ontology]
									.indexOf(child_ont) != -1) {
								show = false;
							}
						}.bind(this));
				if (!show) {
					return;
				}
			}
			
		}
		
		var descripcion = ontology.description;

		if ((typeof (description) == "undefined") || (description == null)
				|| (description.isEmpty())) {
			descripcion = ontology.name;
		}
		this.leaf_nodes.push(ontology.term);
		var filter = "";
		if (this.organism != null) {
			filter = '<a href="../rest/redirect?type=filter&key=keywords&value={0}&organism={1}'
					.format(ontology.term + " - " + ontology.name,
							this.organism)
					+ '"><i class="fa fa-filter">&#160;</i> </a>'
		}
		
		this.divElement
				.find("#ontologies_list")
				.append(
						'"<tr><td><a href="'
								+ hrefOntologyLink(ontology.ontology,
										ontology.term)
								+ '"><small title="click to go to ' +  ontology.ontology.toUpperCase() + '" class="badge bg-yellow">?</small></a>'

								+ ontology.term + "</td><td>" + filter
								+ descripcion + '</td></tr>');
	}
}
