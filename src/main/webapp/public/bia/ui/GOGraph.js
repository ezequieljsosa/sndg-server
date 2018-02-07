//var cytoscape = require('cytoscape');
//var cydagre = require('cytoscape-dagre');
//var dagre = require('dagre');
//
//cydagre( cytoscape, dagre ); // register extension

$.GOGraph = function(divElement, ontologies) {
	this.divElement = divElement;
	this.ontologies = ontologies.concat( [go_bp,go_mf,go_cc]);
	this._valid_terms = $.map(ontologies,function(x){  return x.term;})

	this.edges = []
	this.nodes = []

	this.cy = null;
	this.current_mode = "all";
	this.mode = null; // {"all":[],"bp":[],"cl":[],"mf":[]};
}

$.GOGraph.prototype = {
	fit: function(){
		this._create_dag_layout()
		this.cy.fit();
	},
	change_mode : function(mode) {
		// this.mode[mode].each( function(i, ele){
		// debugger
		// })
		this.cy.remove(this.mode["all"]);
		this.current_mode = mode
		this.cy.add(this.mode[this.current_mode])
		

	},
	_create_nodes_and_edges : function() {
		this.nodes = $.map(this.ontologies, function(ont) {
			$.each(ont.children, function(i, child) {
				if(this._valid_terms.indexOf(child) != -1){
					this.edges.push({
						data : {
							source : ont.term,
							target : child

						}
					});
				}				
			}.bind(this))

			// var node_type = "inter";
			/*
			 * if (ROOT_GO.indexOf(ont.term.toUpperCase()) != -1) { node_type =
			 * "root"; } if (leaf_nodes.indexOf(ont.term) != -1) { node_type =
			 * "leaf"; }
			 */

			return {
				data : {
					id : ont.term,
					name : ont.name,
					database : ont.database,
				// node_type : node_type,
				},classes: 'multiline-auto'
			};
		}.bind(this))
	},
	_get_syle : function() {
		return cytoscape.stylesheet().selector('node').css({
			'content' : 'data(name)',
			'text-valign' : 'top',
			'color' : 'black',
			'font-size' : '20'
		// 'text-outline-width' : 2,
		// 'text-outline-color' : '#888'
		}).selector('edge').css({
			'width' : 10,
			'target-arrow-shape' : 'triangle'
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
            'text-max-width': 100
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
				me.cy.userZoomingEnabled(false)
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
				me._create_dag_layout();
				
				if (me.mode == null) {
					me.bp = me.cy.nodes("[id='go:0008150']")
					me.cl = me.cy.nodes("[id='go:0005575']")
					me.mf = me.cy.nodes("[id='go:0003674']")

					me.mode = {
						"all" : me.cy.elements(),
						"bp" : me.bp.successors(),
						"cl" : me.cl.successors(),
						"mf" : me.mf.successors()
					};
				}
				me.cy.nodes().leaves().css({
					'background-color' : 'green'
				})
				me.cy.nodes().roots().css({
					'background-color' : 'brown'
				})
								
				window.cy = me.cy
				me.cy.minZoom(me.cy.zoom())
				me.cy.maxZoom(5.4)
				
			}
		});
	},
	_create_dag_layout : function() {
		var options = {
			name : 'dagre',

			// dagre algo options, uses default value on undefined
			nodeSep : 200, // the separation between adjacent nodes in
			// the same rank
			edgeSep : undefined, // the separation between adjacent
			// edges in the same rank
			rankSep : 70, // the separation between adjacent nodes in
			// the same rank
			rankDir : undefined, // 'TB' for top to bottom flow, 'LR'
			// for left to right
			minLen : function(edge) {
				return 1;
			}, // number of ranks to keep between the source and target
			// of the edge
			edgeWeight : function(edge) {
				return 1;
			}, // higher weight edges are generally made shorter and
			// straighter than lower weight edges

			// general layout options
			fit : true, // whether to fit to viewport
			padding : 30, // fit padding
			animate : false, // whether to transition the node
			// positions
			animationDuration : 500, // duration of animation in ms
			// if enabled
			boundingBox : undefined, // constrain layout bounds; {
			// x1, y1, x2, y2 } or { x1, y1,
			// w, h }
			ready : function() {
			}, // on layoutready
			stop : function() {
			} // on layoutstop
		};

		this.cy.layout(options);
	},
	init : function() {
		this._create_nodes_and_edges();

		this._create_grapth();
		

	}
}