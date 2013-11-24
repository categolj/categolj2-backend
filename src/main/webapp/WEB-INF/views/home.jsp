<!DOCTYPE html>
<html lang="ja">
<head>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Admin</title>
<link
    href="${pageContext.request.contextPath}/resources/vendor/bootstrap/dist/css/bootstrap.min.css"
    rel="stylesheet" media="screen">
<link
    href="${pageContext.request.contextPath}/resources/vendor/bootstrap/dist/css/bootstrap-theme.min.css"
    rel="stylesheet" media="screen">
<style>
body {
    padding-top: 70px;
}
</style>
</head>
<body>
    <!-- ナビゲーションバー -->
    <sec:authentication property="principal.user" var="user" />
    <div class="navbar navbar-inverse navbar-fixed-top">
        <div class="container">
            <div class="navbar-header">
                <button type="button" class="navbar-toggle"
                    data-toggle="collapse"
                    data-target=".navbar-collapse">
                    <span class="icon-bar"></span> <span
                        class="icon-bar"></span> <span class="icon-bar"></span>
                </button>
                <a class="navbar-brand" href="#">TinyBlog 管理画面</a>
            </div>
            <div class="collapse navbar-collapse">
                <ul class="nav navbar-nav pull-right">
                    <li><a href="index.html">サイト確認</a></li>
                    <li><a href="#config">設定</a></li>
                    <li class="nav-divider"></li>
                    <li class="dropdown"><a href="#"
                        class="dropdown-toggle" data-toggle="dropdown"><span
                            class="glyphicon glyphicon-user"></span>
                            ${f:h(user.firstName)}
                            ${f:h(user.lastName)}さん <b class="caret"></b></a>
                        <ul class="dropdown-menu">
                            <li><a href="#users/update"><span
                                    class="glyphicon glyphicon-edit"></span>
                                    ユーザー情報変更</a></li>
                            <li><a
                                href="${pageContext.request.contextPath}/logout"><span
                                    class="glyphicon glyphicon-off"></span>
                                    ログアウト</a></li>
                        </ul></li>
                </ul>
            </div>
            <!--/.nav-collapse -->
        </div>
    </div>
    <!-- メインコンテンツ -->
    <div class="container">
        <div class="row">
            <div class="col col-sm-3">
                <ul id="nav-tab" class="nav nav-pills nav-stacked">
                    <li><a href="#dashboard"><span
                            class="glyphicon glyphicon-home"></span>
                            ダッシュボード</a></li>
                    <li><a href="#entries"><span
                            class="glyphicon glyphicon-pencil"></span>
                            記事管理</a></li>
                    <li><a href="#categories"><span
                            class="glyphicon glyphicon-bookmark"></span>
                            カテゴリー管理</a></li>
                    <li><a href="#users"><span
                            class="glyphicon glyphicon-user"></span>
                            ユーザー管理</a></li>
                    <li><a href="#reports"><span
                            class="glyphicon glyphicon-signal"></span>
                            レポート</a></li>
                </ul>
            </div>
            <div class="col col-sm-9">
                <div class="tab-content">
                    <div id="dashboard" class="tab-pane">
                        <div class="panel panel-default">
                            <div class="panel-heading">
                                <h3 class="panel-title">ダッシュボード</h3>
                            </div>
                            <div class="panel-body"></div>
                        </div>
                    </div>
                    <div id="entries" class="tab-pane">
                        <div class="panel panel-default">
                            <div class="panel-heading">
                                <h3 class="panel-title">記事管理画面</h3>
                            </div>
                            <div class="panel-body">
                                <div id="entries-body"></div>
                            </div>
                        </div>
                    </div>
                    <div id="categories" class="tab-pane">
                        <div class="panel panel-default">
                            <div class="panel-heading">
                                <h3 class="panel-title">カテゴリー管理画面</h3>
                            </div>
                            <div class="panel-body"></div>
                        </div>
                    </div>
                    <div id="users" class="tab-pane">
                        <div class="panel panel-default">
                            <div class="panel-heading">
                                <h3 class="panel-title">ユーザー管理画面</h3>
                            </div>
                            <div class="panel-body">
                                <div id="users-body"></div>
                            </div>
                        </div>
                    </div>
                    <div id="reports" class="tab-pane">
                        <div class="panel panel-default">
                            <div class="panel-heading">
                                <h3 class="panel-title">レポート</h3>
                            </div>
                            <div class="panel-body"></div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <script
        src="${pageContext.request.contextPath}/resources/vendor/jquery/jquery.min.js"></script>
    <script
        src="${pageContext.request.contextPath}/resources/vendor/underscore/underscore-min.js"></script>
    <script
        src="${pageContext.request.contextPath}/resources/vendor/bootstrap/dist/js/bootstrap.min.js"></script>
    <script
        src="${pageContext.request.contextPath}/resources/app/js/AdminView.js"></script>
    <script
        src="${pageContext.request.contextPath}/resources/app/js/EntriesView.js"></script>
    <script
        src="${pageContext.request.contextPath}/resources/app/js/UsersView.js"></script>
    <script>
					$(function() {
						new categolj2.AdminView();
					});
				</script>
</body>
</html>
