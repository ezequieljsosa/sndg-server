(function($) {
	$.isBlank = function(obj) {
		return (!obj || $.trim(obj) === "");
	};
})(jQuery);
if (!String.prototype.format) {
	String.prototype.format = function() {
		var args = arguments;
		return this.replace(/{(\d+)}/g, function(match, number) {
			return typeof args[number] != 'undefined' ? args[number] : match;
		});
	};
}
String.prototype.endsWith = function(suffix) {
	return this.indexOf(suffix, this.length - suffix.length) !== -1;
};
String.prototype.startsWith = function(suffix) {
	return this.substring(0, suffix.length).indexOf(suffix) !== -1;
};
String.prototype.isEmpty = function() {
	return $.isBlank(this);
};
if (typeof String.prototype.startsWith != 'function') {
	// see below for better implementation!
	String.prototype.startsWith = function(str) {
		return this.indexOf(str) == 0;
	};
}

String.prototype.capitalize = function() {
	return this.charAt(0).toUpperCase() + this.slice(1);
};


Array.prototype.randomElement = function () {
    return this[Math.floor(Math.random() * this.length)];
}

function formatStringForLine(string, line_size) {
	var new_str = "";
	var currentLineSize = 0;
	$.each(string.split(" "), function(i, word) {
		currentLineSize = currentLineSize + word.length;
		new_str = new_str + word + " ";
		if (currentLineSize > line_size) {
			currentLineSize = 0;
			new_str = new_str + '<br />';
		}
	});
	return new_str;
}

function wrapStringShowMore(string, size) {
	if (string.length > size) {
		return '<span>' + string.substring(0, size) + '<a txt="' + string
				+ '" href="javascript:void(0);" '+ ' onclick="$(this).parent().html( $(this).attr(\'txt\'))" > more... </a> </span>'
	} else {
		return string;
	}
}


(function($) {
	$.QueryString = (function(a) {
		if (a == "")
			return {};
		var b = {};
		for (var i = 0; i < a.length; ++i) {
			var p = a[i].split('=');
			if (p.length != 2)
				continue;
			b[p[0]] = decodeURIComponent(p[1].replace(/\+/g, " "));
		}
		return b;
	})(window.location.search.substr(1).split('&'))
})(jQuery);


ROOT_GO = [ 'GO:0008150', 'GO:0005575', 'GO:0003674' ]

EXCLUDED_SO = [ "SO:0001080", "SO:0001128", "SO:0000408", "SO:0001068",
		"SO:0001114", "SO:0001111", "SO:0001089", "SO:0000001", "SO:0000419" ];
SO_TERMS = {
	"SO:0000236" : "ORF",
	"SO:0000704" : "Gene",
	"SO:0000996" : "predicted_gene",
	"SO:0001079" : "polypeptide_structural_motif",
	"SO:0000857" : "homologous",
	"SO:0000417" : "polypeptide_domain",
	"SO:0000253" : "tRNA",
	"SO:0000419" : "mature_protein_region",
	"SO:0000419" : "chain",
	"SO:0000839" : "polypeptide_region",
	"SO:1000002" : "substitution",
	"SO:0000409" : "binding_site",
	"SO:0001104" : "catalytic_residue",
	"SO:0001128" : "polypeptide_turn_motif",
	"SO:0001077" : "transmembrane_polypeptide_region",
	"SO:0001630" : "splice_region_variant",
	"SO:0001656" : "metal_binding_site",
	"SO:0001811" : "phosphorylation_site",
	"SO:0001971" : "zinc_finger_binding_site",
	"SO:0001429" : "DNA_binding_site",
	"SO:0001114" : "peptide_helix",
	"SO:0001068" : "polypeptide_repeat",
	"SO:0001111" : "beta_strand",
	"SO:0001089" : "post_translationally_modified_region",
	"SO:0001088" : "disulfide_bond",
	"SO:0001062" : "propeptide",
	"SO:0001060" : "sequence_variant",
	"SO:0001067" : "polypeptide_motif",
	"SO:0000001" : "region",
	"SO:0001066" : "compositionally_biased_region_of_peptide",
	"SO:0000418" : "signal_peptide",
	"SO:0100009" : "lipo_signal_peptide",
	"SO:0000419" : "mature_protein_region",
	"SO:0000691" : "cleaved_initiator_methionine"
};

