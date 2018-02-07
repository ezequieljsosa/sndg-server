$.Layer = function(name, atoms, style, hetatm) {
	this.name = name;
	this.atoms = atoms;
	this.style = style;
	this.visible = true;
	this.hetatm = hetatm;
	this.show_labels = false;
}

$.Layer.prototype = {
	refresh : function() {
		this.glmol.refreshAll()
	},
	render : function() {
		if (this.visible) {
			if(this.show_labels){
				atoms_glmol = this.glmol.atoms;
				this.glmol.draw_labels( this.atoms.filter(x => atoms_glmol[x].atom == "CA"  ) )	;
			}
			
			this.style.render(this.glmol, this.atoms);
		}
	},
	center_view: function(){
		this.glmol.zoomInto(this.atoms)
		this.glmol.refreshAll();	
	}
	
}