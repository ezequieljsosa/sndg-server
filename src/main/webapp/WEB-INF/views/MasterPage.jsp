<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://www.opensymphony.com/sitemesh/decorator"
           prefix="decorator" %>

<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>


<!DOCTYPE html>
<html>
<head>
    <!-- Global site tag (gtag.js) - Google Analytics -->
    <script async src="https://www.googletagmanager.com/gtag/js?id=UA-89927538-3"></script>
    <script>
        window.dataLayer = window.dataLayer || [];

        function gtag() {
            dataLayer.push(arguments);
        }

        gtag('js', new Date());

        gtag('config', 'UA-89927538-3');
    </script>


    <c:set var="reqUrl">${pageContext.request.requestURL}</c:set>
    <c:set var="baseURL" value="${fn:replace(reqUrl, pageContext.request.requestURI,pageContext.request.contextPath)}"/>

    <meta http-equiv="no-cache"/>
    <meta http-equiv="Expires" content="-1"/>

    <meta http-equiv="Cache-Control" content="no-cache, no-store, must-revalidate"/>
    <meta http-equiv="Pragma" content="no-cache"/>
    <meta http-equiv="Expires" content="0"/>


    <title><decorator:title default="SNDG"/></title>
    <meta http-equiv="content-type" content="text/html; charset=utf-8"/>

    <meta
            content='width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no'
            name='viewport'/>

    <!-- bootstrap 3.0.2 -->
    <link href="${baseURL}/public/theme/css/bootstrap.css"
          rel="stylesheet" type="text/css"/>

    <link href="${baseURL}/public/widgets/bootstrap-tour-0.11.0/bootstrap-tour-standalone.css"
          rel="stylesheet" type="text/css"/>

    <!-- font Awesome -->
    <link href="${baseURL}/public/theme/css/font-awesome.css"
          rel="stylesheet" type="text/css"/>
    <!-- Ionicons -->
    <link href="${baseURL}/public/theme/css/ionicons.min.css"
          rel="stylesheet" type="text/css"/>

    <link rel="shortcut icon" href="${baseURL}/public/bia/images/favicon.png"/>


    <!-- Theme style -->
    <link href="${baseURL}/public/theme/css/AdminLTE.css" rel="stylesheet"
          type="text/css"/>

    <style type="text/css">
        div.dataTables_processing {
            z-index: 1;
        }

        <!--
        .ext {
            background: url(${baseURL}/public/external.gif) no-repeat center right;
            padding-right: 12px;
            cursor: pointer;
        }

        -->


    </style>

    <!-- Google Analytics -->
    <script>
        /* (function(i,s,o,g,r,a,m){i['GoogleAnalyticsObject']=r;i[r]=i[r]||function(){
        (i[r].q=i[r].q||[]).push(arguments)},i[r].l=1*new Date();a=s.createElement(o),
        m=s.getElementsByTagName(o)[0];a.async=1;a.src=g;m.parentNode.insertBefore(a,m)
        })(window,document,'script','https://www.google-analytics.com/analytics.js','ga');

        ga('create', 'UA-89927538-2', 'auto');
        ga('send', 'pageview'); */
    </script>
    <!-- End Google Analytics -->


    <decorator:head/>
</head>
<body class="skin-blue">


<!-- jQuery 2.1.1 -->
<script src="${baseURL}/public/theme/js/jquery.min.js"></script>

<!-- Bootstrap -->
<script src="${baseURL}/public/theme/js/bootstrap.min.js"
        type="text/javascript"></script>

<script src="${baseURL}/public/widgets/bootstrap-tour-0.11.0/bootstrap-tour.js"
        type="text/javascript"></script>

<!-- 	AdminLTE App -->
<script src="${baseURL}/public/theme/js/iCheck/icheck.min.js"
        type="text/javascript"></script>

<!-- 	AdminLTE App -->
<script src="${baseURL}/public/theme/js/AdminLTE/app.js"
        type="text/javascript"></script>


