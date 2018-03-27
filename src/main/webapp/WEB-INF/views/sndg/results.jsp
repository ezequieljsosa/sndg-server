<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page"
          xmlns:spring="http://www.springframework.org/tags"
          xmlns:c="http://java.sun.com/jsp/jstl/core"
          xmlns:form="http://www.springframework.org/tags/form"
          xmlns:fn="http://java.sun.com/jsp/jstl/functions"
          xmlns:sec="http://www.springframework.org/security/tags" version="2.0">
    <jsp:directive.page language="java" contentType="text/html"/>

    <c:set var="reqUrl">${pageContext.request.requestURL}</c:set>
    <c:set var="baseURL"
           value="${fn:replace(reqUrl, pageContext.request.requestURI,pageContext.request.contextPath)}"/>


    <html>
    <head>

        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="header_title" content="Resultados"/>

    </head>
    <body>


    <script type="text/javascript">
        //<![CDATA[
    </script>

    <script type="text/javascript">

        function render_filter(filter_props, parent) {
            var tr = $("<tr/>").appendTo(parent);
            var enable = $("<input/>", {id: "in_" + filter_props.name, type: "checkbox"});
            enable.change(function () {
                filter_props.enabled = enable.is(':checked');
            });
            $("<td/>").appendTo(tr).append(enable)

            var title = filter_props.name.toUpperCase().substring(0, 1) + filter_props.name.toLowerCase().replace("_", " ").substring(1);
            if (filter_props.caption) {
                title = filter_props.caption;
            }
            tr.append($("<td/>").html(title));
            var elem = $("<td/>").appendTo(tr);
            if (filter_props.type === "text") {
                var input = $("<input/>", {id: "in_" + filter_props.name, type: "text"});

                elem.append(input);
                input.val(filter_props.value);
                input.keypress(function () {
                    filter_props.value = input.val();
                    filter_props.enabled = true;
                    enable.prop('checked', true);
                })
            }
            if (filter_props.type === "select") {
                var sel = $("<select/>", {id: "in_" + filter_props.name}).appendTo(elem);
                elem.append(sel);
                filter_props.options.forEach(x => {
                    if (x instanceof Array) {
                        sel.append($("<option/>", {value: x[1]}).html(x[0]));
                    } else {
                        sel.append($("<option/>", {value: x}).html(x));
                    }

                });
                sel.val(filter_props.value);
                sel.change(function () {
                    filter_props.value = sel.val();
                    filter_props.enabled = true;
                    enable.prop('checked', true);
                })
            }
            if (filter_props.type === "check") {
                var check = $("<input/>", {id: "in_" + filter_props.name, type: "checkbox"});
                elem.append(check);
                if (filter_props.value == "true") {
                    check.prop('checked', true);
                }
                check.change(function () {
                    filter_props.value = check.is(':checked');
                    filter_props.enabled = true;
                    enable.prop('checked', true);
                })
            }
            if (filter_props.type === "range") {

                var values = filter_props.value.split("_");
                var display = $("<span/>").appendTo(elem);
                var sel = $("<select/>", {id: "in_" + filter_props.name}).appendTo(elem);
                sel.append($("<option/>", {value: "gt"}).html(">="));
                sel.append($("<option/>", {value: "lt"}).html("<="));
                sel.val(values[0]);
                var rinput = $("<input/>", {
                    id: "in_" + filter_props.name,
                    type: "range",
                    min: filter_props.min,
                    max: filter_props.max
                });
                rinput.val(values[1])
                elem.append(rinput);
                sel.change(function (evt) {
                    filter_props.value = sel.val() + "_" + rinput.val().toString();
                    filter_props.enabled = true;
                    enable.prop('checked', true);
                });
                rinput.change(function (evt) {
                    filter_props.value = sel.val() + "_" + rinput.val().toString();
                    display.html(rinput.val())
                    filter_props.enabled = true;
                    enable.prop('checked', true);
                });
                display.html(rinput.val())
            }
            if (filter_props.help) {
                tr.append($("<td/>").html(filter_props.help));
            }
        }

        var species_filter = {
            name: "species", type: "select", caption: "Especie", value: "Bacteria",
            options: [["Bacteria", 2], ["Animales", 33208], ["Plantas", 3193], ["Hongos", 4751],
                ["Arquea", 2157], ["Virus", 10239]]
        };
        var tax_filter = {
            name: "taxonomia",
            caption: "Taxonomía",
            value: "",
            type: "text",
            help: "identificador numérico o palabra clave"
        };
        var assay_filter = {
            name: "ensayo", type: "select", options: ["X-RAY", "NMR", "MICROSCOPY", "MODEL"], value: "X-RAY"
        };
        var ligand_filter = {
            name: "has_ligand", type: "check", caption: "Tiene ligando", value: "false"
        };
        var structure_filter = {
            name: "has_structure", type: "check", caption: "Tiene estructura", value: "false"
        };

        //var length_filter_seq = {name: "length", type: "range", value: "gt_200", min: 1, max: 10000};
        var length_filter_prot = {name: "length", caption: "Largo",type: "range", value: "gt_200", min: 1, max: 2000};

        var assembly_level_filter = {
            name: "assembly_level", type: "select", value: "Complete genome", options: ["Complete Genome",
                "Chromosome", "Scaffold", "Contig"]
        };
        var markercode_filter = {
            name: "markercode", type: "select", value: "COI-5P", options: [
                "COI-5P", "ARK", "atp6", "28S", "CHD-Z", "H4", "ITS1", "ITS",
                "18S", "COII", "TPI", "PGD", "rbcLa", "COI-3P",
                "ITS2", "UPA", "16S", "ENO", "hcpA", "12S",
                "MB2-EX2-3", "MC1R", "rbcL", "5.8S", "CAD", "matK", "CYTB", "AATS", "Wnt1", "CAD4", "D-loop", "H3"
            ]
        };


        var filters = {
            tool: [{
                name: "tool_type", type: "select", value: "webserver", options: ["webserver",
                    "app", "database", "library", "plugin", "program"
                ]
            }],
            struct: [species_filter, tax_filter,  ligand_filter],
            prot: [species_filter, tax_filter, structure_filter, length_filter_prot], //"ec", "go",
            seq: [species_filter, tax_filter, assembly_level_filter], //"ec", "go",
            barcode: [species_filter, tax_filter, markercode_filter],
            genome: [species_filter, tax_filter, assembly_level_filter] //"ec", "go",

        };

        var hide_filters = true;
        filters["${datatype}"].forEach(function (x) {
            if ($.QueryString[x.name]) {
                hide_filters = false;
                x.value = $.QueryString[x.name];

            }
        });


        var urlMap = {
            "seq": x => "${baseUrl}/sndg/genome/" + x.organism + "/contig/" + x.name + "?start=1&end=20000" ,
            "genome": x => "${baseUrl}/sndg/genome/" + x.name,
            "prot": x => "${baseUrl}/sndg/protein/" + x._id,
            "struct": x => "${baseUrl}/sndg/structure/" + x.name,
            "barcode": x => "${baseUrl}/sndg/barcode/" + x.processid,
            "tool": x => x.url
        };
        var nameMap = {
            "seq": "Secuencias Ensambladas",
            "genome": "Genomas",
            "prot": "Proteinas",
            "struct": "Estructuras",
            "barcode": "Barcodes",
            "tool": "Herramientas"
        }

        $.maxVisiblePages = 5
        $.pageSize = 50;
        $.page = ${page};

        function searchUrl(page, pageSize) {

            if (pageSize == undefined) {
                pageSize = $.pageSize
            }
            var params = [];
            filters["${datatype}"].filter(x => x.enabled).forEach(x => {
                params.push(x.name + "=" + x.value.toString());
            });

            return ('${baseURL}/search/results?type=${datatype}&query=' + $("#searchInput").val() +
                "&pageSize=" + pageSize.toString() + "&start=" + (page * pageSize).toString()) + "&" + params.join("&");
        }

        function createfilters(elem) {
            filters["${datatype}"].forEach(uiFilter => {
                render_filter(uiFilter, elem)
            });
            if (hide_filters) {
                elem.hide();
            }

        }

        function init() {

            createfilters($('#filters_div'));


            filters["${datatype}"].forEach(function (x) {
                if ($.QueryString[x.name]) {
                    x.enabled = true;
                    $("#in_" + x.name).prop('checked', "true");
                }
            });

            $("#base_breadcrumb").html(nameMap["${datatype}"]);

            $('#searchBtn').click(function (evt) {
                evt.preventDefault();

                window.location.href = searchUrl(0) ;
            })

            $("#searchInput").keyup(function (evt) {
                if (evt.keyCode == 13) {
                    evt.preventDefault()
                    window.location.href = searchUrl(0)

                }
            });

            var request = new Request('${baseUrl}/sndg/search/data_result?' + window.location.href.split("?")[1], {
                headers: new Headers({
                    'Content-Type': 'application/json'
                })
            });
            fetch(request).then(function (response) {
                return response.json();
            }).then(function (res) {
                var resultTableBody = $("#resultTableBody");
                resultTableBody.empty();

                res.data.forEach((record, i) => {
                    var tr = $("<tr />").appendTo(resultTableBody);
                    $("<td />").appendTo(tr).html(i + 1 + ($.page * $.pageSize))
                    var name = record.name;
                    if (record.ncbi_assembly) {
                        name = record.ncbi_assembly;
                    }
                    if (record.processid) {
                        name = record.processid;
                    }

                    $("<td />").appendTo(tr).append($("<a />", {href: urlMap['${datatype}'](record)}).html(name))
                    $("<td />").appendTo(tr).html(record.description)
                    if(  ["prot","seq"].indexOf( '${datatype}') !== -1 ){
                        $("<a/>",{href: urlMap['genome']({name:record.organism})}).html(record.colDescription) .appendTo(
                        $("<td />").appendTo(tr))
                    }

                });


                var prev = $("<li />").appendTo(".pagination").append(
                    $("<a />", {"aria-label": "Previous", "href": searchUrl($.page - 1)})
                        .append($("<span />").html("&#171;"))
                )

                if ($.page == 0) {
                    prev.addClass("disabled");
                }

                var idx = 0;
                if (($.page) == $.maxVisiblePages) {
                    idx = $.page
                    $("<li />").appendTo(".pagination").append(
                        $("<a />")
                            .append($("<span />").html("...")));

                }

                while ((idx < res.recordsFiltered / $.pageSize) && ((idx - $.page) < $.maxVisiblePages)) {
                    //<li class="active"><a href="#">1 <span class="sr-only">(current)</span></a></li>
                    var li = $("<li />").appendTo(".pagination").append(
                        $("<a />", {"href": searchUrl(idx)})
                            .append($("<span />").html(idx + 1))
                    );

                    if (idx == $.page) {
                        li.addClass("active");
                    }

                    idx++;
                }
                if (idx < res.recordsFiltered / $.pageSize) {
                    $("<li />").appendTo(".pagination").append(
                        $("<a />")
                            .append($("<span />").html("...")));
                }

                var next = $("<li />").appendTo(".pagination").append(
                    $("<a />", {"aria-label": "Next", "href": searchUrl(idx)})
                        .append($("<span />").html("&#187;"))
                );

                if ($.page == Math.floor(res.recordsFiltered / $.pageSize)) {
                    next.addClass("disabled")
                }

                $("<li />").appendTo(".pagination").html('&#160; &#160; &#160; Total: <b> ' + res.recordsFiltered.toString() + ' </b>');

            });

        }

        $(document).ready(init);
    </script>

    <script type="text/javascript">
        // ]]>
    </script>

    <div class="row">

        <section class="col-lg-12">

            <table width="100%">
                <tr>

                    <td width="100%"><input id="searchInput" width="100%"
                                            type="text" value="${query}" class="form-control"/></td>
                    <td>
                        <button id="searchBtn" class="btn btn-info">
                            <i class="fa fa-search">&#160;</i>
                        </button>
                    </td>
                </tr>
                <tr>
                    <td>
                        <div align="right">
                            <a onclick="$('#filters_div').show();$(this).hide()">Más filtros</a>
                        </div>

                        <table id="filters_div"></table>
                    </td>
                </tr>

            </table>
            <br/>
        </section>
    </div>
    <div class="row">

        <section class="col-lg-12">

            <div class="panel panel-default">
                <div class="panel-heading">Resultados</div>
                <table class="table">
                    <thead>
                    <tr>
                        <th>#</th>
                        <th>Nombre</th>
                        <th>Descripcion</th>
                        <th>Detalles</th>
                    </tr>
                    </thead>
                    <tbody id="resultTableBody">
                    <tr>
                        <td><img src="${baseURL}/public/theme/img/ajax-loader.gif" alt="cargando..."/></td>
                    </tr>

                    </tbody>
                </table>
                <ul class="pagination">


                </ul>

            </div>

        </section>
    </div>

    </body>
    </html>
</jsp:root>
