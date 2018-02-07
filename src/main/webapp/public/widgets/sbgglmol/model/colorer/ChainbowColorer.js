$.ChainbowColorer = function(color) {

}

$.ChainbowColorer.prototype = {
	color : function(glmol, atoms) {
		glmol.colorChainbow(atoms);
	}
}