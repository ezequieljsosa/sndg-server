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

        <title>Target Pathogen</title>

        <style type="text/css">
            .box .box-header {
                padding-bottom: 10px;
            }

            .sidebar {
                position: fixed;
                top: 0;
                bottom: 0;
                left: 0;
                z-index: 100; /* Behind the navbar */
                padding: 0;
                box-shadow: inset -1px 0 0 rgba(0, 0, 0, .1);
            }

            .sidebar-sticky {
                position: -webkit-sticky;
                position: sticky;
                top: 48px; /* Height of navbar */
                height: calc(100vh - 48px);
                padding-top: .5rem;
                overflow-x: hidden;
                overflow-y: auto; /* Scrollable contents if viewport is shorter than content. */
            }

            .sidebar .nav-link {
                font-weight: 500;
                color: #333;
            }

            .sidebar .nav-link .feather {
                margin-right: 4px;
                color: #999;
            }

            .sidebar .nav-link.active {
                color: #007bff;
            }

            .sidebar .nav-link:hover .feather,
            .sidebar .nav-link.active .feather {
                color: inherit;
            }


        </style>

        <!-- DATA TABLES -->
        <link
                href="${baseURL}/public/theme/css/datatables/dataTables.bootstrap.css"
                rel="stylesheet" type="text/css"/>

    </head>

    <body>


    <script type="text/javascript">
        $("body").addClass("container")
        $(".content-header").remove()
    </script>
    <div class="jumbotron text-justify text-center" style="padding-top:20,padding-bottom:20">
        <img src="${baseURL}/public/html/Logo PathogenTARGET.jpg"/>

    </div>
    <div class="page-header">

        Target-Pathogen database is a bioinformatic approach to prioritize drug targets in pathogens. Available genomic
        data for pathogens has created new opportunities for drug discovery and development, including new species,
        resistant and multiresistant ones. However, this data must be cohesively integrated to be fully exploited and be
        easy to interrogate. Target-Pathogen has been designed and developed as an online resource to allow genome wide
        based data consolidation from diverse sources focusing on structural druggability, essentiality and metabolic
        role of proteins. By allowing the integration and weighting of this information, this bioinformatic tool aims to
        facilitate the identification and prioritization of candidate drug targets for pathogens. With the structurome
        and drugome information Target-Pathogen is a unique resource to analyze whole genomes of relevants pathogens.
    </div>

    <section class="col-lg-6">
        <a href="${baseURL}/genome/" class="btn btn-primary btn-lg btn-block">Select Your Genome</a>
    </section>


    <section class="col-lg-6">
        <a href="http://target.sbg.qb.fcen.uba.ar/targetwp/wp-login.php"
           class="btn btn-success btn-lg btn-block">LogIn</a>
    </section>

    <br/>
    <br/>
    <br/>
    <br/>

    <section class="col-lg-12">
        <p>
            <a href="https://academic.oup.com/nar/article/doi/10.1093/nar/gkx1015/4584621">
                <i>Target-Pathogen: a structural bioinformatic approach to prioritize drug targets in pathogens</i></a>
            : Ezequiel J. Sosa, Germ&#225;n Burguener, Esteban Lanzarotti, Lucas Defelipe, Leandro Radusky, Agust&#237;n
            M. Pardo, Marcelo Marti, Adri&#225;n G. Turjanski, Dar&#237;o Fern&#225;ndez Do Porto <br/>
        </p>
        <b>Nucleic Acids Research</b> (2018) Database Issue

    </section>
    <section class="col-lg-12">
        <h3>TP collaborates with the following institutions:</h3>
        <img width="100%" src="${baseURL}/public/html/collaborators.png" />
    </section>
    <br/>
    <hr/>
    <br/>

    <script>

        $.get("http://target.sbg.qb.fcen.uba.ar/targetwp/feed", function (data) {
            const ul = $("#rss-feeds");
            $(data.getElementsByTagName("item")).each((i, x) => {
                const title = $(x.getElementsByTagName("title")).text();
                const link = $(x.getElementsByTagName("link")).text();
                let pubDate = $(x.getElementsByTagName("pubDate")).text();
                pubDate = pubDate.split(" ").slice(0, 4).join(" ");
                const description = $(x.getElementsByTagName("description")).text();

                const li = $("<li />");
                li.appendTo(ul);
                li.append($("<span />").html(pubDate));
                li.append($("<br />"));
                li.append($("<a />", {href: link}).html("<b>" + title + "</b>").css("color","blue"));
                li.append($("<br />"));
                li.append($("<span />").html(description));


            });

        })

    </script>
    <nav class="col-md-2 d-none d-md-block bg-light sidebar">
        <div class="sidebar-sticky">

            <ul id="rss-feeds">


            </ul>

        </div>
    </nav>


    </body>
    </html>
</jsp:root>