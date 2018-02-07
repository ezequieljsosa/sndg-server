$.CartoonStyle = function(colorer, curveWidth, thickness) {
	this.colorer = colorer;
	this.curveWidth = curveWidth;
	this.thickness = thickness;
}

$.CartoonStyle.prototype = {
	render : function(glmol, atoms) {
		this.colorer.color(glmol, atoms)
		glmol.drawCartoon(glmol.modelGroup, atoms, this.curveWidth,
				this.thickness);
	}
}