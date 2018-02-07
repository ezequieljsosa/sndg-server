$.AtomColorer = function(color) {
	this._color = color
}

$.AtomColorer.prototype = {
	color : function(glmol, atoms) {
		glmol.colorAtoms(atoms, this._color);
	}
}