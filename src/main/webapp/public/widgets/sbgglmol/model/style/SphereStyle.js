$.SphereStyle = function(colorer, sphereRadius) {
	this.colorer = colorer;
	this.sphereRadius = sphereRadius;
}

$.SphereStyle.prototype = {
	render : function(glmol, atoms) {
		this.colorer.color(glmol, atoms)
		glmol.drawAtomsAsSphere(glmol.modelGroup, atoms, this.sphereRadius,this.sphereRadius * glmol.thickness );
	}
}
