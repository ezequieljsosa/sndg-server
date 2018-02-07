$._delete = function(url, data, callback, type, error_callback) {

	if ($.isFunction(data)) {
		type = type || callback, callback = data, data = {}
	}
	return $.ajax({
		url : url + "?" + $.param(data),
		type : 'DELETE',
		success : callback,
		error : error_callback,
		contentType : type
	});
}

$.API = function(post_params, url, krona_url) {
	this.url = $.isDefAndNotNull(url) ? url : "/";
	this.krona_url = $.isDefAndNotNull(krona_url) ? krona_url
			: "public/jbrowse/?data=data/";
	this.post_params = post_params;

	/*
	 * .fail(function(jqxhr, textStatus, error) { var err = textStatus + ", " +
	 * error; console.log("Request Failed: " + err); });
	 */
	this.default_error = function(e) {
		console.log(e)
	}
}

$.API.prototype = {
	redirect : function(type, collection, key, value) {

		return ""
	},
	url_login : function() {
		return this.url + '/login';
	},
	url_search : function() {
		return this.url + '/search/product/';
	},
	url_search_keyword : function(keywords) {
		return this.url + '/search/' + keywords;
	},
	url_search_genome_keyword : function(genome_name, keywords) {
		return this.url + '/search/' + encodeURI(genome_name) + "/product/"
				+ keywords;
	},
	url_search_genome_gene : function(genome_name, gene) {
		return this.url + '/search/' + encodeURI(genome_name) + "/gene/" + gene;
	},

	url_genomes : function() {
		return this.url + "/genome";
	},
	url_genome : function(genome_name) {
		return this.url + "/genome/" + encodeURI(genome_name);
	},
	url_search_genome_expression : function(genome_name) {
		return this.url + '/genome/' + encodeURI(genome_name) + '/expression/';
	},
	url_genome_pathways : function(genome_name) {
		return this.url + "/genome/" + encodeURI(genome_name) + '/pathway/';
	},
	url_genome_pathway : function(genome_name, pathway) {
		return this.url_genome_pathways(genome_name) + pathway;
	},
	url_score_pathways : function(genome_name){
		return this.url + "/search/" + genome_name + "/pathways/"; 
	},

	url_protein : function(protein_id) {
		return this.url + "/protein/" + protein_id;
	},
	url_protein_gene : function(protein_name) {
		return this.url + "/protein/gene/" + protein_name + "/";
	},
	url_gene : function(genome_name, gene) {
		return this.url + "/genome/" + encodeURI(genome_name) + "/gene/" + gene;
	},
	url_genome_druggability : function(genome_name, gene) {
		return this.url + "/druggability/" + encodeURI(genome_name);
	},

	url_structure : function(structure_name,proteinId) {
		if(proteinId == undefined){
			return this.url + "/structure/" + structure_name;	
		} else {
			return this.url + "/structure/" + structure_name + "?protein=" + proteinId;
		}
		
	},
	url_tree_genome_go : function(genome_name) {
		return this.url + "/tree/" + genome_name + "/go/";
	},
	url_search_genome_go : function(genome_name, search) {
		// + "?includeEmpty=" + includeEmpty
		var url = this.url + '/tree/' + encodeURI(genome_name) + "/go/"
		if ($.isDefAndNotNull(search) && ($.trim(search).length > 0)) {
			url = url + search + "/";
		}

		return url;
	},
	url_blast : function(seqName, start, end) {
		if (end == null) {
			return this.url + "/tool/blastp?seq_name=" + seqName;
		} else {
			return this.url + "/tool/blastp?seq_name=" + seqName + "&start="
					+ start + "&end=" + end;
		}

	},
	url_job : function(jobType, jobId, query) {
		if ($.isDefAndNotNull(query)) {
			return this.url + "/tool/" + jobType + "/" + jobId + "?seq_name="
					+ query;
		} else {
			return this.url + "/tool/" + jobType + "/" + jobId;
		}

	},

	url_genome_annotation_download : function(genome_name) {
		return this.url + "/genome/" + genome_name + "/download/gff";
	},

	url_genome_contings_download : function(genome_name) {
		return this.url + "/genome/" + genome_name + "/download/fasta";
	},
	
	url_genome_part: function(contig,genome, start, end){
		return this.url + "/genome/" + genome + "/contig/" + contig + "?start=" + start.toString() + "&end=" + end.toString() ;
	},
	
	url_barcode: function(barcode){
		return this.url + "/barcode/" + barcode;
	},

	/**
	 * 
	 * @param collection_name
	 * @param nav
	 * @param localization
	 *            Localization ref,start,stop
	 * @param highlight
	 *            Localization ref,start,stop
	 * @returns {String}
	 */
	jbrowse : function(collection_name, nav, highlight, localization) {

		return this.krona_url + '/' + genome.name + "&tracks=DNA%2CGenes&nav="
				+ nav.toString() + "&tracklist=false&highlight="
	},
	session_proteins : function(done_handler, error_handler) {
		var error = $.isDefAndNotNull(error_handler) ? error_handler
				: this.default_error;
		$.getJSON(this.url + "/session/proteins").done(done_handler).error(
				error);

	},
	user : function(user, done_handler, error_handler) {
		var error = $.isDefAndNotNull(error_handler) ? error_handler
				: this.default_error;
		$.getJSON(this.url + "/user/" + user).done(done_handler).error(error);

	},
	genome : function(genome, done_handler, error_handler) {
		var error = $.isDefAndNotNull(error_handler) ? error_handler
				: this.default_error;
		$.getJSON(this.url_genome(genome)).done(done_handler).error(error);
	},

	genome_pathways : function(genome, pathways, done_handler, error_handler) {
		var error = $.isDefAndNotNull(error_handler) ? error_handler
				: this.default_error;
		var pathways_txt = pathways;
		return $.getJSON(
				this.url
						+ '/genome/{0}/pathway/{1}'.format(genome, pathways_txt
								.join(","))).done(done_handler).error(error);

	},
	organisms : function(done_handler, error_handler) {
		var error = $.isDefAndNotNull(error_handler) ? error_handler
				: this.default_error;
		$.getJSON(this.url + '/user/organism/').done(done_handler).error(error);
	},

	ontology_terms : function(organism, ontologies, done_handler, error_handler) {
		var error = $.isDefAndNotNull(error_handler) ? error_handler
				: this.default_error;

		var post_data = $.extend({
			search : ontologies
		}, options);
		if ($.isDefAndNotNull(organism)) {
			post_data['organism'] = organism;
		}

		return $.post(this.url + "/ontologies/terms", this.post_data,
				"application/json").done(done_handler)
	},
	url_pw_ontology_search : function() {
		return this.url + "/ontologies/search"
	},

	protein_structures : function(protein_id, done_handler, error_handler) {
		var error = $.isDefAndNotNull(error_handler) ? error_handler
				: this.default_error;

		return $.get(this.url + "/structure/protein/" + protein_id).done(
				done_handler).error(error);
	},
	url_download : function(resource, keywords, genome_name) {
		if ($.isDefAndNotNull(genome_name)) {
			return this.url_search_genome_keyword(genome_name, keywords)
					+ "/download"
		} else {
			return this.url_search_keyword(keywords) + "/download"
		}
	},
	url_download_structure : function(structure_name) {
		return this.url + "/structure/" + structure_name + "/download"

	},
	url_search_ontologies : function() {
		return this.url + "/ontologies/search"
	},
	blastJob : function(blastJobParams, done_handler, error_handler,always_handler) {		
		var error = $.isDefAndNotNull(error_handler) ? error_handler
				: this.default_error;

		return $.ajax({
			  type: 'POST',
			  url:  this.url + "/blast/",
			  data: blastJobParams,
			  datatype: 'application/json',
			  success: done_handler,
			  error: error_handler
			});
		
		// $.post(this.url + "/blast/", blastJobParams, "application/json")
		//		.done(done_handler).error(error_handler).fail(error_handler);
	},
	
	blastJobStatus : function(jobId, done_handler, error_handler) {
		var error = $.isDefAndNotNull(error_handler) ? error_handler
				: this.default_error;
		$.getJSON(this.url + "/blast/" + jobId).done(done_handler).fail(error);
	},
	jobList : function(done_handler, error_handler) {
		var error = $.isDefAndNotNull(error_handler) ? error_handler
				: this.default_error;
		$.getJSON(this.url + "/user/jobs/").done(done_handler).fail(error);
	},
	add_session_resource : function(resource, name, id, done_handler,
			error_handler) {
		var error = $.isDefAndNotNull(error_handler) ? error_handler
				: this.default_error;
		var post_data = $.extend({
			resourceName : resource,
			name : name,
			id : id
		}, this.post_params);
		$.post(this.url + "/session/", post_data).done(done_handler)
				.fail(error);
	},
	remove_session_resource : function(resource, name, done_handler,
			error_handler) {
		var error = $.isDefAndNotNull(error_handler) ? error_handler
				: this.default_error;

		var post_data = $.extend({
			resourceName : resource,
			name : name
		}, this.post_params);
		$._delete(this.url + "/session/", post_data, done_handler, "json",
				error);

	}

}
