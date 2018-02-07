$.GenomeOverview = function(divElement, genome) {
	this.divElement = divElement;
	this.genome = genome;
}

$.GenomeOverview.prototype = {
	init : function() {
		
		var genome = this.genome;
		
			var conservar_overview = false; 
			$("#data_table").append(
						'<li><a href="' + $.api.url_genome_contings_download(genome.name)	
							+ '"> Download Genome Sequence </a></li>');
			
			$("#data_table").append(
					'<li><a href="' + $.api.url_genome_annotation_download(genome.name)	
						+ '"> Download GFF annotation </a></li>');
			
			

			if (genome.status) {
				conservar_overview = true;
				this.divElement.find("#overview_table").append(
						"<tr><td>Status</td><td>" + genome.status
								+ "</td></tr>");
			}

			if (genome.assembly != null) {
				conservar_overview = true;
				var keys = Object.keys(genome.assembly).sort().reverse()
				$.each(keys, function(i,key) {
					this.divElement.find("#overview_table").append(
							"<tr><td>" + key.capitalize() + "</td><td>" + genome.assembly[key]
									+ "</td></tr>");
				}.bind(this));
			}
			
			if (!conservar_overview){
				$("#overview_section").remove()
			}

//		} else {
//			this.divElement.remove();
//			$("#statistics_section").removeClass("col-lg-4").addClass(
//					"col-lg-6");
//			$("#search_section").removeClass("col-lg-4").addClass(
//					"col-lg-6");
//		}

		if (genome.statistics != null) {
			var dataSet = []
			$.each(genome.statistics, function(i, metric) {
				if ( $.isDefAndNotNull(metric.value)  && (metric.value > 0) ){
					if (metric.name != "Models"){
						dataSet.push([format_table_key(metric.name,
								genome.name), metric.value ]);	
					}
						
				}
				
				/*
				if (values instanceof Object) {
					$.each(values, function(key, value) {
						if (EXCLUDED_SO.indexOf(key) == -1) {
							if (value > 0) {
								dataSet.push([
										format_table_key(key,
												genome.organism), value ]);
								
							}
						}
					});
				} else {
					dataSet.push([
							format_table_key(category, genome.organism),
							values ]);
				}
				*/

			});
			$("#statistics_table").dataTable({
				 "order": [[ 1, "desc" ]],
				 "language" : {					
						"url": "/sndg/public/widgets/datatables/Spanish.json"
					},
				 paging: false,
				 "dom": "tip",
				"data" : dataSet,
				columns : [ {
					title : "a"
				}, {
					title : "b"
				} ],
				
			});
		}
	}
}