$.ScoreParam = function(name) {
	this.name = name;
	this.description = "";
	this.coefficient = "";
	this.value = null;
	this.operation = "equal";
	this.type = "value";
	this.target = "";
	this.options = [];	
	this.coefficient = 1;
	this.groupoperation = "";	
	this.uploader = "demo";

}
$.ScoreParam.prototype = {
	complete_name : function() {
//		if (this.value != null) {
//			return this.name + " (" + this.operation + " to " + this.value
//					+ ")"
//		} else {
			return this.name
//		}

	},
	clone : function(){
		var cloned = new $.ScoreParam(this.name);
		
		cloned.description = this.description;
		cloned.coefficient = this.coefficient;
		cloned.value = this.value;
		cloned.target = this.target;
		cloned.operation = this.operation;
		cloned.type = this.type;
		cloned.options = this.options;
		cloned.uploader = this.uploader;
		return cloned;
	}
}

$.ScoreTable = function(tableElement) {
	this.tableElement = tableElement;
	this.onDelete = function(x){}
	this.onAdd = function(x){}
	this.onChange = function(x,evt) {
	}
	
}

$.ScoreTable.prototype = {
		data: function(){
			return $.map( this.tableElement.find("tbody").children(), function(x){
				return $(x).data() 
				}  );
		},
	reload_weigths : function() {
		var total = 0;
		$.each(this.tableElement.find("tbody").children(), function(i, child) {
			if ($(child).data().coefficient > 0) {
				total = $(child).data().coefficient + total;
			} 

		});
		var formula = "Score = ";
		$.each(this.tableElement.find("tbody").children(), function(i, child) {
			var data = $(child).data();
			
			if (data.coefficient != 0) {
				var paramName = data.name;
				if ((paramName == "ontology") ||  (paramName == "keyword")){
					paramName = data.value
				}
				if (data.coefficient == 1) {
					formula += paramName + " <b>+</b> ";
				} else {
				
				if (data.coefficient < 0) {
					formula += "(" + data.coefficient + ") * " + paramName
							+ " <b>+</b> ";
				}
				if (data.coefficient > 0) {
					formula += data.coefficient + " * " + paramName
							+ " <b>+</b> ";
				}
				}
				var norm_weigth = data.coefficient * 1.0 / total;
				$($(child).children()[$(child).children().length - 1]).html(
						norm_weigth.toFixed(2))
			} else {
				$($(child).children()[$(child).children().length - 1]).html(
						0)
			}

		});
		$("#score_div").html(formula.substring(0, formula.length - 10));

	},
	addParam : function(paramData) {
		
		var me = this;
		
		if (paramData.type != "value") {
			paramData.value = null;
		}
		
		var tr = $("<tr/>").appendTo(this.tableElement);
		
		
		$("<td/>").appendTo(tr).append(
				$("<a/>").css("cursor", "pointer").html("X").addClass("block_on_search").attr("disabled",false).click(
						function(evt) {
							if(!$(evt.target).attr("disabled")){
								tr.remove();
								me.reload_weigths();
								this.onDelete(tr.data())	
							}
							
							
						}.bind(this)));
		
		var canDrawDist = (["ontology","keyword"].indexOf(paramData.complete_name()) == -1) && (paramData.uploader=="demo")
		
		var td_name = $("<td/>").appendTo(tr).html('<p style="width:110px;word-wrap: break-word;">' + paramData.complete_name().replace(new RegExp("_","g")," ") + "</p>")
		
		
		if(canDrawDist)
		{
			$("<a/>").appendTo(td_name).html("Show distribution").click( function(evt) {if ((me.onClick != null) ){me.onClick(paramData.complete_name())}} )
		}	
		;
		$("<td/>").appendTo(tr).html('<p style="width:200px;word-wrap: break-word;">' + (paramData.description) ? paramData.description : "" + "</p>") ;

		var td_coefficient = $("<td/>",{width:"100px",style:"width:100px;word-wrap: break-word;"}).append($("<input/>", {
			type:"text",
			step:"1",
			width : "50px",
			
		}).val(paramData.coefficient).keypress(function(evt) {
			
			if( evt.key.replace(/[0-9\.,\-]/g, "") != "" ){
				evt.preventDefault();				
				return
			} 
			
			/*if(evt.keyCode == 46){
				$(this).val( $(this).val());
				evt.preventDefault();				
				return
			} */
								
			
		}).keyup(function(evt){
			var data = tr.data();
			data.coefficient = parseFloat($(this).val());
			tr.data(data);
			me.reload_weigths()
			me.onChange(data,evt);
		}).change(function(evt){
			var data = tr.data();
			data.coefficient = parseFloat($(this).val());
			tr.data(data);
			me.reload_weigths()
			me.onChange(data);
		})).appendTo(tr);

		
		if(paramData.groupoperation != ""){
			var select = $("<select/>").change(function(evt) {
				var data = tr.data();
				data.groupoperation = $(this).val();
				tr.data(data);
				me.onChange(data);
			});
			
			$.each(["sum","max","min","avg"], function(i, x) {
				
					$("<option/>").appendTo(select).html(x);	
				
				

			});
			$("<td/>").appendTo(tr).append(	select);
			
			select.val(paramData.groupoperation)

		} else {
			$("<td/>").appendTo(tr);
		}
		
		if (paramData.type == "value") {

			if (paramData.options.length == 0) {
				
				$("<td/>").appendTo(tr).html(
						"if is <b>" + paramData.operation + "</b> to " + paramData.value)
			} else {

				var select = $("<select/>").change(function(evt) {
					var data = tr.data();
					data.value = $(this).val();
					tr.data(data);
					me.onChange(data);
				});
				
				$.each(paramData.options, function(i, x) {
					
						$("<option/>").appendTo(select).html(x);	
					
					

				});
				
				if (paramData.value == null) {
					paramData.value = paramData.options[0];
				} 
				select.val(paramData.value);

				$("<td/>").appendTo(tr).html(
						"if is <b>" + paramData.operation + "</b> to ").append(
						select);
			}
		} else {
			$("<td/>").appendTo(tr);
		}

		$("<td/>").appendTo(tr);
		
		tr.data(paramData);
		this.onAdd(paramData);
		this.reload_weigths()
	}
}
