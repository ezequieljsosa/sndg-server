$.SequencePanel = function(divElement, sequence, seq_name, seq_id,api) {
	this.divElement = divElement;
	this.sequence = sequence;
	this.seq_id = seq_id;
	this.seq_name = seq_name;
	this.mySequence = null;
	this.start_seq_input = null;
	this.end_seq_input = null;
	this.selected_seq = "";
	this.colNum = 40;
	this.api = api;
}

$.SequencePanel.prototype = {
	init : function() {
		this.mySequence = new Biojs.Sequence({
			sequence : this.sequence,
			target : "sequence_box",
			format : 'FASTA',
			id : this.seq_name
		});

		$("#bps_per_line").val(this.colNum)
		//this.change_size(null);
		this.mySequence.onSelectionChanged(this.selection_changed.bind(this));
		$("#blast_button").click(this.blast_sequence.bind(this));
		$("#bps_per_line_btn").click(this.change_size.bind(this));
	},
	change_size : function(evt) {
		this.mySequence.setNumCols(parseInt($("#bps_per_line").val()));
	},
	selection_changed : function(objEvent) {
		this.start_seq_input = objEvent.start - 1;
		this.end_seq_input = objEvent.end;
		this.selected_seq = this.sequence.substring(objEvent.start - 1,
				objEvent.end);

		$("#copy_area").val(this.selected_seq);
		$("#copy_area")
				.attr("rows", ((objEvent.end - objEvent.start) / 50) + 1);

		$("#start_end_seq_input").val(
				this.start_seq_input + ":" + this.end_seq_input);
		// $("#end_seq_input").val(this.end_seq_input);
	},
	blast_sequence : function(evt) {
		
		window.location = this.api.url_blast(this.seq_name,this.start_seq_input,this.end_seq_input); 
		//'Blast.jsp?seqId=' + this.seq_id + '&selected_start='
		//		+ this.start_seq_input + '&selected_end=' + this.end_seq_input;
	},
	visible_changed : function(status, feature_tracks) {
		// var fid =
		// ([feature_track.name].concat(feature_track.intervals[0])).join("_")

		if (status) {
			var regions = []
			$.each(feature_tracks, function(i, feature_track) {

				regions.push({
					start : feature_track.intervals[0][0],
					end : feature_track.intervals[0][1]
				});

			});
			this.mySequence.addAnnotation({
				fid : feature_tracks[0].id,
				name : feature_tracks[0].name,
				// html : feature_track.name,
				color : feature_tracks[0].style.back_color,
				regions : regions
			})
		} else {
			$.each(feature_tracks, function(i, feature_track) {
				this.mySequence.removeAnnotation(feature_track.id);
			}.bind(this));
		}

	}
}