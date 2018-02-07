$.StructureList = function(divElement, api) {
	this.divElement = divElement;
	
	this.column_renderers = []
	this.api = api;
	this.on_end = function(x){}
}

$.StructureList.prototype = {
	init : function(protein,structures) {
		this.protein = protein;
		this.struct_features = $.grep(protein.features, function(f) {
			return f.type == "SO:0001079";
		});;
		this.load_structs(structures);
		
	},
	web_init : function(protein) {
		this.protein = protein;
		this.struct_features = $.grep(protein.features, function(f) {
			return f.type == "SO:0001079";
		});;
		api.protein_structures(this.protein_id,this.load_structs.bind(this))
		
	},
	load_structs : function(structs) {
		
		this.divElement.find("#structures_list_overlay").remove();
		this.divElement.find("#structures_list_loading-img").remove();
		var table = this.divElement.find('#structures_list');
		 
		//$.each(this.struct_features,function(i,feature){
		//var added_structs = []
		$.each(structs,function(i,struct){
			
			//var struct = $.grep(structs,function(str){return feature.identifier.toUpperCase().indexOf(str.id.toUpperCase()) != -1 })[0];
			var features = $.grep(this.struct_features,function(feature){return feature.identifier.substring(0,4).toUpperCase().indexOf(struct.name.toUpperCase()) != -1 });
			var fnChain = (x) => x.identifier.split("_")[1]
			features = features.sort(function(a,b) {return (fnChain(a) > fnChain(b)) ? 1 : ((fnChain(b) > fnChain(a)) ? -1 : 0);} ); 
			var subseq= "no data";
			var struct_name = struct.name;
			
			//if ( added_structs.indexOf(struct_name) == -1 ){
				//added_structs.push(struct_name)
				var domain = "-";
				if((features.length > 0) && (features[0].identifier.split("_").length>2)){					
					var domain2 = features[0].identifier.split("_")[features[0].identifier.split("_").length - 3]
					if (domain2.startsWith("PF")) {
						domain = domain2
					}
				} 
				if(this.protein.features.length > 0){
					try {
					domain = "";
					var aln = (struct.templates) ?  struct.templates[0].aln_query : features.filter(x => x.identifier.startsWith(struct.name))[0].location
					this.protein.features.filter(x => x.type == "SO:0000417").forEach(f => {
						if(f.location.start >= aln.start &&   f.location.end <= aln.end){
							domain += f.identifier + " "
						}
					} );
					} catch(ex){
						console.log("Error binding domain to structure")
					}
				}
				if(features.length > 0){
					feature = features[0];
					
					subseq =  feature.aln.aln_hit.name.split("_")[1] + " " + feature.aln.aln_hit.start + ":" + feature.aln.aln_hit.end;
					if (features.length > 1){						
						subseq = "(" +   $.map(features,function(x){return  x.aln.aln_hit.name.split("_")[1] + " " + x.aln.aln_hit.start + ":" + x.aln.aln_hit.end;}).join(") - (");
						subseq = subseq.substring(0, subseq.length )
						subseq = subseq + ")"
					}
				} else {
					feature = struct.templates[0];
					subseq =   feature.aln_query.start + ":"  + feature.aln_query.end;
				}
				
				
				var struct_id = '<a href="'  + this.api.url_structure(struct.name,this.protein.id)  +  '">' + struct_name + "</a>";
				var template= ( $.isDefAndNotNull(struct.templates)  ) ? struct.templates[0].aln_hit.name : "-"  ;
				 
				
				
				var pocket_drugggability = $.map(struct.pockets,function(pocket){
					return parseFloat( pocket.druggability_score);
				}) ;
				var druggability = 0;
				if (pocket_drugggability.length>0){
					druggability = Math.max.apply(Math, pocket_drugggability);
				}			
				
				var row = $("<tr/>").appendTo(this.divElement.find('#structures_list_body'));
				var cell = $("<td/>").html( (template == "-") ? "experiment" : "model"  ).appendTo(row);
				cell = $("<td/>").html(struct_id).appendTo(row);
				cell = $("<td/>").html(template).appendTo(row);
				cell = $("<td/>").html(subseq).appendTo(row);
				cell = $("<td/>").html(domain).appendTo(row);
				cell = $("<td/>").html(druggability).appendTo(row);
				/*
				$.each(this.column_renderers , function(i,column_render){
					column_render(row,feature,struct);
				});*/
			//}
		}.bind(this));
		
		var in_detail = false;
		if (structs.length > 5) {
			in_detail = true;
		}
		
		this.divElement.find('#structures_list').dataTable({
			"order" : [ 5, 'desc' ],
			"paging" : in_detail,
			"info" : in_detail,
			"searching" : in_detail

		});
		this.on_end(structs);
	}
}