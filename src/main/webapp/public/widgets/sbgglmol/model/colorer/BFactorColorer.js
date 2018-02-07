$.BFactorColorer = function(color) {

}

$.BFactorColorer.prototype = {
	color : function(glmol, atoms) {
		glmol.colorByBFactor(atoms);
	}
}