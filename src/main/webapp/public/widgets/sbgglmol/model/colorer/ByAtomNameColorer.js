$.ByAtomNameColorer = function(color_map) {
	this.atom_color_map = color_map
}

$.ByAtomNameColorer.prototype = {
	color : function(glmol, atoms) {
		glmol.colorByAtomName(atoms, this.atom_color_map);
	}
}