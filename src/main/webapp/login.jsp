<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="description" content="">
    <meta name="author" content="">
    <title>Signin</title>
    <!-- Bootstrap core CSS -->
    <link
            href="${pageContext.request.contextPath}/resources/vendor/bootstrap/dist/css/bootstrap.min.css"
            rel="stylesheet" media="screen">
    <link
            href="${pageContext.request.contextPath}/resources/vendor/bootstrap/dist/css/bootstrap-theme.min.css"
            rel="stylesheet" media="screen">
    <style>
        body {
            padding-top: 40px;
            padding-bottom: 40px;
            background-color: #eee;
        }

        .form-signin {
            max-width: 330px;
            padding: 15px;
            margin: 0 auto;
        }

        .form-signin .form-signin-heading,
        .form-signin .checkbox {
            margin-bottom: 10px;
        }

        .form-signin .checkbox {
            font-weight: normal;
        }

        .form-signin .form-control {
            position: relative;
            font-size: 16px;
            height: auto;
            padding: 10px;
            -webkit-box-sizing: border-box;
            -moz-box-sizing: border-box;
            box-sizing: border-box;
        }

        .form-signin .form-control:focus {
            z-index: 2;
        }

        .form-signin input[type="text"] {
            margin-bottom: -1px;
            border-bottom-left-radius: 0;
            border-bottom-right-radius: 0;
        }

        .form-signin input[type="password"] {
            margin-bottom: 10px;
            border-top-left-radius: 0;
            border-top-right-radius: 0;
        }
    </style>
</head>

<body>

<div class="container">
    <form class="form-signin" role="form" method="post"
          action='${pageContext.request.contextPath}/j_spring_security_check'>
        <h2 class="form-signin-heading">Please sign in</h2>

        <c:if test="${param.error}">
            <t:messagesPanel messagesType="danger"
                             messagesAttributeName="SPRING_SECURITY_LAST_EXCEPTION"/>
        </c:if>

        <input name="j_username" type="text" class="form-control" value="admin" placeholder="Username" required
               autofocus>
        <input name="j_password" type="password" class="form-control" value="demo" placeholder="Password" required>
        <label class="checkbox">
            <input type="checkbox" name="_spring_security_remember_me"> Remember me
        </label>
        <input type="hidden"
               name="${f:h(_csrf.parameterName)}"
               value="${f:h(_csrf.token)}"/>
        <button class="btn btn-lg btn-success btn-block" type="submit">Sign in</button>
    </form>

</div>
<!-- /container -->

</body>
</html>