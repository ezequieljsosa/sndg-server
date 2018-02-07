$.StickStyle = function(colorer) {
	this.colorer = colorer;
	this.bondRadius = 0.2;
	this.atomRadius = 0.4;
	this.ignoreNonbonded = true;
	this.multipleBonds = false;
	this.scale = 0.3;
}

$.StickStyle.prototype = {
	render : function(glmol, atoms) {
		this.colorer.color(glmol, atoms)
		var residues = glmol.getProteins(atoms);
		glmol.drawBondsAsStick(glmol.modelGroup, residues, this.bondRadius,
				this.atomRadius, this.ignoreNonbonded, this.multipleBonds,
				this.scale);
	}
}
