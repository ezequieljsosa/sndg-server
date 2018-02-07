$.FeaturesList = function(divElement, features, organism) {
	this.divElement = divElement;
	this.features_ids = [];
	this.ontologies = [];
	this.identifiers = [];

	this.features = features;

	this.organism = organism;
	this.listeners = [];
	
	this.color_map = {
		"SO:0000417" : [ "#006400", "#483D8B", "#DAA520", "#778899", "#00008B",
				"#D2691E" ],

		"SO:0001111" : [ "#808080" ],
		"SO:0001114" : [ "#FFFF00" ],
		"SO:0001128" : [ "#008000" ],

		"SO:0001104" : [ "#FF0000" ],

		"SO:0000409" : [ "#808080" ],
		"SO:0001656" : [ "#A52A2A" ],

		"SO:0000418" : [ "#008000" ],
		"SO:0001077" : [ "#0000FF" ],

		"SO:0000857" : [ "#6495ED" ],

		"SO:0001811" : [ "#556B2F" ],
		"SO:0001089" : [ "#483D8B" ],
		"SO:0000691" : [ "#ADFF2F" ],

		"SO:0001079" : [ "#01640E" ],
		
		"SO:0001583": ["#01640E"]
	}
	var pdb_cat = new $.FeatureTrackCategory("polypeptide_structural_motif",
			"Homologous PDB", [ "SO:0001079" ]);
	var pdb_name = function(feature_track) {
		
		var identifier = feature_track.identifier;
		
		if (identifier.split("_").length == 4){
			if ((feature_track.qualifiers != null)
					&& (typeof (feature_track.qualifiers["model"]) != "undefined")) {
				var arr = identifier.split("/");
				return arr[arr.length - 1].replace(".pdb","").replace(".","__")
				
			} else {
				if (identifier.length == 4) {
					return identifier.toLowerCase();
				} else {
					return identifier.substring(0, 4).toLowerCase();
				}
			}
		} else  {
			return identifier
		}
		
	}
	var modify_text = function(feature_track,text){
		if ((feature_track.qualifiers != null)
				&& (typeof (feature_track.qualifiers["model"]) != "undefined")) {
			var arr = text.replace("<br />","").split("/");
			return arr[arr.length - 1].split(".")[0]
		} 
		return text
	}
//	pdb_cat.render_link = function(feature_track, text) {
//		
//		return '<a href="' + $.api.url_structure( pdb_name(feature_track))
//				+ '"> ' +modify_text( feature_track,text)
//				+ '<i class="fa fa-fw fa-sitemap"> &#160;</i></a>';
//	}
	this.categories = [
			new $.FeatureTrackCategory("missense_variant", "Variants",
				[ "SO:0001583" ]),
			new $.FeatureTrackCategory("domains", "Family/Domains",
					[ "SO:0000417" ]),
			new $.FeatureTrackCategory("secondary", "Secondary Structure", [
					"SO:0001111", "SO:0001114", "SO:0001128" ]),
			new $.FeatureTrackCategory("catalitic", "Catalitic Sites",
					[ "SO:0001104" ]),
			new $.FeatureTrackCategory("binding", "Binding Sites", [
					"SO:0000409", "SO:0001656" ]),
			new $.FeatureTrackCategory("localization", "Localization", [
					"SO:0000418", "SO:0001077" ]),
			new $.FeatureTrackCategory("homologous", "Homologous",
					[ "SO:0000857" ]),
			pdb_cat,
			new $.FeatureTrackCategory("post_trad",
					"Post Traductional Modification", [ "SO:0001811",
							"SO:0001089", "SO:0000691" ]),

	];

	this.on_finish = function() {
	};

}

