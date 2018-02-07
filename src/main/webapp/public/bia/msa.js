$.MSA_UI = function(divElementId,aln) {
	
	this.divElement = document.getElementById(divElementId);
	this.aln = aln;
	this.msa_lib = require("msa");
	this.opts = {
			el : this.divElement,			
			vis : {
				conserv : false,
				overviewbox : false,
				seqlogo : false,
				labelId : false,
				labelName : true
	}}
	this.opts.seqs = this.aln ;
	
	
	
	
	this.opts.colorscheme = {
		"scheme" : ""
	};
	this.opts.zoomer = {
		labelNameLength : 200
	};
	

}

$.MSA_UI.prototype = {
	init : function() {
		this.msa = this.msa_lib(this.opts);
		
		this.msa.render();
		this.msa.g.zoomer.set("alignmentHeight", 100)		
	}
}