BIAlinks = {
	"go" : "http://amigo.geneontology.org/amigo/term/",
	"so" : "http://www.sequenceontology.org/browser/current_svn/term/",
	"ec" : "http://enzyme.expasy.org/EC/",
	"pfam" : "http://pfam.xfam.org/family/",	
	"pdb_org" : "http://www.rcsb.org/pdb/explore/explore.do?structureId=",
	"unip" : "www.uniprot.org/help/",
	"biocyc" : "http://www.biocyc.org/META/NEW-IMAGE?object=",
	"biocyc_reac": "http://www.biocyc.org/META/NEW-IMAGE?object=",
	"biocyc_pw": "http://www.biocyc.org/META/NEW-IMAGE?object=",
	//"biocyc_comp": "https://biocyc.org/compound?orgid=META&id=",
	"biocyc_comp":"https://biocyc.org/META/substring-search?type=NIL&quickSearch=Quick+Search&object=",
	"pdb_ligand" : "http://www.rcsb.org/pdb/ligand/ligandsummary.do?hetId=",
	"uniprot": "www.uniprot.org/uniprot/",
	"tbdream": (drug,gene) => "https://tbdreamdb.ki.se/Data/MutationDetail.aspx?AreaId=" + drug + "&GeneID=" + gene
};

var go_bp =  {"slims": ["generic"], 
		"successors_relationships": [["go:0044699", "is_a"], ["go:0044848", "is_a"], ["go:0032501", "is_a"], ["go:0008152", "is_a"], ["go:0050896", "is_a"], ["go:0040007", "is_a"], ["go:0071840", "is_a"], ["go:0040011", "is_a"], ["go:0022610", "is_a"], ["go:0002376", "is_a"], ["go:0051234", "is_a"], ["go:0023052", "is_a"], ["go:0000003", "is_a"], 
                                                                 ["go:0001906", "is_a"], ["go:0051179", "is_a"], ["go:0048511", "is_a"], ["go:0046879", "is_a"], ["go:0065007", "is_a"], ["go:0022414", "is_a"], ["go:0051704", "is_a"], 
                                                                 ["go:0009987", "is_a"], ["go:0032502", "is_a"], ["go:0050789", "regulates"], ["go:0048518", "positively_regulates"], ["go:0048519", "negatively_regulates"]], 
          "term": "go:0008150", "ontology": "go", "name": "biological_process", 
     "children": ["go:0032502", "go:0032501", "go:0009987", "go:0022414", "go:0044848", "go:0008152", "go:0051234", "go:0051179", "go:0051704", "go:0040011", 
                                                                              "go:0050896", "go:0002376", "go:0044699", "go:0023052", "go:0050789", "go:0071840", "go:0000003", "go:0040007", "go:0046879", "go:0048511", 
                                                                              "go:0065007", "go:0048518", "go:0048519", "go:0001906", "go:0022610"], 
     "desc": "Any process specifically pertinent to the functioning of integrated living units: cells, tissues, organs, and organisms. A process is a collection of molecular events with a defined beginning and end. [GOC:go_curators, GOC:isa_complete]"
                                                                         }

var go_mf =   {"successors_relationships": [["go:0005085", "is_a"], ["go:0005198", "is_a"], ["go:0045499", "is_a"], ["go:0009055", "is_a"], ["go:0005488", "is_a"], 
                                            ["go:0005215", "is_a"], ["go:0030234", "is_a"], ["go:0000988", "is_a"], ["go:0045735", "is_a"], ["go:0016015", "is_a"], 
                                            ["go:0003824", "is_a"], 
                                            ["go:0016247", "is_a"], ["go:0016530", "is_a"], ["go:0036370", "is_a"], ["go:0030545", "is_a"], ["go:0031386", "is_a"], 
                                            ["go:0060089", "is_a"], ["go:0004872", "is_a"], ["go:0045182", "is_a"], ["go:0042056", "is_a"], ["go:0001071", "is_a"], 
                                            ["go:0016209", "is_a"], ["go:0065009", "regulates"], ["go:0044093", "positively_regulates"], ["go:0044092", "negatively_regulates"]], 
                                            "term": "go:0003674", "name": "molecular_function", "ontology": "go", 
                                            "slims": ["generic"], "children": ["go:0005215", "go:0030234", "go:0003824", "go:0045182", "go:0009055", "go:0036370", 
                                                                               "go:0045499", "go:0045735", "go:0016015", "go:0031386", "go:0016209", "go:0042056", 
                                                                               "go:0044092", "go:0005085", "go:0060089", "go:0004872", "go:0030545", "go:0005198", 
                                                                               "go:0044093", "go:0000988", "go:0016247", "go:0016530", "go:0065009", "go:0005488",
                                                                               "go:0001071"], 
                                                                               "desc": "Elemental activities, such as catalysis or binding, describing the actions of a gene product at the molecular level. A given gene product may exhibit one or more molecular functions."}
