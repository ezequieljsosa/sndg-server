$.SearchTable = function(divElement, url, api) {
	this.divElement = divElement;
	this.divElement.data(this);
	this.url = url;
	this.api = api;
	this.filters = [];
	this.startSearch = function(){}
	this.endSearch = function(){}
	var me = this;
	$(this.divElement).on('xhr.dt', function ( e, settings, data ) {		
       me.endSearch(data)
    } )
    $(this.divElement).on('preXhr.dt', function ( e, settings, data ) {
    	me.startSearch()
    } )
	
	var me = this;
	this.properties_column = {
			"name" : "properties",
			"title" : "Properties",
			"data" : null,
			"defaultContent" : "-",
			"orderable" : false,
			
			"render": function(protein, type, row){
				var data = protein.search;
				var propsText =  $.map( $.scoreTable.data(), function(x){
					
					function esta_en_data(){
						
						
						
						if (x.uploader == "demo" || x.uploader == undefined){
						return data[x.name] != undefined	;
						} else {
							if(data[x.uploader] != undefined){
								return data[x.uploader][x.name] != undefined;	
							} else {
								return false;
							}
							
						}
					}
					function get_data(){
						
						var value ="";
						if (x.uploader == "demo" || x.uploader == undefined){
							value = data[x.name] 	;
							} else {
								
								value = data[x.uploader][x.name] ;
								
								
							}
						try{
							var value_tmp = parseFloat(value).toFixed(2)
							if (value_tmp.toString() != "NaN"){
								return value_tmp;
							}
						} catch (ex) {
							return value
						}
						return value;
					}
					
					var no_data = y => (y == null) ? "no data" : y ;
					var description = x.name;
					if (x.description != null){
						description = x.description.replace(" ","&#160;");	
					}
					
					if (esta_en_data()){
						return  "<b>" + x.name + "</b>: " + get_data() 
					} else {
						if (( ["ontology","keyword","pathway"].indexOf(x.name) != -1 )	){	

							
						
							if(x.value.startsWith("ec:")){																
								ecs = $.grep(protein.keywords,   j => j.startsWith(x.value.replace(new RegExp(".-", 'g'),""))   )
								if (ecs.length > 0){
									return  "<b>" + x.value + " - "  + description + "</b>: annotated";
								}
									
							} else {
								if(row.keywords.indexOf(x.value) != -1){
									return  "<b>" + x.value + " - "  + description + "</b>: annotated";	
								}	else {
									return '';		
								}
										
							} 
							
							
						}
					}
					
				} )
				
				return propsText.join(", ");
			}
	}
	this.score_column = {
		"name" : "score",
		"title" : "Score",
		"data" : "score",
		"defaultContent" : "0"
	};
	this._options = {
		"ajax" : {"url":this.url , "type": "POST"   },
		"lengthMenu": [[100, -1], [100, "All"]],
		"deferLoading": 0,   //Para que no haga la carga inicial
		"processing" : true,
		"serverSide" : true,
		"ordering": false,
		"language" : {
			"search" : "Filter: "
		},
		"initComplete" : this.tableInitComplete.bind(this),
		dom : 'iBrtilp',
		colVis : {
			exclude : [ 0, 1 ],
			restore : "Restore",
			showAll : "Show all",
			showNone : "Show none"
		},
		buttons: [
		          {
		               extend: 'csvHtml5',
		               text: 'Export first 100 to CSV'}
		      ],
		searchCols : [ null, null, null, null, null, null ],
		"columns" : [
				{
					"name" : "msa",
					"visible": false,
					"title" : '<i title="select for multiple aligment" class="fa fa-align-center">&#160;</i>',
					"orderable" : false,
					"data" : null,
					"render" : function(data, type, row) {
						var checked = "";
						if ((typeof window.proteins != "undefined")
								&& (typeof window.proteins.values[data.name] != "undefined")
								&& window.proteins.values[data.name] == data.id) {
							checked = "checked=checked";
						}

						return '' + '<input ' + checked
								+ ' onclick="addProt(\'' + data.name + "','"
								+ data.id + '\',$(this).is(\':checked\'));" '
								+ 'type="checkbox" />';
					}
				},
				{
					"name" : "product",
					"title" : "Protein Product",
					"orderable" : false,
					"data" : null,
					"render" : function(data, type, row) {
						return '<a href="' + me.api.url_protein(data.id) + '">'
								+ data.name + '</a>';
					},
					"defaultContent" : ''
				},
				 {
					"name" : "size",
					"title" : "Size",
					"data" : "size",
					"render" : function(data, type, row) {						
						return  data.len.toString() + " " + data.unit ;
					}
				} ],
		"order" : [ [ 3, 'asc' ] ]
	// Ojo 1 es Organims! si se cambia el orden de la columna, hay que cambiar
	// esto
	};
}

