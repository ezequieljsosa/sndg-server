$.ChainColorer = function() {
	this.color_map = {}
}
$.ChainColorer.prototype = {
	color : function(glmol, atoms) {
		glmol.colorByChain(atoms);
	}
}