$.Localization = function(reference,start,end) {
	this.reference = reference;
	this.start =  start;
	this.end = end;
}

$.Localization.prototype = {
		toString: function(){
			return this.reference + ":" + this.start.toString() + ".." + this.end.toString(); 
		},
		expand : function(){
			var size = this.end - this.start + 1

			var end = this.end + Math.round(size * 0.5);

			var begin = this.start - Math.round(size * 0.5);
			if (begin < 0)
				begin = 0;
			return new $.Localization(this.reference,begin,end)
		}
}