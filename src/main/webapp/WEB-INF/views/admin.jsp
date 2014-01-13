<!DOCTYPE html>
<html>
<head>
    <title>Admin Console</title>
    <link
            href="resources/vendor/bootstrap/dist/css/bootstrap.min.css"
            rel="stylesheet" media="screen">
    <link
            href="resources/vendor/bootstrap/dist/css/bootstrap-theme.min.css"
            rel="stylesheet" media="screen">
    <link
            href="resources/vendor/handsontable/dist/jquery.handsontable.full.css"
            rel="stylesheet" media="screen">
    <style>
        body {
            padding-top: 70px;
        }
    </style>
    <meta name="_csrf" content="${f:h(_csrf.token)}"/>
    <meta name="_csrf_header" content="${f:h(_csrf.headerName)}"/>
</head>
<body>
<sec:authentication property="principal.user" var="user"/>
<div class="navbar navbar-inverse navbar-fixed-top">
    <div class="container">
        <div class="navbar-header">
            <button type="button" class="navbar-toggle"
                    data-toggle="collapse"
                    data-target=".navbar-collapse">
                <span class="icon-bar"></span> <span
                    class="icon-bar"></span> <span class="icon-bar"></span>
            </button>
            <a class="navbar-brand" href="#">Admin Console</a>
        </div>
        <div class="collapse navbar-collapse">
            <ul class="nav navbar-nav pull-right">
                <li><a href="index.html">Check site</a></li>
                <li><a href="#config">Config</a></li>
                <li class="nav-divider"></li>
                <li class="dropdown"><a href="#"
                                        class="dropdown-toggle" data-toggle="dropdown"><span
                        class="glyphicon glyphicon-user"></span> Welcome,
                    ${f:h(user.firstName)}
                    ${f:h(user.lastName)} <b class="caret"></b></a>
                    <ul class="dropdown-menu">
                        <li><a href="#users/me/form"><span
                                class="glyphicon glyphicon-edit"></span>
                            Change information</a></li>
                        <li><a
                                href="${pageContext.request.contextPath}/logout"><span
                                class="glyphicon glyphicon-off"></span>
                            Logout</a></li>
                    </ul>
                </li>
            </ul>
        </div>
        <!--/.nav-collapse -->
    </div>
</div>
<div class="container">
    <div class="row">
        <div class="col col-sm-2">
            <div id="nav-item-list"></div>
        </div>
        <div class="col col-sm-10">
            <div class="tab-content"></div>
        </div>
    </div>
</div>
<script data-main="resources/app/js/admin/main" src="resources/vendor/requirejs/require.js"></script>
</body>
</html>