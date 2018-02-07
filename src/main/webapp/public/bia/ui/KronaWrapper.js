$.KronaWrapper = function(div, base_url, ecIndex, goIndex, collection_name) {
	this.div = div;
	this.base_url = base_url
			;
	this.collection_name = collection_name;
	this.ecIndex = ecIndex;
	this.goIndex = goIndex;

	this.default_error = function(e) {
		console.log(e)
	}
}

$.KronaWrapper.prototype = {
	init : function(ontology, term) {
		this._remove_invalid_options(this.ecIndex, this.goIndex);
		this._init_select();
		this.update(ontology, term);
	},
	update : function(ontology, term) {
//		var krona_url = this.base_url + this.collection_name + "&term=" + term
//				+ "&ontology=" + ontology;
//		
//		this.div.find('#krona').attr("src", krona_url);
		$("krona").remove();
		$("krona").remove();
		$.ajax({
			url : this.base_url + "/" + this.collection_name 
					+ "?term=" + term + "&ontology="	+ ontology, dataType : 'text',
			success : function(xml) {
				
				$("#krona2").html(xml)
				resetValues()
				$("#krona1").empty()
				$.krona = document.getElementById("krona1")
				load()			
				
				//$(window.parent.document.getElementById("krona-box")).removeClass("loading-img2");
				$(window.document.getElementById("krona-box")).removeClass("loading-img2");
			},
			error : function(jqxhr, textStatus, error) {
				var err = textStatus + ", " + error;
				//console.log("Request Failed: " + err);
			}
		});
		
	},
	_remove_invalid_options : function(ecIndex, goIndex) {
		if (!goIndex) {
			this.div.find('#bp_option').remove();
			this.div.find('#cc_option').remove();
			this.div.find('#mf_option').remove();

			this.div.find('#gosearch_tr').remove();
			if (!ecIndex) {
				this.div.remove();
			}
		}
		if (!ecIndex) {
			this.div.find('#ec_option').remove();
		}
	},
	_init_select : function() {
		var me = this;
		this.div.find("#krona_select").change(
				function(e) {
					$("#krona-box").addClass("loading-img2");
					if ($(this).val() == "ec") {
						me.update("ec", "root");
					}
					if ($(this).val() == "bp") {
//						krona_url = "/xomeq/public/krona/index.html?genome="
//								+ $.QueryString["genome"]
//								+ "&term=go:0008150&ontology=go&level=3";
						me.update("go", "go:0008150");
					}
					if ($(this).val() == "mf") {
						me.update("go", "go:0003674");
					}
					if ($(this).val() == "cc") {
						me.update("go", "go:0005575");
					}
				});
	}
}