$.SearchTable.prototype = {
	init : function(options, on_table_initialized) {
		
		var datatableOptions = $.extend(this._options, options);
		
		this._configure_columns();
		this.on_table_initialized = on_table_initialized;
		this.dataTable = this.divElement.dataTable(datatableOptions);
		this.dtapi = this.dataTable.api();
	},

	addFilter : function(filter){
		this.filters.push(filter);
		filter.searchTable = this;
	},
	searchBy : function(column_name,search,regexp,otro){
		if(otro == undefined){
			otro = true;
		}
		table = this.divElement.DataTable();
		table.column(column_name).search(search,regexp,otro).draw();
	},
		
	search_gene_prods : function() {		
		var data = JSON.stringify( {filters:$.filterTable.data(), scores:$.scoreTable.data()} );
		var table = this.divElement.DataTable();
		table.search(data).draw();		
	},

	remove_dot_minus : function(text) {
		var result_text = text;
		var result_previous = "";
		if (result_text.indexOf(".-") != -1) {
			while (result_text != result_previous) {
				result_previous = result_text;
				result_text = result_text.replace(new RegExp(".-$", 'g'), "");
			}
		}
		return result_text;

	},

	tableFilter : function(text, add_state) {

		if (typeof add_state == "undefined") {
			add_state = true;
		}

		var text_to_add = this.remove_dot_minus(text);
		

	},
	
	

	_add_download_button : function() {
		$(".ColVis")
				.append(
						'<button id="download_list" class="ColVis_Button ColVis_MasterButton"><span>Download</span></button>')

		$('#download_list').click(
				function(evt) {

					var keywords = $('#search_txt').val()
					var selected_ont = $('#search_select2').val()
					var organism = null;

					if ((selected_ont != null) && (selected_ont.length > 0)) {

						keywords = keywords
								+ $.map($('#search_select2').val(),
										function(x) {
											return $.trim(x.split("-")[0])
										}).join(" ")
					}
					if ($.isDefAndNotNull($('#organism_select').val())
							&& ($('#organism_select').val() != "")) {
						organism = $('#organism_select').val()
					}
					var download_url = $.api.url_download('product', keywords,
							organism);
					window.location = download_url;
				});
	},

	

	_init_column_filter : function() {
		$('#search-table').on(
				'column-visibility.dt',
				function(e, settings, column, state) {

					console.log('Column ' + column + ' has changed to '
							+ (state ? 'visible' : 'hidden'));

					if (column == $('#search-table').DataTable().column(
							'gene:name')[0]) {

						this._init_gene_filter();

					}

				}.bind(this));
	},

	_configure_filters : function() {
		$.each(this.filters, function(i, filter) {
			if (filter.init != undefined) {
				filter.init()
			}
		});
	},
	_configure_columns : function() {
		$.each(this.filters, function(i, filter) {
			if (filter.column != undefined) {
				this._options.columns.push(filter.column());
			}
		}.bind(this));
		//this._options.columns.push(this.properties_column);
		//this._options.columns.push(this.score_column);
	},
	_init_control_values : function() {
		$.each(this.filters, function(i, filter) {
			if (filter.init_control_value != undefined) {
				this._options.columns.push(filter.colum);
			}
		});
		

	},
	
	tableInitComplete : function(settings, json) {

		this._add_download_button();
		
		this._configure_filters();
		this._init_control_values();

		$(".loading-img").hide();
		$(".overlay").hide();
		this.on_table_initialized();
	},

}