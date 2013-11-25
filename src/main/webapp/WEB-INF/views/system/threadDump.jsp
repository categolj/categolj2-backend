<!doctype html>
<html>
<head>
<link
    href="${pageContext.request.contextPath}/resources/vendor/bootstrap/dist/css/bootstrap.min.css"
    rel="stylesheet" media="screen">
<link
    href="${pageContext.request.contextPath}/resources/vendor/bootstrap/dist/css/bootstrap-theme.min.css"
    rel="stylesheet" media="screen">
</head>
<body>
    <div class="container">

        <c:forEach items="${threadDumps}" var="threadDump">
            <h2>${f:h(threadDump.key)}</h2>
            <pre><code>${f:h(threadDump.value)}</code></pre>
        </c:forEach>

    </div>
</body>
</html>