<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page"
	xmlns:spring="http://www.springframework.org/tags"
	xmlns:c="http://java.sun.com/jsp/jstl/core"
	xmlns:fn="http://java.sun.com/jsp/jstl/functions"
	xmlns:form="http://www.springframework.org/tags/form" version="2.0">
	<jsp:directive.page language="java" contentType="text/html" />
	<c:set var="reqUrl">${pageContext.request.requestURL}</c:set>
	<c:set var="baseURL"
		value="${fn:replace(reqUrl, pageContext.request.requestURI,pageContext.request.contextPath)}" />
	<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />

<meta name="header_title" content="Tree" />
<meta name="header_title_desc" content="Samples tree" />

<meta name="_csrf" content="${_csrf.token}" />
<!-- default header name is X-CSRF-TOKEN -->
<meta name="_csrf_header" content="${_csrf.headerName}" />

<title>Genome Pathways</title>

<style type="text/css">
.box .box-header {
	padding-bottom: 10px;
}

#phylocanvas {
	width: 100%;
	height: 1000px;
}
</style>



</head>
<body>

	<script type="text/javascript">
		//<![CDATA[
	</script>
	<script src="${baseURL}/public/widgets/phylocanvas-quickstart.js"
		type="text/javascript"></script>



	<script type="text/javascript">
		
		var genome = ${genome} ;
		var newick = "${tree}" ;
		var strains = '${strains}' ;
		
	</script>


	<script type="text/javascript">
		
		$(document).ready(function() {
			
			var tree = Phylocanvas.createTree('phylocanvas');
		      
		      
		      
		      
		      //tree.setRoot(tree.findLeaves("NZ_LN854556")[0]);
		      
		      //tree.findLeaves(/NZ_LN854556/)[0].pruned = true
		      //tree.findLeaves(/NC_002745/)[0].pruned = true
		      tree.setNodeSize(10 );
		      tree.setTextSize(52);
		      
		      tree.showInternalNodeLabels = true;
		      //tree.showBranchLengthLabels = true;
		      tree.internalLabelStyle.colour = 'red';
		      tree.internalLabelStyle.textSize = 18
		      
		      tree.lineWidth = 2;
		      var props = {}
		      genome.strainsProps.forEach(x=> {
		    	  props[x.name] = {
		    			  region:x.region,
		    			  date:x.date
		    			  /*eSNPs: x.properties.eSNPs,
		    			  MLST_CC: x.properties.MLST_CC,
		    			  PFGE: x.properties.PFGE,
		    			  SCCmec: x.properties.SCCmec*/
		    	  }		    	   
		    	  Object.keys(x.properties).forEach(y => {props[x.name][y] = x.properties[y]   ;})
		      });
		      tree.on('beforeFirstDraw', function () {
		    	  for (var i = 0; i < tree.leaves.length; i++) {
		    		  var data = {};
		    		var cols = ["eSNPs","MLST_CC","PFGE","SCCmec",'AacA-AphD', 'AphA-3', 'Erm33', 'ErmA', 'ErmB', 'ErmC', 'ErmT',"date","region"];
		    		
		    		cols.forEach((x,j) => {
		    			var value = props[tree.leaves[i].id][x];
		    			if(x === "date"){
		    				value = value.split(" ")[value.split(" ").length - 1];
		    			}
		    			data[x] = {
				    	        colour: "white",//'#3C7383',
				    	        label: value
				    	}
		    		});
		    	    tree.leaves[i].data = data
		    	  }
		    	});
		      
		      
		      //tree.findLeaves(/NZ_LN854556/)[0].parent.pruned = true
		      //var root = tree.findLeaves(/NZ_LN854556/)[0].parent.children[1];
		      //tree.setRoot( root)
		      tree.alignLabels = true;
		      tree.setTreeType('rectangular');
		      tree.load(newick);
		      //tree.fitInPanel(tree.findLeaves(/^((?!NZ_LN854556)[\s\S])*$/))
		      
		      tree.on('click', function (e) {
  var node = tree.getNodeAtMousePosition(e);
  if (node) {
    tree.redrawFromBranch(node);
  } 
    
  
});
	$("#redraw_btn").click(x => tree.redrawOriginalTree() );	      
		      //console.log(tree.findLeaves(/^((?!NZ_LN854556)[\s\S])*$/))
		      //tree.draw();

		});

	</script>

	<script type="text/javascript">
		// ]]>
	</script>



	<div class="row">
		<section class="col-lg-12 ">
			<div class="box box-primary">
				<div class="box-header">
					<!-- tools box -->
					<div class="pull-right box-tools">
						
						<button id="redraw_btn" class="btn btn-info btn-sm" >
							Redraw 
						</button>

					</div>
					
					<i class="fa  fa-sitemap">&#160;</i>
					<h3 class="box-title">Tree</h3>
				</div>
				<div id="pocket_list_div" class="box-body">
					<div id="phylocanvas"></div>
				</div>
			</div>
		</section>
	</div>




</body>
	</html>
</jsp:root>