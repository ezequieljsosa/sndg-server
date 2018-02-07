$.FeatureTrack = function(id, name, desc, type,intervals) {

	this.id = id;
	this.name = name;
	this.desc = desc;
	this.type = type;
	this.intervals = intervals; // [[start1,end1],[start2,end2],...]
	this.style = new $.FeatureTrackStyle("black",null,{});
	this.map_attr = {}

}

$.FeatureTrack.prototype = {
	init : function() {

	},
	get_attr:function(attr){
		if( typeof this.map_attr[attr]  == "undefined"){
			return "";
		} else {
			return this.map_attr[attr];
		}
		
	}
}