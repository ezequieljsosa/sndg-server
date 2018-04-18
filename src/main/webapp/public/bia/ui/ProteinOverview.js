$.ProteinOverview = function(divElement, protein,api) {
	this.divElement = divElement;
	this.protein = protein;
	this.api = api;
	this.propTypes = ["overexpression","pathways","chokepoint","essentiality","metadata","dbxref","demo"]
	this.ontologies = [];
	this.organism = protein.organism;
}

$.ProteinOverview.prototype = {
		
	init : function() {
		this.divElement.find("#prot_name").html(this.protein.name);
		if (this.protein.taxon != null) {
			this.divElement.find("#taxon").html(this.protein.taxon);
		} else {
			this.divElement.find("#taxon-row").remove()
		}

		this._init_genome();
		this._init_gene();
		
		if( ( this.protein.description != "<unknown description>") &&  (this.protein.description != null ) &&  (this.protein.description.replace(" ","").length > 0)){
			$("#prot_desc").html("<b>" + this.protein.description + "</b>");	
		} else {
			$("#prot_desc_tr").remove();	
		}
		
		
		this._print_no_grouping();
		//this._print_grouping()
		
		this.divElement.find("#status").html(this.protein.status);
		
	},
	ontology: function(ont){
		 return $.grep(this.ontologies, function(e) {
				return e.term.toUpperCase() == ont.toUpperCase() ;
			})[0]; 
	},
	_print_no_grouping : function(){
		var nn = x => (x!= undefined && x!=null) ? x.replace('u"[',"").replace(']"',"") : "" ;
		
		var props = this.protein.properties.sort( (x,y) => x._type + x.property >  y._type + y.property  ).
			filter(x =>  this.propTypes.indexOf(x._type ) != -1 );
		
		$.each(props,function(i,prop){
			
			var tr = $("<tr/>").appendTo(this.divElement);
			
			$("<td/>").appendTo(tr).html(prop._type + " - " +  "<i>" + nn(prop.property) + "</i>" );
			var td = $("<td/>").appendTo(tr);
			
			if(prop._type == "ref_genome"){
				var genome_locus = prop.value.split("|");
				$("<a/>",{href: "../genome/" + genome_locus[0] + "/gene/" + genome_locus[1] }).html( prop.value ).appendTo(td);	
			} else {
				
			
			if(prop.value == undefined) {
				$.each(prop,function(k,v){
					
					if((k != "_type") & (k != "property")){
						if( (k == "metabolites") && (prop.property == "chokepoint")){
							var  mets = eval(v);
							var txt = ""; 
							
							mets.forEach(x => {
								var name = x;
								var ont = this.ontology(x)
								if (ont != undefined){
									name = ont.name;
								}
								txt +=  name + ' <a href="'
									+ hrefOntologyLink("biocyc_comp",  x )
									+ '"><small title="go to Biocyc" class="badge bg-yellow">?</small></a> '} );
							$("<span/>").html(k + ": " + txt + " <br /> ").appendTo(td)
						} else {
							$("<span/>").html(k + ": " + nn(v) + " <br /> ").appendTo(td)	
						}
							
					}
					
					
				}.bind(this));
				$("<td/>").appendTo(tr)
			} else if( (prop.value.toLowerCase().indexOf("true") != -1 ||
					prop.value.toLowerCase().indexOf("false") != -1)	){
					var value = "Yes";
					if (prop.value.toLowerCase().indexOf("false") != -1	){
						value = "No";
					}
					td.html( value  );
					
					td = $("<td/>").appendTo(tr);
					
					if (prop.url != undefined && prop.url != null && prop.url != ""){
						$("<span/>",{title:prop.description}).html(' <a class="ext" href="' + nn(prop.url) + '">' + nn(prop.source)  + '</a> <br /> ').appendTo(td)	
					} 
					
				} else {
					
					if ( (prop._type == "dbxref") && (prop.property == "links")) {
						var  links = eval(prop.value);
						var txt = ""; 
						links.forEach(x => {
							
							txt +=  x.split(":")[1] + ' <a href="//'
							+ hrefOntologyLink(x.split(":")[0].toLowerCase(), x.split(":")[1])
							+ '"><small  class="badge bg-yellow">?</small></a> ';
							
							//txt +=' <a href="//' + hrefOntologyLink(x.split(":")[0].toLowerCase(), x.split(":")[1]) + '">' +  x.split(":")[1] + '</a> '; 
						});
						
						$("<span/>").html( txt ).appendTo(td)
					} else if ( (prop._type == "dbxref") && (prop.property == "locus_tag")) {
						
						$("<a/>",{href:this.api.url_genome( prop.source)}).html( prop.source ).appendTo(td)
						$("<span/>").html( " -> " ).appendTo(td)
						$("<a/>",{href:this.api.url_gene(prop.source, prop.value)}).html( prop.value ).appendTo(td)
					} else {
						
					
					var valueToShow = prop.value;
					try{
						
						valueToShow=parseFloat(valueToShow);
						if (isNaN( valueToShow)){
							valueToShow = nn(prop.value)
						} else {
							valueToShow = valueToShow.toFixed(4)  ;	
						}
						
							}
					catch(ex){						
					}
					$("<span/>",{title:prop.description}).html( "<b>" + valueToShow + "</b>" ).appendTo(td)
					td = $("<td/>").appendTo(tr);
					if (prop.url != undefined && prop.url != null && prop.url.length > 0){
						$("<span/>",{title:prop.description}).html(' <a class="ext" href="' + nn(prop.url) + '">' + nn(prop.source)  + '</a> <br /> ').appendTo(td)	
					}
					}
					
				}}
		}.bind(this));
	},
	
	_print_grouping : function(){
		var types = {};
		
		$.each(this.protein.properties,function(i,prop){
			if (! $.isDefAndNotNull(types[prop._type] )){
				types[prop._type] = []
			}
			if((prop._type != "pathways" )&&  (prop._type != "chokepoint")) {
				types[prop._type].push(prop);	
			}
			
			
			
			
		});
		
		$.each(types,function(type,props){
			var tr = $("<tr/>").appendTo(this.divElement);
			$("<td/>").appendTo(tr).html(type)
			var td = $("<td/>").appendTo(tr)
			var nn = x => (x!= undefined && x!=null) ? x : "" 
			var sources = [];
			$.each(props,function(idx,prop){
				if ( $.isDefAndNotNull(prop.value )){
					
					
					
					if(prop.value.toLowerCase().indexOf("true") != -1 ||
						prop.value.toLowerCase().indexOf("false") != -1	){
						var color = "green";
						if (prop.value.toLowerCase().indexOf("false") != -1	){
							color = "red";
						}
						$("<span/>",{title:prop.description}).html( " " + prop.property  + '  <a title="Go to Source" class="ext" href="' + nn(prop.url) + '"></a> &#160;&#160; '  ).appendTo(td).css("color",color)
					} else {
						
						$("<span/>",{title:prop.description}).html( "<i>" + prop.property + "</i>: <b>" + prop.value + "</b><b> |</b> " 
								 + ' <a class="ext" href="' + nn(prop.url) + '">' + nn(prop.source)  + '</a> <br /> ').appendTo(td)	
					}
					
				} else {
					
					$.each(prop,function(k,v){
						if((k != "_type")){
							$("<span/>").html(k + ":-" + v + " <br /> ").appendTo(td)	
						}
						
						
					}.bind(this));
					$("<br/>").appendTo(td)
					
				}
			}.bind(this));
			
		}.bind(this));
	},
	
	_init_genome : function() {
		$("#strSize").html(this.protein.strSize)
		this.divElement.find("#organism").html(
				'<a href="' + this.api.url_genome( this.protein.organism )
						+ '">' + this.organism + '</a>');

	},
	
	
	_init_gene : function() {

		if (this.protein.gene.length > 0) {
			
			var gene_str = '';
			var genes = $.parseJSON(this.protein.gene);
			$.each(genes, function(i, gene) {
				gene_str += '<a href="' + this.api.url_gene(this.protein.organism, genes[0]) 
				+ '"> '		+ gene + '</a>';
			}.bind(this));
			
			this.divElement.find("#gene_link").html(gene_str);
		} else {
			this.divElement.find("#gene_row").remove();
		}
	}

}