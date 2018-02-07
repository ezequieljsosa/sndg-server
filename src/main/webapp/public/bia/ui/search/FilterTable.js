$.FilterTable = function(tableElement, filterScore) {
	this.tableElement = tableElement
	this.filterScore = filterScore;
	this.onDelete = function(x) {
	}
	this.onAdd = function(x) {
	}
	this.onChange = function(x,evt) {
	}
	this.duplicate_button = true;
	this.onClick = null;
	this.canDrawDist = true; 
}
$.FilterTable.prototype = {
	data : function() {
		return $.map(this.tableElement.find("tbody").children(), function(x) {
			return $(x).data()
		});
	},
	moveAllToScore: function(){
		var me = this;
		$.each(this.tableElement.find("tbody").children(),function(i,tr){
			var data = $(tr).data();
			$(tr).remove();
			me.filterScore.addParam(data);
			
			me.onDelete(data);
		});
	},
	duplicateAllToScore : function(){
		var me = this;
		$.each(this.tableElement.find("tbody").children(),function(i,tr){
			var data = $(tr).data().clone();
			
			me.filterScore.addParam(data);
			
			
		});
	},
	addParam : function(paramData) {
		
		var me = this;
		var tr = $("<tr/>").addClass("block_on_search").appendTo(this.tableElement.find("tbody"));

		$("<td/>").appendTo(tr).append(
				$("<a/>").css("cursor", "pointer").addClass("block_on_search")
						.html("X").attr("disabled", false).click(function(evt) {
							if (!$(evt.target).attr("disabled")) {
								tr.remove();
								this.onDelete(tr.data())
							}
						}.bind(this)));
		
		var canDrawDist = (["ontology","keyword"].indexOf(paramData.complete_name()) == -1) && (paramData.uploader=="demo") && this.canDrawDist
		var td_name = $("<td/>",{width:"100px"})
			
			.appendTo(tr).html( '<p style="width:100px;word-wrap: break-word;">' + 
				paramData.complete_name().replace(new RegExp("_","g")," ") + "</p>");
		if(canDrawDist)
		{
			$("<a/>").appendTo(td_name).html("Show distribution").click( function(evt) {if ((me.onClick != null) ){me.onClick(paramData.complete_name())}} )
		}			
		$("<td/>",{width:"150px"}).appendTo(tr).html( '<p style="width:150px;word-wrap: break-word;">' + ( (paramData.description) ? paramData.description : "")  + "</p>");
		
		var select = $("<select/>").change(function(evt) {
			var data = tr.data();
			data.operation = $(this).val();
			tr.data(data);
			me.onChange(data);
		});

		if (paramData.type == "number") {
			select.append($("<option/>", {
				type : "number",
				value : ">"
			}).html(">")).append($("<option/>", {
				value : "<"
			}).html("<"));
			if (paramData.value == null) {
				paramData.value = 0;
			}
		} else {
			select.append($("<option/>", {
				value : "equal"
			}).html("equal")).append($("<option/>", {
				value : "non equal"
			}).html("non equal"));
		}
		select.val(paramData.operation);
		var td_operation = $("<td/>").appendTo(tr).append(select);

		var td_value = null;
		if (paramData.type == "value") {
			if (paramData.options.length == 0) {
				td_value = $("<td/>",{ width:"100px",style:"width:100px;word-wrap: break-word;"}).appendTo(tr).html(
						"if is <b>" + paramData.operation + "</b> to "
								+ paramData.value);
			} else {
				var select = $("<select/>").change(function(evt) {
					var data = tr.data();
					data.value = $(this).val();
					tr.data(data);
					me.onChange(data);
				});
				$.each(paramData.options, function(i, x) {
					$("<option/>", {
						value : x
					}).appendTo(select).html(x);

				});
				if (paramData.value == null) {
					paramData.value = paramData.options[0];

				}
				select.val(paramData.value);

				td_value = $("<td/>",{style:"width:100px;"}).appendTo(tr).append(select);
			}

		} else {
			td_value = $("<td/>",{style:"width:100px;"}).appendTo(tr).append(
					$("<input/>",{style:"width:50px;"}).val(paramData.value).keyup(function(evt) {
						var data = tr.data();
						data.value = $(this).val();
						tr.data(data);
						me.onChange(data,evt);
					}));
		}
		if(this.duplicate_button){
			$("<td/>").appendTo(tr).append(
					$("<button/>",{style:"width:70px;word-wrap: break-word;"}).addClass("btn-xs").addClass("block_on_search")
							.attr("disabled", false).addClass("btn-info").html("Duplicate To Score")
							.click(function(evt) {
								var data = tr.data().clone(); 
								//tr.remove();
								me.filterScore.addParam(data);
								
								//me.onDelete(data);
							}));
			
		}

		tr.data(paramData)
		this.onAdd(paramData);
	}
}