<script src="${baseURL}/public/bia/bia.js"></script>

<script src="${baseURL}/public/bia/api.js" type="text/javascript"></script>


<script type="text/javascript">
    function addProt(name, id, status) {
        if (status) {
            $.api.add_session_resource('proteins', name, id, function (result) {
                window.proteins.values[name] = id;
            })

        } else {
            $.api.remove_session_resource('proteins', name,
                function (result) {
                    delete window.proteins.values[name];
                })

        }

    }

    function format_table_key(key, organism) {

        if (["ec", "gene", "go", "proteins", "rRNA", "tRNA"].indexOf(key) != -1) {
            return key.capitalize()
        }
        if ("proteins_with_structure" == key) {
            return '<a href="'
                + $.api.url_search_genome_keyword(organism,
                    "has_structure") + '">'
                + key.split("_").join(" ").capitalize() + '</a>'
        }

        var key_to_show = key;
        var tooltip = ""
        var keyword = key
        if (key.startsWith("SO:")) {

            key_to_show = SO_TERMS[key];
            if (typeof key_to_show == "undefined") {
                key_to_show = key;
            } else {
                key_to_show = key_to_show.replace(/_/g, " ").capitalize();
                keyword = encodeURI(keyword + " - " + key_to_show);
            }
            tooltip = '<a href="' + hrefOntologyLink("so", key)
                + '"><small class="badge bg-yellow">?</small></a>';

        }
        return tooltip + '<a href="'
            + $.api.url_search_genome_keyword(organism, keyword) + '">'
            + key_to_show + '</a>';
    }

    var init_user = function (user) {

        $("#username").html(user.username);
        $("#user_name").html(user.name);
        $("#institutions").html(user.institutions);
        $("#email").html(user.email);
        if (user.username == "demo") {
            $("#logoutbtn").html("Log in").click(() => {
                window.location = '${baseUrl}/sndg/login'
            })
        }


        if (user.links != null) {
            $
                .each(
                    user.links,
                    function (index, value) {

                        link = '<a href="' + value.url + '"><i class="fa fa-link">&nbsp;</i>  </a>';
                        $("#links-table").append(
                            "<tr><td>" + link + "</td><td>"
                            + value.type + "</td><td> "
                            + value.description);

                    });
        }
        if (user.menues != null) {
            $
                .each(
                    user.menues,
                    function (index, menu) {


                        $("#" + menu.parent)
                            .addClass("treeview")
                            .append(
                                '<ul class="treeview-menu"></ul>');
                        $("#" + menu.parent + " a")
                            .attr("href", "#")
                            .append(
                                ' <i class="fa fa-angle-left pull-right"></i>');


                        $("#" + menu.parent + " ul")
                            .append(
                                '<li id="' + menu.selector + '"><a href="' + menu.link
                                + '"><i	class="fa fa-angle-double-right"></i> '
                                + menu.name
                                + '</a></li>');
                    });
        }
        $(".sidebar .treeview").tree();

    };
    var maste_page_init = function (user, post_params) {

        $.api = new $.API(post_params, '${baseURL}');

        $.api.user(user, init_user)

    };
    $(document).ready(
        function () {
            window.proteins = ('' == '${sessionProts}') ? [] : JSON
                .parse('${sessionProts}')
            var _csrf = {}
            if ('${_csrf.parameterName}' != '') {
                _csrf = {
                    '${_csrf.parameterName}': '${_csrf.token}'
                };
            }

            $.breadcrum = $(".breadcrumb");
            //base_breadcrumb

            maste_page_init('${user.name}', _csrf);
        });
</script>

