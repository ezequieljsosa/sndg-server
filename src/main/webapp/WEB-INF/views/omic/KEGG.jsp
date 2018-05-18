<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page"
          xmlns:spring="http://www.springframework.org/tags"
          xmlns:c="http://java.sun.com/jsp/jstl/core"
          xmlns:fn="http://java.sun.com/jsp/jstl/functions"
          xmlns:form="http://www.springframework.org/tags/form" version="2.0">
    <jsp:directive.page language="java" contentType="text/html"/>
    <c:set var="reqUrl">${pageContext.request.requestURL}</c:set>
    <c:set var="baseURL" value="${fn:replace(reqUrl, pageContext.request.requestURI,pageContext.request.contextPath)}"/>
    <html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>

        <meta name="header_title" content="Pathway"/>
        <meta name="header_title_desc" content="reactions in pathway"/>

        <meta name="_csrf" content="${_csrf.token}"/>
        <!-- default header name is X-CSRF-TOKEN -->
        <meta name="_csrf_header" content="${_csrf.headerName}"/>

        <title>KEGG Pathways</title>

        <style type="text/css">
            .ext {
                background-image: "external.gif";
            }
            .box .box-header {
                padding-bottom: 10px;
            }

            /*div.dataTables_paginate ul.pagination li.paginate_button {*/
            /*margin: 0px;*/
            /*padding: 0px;*/
            /*border: 0px;*/
            /*white-space: nowrap;*/
            /*}*/

            /*.dataTables_wrapper .dataTables_paginate .paginate_button:hover {*/
            /*border: 0px;*/
            /*}*/

            /*.dataTables_wrapper .dataTables_paginate .paginate_button.disabled {*/
            /*border: 0px;*/
            /*}*/

            /*.dataTables_wrapper .dataTables_paginate .paginate_button.disabled:hover {*/
            /*border: 0px;*/
            /*}*/

        </style>

        <link rel="stylesheet" type="text/css" href="https://cdn.datatables.net/v/bs/dt-1.10.16/datatables.min.css"/>


    </head>
    <body>

    <script type="text/javascript" src="https://cdn.datatables.net/v/bs/dt-1.10.16/datatables.min.js"></script>

    <script type="text/javascript">
        //<![CDATA[
    </script>


    <script type="text/javascript">


        let genome = ${genome};
        let selected_pathway = '${pathway}';
        let kegg = ${kegg};
        let proteins = ${proteins};


        $(document).ready(function () {
            $("#orgKegg").attr("href", kegg["pageUrl"]);

            $('#pathways_table').DataTable({
                data: genome.kegg.map(pw => [pw.term.split(":")[1], pw.name, pw.count]),
                columns: [
                    {
                        title: "PW",
                        render: x => '<a href="${baseURL}/kegg/' + genome.name + '?pathway=ko' + x + '">' + x + '</a>'
                    },
                    {title: "Name"},
                    {title: "PW size"},

                ]
            });

            if (window.location.href.indexOf("?pathway") != -1) {
                const box = $("#params_box");
                const bf = box.find(".box-body, .box-footer");
                if (!box.hasClass("collapsed-box")) {
                    box.addClass("collapsed-box");
                    bf.slideUp();
                }
            }


            $("#kegg_container").append($("<img />", {
                name: "pathwayimage", usemap: "#mapdata", border: "0", src: kegg["imageUrl"]

            }));
            const datamap = $.parseHTML(kegg["mapdata"])[0];
            $("#kegg_container").append(datamap);


            var genes_table = $('#genes-table').DataTable({
                data: proteins.map(x => [eval(x[0]), eval(x[1])]),
                columns: [
                    {
                        title: "Gene",
                        render: x => '<a href="${baseURL}/genome/' + genome.name + '/gene/' + x[0] + '/">' + x[0] + '</a>'
                    },
                    {
                        title: "Annotations",
                        render: x => x.sort().map(y => {
                            let txt= "";
                            if(y.startsWith("kegg:")){
                                txt = '<a  class="ext" href="http://www.kegg.jp/dbget-bin/www_bget?' +
                                    y.split('kegg:')[1] + '">' + y.split('kegg:')[1] + '</a>';
                            }
                            if(y.startsWith("ec:")){
                                txt = y;
                            }
                            return txt;
                        }).filter(x => x.length > 0).join(", ")
                    }


                ]
            });

            $.each(datamap.children, (i, x) => {
                /*
 <area shape=rect	coords=464,919,510,936	href="/dbget-bin/www_bget?K22473+K22474"	title="K22473 (adhA), K22474 (adhB)" />
<area shape=rect	coords=625,220,687,457	href="/kegg-bin/show_pathway?ko00030"
<area shape=circle	coords=181,228,4	href="/dbget-bin/www_bget?C00267"	title="C00267 (alpha-D-Glucose)" onmouseover="popupTimer(&quot;C00267&quot;, &quot;C00267 (alpha-D-Glucose)&quot;, &quot;#ffffff&quot;)" onmouseout="hideMapTn()" />

                * */


                const href = $(x).attr("href");
                if (href.indexOf("dbget-bin/www_bget") != -1) {
                    if ($(x).attr("shape") == "rect") {
                        $(x).removeAttr("href");

                        $(x).click(() => {
                            const filter = href.split("?")[1].replace(new RegExp("\\+", 'g'), "|");

                            genes_table.search(filter, true, false).draw();
                        });
                    } else {
                        $(x).attr("href", "http://www.kegg.jp/" + $(x).attr("href"));
                    }
                } else if (href.indexOf("show_pathway") != -1) {
                    $(x).attr("href", "${baseURL}/kegg/" + genome.name + "?pathway=" + href.split("?")[1])
                }

                $(x).removeAttr("onmouseover");
                $(x).removeAttr("onmouseout");


            });


        });

    </script>

    <script type="text/javascript">
        // ]]>
    </script>


    <div>

        <div class="row">
            <section class="col-lg-12 connectedSortable">
                <div id="params_box" class="box box-primary">
                    <div class="box-header" style="cursor: move;">
                        <!-- tools box -->
                        <div class="pull-right box-tools">

                            <button class="btn btn-info btn-sm" data-widget="collapse"
                                    data-toggle="tooltip" title="" data-original-title="Collapse">
                                <i class="fa fa-minus"> &#160;</i>
                            </button>

                        </div>
                        <!-- /. tools -->
                        <h3 id="pathways_list_title" class="box-title">KEGG Pathways</h3>


                    </div>
                    <div class="box-body">
                        <table class="table table-striped" id="pathways_table">
                            <thead>
                            <tr>

                                <th width="30px">Term</th>
                                <th>Name</th>
                                <th>Protein Count</th>
                            </tr>
                            </thead>
                        </table>
                    </div>
                </div>
            </section>
        </div>


        <div class="row">
            <section id="kegg_container" class="col-lg-9 connectedSortable">
<a class="ext" id="orgKegg"> Original KEGG PW page </a><br />

            </section>
            <section id="genes_container" class="col-lg-3 connectedSortable">
                <table class="table table-striped" id="genes-table">
                    <thead>
                    <th></th>
                    <th></th>
                    </thead>
                    <tbody></tbody>
                </table>

            </section>

        </div>
    </div>
    <script type="text/javascript">
        function searchKOs(kos) {/*
            $("#genes_container").html("wait")
            fetch("



            ${baseURL}/kegg/" + genome.name + "/genes/" + kos.join("+") )
                .then(
                    function(response) {
                        $("#genes_container").html("");
                        if (response.status !== 200) {
                            console.log('Looks like there was a problem. Status Code: ' +
                                response.status);
                            alert("error querying kegg associated genes")
                            return;
                        }


                        response.json().then(function(data) {
                            $("#genes_container").html(JSON.stringify(data));
                        });
                    }
                )
                .catch(function(err) {
                    $("#genes_container").html("");
                    alert("error querying kegg associated genes")
                    console.log('Fetch Error :-S', err);
                });*/
        }

    </script>


    </body>
    </html>
</jsp:root>