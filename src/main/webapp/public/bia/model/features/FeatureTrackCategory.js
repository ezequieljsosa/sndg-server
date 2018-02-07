$.FeatureTrackCategory = function(id,name,types) {
	this.id = id;
	this.name = name;
	this.types = types;
	this.render_link = function(feature_track,text){return text;}

}
$.FeatureTrackCategory.prototype = {
		matches : function(feature_track) {
			return this.types.indexOf(feature_track.type) != -1;
		}
//		initialize: function(feature_track) {
//			if(this.matches(feature_track)){
//				feature_track.category = this.name;
//				feature_track.syle.
//			}
//			
//		},
}
