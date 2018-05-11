<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page"
          xmlns:spring="http://www.springframework.org/tags"
          xmlns:c="http://java.sun.com/jsp/jstl/core"
          xmlns:fn="http://java.sun.com/jsp/jstl/functions"
          xmlns:form="http://www.springframework.org/tags/form" version="2.0">
    <jsp:directive.page language="java" contentType="text/html"/>
    <c:set var="reqUrl">${pageContext.request.requestURL}</c:set>
    <c:set var="baseURL"
           value="${fn:replace(reqUrl, pageContext.request.requestURI,pageContext.request.contextPath)}"/>
    <html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>

        <meta name="header_title" content="Tree"/>
        <meta name="header_title_desc" content="Samples tree"/>

        <meta name="_csrf" content="${_csrf.token}"/>
        <!-- default header name is X-CSRF-TOKEN -->
        <meta name="_csrf_header" content="${_csrf.headerName}"/>

        <title>Strains Analysis</title>

        <style type="text/css">
            .box .box-header {
            padding-bottom: 10px;
            }
            .content {padding-top: 0px}
            #phylocanvas {
                /*width: 100%;*/
                height: 800px;
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

        var genome = ${genome};
        var project = ${project};
        var newicks = ${trees};
        var strains = '${strains}';
        var defaultNewick = ${defaultTree};



    </script>


    <script type="text/javascript">

        $(document).ready(function () {
            $("<a/>", {
                "href": "${baseURL}/genome"
            }).html("Genomes").appendTo($("#base_breadcrumb"));
            var li = $("<li/>").appendTo($(".breadcrumb"));
            $("<a/>", {
                "href": "${baseURL}/genome/" + genome.name
            }).html("<i>" + genome.organism + "</i>").appendTo(li);
            var li = $("<li/>").addClass("active").appendTo($(".breadcrumb"));
            $("<a/>", {
                "href": "${baseURL}/variant/" + project.id
            }).html("<i>" + project.name + "</i>").appendTo(li);

            $("#treeTitle").html(project.description);


            var tree = Phylocanvas.createTree('phylocanvas', {
                alignLabels: true,
            });
            $('#treeTypeSelect').change(() => tree.setTreeType($('#treeTypeSelect').val()));
            var groups = project.trees[defaultNewick].groups;
            Object.keys(groups).forEach(x => {
                $('#strainGroupSelect').append($("<option/>",{value:groups[x]}).html(x));
            });

            $('#strainGroupSelect').change(() => {
                if ($('#strainGroupSelect').val() == "root") {
                    tree.redrawOriginalTree();
                } else {
                    tree.redrawFromBranch(tree.branches[$('#strainGroupSelect').val()]);
                }

            });

            if(project.trees.length > 1){
                project.trees.forEach(x => {
                    $('#treesSelect').append($("<option/>",{value:x.name}).html(x.description));
                });
                $('#treesSelect').val($.QueryString["selected"]);
                $('#treesSelect').change(() => {
                    window.location = '${baseURL}/variant/' + project.id + "/tree?selected=" + $('#treesSelect').val();
                });
            } else {
                $('#treesSelect').remove()
            }




            //var categories = {}
            //genome.strainsProps.map(x => x.category).forEach(x =>{ categories[x]  = 1});
            //categories = Object.keys(categories);
            var categories = project.trees[defaultNewick].categories;
            var catTable = $("<table />").append($("<tr />").append($("<td />").html("Category"))
                .append($("<td />").html("Show")));
            $("#propCategories").append(catTable);
            catTable.addClass("table" ).addClass("table-bordered");
            var selected_categories = categories;
            categories.forEach(category => {
                   var tr = $("<tr />").appendTo(catTable)
                    $("<td />").html(category).appendTo(tr);
                   var check = $("<input />",{type:"checkbox",checked:"checked"});
                $("<td />").appendTo(tr).append(check  );
                check.change(() => {
                    if( check.is( ":checked" ) ){
                        selected_categories.push(category);
                    } else {
                        selected_categories = selected_categories.filter(x => x != category )
                    }
                    tree.redrawOriginalTree();
                    $('#strainGroupSelect').val("root");
                });
            });

            //tree.setRoot(tree.findLeaves("NZ_LN854556")[0]);

            //tree.findLeaves(/NZ_LN854556/)[0].pruned = true
            //tree.findLeaves(/NC_002745/)[0].pruned = true
            tree.setNodeSize(10);
            tree.setTextSize(52);

            //tree.showInternalNodeLabels = true;
            //tree.showBranchLengthLabels = true;
            tree.internalLabelStyle.colour = 'red';
            tree.internalLabelStyle.textSize = 18

            tree.lineWidth = 2;
            var props = {}
            genome.strains.forEach(strain => {
                props[strain.name] = {
                    region: strain.region,
                    date: strain.date
                    /*eSNPs: x.properties.eSNPs,
                    MLST_CC: x.properties.MLST_CC,
                    PFGE: x.properties.PFGE,
                    SCCmec: x.properties.SCCmec*/
                }
                strain.properties.forEach(y => {
                        props[strain.name][y.name] = y.value;
                })
            });
            tree.on('beforeFirstDraw', function () {
                for (var i = 0; i < tree.leaves.length; i++) {
                    var data = {};
                    var cols = genome.strainsProps// ["eSNPs","MLST_CC","PFGE","SCCmec",'AacA-AphD', 'AphA-3', 'Erm33', 'ErmA', 'ErmB', 'ErmC', 'ErmT',"date","region"];

                    cols.forEach((prop, j) => {
                        if( selected_categories.indexOf(prop.category) != -1){
                            var value = props[tree.leaves[i].id][prop.name];
                            if (prop.name === "date") {
                                value = value.split(" ")[value.split(" ").length - 1];
                            }
                            data[prop.name] = {
                                colour: "white",//'#3C7383',
                                label: value
                            }
                        }

                    });
                    tree.leaves[i].data = data
                }
            });


            //tree.setRoot( root)
            tree.alignLabels = true;
            tree.load(newicks[defaultNewick]);
            tree.setTreeType('rectangular');


            //tree.findLeaves(/NZ_LN854556/)[0].parent.pruned = true
            //var root = tree.findLeaves(/NZ_LN854556/)[0].parent.children[1];
            //tree.fitInPanel(tree.findLeaves(/^((?!NZ_LN854556)[\s\S])*$/))

            tree.on('click', function (e) {
                var node = tree.getNodeAtMousePosition(e);

                if (node) {
                    //tree.redrawFromBranch(node);
                }
            });

            $("#redraw_btn").click(() => {$('#strainGroupSelect').val("All");tree.redrawOriginalTree();});
            //console.log(tree.findLeaves(/^((?!NZ_LN854556)[\s\S])*$/))
            tree.draw();


            var canvas = $("#phylocanvas").children()[0];


            function getBackingStorePixelRatio(context) {
                return (
                    context.backingStorePixelRatio ||
                    context.webkitBackingStorePixelRatio ||
                    context.mozBackingStorePixelRatio ||
                    context.msBackingStorePixelRatio ||
                    context.oBackingStorePixelRatio ||
                    1
                );
            }

            function getPixelRatio(canvas) {
                return (window.devicePixelRatio || 1) / getBackingStorePixelRatio(canvas);
            }

            function translateClick(event, tree2) {
                const pixelRatio = getPixelRatio(tree2.canvas);
                return [
                    (event.offsetX - tree2.offsetx) / tree2.zoom * pixelRatio,
                    (event.offsetY - tree2.offsety) / tree2.zoom * pixelRatio,
                ];
            }


            canvas.addEventListener('mousemove', function (event) {
                canvas.style.cursor = ""

                tree.metadata_elements.forEach(function (element) {
                    var x = translateClick(event, tree)[0] - element.branch.maxx;
                    var y = translateClick(event, tree)[1] - element.branch.centery + 4;
                    if (y > element.top && y < element.top + element.height
                        && x > element.left - element.width && x < element.left) {

                        canvas.style.cursor = 'pointer';

                    }
                });

            }, false);

            canvas.addEventListener('click', function (event) {

                tree.metadata_elements.forEach(function (element) {
                    var x = translateClick(event, tree)[0] - element.branch.maxx;
                    var y = translateClick(event, tree)[1] - element.branch.centery + 4;
                    if (y > element.top && y < element.top + element.height
                        && x > element.left - element.width && x < element.left) {
                        //console.log('clicked an element:' + element.leave + " : " + element.prop );
                        $("#selectedMetaPanel").html(
                            '<table>' +
                            "<tr><td>Strain</td><td>" + element.leave + "</td>" +
                            "<tr><td>" + element.prop + "</td><td>" + element.column.label + "</td>" +


                            "</table>"
                        ).css("position", "relative").css("top", event.pageY);


                    }
                });

            }, false);


        });

    </script>

    <script type="text/javascript">
        // ]]>
    </script>


    <div class="row">
        <section class="col-lg-12">
        <h3 id="treeTitle" class="box-title">Tree</h3>
        </section>
        <section class="col-lg-9 ">

            <div class="box box-primary">
                <div class="box-header">
                    <!-- tools box -->
                    <div class="pull-right box-tools">
                        Change tree: <select id="treesSelect">-</select>
                        Groups:
                        <select id="strainGroupSelect">-</select>
                        Tree Type:
                        <select id="treeTypeSelect">

                            <option selected="selected">rectangular</option>
                            <option>radial</option>
                            <option>circular</option>
                            <option>diagonal</option>
                            <option>hierarchical</option>
                        </select>
                        <button id="redraw_btn" class="btn btn-info btn-sm">
                            Redraw
                        </button>

                    </div>

                    <i class="fa  fa-sitemap">&#160;</i>
                    <h3  class="box-title">Tree</h3>
                </div>
                <div id="phylocanvas">-</div>


            </div>
        </section>
        <section class="col-lg-3">
            <div id="propCategories" class="box">

            </div>
            <div id="selectedMetaPanel" height="100%" class="box">

            </div>
        </section>

    </div>


    </body>
    </html>
</jsp:root>