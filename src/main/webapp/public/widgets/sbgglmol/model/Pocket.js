$.Pocket = function(name, atoms, alpha_spheres, style) {
	this.name = name;
	this.atoms = atoms;
	this.alpha_spheres = alpha_spheres;	
	this.visible = false;

	this.style = new $.PocketStyle(style);
}

$.Pocket.prototype = {
	refresh : function() {
		this.glmol.refreshAll()
	},
	render : function() {
		if (this.visible) {
			var atoms = [];
			
			if (this.style.show_alpha_spheres) {
				atoms = this.alpha_spheres;
			} else {
				atoms = this.atoms;
			}
			this.style.render(this.glmol, atoms);
		}
	},
	center_view: function(){
		this.glmol.zoomInto(this.atoms)
		this.glmol.refreshAll();	
	}
}

