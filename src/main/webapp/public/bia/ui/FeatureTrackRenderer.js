function bpfeature2trackfeature(feature, features) {
	var start =  feature.location.start
	var end =   feature.location.end
	
	
	var featureStart = start
			+ feature_offset(feature.strLocus, features);
	var featureEnd = end
			+ feature_offset(feature.strLocus, features);
	var description = feature.description;
	if (feature.description == null) {
		description = "";
	}
	var identifier = feature.identifier;
	
	if (feature.identifier == null) {
		identifier = feature.type 
		if (typeof SO_TERMS[feature.type] != "undefined") {
			identifier = SO_TERMS[feature.type]
		}
	}
	var intervals = [ [ featureStart, featureEnd ] ] ;
	if ((typeof feature.intervals != "undefined") && (feature.intervals != null)){
		intervals = feature.intervals
	}
	var ft = new $.FeatureTrack(feature.id, identifier, description,
			feature.type, intervals);
	ft.aln = feature.aln;
	
	ft.qualifiers = feature.qualifiers
	if ($.isDefAndNotNull(feature.source)){
		ft.source = feature.source;
	}
	
	
	return ft

}

$.FeatureTrackRenderer = function(divElementId, name, features, length) {
	this.divElementId = divElementId;
	this.name = name;
	this.features = features;
	this.length = length;
	this.onSelect = function(ft){}
	this.onUnSelect = function(ft){}
	this.id_map = {};
	// "diamond" for active sites, "triangle" for PTMs, "hexagon" for
	// glycosylation, "wave" for lipids, and "circle" for metals
	this.type_map = {
		"SO:0001811" : "triangle", // phosphorylation_site
		"SO:0000409" : "diamond", // Binding Site
		"SO:0001089" : "triangle", // post_translationally_modified_region
		"SO:0001656" : "circle", // metal_binding
		"SO:0000691" : "triangle", // cleaved_initiator_methionine
		"SO:0001104" : "diamond" // catalitic_residue
	};
}

$.FeatureTrackRenderer.prototype = {

	feature_type : function(feature_track) {
		var ftype = "rect";
		if (typeof (this.type_map[feature_track.type]) != "undefined") {
			ftype = this.type_map[feature_track.type];
		}
		return ftype;
	},
	feature_id : function(feature_track) {
		identifier = feature_track.id;
		if (typeof (this.id_map[feature_track.id]) != "undefined") {

			// identifier = "Modeled sequence:" + identifier;
			identifier = this.id_map[feature_track.id](identifier)
		}

		if (identifier == null) {
			identifier = feature_track.type;
		}
		return identifier;
	},
	feature_desc : function(feature_track) {
		var description = "";
		if (feature_track.desc != null) {
			description = feature_track.desc;
		}
		return description;
	},
	feature_name : function(feature_track) {
		var name = "";
		if (feature_track.name != null) {
			name = feature_track.name;
		} else {
			name = feature_track.type;
		}
		return name;
	},
	render_viewer_feature : function(feature_track) {
		var fname = this.feature_name(feature_track)
		var description = this.feature_desc(feature_track);
		var identifier = this.feature_id(feature_track);
		var ftype = this.feature_type(feature_track);

		var rendered_features = [];
		$.each(feature_track.intervals, function(i, interval) {
			var rendered_feature = {
				featureId : identifier,
				featureStart : interval[0], // feature_track.location.start +
				// feature_offset(feature_track.strLocus,this.features),
				featureEnd : interval[1], // feature_track.location.end +
				// feature_offset(feature_track.strLocus,
				// this.features),
				typeLabel : fname,
				featureLabel : fname,
				featureTypeLabel : "",
				typeCategory : "",
				typeCode : feature_track.type,
				evidenceText : "",// feature_track.get_attr("evidenceText"),
									// // "predicted",
				evidenceCode : "",
				type : ftype,
				color : feature_track.style.back_color
			// colorer.getRandomColor(identifier)
			};
			rendered_features.push(rendered_feature)
		});

		return rendered_features;
	},
	render_viewer_features : function() {
		// colorer = new Colorer();
		var rendered_features = [];
		$.each(this.features, function(i, feature) {
			rendered_features = rendered_features.concat(this
					.render_viewer_feature(feature));
		}.bind(this));
		return rendered_features;
	},
	load_feature_viewer : function(html_element_id, seq_id, features_to_render,
			seq_length) {

		this.sfv = new Biojs.SimpleFeatureViewer({
			// proxyUrl:"http://localhost/proxy/proxy.php",
			target : html_element_id,
			sequenceId : seq_id,
			features : features_to_render,
			sequenceLength : seq_length,
			showPrintButton : false,
			imageWidth : $("#" + html_element_id).width() - 10
		});
		this.sfv.onFeatureSelected(function( obj ) {
			var features = $.grep(this.features,function(feature){
				return feature.id == obj.featureId
			});
			this.onSelect(features[0])
		}.bind(this));
		this.sfv.onFeatureUnselected(function( obj ) {
			var features = $.grep(this.features,function(feature){
				return feature.id == obj.featureId
			});
			this.onUnSelect(features[0])
		}.bind(this));
		// this.sfv.onFeatureOn(
		// function( obj ) {alert(obj)})

	},
	init : function() {
		if (this.features != null) {
			redered_view_features = this.render_viewer_features();
			this.load_feature_viewer(this.divElementId, this.name,
					redered_view_features, this.length);
		} else {
			this.load_feature_viewer(this.divElementId, this.name, [],
					this.length);
		}
	}
}
