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
        <p>
            <a
                href="${pageContext.request.contextPath}/system/threadDump"
                class="btn btn-info">Get thread dump</a>
        </p>
        <h2>System Environment</h2>
        <table
            class="table table-striped table-bordered table-condensed">
            <tr>
                <th>Name</th>
                <th>Value</th>
            </tr>
            <c:forEach items="${env}" var="item">
                <tr>
                    <td>${f:h(item.key)}</td>
                    <td>${f:h(item.value)}</td>
                </tr>
            </c:forEach>
        </table>
        <h2>JVM Properties</h2>
        <table
            class="table table-striped table-bordered table-condensed">
            <tr>
                <th>Name</th>
                <th>Value</th>
            </tr>
            <c:forEach items="${properties}" var="item">
                <tr>
                    <td>${f:h(item.key)}</td>
                    <td>${f:h(item.value)}</td>
                </tr>
            </c:forEach>
        </table>
    </div>
</body>
</html>