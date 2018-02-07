$.JBrowseWrapper = function(iframe_id, base_url,tracks) {
	
	this.iframe = iframe_id;
	this.base_url = $.isDefAndNotNull(base_url) ? base_url : "/xomeq/public/jbrowse/?data=data/";
	this.tracks = $.isDefAndNotNull(tracks) ? tracks : ["DNA","Genes"];

	this.default_error = function(e) {
		console.log(e)
	}
}

$.JBrowseWrapper.prototype = {
	init : function(collection_name, nav, localization, highlight) {
		$.isDefAndNotNull()
		var url = this.base_url + '/' + collection_name
				+ "&tracks=" + this.tracks.join('%2C') + "&nav=" + nav.toString()
				+ "&tracklist=false&highlight=" + $.objOrDef(highlight,"")
				+ "&loc=" + $.objOrDef(localization,"")
				
		this.iframe.attr("src", url);
	}
}