$.FeaturesList.prototype = {

	renderFeature : function(feature, append_to, category) {

		var ident_ontology = $.grep(this.ontologies, function(e) {
			return e.term.toUpperCase() == feature.identifier;
		}.bind(this));
		if (ident_ontology.length == 0) {

			ident_ontology = {
				ontology : "?",
				description : "?"
			};

		} else {
			ident_ontology = ident_ontology[0];
		}

		var so_ontology = $.grep(this.ontologies, function(e) {
			return e.term.toUpperCase() == feature.type.toUpperCase();
		}.bind(this))[0];

		var trid = feature.id;
		if (feature.type == "SO:0001079") { // PDB
			trid = feature.identifier.split("_")[0];
		}

		if (feature.identifier == null) {
			feature.identifier = so_ontology.name;
		}

		// Skip they are secondary sequence features

		// if ((so_ontology != null)
		// && (so_ontology.term == "so:0001111"
		// || so_ontology.term == "so:0001114" || so_ontology.term ==
		// "so:0001128")) {
		// return;
		// }
		var feature_desc = "";

		if (ident_ontology.ontology == "?") {
			if (typeof so_ontology != "undefined") {

				var tooltip = so_ontology.name;

				var link = "#";
				if (so_ontology.term == "so:0000857") {
					link = hrefOrganismLink(organism, feature.identifier);
				} else {
					link = hrefOntologyLink(so_ontology.ontology,
							so_ontology.term);
				}

				feature_desc = '<a href="' + link + '"><small title="'
						+ tooltip + '" class="badge bg-yellow">?</small></a>';

			}
		} else {

			feature_desc =  '<a href="'
					+ hrefOntologyLink(ident_ontology.ontology,
							ident_ontology.term) + '"><small title="'
					+ ident_ontology.description
					+ '" class="badge bg-yellow">?</small></a>' + ((ident_ontology.description != null) ? ident_ontology.description : "");
		}

		checked = '';// 'checked="checked"'
		row = '<tr id="'
				+ feature.id
				+ '"><td><input type="checkbox" class="chk_feature" id="chk_'
				+ feature.id
				+ '" '
				+ checked
				+ ' /></td>'
				+ '<td> <div class="color_feature" id="color_'
				+ feature.id
				+ '"  style="height: 16px; width: 16px;"  >&#160;</div>'

				// + '<td> <input class="color_feature" id="color_'
				// + feature.id
				// + '" type="text" value="#000000" />'
				+ '</td>'
				+ '<td>'
				+ feature_desc
				+ "&nbsp;"
				+ category.render_link(feature, formatStringForLine(
						feature.identifier, 30))
				+ '</td><td>'
				+ wrapStringShowMore(absolute_position(feature.strLocus,
						this.features), 20) + '</td></tr>'; // <td>' +
		// feature.strSize +
		// '</td>
		append_to.append(row);

	},
	_renderHederRow : function(title) {
		var row = '<tr><td width="10px"></td><td width="10px"></td><td><b>'
				+ title + '</b></td><td><b>Segments</b> (start:end)</td></tr>';
		$("#sequence_list").append(row);
	},
	renderFeaturesFromCategories : function(grouped_features, divElement) {
		$.each(this.categories, function(i, category) {
			this.renderFeaturesFromCategory(category, grouped_features,
					divElement);
		}.bind(this));
	},
	renderFeaturesFromCategory : function(category, grouped_features,
			divElement) {
		var features_to_append = [];
		$.each(this.feature_tracks, function(i, feature_track) {
			if (category.matches(feature_track)) {
				// Inicializa la categoria, tendria que ir en otro lado
				feature_track.category = category.id;
				// ****************************
				features_to_append.push(feature_track)
			}
		});
		if (features_to_append.length > 0) {
			this._renderHederRow(category.name);

			$.each(category.types, function(i, type) {

				if (typeof grouped_features[type] != "undefined") {
					this._render_grouped_by_identifier(grouped_features[type],
							divElement, category);
					delete grouped_features[type];
				}

			}.bind(this));
		}
	},
	renderFeatures : function() {
		this.feature_tracks = $.map(this.features, function(feature) {
			return bpfeature2trackfeature(feature, this.features)
		}.bind(this));
		$.each(this.feature_tracks, function(i, feature_track) {
			var color = "#000000"
			if (typeof this.color_map[feature_track.type] != "undefined") {
				color = this.color_map[feature_track.type].randomElement();
			}
			feature_track.style.back_color = color;
		}.bind(this));

		
		var grouped_features = this._group_by_type(this.features);
		this
				.renderFeaturesFromCategories(grouped_features,
						$("#sequence_list"));

		var onclick = '$(this).attr(\'onclick\',\'\'); $(\'#sequence_list\').append($(\'#other_features\').html().replace(\'<tbody>\',\'\').replace(\'</tbody>\',\'\'));$(\'#other_features\').remove();'
		row = '<tr><td>?</td><td><b> <a href="javascript:void(0);" onclick="'
				+ onclick
				+ '">'
				+ 'Others </a> </b></td><td><b>Segments</b> (start:end)</td></tr>';
		//$("#sequence_list").append(row);

		$.each(grouped_features,
				function(group, features2) {

					this._render_grouped_by_identifier(features2,
							$("#other_features"),this.categories[0]);

				}.bind(this));

		var me = this;
		$(".chk_feature").change(
				function(evt) {					
					me.on_visible_change($(this).attr("id").split("_")[1], $(
							this).is(':checked'));
				});
		$.each($(".color_feature"), function(i, elem) {

			var feature_tracks = this._feature_tracks_from_id($(elem)
					.attr("id").split("_")[1]);
			$.each(feature_tracks,
					function(i, feature_track) {
						$(elem).css('background-color',
								feature_track.style.back_color);
					});

			/*
			 * $(elem).val(feature_track.style.back_color );
			 * $(elem).colorPicker();
			 * 
			 * 
			 * $(elem).change(function() { var color = $(this).val();
			 * me.on_color_change($(this).attr("id").split("_")[1], color); })
			 */;
		}.bind(this));

	},
	_feature_track_from_id : function(id) {
		var feature_track = $.grep(this.feature_tracks, function(e) {
			return e.id == id;
		}.bind(this));
		if (feature_track.length > 0) {
			return feature_track[0]
		} else {
			return null;
		}
	},
	_feature_tracks_from_id : function(id) {
		return $.map(id.split(" - "), this._feature_track_from_id.bind(this))
	},
	on_color_change : function(id, color) {
		var feature_tracks = this._feature_tracks_from_id(id);
		$.each(feature_tracks, function(i, feature_track) {
			feature_track[0].style.back_color = color;
		});

		// listener.color_changed(feature[0], color);

	},
	on_visible_change : function(ids, status) {
		var feature_tracks = [];
		$.each(ids.split(" - "), function(i, id) {
			var feature_track = $.grep(this.feature_tracks, function(e) {
				return e.id == id;
			}.bind(this));
			feature_tracks.push(feature_track[0]);

		}.bind(this));

		$.each(this.listeners, function(i, listener) {
			
			listener.visible_changed(status, feature_tracks);
		}.bind(this));

	},
	_render_grouped_by_identifier : function(features, append_to, category) {
		grouped_ident = this._group_by_identifier(features);
		$.each(grouped_ident, function(identifier, features3) {
			var str_ids = "";
			var features_to_show = features3;
			if (identifier != "") {
				features_to_show = [ JSON.parse(JSON.stringify(features3[0])) ]
				var range = "";
				$.each(features3, function(i, feature) {
					var start =  feature.location.start
					var end =   feature.location.end
					range = range + start + ":"
							+ end + " - "
					str_ids = str_ids + feature.id + " - "
				});
				range = range.substring(0, range.length - 3)
				str_ids = str_ids.substring(0, str_ids.length - 3)
				features_to_show[0].strLocus = range;
				features_to_show[0].id = str_ids;

			}
			$.each(features_to_show, function(i, feature_to_show) {
				this.renderFeature(feature_to_show, append_to, category);
			}.bind(this));
		}.bind(this));
	},
	web_init : function(api) {
		var ontologies = []
		$.each(this.features, function(type, feature) {
			if (feature.type != "missense_variant") {
				this.features_ids.push(feature.id);
				ontologies.push(feature.type);
				ontologies.push(feature.identifier);
				this.identifiers.push(feature.identifier);
			}

		}.bind(this));

		this._fetch_ontologies(ontologies,api);

	},
	_group_by_type : function(features) {
		grouped = {};
		$.each(features, function(i, feature) {
			ftype = feature.type;
			if (typeof (grouped[ftype]) == 'undefined') {
				grouped[ftype] = []
			}
			grouped[ftype].push(feature);

		});
		return grouped;
	},
	_group_by_identifier : function(features) {
		grouped = {};
		$.each(features, function(i, feature) {
			ftype = feature.identifier;
			if (typeof (ftype) == "undefined") {
				ftype = "";
			}
			if (typeof (grouped[ftype]) == 'undefined') {
				grouped[ftype] = []
			}
			grouped[ftype].push(feature);

		});
		return grouped;
	},
	_fetch_ontologies : function(ontology_names,api) {
		api.ontology_terms(this.organism,ontology_names,this.renderFeatures.bind(this))
		.always(function() {
			this.on_finish();
		}.bind(this));		
	
	},
	init: function(ontologies){
		this.ontologies = ontologies;
		this.renderFeatures();
		this.on_finish();
	}

}