var go_cc =  {"successors_relationships": [["go:0044423", "is_a"], ["go:0055044", "is_a"], ["go:0043226", "is_a"], ["go:0031012", "is_a"], ["go:0030054", "is_a"], ["go:0044422", "is_a"], ["go:0044420", "is_a"], 
                                           ["go:0044425", "is_a"], ["go:0044456", "is_a"], ["go:0016020", "is_a"], 
                                           ["go:0045202", "is_a"], ["go:0097423", "is_a"], ["go:0032991", "is_a"], ["go:0005576", "is_a"], ["go:0005623", "is_a"], ["go:0044421", "is_a"], ["go:0031974", "is_a"], 
                                           ["go:0044464", "is_a"], ["go:0039679", "is_a"], ["go:0005581", "is_a"], ["go:0009295", "is_a"], ["go:0019012", "is_a"]], 
                                           "term": "go:0005575", "name": "cellular_component", "ontology": "go", 
                                           "children": ["go:0055044", "go:0032991", "go:0039679", "go:0097423", "go:0044464", "go:0005623", "go:0043226",
                                                        "go:0005576", "go:0044425", "go:0044420", "go:0044421", "go:0044422", "go:0044423", "go:0031974", 
                                                        "go:0031012", "go:0045202", "go:0009295", "go:0030054", "go:0005581", "go:0016020", "go:0044456", 
                                                        "go:0019012"], 
                                                        "desc": "The part of a cell or its extracellular environment in which a gene product is located. A gene product may be located in one or more parts of a cell and its location may be as specific as a particular macromolecular complex, that is, a stable, persistent association of macromolecules that function together."
                                                        }

function hrefOntologyLink(ontology, term) {
	if (ontology == "?")
		return "?";

	var url_term = term.toUpperCase();

	if (ontology == "ec") {
		url_term = url_term.replace("EC:", "");
	}
	return BIAlinks[ontology] + url_term;
}
function hrefOrganismLink(organism, gene) {

	var link = "http://www.uniprot.org/uniprot/?query=";
	if (organism == "Puccinia_Sorghi") {
		link = link + gene.split("_")[0] + "_"
				+ gene.split("_")[1].split("T")[0];
	} else {
		link = link + gene;
	}
	return link;
}

function absolute_position_simple(strLocus, features) {
	if (strLocus.split(":").length == 3) {
		var ref = strLocus.split(":")[0];
		var start = parseInt(strLocus.split(":")[1]);
		var end = parseInt(strLocus.split(":")[2].split("(")[0]);

		var result = $.grep(features, function(e) {
			return e.id == ref;
		})[0];
		var ref_start = parseInt(result.strLocus.split(":")[0]);
		return (start + ref_start).toString() + ":"
				+ (end + ref_start).toString();
	} else {
		return strLocus;
	}
}

function absolute_position(strLocus, features) {
	if (strLocus.indexOf("-") != -1) {
		var abs_pos = "";

		$.each(strLocus.split(" - "), function(i, strLocusSimple) {
			if ((i != 0) && (i % 5) == 0) {
				abs_pos = abs_pos + "<br />";
			}
			abs_pos = abs_pos
					+ absolute_position_simple(strLocusSimple, features)
					+ " - "
		});
		return abs_pos.substring(0, abs_pos.length - 3);
	}
	return absolute_position_simple(strLocus);
}
function feature_offset(strLocus, features) {
	if (strLocus.split(":").length == 3) {
		var ref = strLocus.split(":")[0];

		var result = $.grep(features, function(e) {
			return e.id == ref;
		})[0];
		return parseInt(result.strLocus.split(":")[0]);
	} else {
		return 0;
	}
}


function Colorer() {
	this.colors = {}
	this.getRandomColor = function(identifier) {

		if (!(identifier in this.colors)) {
			var letters = '0123456789ABCDEF'.split('');
			var color = '#';
			for (var i = 0; i < 6; i++) {
				color += letters[Math.floor(Math.random() * 16)];
			}
			this.colors[identifier] = color;
		}
		return this.colors[identifier];
	}
}



$.isUndefined = function(object){
	 
	  return typeof object == "undefined"; 
}
$.isDefAndNotNull = function(object){
	 
	  return (! $.isUndefined(object)) && (object != null); 
}
$.objOrDef = function(object,default_val){
	return ($.isDefAndNotNull(object)) ? object : default_val  
}

