$.ByAtomColorer = function(color_map) {
	
	if($.isDefAndNotNull(color_map)){
		this.atom_color_map = color_map;
	} else {
		this.atom_color_map = $.ByAtomColorer.complete_map;
	}
	
}

$.ByAtomColorer.complete_map = {"H": 0xCCCCCC, "C": 0xAAAAAA, "O": 0xCC0000, "N": 0x0000CC, "S": 0xCCCC00, "P": 0x6622CC,
        "F": 0x00CC00, "CL": 0x00CC00, "BR": 0x882200, "I": 0x6600AA,
        "FE": 0xCC6600, "CA": 0x8888AA}

$.ByAtomColorer.aa_atoms_map = {"H": 0xCCCCCC, "C": 0xAAAAAA, "O": 0xCC0000, "N": 0x0000CC, "S": 0xCCCC00         }


$.ByAtomColorer.prototype = {
	color : function(glmol, atoms) {
		glmol.colorByAtom(atoms, this.atom_color_map);
	}
}