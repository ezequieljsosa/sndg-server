
$.ByResidueColorer = function(color_map) {
	this.atom_color_map = color_map
}

$.ByResidueColorer.prototype = {
	color : function(glmol, atoms) {
		glmol.colorByResidue(atoms, this.atom_color_map);
	}
}