<header class="header">

    <nav class="navbar navbar-static-top" role="navigation">

        <div class="navbar-left">
            <ul class="nav navbar-nav">

                <li class="active"><a href="${baseURL}/"><img height="25px"
                                                              src="${baseURL}/public/bia/images/favicon.png"/>
                </a></li>


                <li class="active"><a href="${baseURL}/"> <i
                        class="fa fa-align-left"></i> <span> Inicio </span>
                </a></li>


                <li class="dropdown">

                    <%-- <a href="#" class="dropdown-toggle" data-toggle="dropdown" aria-expanded="false" > <i class="fa fa-edit"></i>
                            <span><spring:message code="master.tools" /></span> <span class="caret"></span>
                    </a> --%>
                    <ul class="dropdown-menu" role="menu">
                        <li><a href="${baseURL}/tool/blastp/"><i
                                class="fa fa-angle-double-right"></i> <spring:message
                                code="master.prot_blast"/> </a></li>
                        <li><a href="${baseURL}/tool/msa/"><i
                                class="fa fa-angle-double-right"></i> <spring:message
                                code="master.prot_msa"/> </a></li>
                        <li class="divider"></li>
                        <li><a href="${baseURL}/tool/job/"><i
                                class="fa fa-angle-double-right"></i> <spring:message
                                code="master.jobs"/> </a></li>

                    </ul>
                </li>

                <li style="display: None"><a href="${baseURL}/user/methodology"> <i
                        class="fa fa-list-ol"></i> <span>Methodology</span>
                </a></li>

                <li style="display: None"><a href="${baseURL}/user/user_guide"> <i
                        class="fa fa-question-circle"></i> <span>User Guide</span>
                </a></li>

                <li style="display: None"><a href="${baseURL}/user/tutorial"> <i
                        class="fa fa-file-text"></i> <span>Tutorial</span>
                </a></li>

                <li><a href="${baseURL}/tool/blast"> <i
                        class="fa  fa-search"></i> <span>Blast</span>
                </a></li>


                <li><a href="${baseURL}/user/about"> <i
                        class="fa  fa-info-circle"></i> <span>Informaci&#243;n</span>
                </a></li>

            </ul>

        </div>
        <div class="navbar-right">
            <ul class="nav navbar-nav">

                <li class="dropdown user user-menu"><a href="#"
                                                       class="dropdown-toggle" data-toggle="dropdown"> <i
                        class="glyphicon glyphicon-user"></i> <span> <span
                        id="user_name"></span> <i class="caret"></i></span>
                </a>
                    <ul class="dropdown-menu">
                        <!-- User image -->
                        <li class="user-header bg-light-blue">
                            <p>
                                <small><spring:message code="master.username"/>:</small>
                                <span
                                        id="username"></span>
                            </p>
                            <p>

                                <small>Email:</small>
                                <span id="email"></span>

                            </p>
                            <p>
                                <small><spring:message code="master.institutions"/>:</small>
                                <span id="institutions"></span>
                            </p>
                        </li>

                        <li class="user-footer">

                            <div class="pull-right">
                                <form id="formlogout" method="POST"
                                      enctype="application/x-www-form-urlencoded"
                                      action="${baseURL}/logout">
                                    <a id="logoutbtn" href="#" onclick="$('#formlogout').submit()"
                                       class="btn btn-default btn-flat"> <spring:message
                                            code="master.signout"/>
                                    </a> <input type="hidden" name="${_csrf.parameterName}"
                                                value="${_csrf.token}"/>
                                </form>
                            </div>
                        </li>
                    </ul>
                </li>
            </ul>
        </div>
    </nav>
</header>


<!-- Content Header (Page header) -->
<section class="content-header">

    <ol class="breadcrumb" style="padding: 8px 14px 0;   margin-bottom: 0px;">
        <li><a href="#"><i class="fa fa-bars">&#160;</i> <span id="base_breadcrumb"></span> </a></li>

    </ol>
</section>

<!-- Main content -->
<section class="content">
    <decorator:body/>

</section>
<!-- /.content -->

</div>
<!-- ./wrapper -->


</body>
</html>