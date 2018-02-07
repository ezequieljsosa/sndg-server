$.BioCycGraph = function(divElement, reactions) {
	this.divElement = divElement;
	this.reactions = reactions;
	this.edges = []
	this.nodes = []
	// this.pathway = pathway;
}
$.BioCycGraph.prototype = {
	_create_nodes_and_edges : function() {

		
		var node_names = []
		var connect = {}

		$.each(this.reactions, function(i, reaction) {

			if (node_names.indexOf(reaction.protein) == -1) {
				node_names.push(reaction.protein)
				var node = {
					data : {
						"id" : reaction.protein,
						"name" : reaction.protein + "\n" + reaction.reaction 		,
						"reaction" : reaction.reaction

					// node_type : node_type,
					}, classes: 'multiline-auto'
				};
				this.nodes.push(node);
			}
		}.bind(this));

		$.each(this.reactions, function(i, reaction) {
			if (typeof reaction.products != "undefined") {
				$.each(reaction.products, function(i, product) {
					$.each(this.reactions, function(i, reaction2) {
						
						if ((reaction2.protein != reaction.protein) && (typeof reaction2.substrates != "undefined") ){
							$.each(reaction2.substrates, function(i, substrate) {
								if (product == substrate) {
									var edge = {
										data : {
											source : reaction2.protein,
											target : reaction.protein,
											metabolite : product
										}, classes: 'multiline-auto'
									}
									
									var edge2 = $.grep(this.edges,function(x){
										return (((x.data.source == reaction.protein) && 
												//(x.data.metabolite == product) &&
												(x.data.target == reaction2.protein) ) ||
												((x.data.source == reaction2.protein) && 
														//(x.data.metabolite == product) &&
														(x.data.target == reaction.protein)))
												
												;})
									if(edge2.length ==0){
										this.edges.push(edge);	
									} else {
										edge2[0].data.metabolite += "\n" +   product
									}
									
								}
							}.bind(this));
						}
					}.bind(this));
				}.bind(this));
			}
		}.bind(this))

	},
	_get_syle : function() {
		return cytoscape.stylesheet().selector('node').css({
			'content' : 'data(name)',
			'text-valign' : 'top',
			'color' : 'blue',
			'font-size' : '20'
		// 'text-outline-width' : 2,
		// 'text-outline-color' : '#888'
		}).selector('edge').css({
			"curve-style" : "bezier",
			'width' : 10,
			'target-arrow-shape' : 'triangle',
			//'content' : 'data(metabolite)',
			'text-valign' : 'top',
			'color' : 'red',
			'font-size' : '20'
		}).selector(':selected').css({
			'background-color' : 'black',
			'line-color' : 'black',
			'target-arrow-color' : 'black',
			'source-arrow-color' : 'black'
		}).selector('.faded').css({
			'opacity' : 0.25,
			'text-opacity' : 0
		/*
		 * }).selector('node[node_type = "root"]').css({ 'background-color' :
		 * 'brown', 'text-outline-color' : '#666' }).selector('node[node_type =
		 * "leaf"]').css({ 'background-color' : 'green', 'text-outline-color' :
		 * '#666'
		 */
		}).selector(".multiline-auto").css({
			'text-wrap': 'wrap',
            'text-max-width': 6
		})

	},
	_create_grapth : function() {
		var me = this;
		this.divElement.cytoscape({
			style : me._get_syle(),

			elements : {
				nodes : me.nodes,
				edges : me.edges
			},

			ready : function() {
				me.cy = this;

				// giddy up...

				me.cy.elements().unselectify();

				me.cy.on('tap', 'node', function(e) {
					var node = e.cyTarget;
					var neighborhood = node.neighborhood().add(node);

					cy.elements().addClass('faded');
					neighborhood.removeClass('faded');
				});

				me.cy.on('tap', function(e) {
					if (e.cyTarget === cy) {
						cy.elements().removeClass('faded');
					}
				});

				window.cy = me.cy
				var options = {
						  name: 'grid',
						  
						  fit: true, // whether to fit the viewport to the graph
						  padding: 100, // the padding on fit
						  startAngle: 3/2 * Math.PI, // the position of the first node
						  counterclockwise: false, // whether the layout should go counterclockwise/anticlockwise (true) or clockwise (false)
						  minNodeSpacing: 200, // min spacing between outside of nodes (used for radius adjustment)
						  boundingBox: undefined, // constrain layout bounds; { x1, y1, x2, y2 } or { x1, y1, w, h }
						  avoidOverlap: true, // prevents node overlap, may overflow boundingBox if not enough space
						  height: undefined, // height of layout area (overrides container height)
						  width: undefined, // width of layout area (overrides container width)
						  concentric: function(node){ // returns numeric value for each node, placing higher nodes in levels towards the centre
						    return node.degree();
						  },
						  levelWidth: function(nodes){ // the variation of concentric values in each level
						    return nodes.maxDegree() / 4;
						  },
						  animate: false, // whether to transition the node positions
						  animationDuration: 500, // duration of animation in ms if enabled
						  ready: undefined, // callback on layoutready
						  stop: undefined // callback on layoutstop
						};
				
//				var options = {
//						  name: 'circle',
//
//						  fit: true, // whether to fit the viewport to the graph
//						  padding: 30, // the padding on fit
//						  boundingBox: undefined, // constrain layout bounds; { x1, y1, x2, y2 } or { x1, y1, w, h }
//						  avoidOverlap: true, // prevents node overlap, may overflow boundingBox and radius if not enough space
//						  radius: undefined, // the radius of the circle
//						  startAngle: 3/2 * Math.PI, // the position of the first node
//						  counterclockwise: false, // whether the layout should go counterclockwise (true) or clockwise (false)
//						  sort: undefined, // a sorting function to order the nodes; e.g. function(a, b){ return a.data('weight') - b.data('weight') }
//						  animate: false, // whether to transition the node positions
//						  animationDuration: 500, // duration of animation in ms if enabled
//						  ready: undefined, // callback on layoutready
//						  stop: undefined // callback on layoutstop
//						};

						me.cy.layout( options );
						me.cy.minZoom(me.cy.zoom())
						me.cy.maxZoom(5.4)
			}
		});
	},
	init : function() {
		this._create_nodes_and_edges();

		this._create_grapth();

	}
}
